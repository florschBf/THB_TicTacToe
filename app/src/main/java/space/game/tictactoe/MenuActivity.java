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
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import space.game.tictactoe.handlers.FirebaseLoginHandler;
import space.game.tictactoe.handlers.StatisticsHandler;
import space.game.tictactoe.models.Player;

/**
 * Hauptmenü Android Activity
 */
public class MenuActivity extends AppCompatActivity {

    //handle login status
    private static FirebaseLoginHandler fbLogin;
    private static FirebaseAuth mAuth;
    private static FirebaseUser currentUser;
    private static StatisticsHandler statisticsHandler = StatisticsHandler.getStatisticsHandler();
    public static Player player;

    // See: https://developer.android.com/training/basics/intents/result
    // launches new view when login is started
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    try {
                        onSignInResult(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    /**
     * Methode zum Verarbeiten des Signins
     * @param result Firebase Auth answer
     */
    public void onSignInResult(FirebaseAuthUIAuthenticationResult result) throws Exception {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            updateUserAndUI();
            updatePlayerFirebaseStatus();
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            System.out.println("no user after all");
            updateUserAndUI();
        }
    }

    /**
     * Methode für anonymous login Firebase --> immer dann, wenn es keinen echten login gibt
     */
    public void signInAnon(TextView login_status, Button login_button){
        System.out.println("Trying anon sign-in");
        this.mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //Logged in as Anon
                        System.out.println("finally anon");
                        //There's an anon user authenticated via Firebase
                        player.setName(player.randomName());
                        fbLogin.changeTextAnonUser(login_status, login_button);
                        currentUser = mAuth.getCurrentUser();
                        fbLogin.setmAuth(mAuth);
                        fbLogin.setCurrentUser(currentUser);
                    }
                    else { //should never happen..
                        fbLogin.changeDefaultText(login_status, login_button);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //init Firebase
        mAuth = FirebaseAuth.getInstance();
        //check if we're logged in and save current user / non-user
        currentUser = mAuth.getCurrentUser();
        System.out.println(currentUser);
        fbLogin = new FirebaseLoginHandler(this, mAuth, currentUser);
        this.player = Player.getPlayer();
        if (currentUser == null){
            signInAnon(findViewById(R.id.login_status), findViewById(R.id.button_login));
        }
        try {
            updatePlayerFirebaseStatus();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (currentUser.getDisplayName() == null){
            try {
                fbLogin.logout(this, (MenuActivity) this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // Spiel im Vollbild ausführen
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Button 1 btn_singleGame -> Weiterleitung zum Spiel mit PC GameSingleActivity
        Button btn_singleGame = (Button)findViewById(R.id.btn_singleGame);
        btn_singleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, GameSingleActivity.class);
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
                    Intent intent = new Intent(MenuActivity.this, OnlinespielActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

        // Button3 button_rules -> Weiterleitung zur Spielbeschreibung, Spielregel
        Button button_rules = (Button)findViewById(R.id.button_rules);
        button_rules.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MenuActivity.this, RulesActivity.class);
                startActivity(intent);
            } catch(Exception e) {

            }
        });

        // Button 4 button_showAbout -> Weiterleitung zur Projektbeschreibung
        Button button_showAbout = (Button)findViewById(R.id.button_showAbout);
        button_showAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("Button About clicked");
                    Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                    startActivity(intent);
                } catch(Exception e) {
                    System.out.println("Button About clicked but got error: " + e);
                }
            }
        });

        // Button #3
        Button button_optionen_general = (Button)findViewById(R.id.button_optionen_general);
        button_optionen_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, OptionenActivity.class);
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

                if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getDisplayName() != "" && mAuth.getCurrentUser().getDisplayName() != null){ //logged in & not anon -> logging out
                    System.out.println("User: " + mAuth.getCurrentUser() + " Name: " + mAuth.getCurrentUser().getDisplayName());
                    try{
                        System.out.println("triggering logout");
                        final Context c = v.getContext();
                        fbLogin.logout(c, (MenuActivity) c);

                    } catch (Exception e){
                        System.out.println("whoops, couldn't log out?: " + e);
                    }
                }
                else{
                    System.out.println("User: " + mAuth.getCurrentUser() + " Name: " + mAuth.getCurrentUser().getDisplayName());
                    try {
                        signInLauncher.launch(fbLogin.login());
                        // statisticsHandler.updateLocalPlayerDataWithFbData();
                    } catch (Exception e){
                        System.out.println("Meh, login didn't start?: " + e);
                    }
                }

            }
        });

        //check for login onCreate - happens automatically
        //updateUserAndUI();
    }

    /**
     * Method to update UI according to login status
     * @throws Exception
     */
    public void updateUserAndUI() throws Exception {

        if (mAuth.getCurrentUser() == null){
            System.out.println("Well, no User\n");
            this.signInAnon(findViewById(R.id.login_status), findViewById(R.id.button_login)); //Starte Anonymous user login
        }
        else {
            if(fbLogin.getUserName() == "" && fbLogin.getUserName() != null){
                //login is anon, nothing to do
                fbLogin.changeTextAnonUser(findViewById(R.id.login_status), findViewById(R.id.button_login));
                System.out.println("leaving things as is");
            }
            else if (fbLogin.getUserName() == null){
                //trouble, shouldnt be
                System.out.println("sth wrong here");
                fbLogin.logout(this, (MenuActivity) this);
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
        System.out.println("resuming menu..");
        super.onResume();
        try {
            if(mAuth.getCurrentUser() != null){
                System.out.println("someone here on resume");
                System.out.println(mAuth);
                updateUserAndUI();
                updatePlayerFirebaseStatus();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("absolutely no one here, sign in anon");
            signInAnon(findViewById(R.id.login_status), findViewById(R.id.button_login));
        }
        if (currentUser.getDisplayName() == null){
            try {
                fbLogin.logout(this, (MenuActivity) this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePlayerFirebaseStatus(){
        if (fbLogin.getUserName() != "") {
            //not anon login, update stuff
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            player.setName(fbLogin.getUserName());
            player.setEmail(fbLogin.getEmail());
            player.setFirebaseId(fbLogin.getFirebaseId());
            System.out.println("Firebase-User " + user + " Player: " + player);
            try {
                statisticsHandler.updateLocalPlayerDataWithFbData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            //anon login, doing nth

        }
    }



}