package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

//@author Peggy Kleinert

public class IconwahlActivity extends AppCompatActivity {

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

                    Intent intent = new Intent(IconwahlActivity.this, GameActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
        // Button2 button_icons_pcspiel -> Weiterleitung zum Spiel per PC
        Button button_icon_onlinespiel = (Button) findViewById(R.id.button_icon_onlinespiel);

        button_icon_onlinespiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //int icontrans = checkedIcon;
                    Intent intent = new Intent(IconwahlActivity.this, Matchmaking.class);
                    //intent.putExtra("gechecktes Icon",(int) icontrans) TODO;
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        });
    }

    int checkedIcon;

    @SuppressLint("NonConstantResourceId")
    public int onRadioButtonClickedgetID(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        try {
            switch (view.getId()) {
                case R.id.radiobutton_icon1:
                    if (checked)
                        checkedIcon = R.id.radiobutton_icon1;
                        //checkedIcon = R.drawable.blume_eckig_3d_90;
                    // first Icon
                    break;
                    case R.id.radiobutton_icon2:
                    if (checked)
                        checkedIcon = R.id.radiobutton_icon2;
                        //checkedIcon = R.drawable.donat_3d_blau_90;
                    // second Icon
                    break;
                case R.id.radiobutton_icon3:
                    if (checked)
                        checkedIcon = R.id.radiobutton_icon3;
                        //checkedIcon = R.drawable.stern_90;
                    // third Icon
                    break;
                case R.id.radiobutton_icon4:
                    if (checked)
                        checkedIcon = R.id.radiobutton_icon4;
                        //checkedIcon = R.drawable.donat_3d_gold_90;
                    // fourth Icon
                    break;
                case R.id.radiobutton_icon5:
                    if (checked)
                        checkedIcon = R.id.radiobutton_icon5;
                        //checkedIcon = R.drawable.lebkuchenherz_3d_90;
                    // fifth Icon
                    break;
                case R.id.radiobutton_icon6:
                    if (checked)
                        checkedIcon = R.id.radiobutton_icon6;
                       // checkedIcon = R.drawable.kleeblatt_3d_90;
                    // sixth Icon
                    break;

            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
        return checkedIcon;

    }

    int icon;
    public void onClickedIcon1(View view) {
        this.icon = R.drawable.blume_eckig_3d_90;
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(icon);

    }

    public void onClickedIcon2(View view) {
        this.icon = R.drawable.donat_3d_blau_90;
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(icon);
    }

    public void onClickedIcon3(View view) {
        this.icon = R.drawable.stern_90;
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(icon);
    }

    public void onClickedIcon4(View view) {
        this.icon = R.drawable.donat_3d_gold_90;
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(icon);
    }

    public void onClickedIcon5(View view) {
        this.icon = R.drawable.lebkuchenherz_3d_90;
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(icon);
    }

    public void onClickedIcon6(View view) {
        this.icon = R.drawable.kleeblatt_3d_90;
        ImageView image = (ImageView) findViewById(R.id.icontransport);
        image.setImageResource(icon);
    }


}