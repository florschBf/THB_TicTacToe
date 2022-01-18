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

/** activity for choosing different icons for the local player
 * it is available for single player mode and online player mode
 */
public class IconwahlActivity extends AppCompatActivity {
    private int icon = Player.getPlayer().icon;

    public Player player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iconwahl);
        /*this.icon = Player.getPlayer().getIcon();*/

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

                    Intent intent = new Intent(IconwahlActivity.this, GameSingleActivity.class);
                    //Übergabe des Icon an die nächste Activity -> GameSingleActivity = Gegen PC spielen
                     //handing over the icon to the next activity -> GameSingleActivity

                    intent.putExtra("playerIcon", icon);
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

                    Intent intent = new Intent(IconwahlActivity.this, OnlinespielActivity.class);
                    /**Übergabe des Icon an die nächste Activity -> OnlinespielActivity
                     * forwarding the icon to the next activity
                     */
                    intent.putExtra("playerIcon", icon);
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

                    Intent intent = new Intent(IconwahlActivity.this, MenuActivity.class);
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
        selectIcon(R.drawable.blume_eckig_3d_60, view);
    }

    public void onClickedIcon2(View view) {
        selectIcon(R.drawable.donat_3d_blau_60, view);
    }

    public void onClickedIcon3(View view) {
        selectIcon(R.drawable.stern_60, view);
    }

    public void onClickedIcon4(View view) {
        selectIcon(R.drawable.donat_3d_gold_60, view);
    }

    public void onClickedIcon9(View view) {
        selectIcon(R.drawable.monster_60, view);
    }

    public void onClickedIcon5(View view) {
        selectIcon(R.drawable.lebkuchenherz_3d_60, view);
    }

    public void onClickedIcon6(View view) {
        selectIcon(R.drawable.kleeblatt_3d_60, view);
    }

    public void onClickedIcon7(View view) {
        selectIcon(R.drawable.schaf_60, view);
    }

    public void onClickedIcon8(View view) {
        selectIcon(R.drawable.krebsmonster_60, view);
    }

    private void selectIcon(int icon, View view) {
        this.icon = icon;

        this.player = Player.getPlayer();

        player.setIcon(icon);
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(player.getIcon());
    }
}