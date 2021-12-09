package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//@author Peggy Kleinert

public class IconwahlActivity extends AppCompatActivity {
    private int icon = R.drawable.stern_90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iconwahl);

        // Button1 button_icons_pcspiel -> Weiterleitung zum Spiel per PC
        Button button_icons_pcspiel = (Button) findViewById(R.id.button_icons_pcspiel);
        //listener auf dem Button
        button_icons_pcspiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(IconwahlActivity.this, GameSingleActivity.class);
                    //Übergabe des Icon an die nächste Activity -> GameSingleActivity = Gegen PC spielen
                    intent.putExtra("playerIcon", icon);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
        // Button2 button_icon_onlinespiel -> Weiterleitung zum Onlinespiel
        Button button_icon_onlinespiel = (Button) findViewById(R.id.button_icon_onlinespiel);

        button_icon_onlinespiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //int icontrans = checkedIcon;
                    Intent intent = new Intent(IconwahlActivity.this, OnlinespielActivity.class);
                    //Übergabe des Icon an die nächste Activity -> OnlinespielActivity
                    intent.putExtra("playerIcon", icon);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
        // Button3 btn_on_backTomenu -> Weiterleitung zum Menü von der Iconauswahl
        Button  btn_on_backTomenu= (Button) findViewById(R.id.btn_on_backTomenu);

        btn_on_backTomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //int icontrans = checkedIcon;
                    Intent intent = new Intent(IconwahlActivity.this, MenuActivity.class);
                    /*//Übergabe des Icon an die nächste Activity
                    intent.putExtra("playerIcon", icon);*/
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
    }
    //verbesserter Code ;)
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

    public void onClickedIcon5(View view) {
        selectIcon(R.drawable.lebkuchenherz_3d_60, view);
    }

    public void onClickedIcon6(View view) {
        selectIcon(R.drawable.kleeblatt_3d_60, view);
    }

    private void selectIcon(int icon, View view) {
        this.icon = icon;
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(icon);
    }
}