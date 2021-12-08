package space.game.tictactoe.websocket;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;


import java.net.URI;
import java.nio.ByteBuffer;
import java.text.ParseException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import space.game.tictactoe.OnlinespielActivity;
import space.game.tictactoe.R;


public class TttWebsocketClient extends WebSocketClient{
    private Context context;
    private final TttMessageHandler msgHandler = new TttMessageHandler();
    private final TttCommandHandler cmdHandler = new TttCommandHandler();
    private PlayerListHandler listHandler;

    public TttWebsocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public TttWebsocketClient(URI serverURI, Context context) {
        super(serverURI);
        this.context = context;
        this.listHandler = new PlayerListHandler(context);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("{\"topic\":\"signup\",\"register\":\"player\",\"name\":\"android\",\"firebaseId\":\"none\"}");
        System.out.println("new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }


    /*  HIER BEI ON MESSAGE FINDET DER DATENAUSTAUSCH MIT DEM SERVER STATT
    *   KOMMT EINE MESSAGE VOM SERVER AN, WIRD SIE ANHAND DES PROTOKOLLS GEPRÜFT UND AUSGEWERTET
    *   EIGENE KLASSE FÜR DIE PRÜFUNG: TttMessageHandler
     */
    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
        String handledMessage = null;
        try{
            handledMessage = this.msgHandler.handle(message);
        } catch (ParseException | JSONException e){
            e.printStackTrace();
        }
        switch (handledMessage){
            case ("playerList"):
                this.listHandler.renderList(message);
            case ("1"):
                Integer x = Integer.valueOf(handledMessage);
                renderMove(x);
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    public String startGame(Object selectedPlayer) {
        return cmdHandler.startGame(selectedPlayer);
    }

    public boolean setMove(Integer feld){
        //TODO implement server validation
        boolean moveValid = true;
        String command = cmdHandler.sendMove(feld.toString());
        send(command);
        return moveValid;
    }

    /**
     * Method to render received opponent moves
     * @param feld Feld auf dem Spielfeld, 0-8, oben links bis unten rechts
     */
    public void renderMove(Integer feld){
        //find board in context and mark it
        String idFeld = "block" + feld;
        ImageView fieldToMark = ((Activity) context).findViewById(((Activity) context).getResources().getIdentifier(idFeld, "id", ((Activity) context).getPackageName()));
        fieldToMark.setImageResource(R.drawable.zero);
    }


}
