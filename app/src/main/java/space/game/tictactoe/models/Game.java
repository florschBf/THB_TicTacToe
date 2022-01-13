package space.game.tictactoe.models;

public class Game {

    // private gameResult = win, lose, draw
    // Player1
    Player player;
    Player opponent; // was weiß der Client über den Opponenten? - nur den Namen und dessen ServerId
    String result; // win / lose / draw / interrupted
    String gameId;

}
