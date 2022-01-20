package space.game.tictactoe.menu.options;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import space.game.tictactoe.R;
import space.game.tictactoe.menu.GameSingleActivity;
import space.game.tictactoe.menu.MenuActivity;
import space.game.tictactoe.menu.OnlinespielActivity;
import space.game.tictactoe.models.Player;

public class SpielbrettwahlActivity extends AppCompatActivity {
    private Player player = Player.getPlayer();
    private int board = player.getBoard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spielbrettwahl);
        selectBoard(board);


        /** Button1- button_icons_pcspiel -> forwarding to Activity GameSingleActivity -> single Player mode
         *
         */
        Button button_icons_pcspiel = (Button) findViewById(R.id.button_icons_pcspiel);
        /**listener on the button
         *
         */
        button_icons_pcspiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(SpielbrettwahlActivity.this, GameSingleActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
        /** Button2 button_icon_onlinespiel -> Weiterleitung zum Onlinespiel
         * button 2 forwarding to "Onlinespiel" Activity
         */
        Button button_icon_onlinespiel = (Button) findViewById(R.id.button_icon_onlinespiel);

        button_icon_onlinespiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(SpielbrettwahlActivity.this, OnlinespielActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
        /** Button3 btn_on_backTomenu -> Weiterleitung zum Menü von der Iconauswahl
         * button3 - forwarding to menu from icon choosing activity
         */
        Button  btn_on_backTomenu= (Button) findViewById(R.id.btn_on_backTomenu);
        btn_on_backTomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(SpielbrettwahlActivity.this, MenuActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
    }

    /** function for selecting an icon from the radio button list and giving the chosen icon, setting it as image ressource to class Player and the Player
     *
     * @param view
     */
    public void onClickedIcon1(View view) {
        selectBoard(R.drawable.play_board);
    }

    public void onClickedIcon2(View view) {
        selectBoard(R.drawable.play_board_bw);
    }

    public void onClickedIcon3(View view) {
        selectBoard(R.drawable.play_board_gradientred);
    }

    public void onClickedIcon4(View view) {
        selectBoard(R.drawable.play_board_green3d);
    }

    public void onClickedIcon5(View view) {
        selectBoard(R.drawable.play_board_tealish3d);
    }



    private void selectBoard(int board) {
        this.board = board;
        this.player.setBoard(board);
        ImageView image = (ImageView) findViewById(R.id.boardtransport);
        image.setImageResource(player.getBoard());
    }

}
