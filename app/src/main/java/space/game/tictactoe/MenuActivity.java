package space.game.tictactoe;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

public class MenuActivity extends AppCompatActivity {

    //handle login status
    private FirebaseLoginHandler fbLogin = new FirebaseLoginHandler(this);

    // See: https://developer.android.com/training/basics/intents/result
    // launches new view when login is started
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    fbLogin.onSignInResult(result);
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Spiel im Vollbild ausführen
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Button 1 btn_singleGame -> Weiterleitung zum Spiel mit PC GameActivity
        Button btn_singleGame = (Button)findViewById(R.id.btn_singleGame);
        btn_singleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });
        //Button 5 -> Weiterleitung zum Spiel Multiplayer Matchmaking
        Button button_onlineGame = (Button)findViewById(R.id.button_onlineGame);
        button_onlineGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, Matchmaking.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });
        //Button 6 -> Weiterleitung zur Icon Auswahl
        Button button_icon_waehlen = (Button)findViewById(R.id.button_icon_waehlen);
        button_icon_waehlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, IconwahlActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });
        //Button 7 -> Weiterleitung zu den Statistiken
        Button button_statistiken = (Button)findViewById(R.id.button_statistiken);
        button_statistiken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, IconwahlActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });


        // Button3 button_rules -> Weiterleitung zur Spielbeschreibung, Spielregel
        Button button_rules = (Button)findViewById(R.id.button_rules);
        button_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, RulesActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

        // Button 4 button_showAbout -> Weiterleitung zur Projektbeschreibung
        Button button_showAbout = (Button)findViewById(R.id.button_showAbout);
        button_showAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

        // Button 8 button_login -> Weiterleitung zum LogIn
        // Vorerst weiter drin als Login zu Testzwecken
        Button button_login = (Button)findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fbLogin.isLoggedIn() && !fbLogin.isAnonSignIn()){ //logged in & not anon -> logging out
                    try{
                        System.out.println("triggering logout");
                        final Context c = v.getContext();
                        fbLogin.logout(c);
                    } catch (Exception e){
                        System.out.println("whoops, couldn't log out?: " + e);
                    }
                    Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
                else{
                    try {
                        signInLauncher.launch(fbLogin.login());
                    } catch (Exception e){
                        System.out.println("Meh, login didn't start?: " + e);
                    }
                }

            }
        });

        //check for login onCreate - happens automatically
        //updateUserAndUI();
    }



    //Method to update UI according to login status
    public void updateUserAndUI(){
        boolean loggedIn = this.fbLogin.isLoggedIn();
        boolean anonSignIn = this.fbLogin.isAnonSignIn();

        if (!loggedIn){
            System.out.println("Well, no User\n");
            fbLogin.signInAnon(findViewById(R.id.login_status), findViewById(R.id.button_login)); //Starte Anonymous user login
        }
        else {
            if(anonSignIn){
                //login is anon, nothing to do
                System.out.println("leaving things as is");
            }
            else { //login but not anon
                //There's a user authenticated via Firebase
                String name = fbLogin.getUserName();
                System.out.println("Seems to be a User: " + name);

                // Changing login status and button, doing it right here, should use extra method somewhere
                // other methods like this found in FirebaseLoginHandler
                final TextView login_status = findViewById(R.id.login_status);
                login_status.setText("Du bist eingeloggt als " + name);
                final Button loginBtn = findViewById(R.id.button_login);
                loginBtn.setText("Ausloggen");
            }
        }
    }

    //Use onResume to always check for login when we come back and updateUserAndUI accordingly
    protected void onResume(){
        super.onResume();
        updateUserAndUI();
    }



}