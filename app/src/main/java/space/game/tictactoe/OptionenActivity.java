package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class OptionenActivity extends AppCompatActivity {

//    Switch ton;
//    public static boolean tonOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optionen);

        //Button 1 -> Weiterleitung zur Icon Auswahl
        Button button_icon_waehlen = (Button)findViewById(R.id.button_icon_waehlen);
        button_icon_waehlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OptionenActivity.this, IconwahlActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

        //Button 2 -> Weiterleitung zu den Statistiken
        Button button_statistiken = (Button)findViewById(R.id.button_statistiken);
        button_statistiken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OptionenActivity.this, StatistikenActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

//        // Souneffekte defaul aktiviert
//        ton = (Switch)findViewById(R.id.switch_sound);
//        ton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked == true) {
//                    Toast.makeText(OptionenActivity.this, "Sound is on", Toast.LENGTH_LONG).show();
//                    tonOn = true;
//                } else {
//                    Toast.makeText(OptionenActivity.this, "Sound is off", Toast.LENGTH_LONG).show();
//                    tonOn = false;
//                }
//            }
//        });
//
//        if(tonOn == true) {
//            ton.setChecked(true);
//        } else {
//            ton.setChecked(false);
//        }


    }

}