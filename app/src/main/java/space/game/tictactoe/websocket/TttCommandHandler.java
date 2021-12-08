package space.game.tictactoe.websocket;

import android.app.LauncherActivity;

public class TttCommandHandler {

    public String startGame(Object selectedPlayer){
        String player = selectedPlayer.toString();
        String[] info = player.split(" ");
        System.out.println(info);
        String playerID = info[1];
        System.out.println(playerID);
        String command = "{\"topic\":\"gameSession\",\"command\":\"startgame\",\"playerId\":\""+playerID+"\"}";
        return command;
    }

    public String sendMove(String field){
        String command = "{\"topic\":\"gameMove\",\"command\":\"mark\",\"field\":\""+field+"\"}";
        return command;
    }
}
