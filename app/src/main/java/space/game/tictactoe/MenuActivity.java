package space.game.tictactoe;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    //FirebaseAuthentication Class
    private FirebaseAuth mAuth;
    private boolean loggedIn;
    public boolean isLoggedIn() {
        return loggedIn;
    }



    //Method to update UI according to login status
    private void updateUI(FirebaseUser user){
        if (user == null){
            //Not logged in
            System.out.println("Well, no User");
            loggedIn = false;
        }
        else {
            //There's a user authenticated via Firebase
            System.out.println("Seems to be a User: " + user);
            loggedIn = true;
            String name = user.getDisplayName();
            final TextView login_status = findViewById(R.id.login_status);
            login_status.setText("Du bist eingeloggt als " + name);
            final Button loginBtn = findViewById(R.id.button_login);
            loginBtn.setText("Ausloggen");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //init Firebase
        mAuth = FirebaseAuth.getInstance();

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

        // Button 6 button_login -> Einloggen oder Ausloggen aus Firebase
        Button button_login = (Button)findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()){ //logged in -> logging out
                    try{
                        AuthUI.getInstance()
                                .signOut(MenuActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    } catch (Exception e){
                        System.out.println("whoops, couldn't log out?: " + e);
                    }
                }
                else{
                    try {
                        // Choose authentication providers, not logged in yet
                        List<AuthUI.IdpConfig> providers = Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build() // E-Mail will do for now
                /*new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()*/);

// Create and launch sign-in intent
                        Intent signInIntent = AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build();
                        signInLauncher.launch(signInIntent);
                    } catch(Exception e) {
                        System.out.println("whoops, couldn't log in?: " + e);
                    }
                }

            }
        });

    }

    //check if we're logged in
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            System.out.println(user);
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            System.out.println("no user after all");
        }
    }


}