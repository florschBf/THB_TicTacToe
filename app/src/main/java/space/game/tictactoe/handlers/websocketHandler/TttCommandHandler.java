package space.game.tictactoe.handlers.websocketHandler;

import space.game.tictactoe.models.Player;

public class TttCommandHandler {
    private Player player = Player.getPlayer();

    /**
     * Methode um Spiel mit ausgewähltem Spieler zu starten
     * @param playerId String: Spieler-Id aus der opponents Liste
     * @return String um an den Server gesendet zu werden
     */
    public String startGame(String playerId){
        String command = "{\"topic\":\"gameSession\",\"command\":\"startgame\",\"playerId\":\""+playerId+"\",\"playerIcon\":\""+player.getIcon()+"\"}";
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

    public String acceptGame(){
        String command = "{\"topic\":\"gameSession\",\"command\":\"startgame\",\"answer\":\"confirm\",\"playerIcon\":\"" + this.player.getIcon() + "\"}";
        return command;
    }

    public String denyGame(){
        String command = "{\"topic\":\"gameSession\",\"command\":\"startgame\",\"answer\":\"deny\"}";
        return command;
    }

    public String endGame(){
        String command = "{\"topic\":\"gameSession\",\"command\":\"quitgame\",\"state\":\"initiate\"}";
        return command;
    }

    public String readyForGame() {
        String command = "{\"topic\":\"gameSession\",\"command\":\"gameState\",\"info\":\"whoseTurn\"}";
        return command;
    }
}
