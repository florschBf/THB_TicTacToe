package space.game.tictactoe.websocket;

import android.app.LauncherActivity;

import space.game.tictactoe.models.Player;

public class TttCommandHandler {
    private Player player = Player.getPlayer();

    /**
     * Methode um Spiel mit ausgewähltem Spieler zu starten
     * @param selectedPlayer Spieler aus der Liste, aktuelles Format "Name Uid"
     * @return String um an den Server gesendet zu werden
     */
    public String startGame(Object selectedPlayer){
        //TODO fix playerId selection / save playerID somewhere on list broadcast
        String player = selectedPlayer.toString();
        String[] info = player.split(" ");
        System.out.println(info);
        String playerID = info[1];
        System.out.println(playerID);
        String command = "{\"topic\":\"gameSession\",\"command\":\"startgame\",\"playerId\":\""+playerID+"\"}";
        return command;
    }

    /**
     * Methode um die Zufallsgegner-Suche zu starten
     * @return String um an den Server gesendet zu werden
     */
    public String startRandom(){
        System.out.println("Joining random game queue");
        System.out.println("Sending icon: " + this.player.getIcon());
        String command = "{\"topic\":\"gameSession\",\"command\":\"startRandom\",\"playerIcon\":\"" + this.player.getIcon() + "\"}";
        return command;
    }

    /**
     * Methode um die Zufallsgegner-Suche zu beenden
     * @return String um an den Server gesendet zu werden
     */
    public String stopRandom(){
        System.out.println("Leaving random game queue");
        String command = "{\"topic\":\"gameSession\",\"command\":\"stopRandom\"}";
        return command;
    }

    /**
     * Methode um Zug an den Server zu senden
     * @param field TicTacToe Feldnummer von 0-8 als String
     * @return String um an den Server gesendet zu werden
     */
    public String sendMove(String field){
        String command = "{\"topic\":\"gameMove\",\"command\":\"mark\",\"field\":\""+field+"\"}";
        return command;
    }
}
