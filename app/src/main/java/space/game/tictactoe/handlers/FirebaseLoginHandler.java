package space.game.tictactoe.handlers;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.AuthResult; //Might still use, leaving for now
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import space.game.tictactoe.R;
import space.game.tictactoe.models.Player;

/** Class to handle Firebase Authentication
 * called from MenuActivity, checks for logged in person
 * provides methods to get login status and user data
 * provides method to kickoff firebaseUI account creation/login
 * @author fs for help
 */
public class FirebaseLoginHandler extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private boolean loggedIn;
    private boolean anonSignIn;
    final private Context menuContext;
    public Player player;

    protected void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    protected void setAnonSignIn(boolean anonSignIn) { this.anonSignIn = anonSignIn; }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    public boolean isAnonSignIn() { return anonSignIn; }

    //CONSTRUCTOR METHOD
    public FirebaseLoginHandler(Context menuContext){
        this.menuContext = menuContext; // Context benötigt für UI Änderungen je nach Login -> potentiell in eigene Klasse bei Gelegenheit
        //init Firebase
        mAuth = FirebaseAuth.getInstance();

        //check if we're logged in and save current user / non-user
        currentUser = mAuth.getCurrentUser();
    }



    /**
     * Methode zum Verarbeiten des Signins
     * @param result Firebase Auth answer
     */
    public void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            this.player = Player.getPlayer();

            player.setName(getUserName());
            player.setFirebaseId(getFirebaseId());
            System.out.println("Firebase-User " + user + " Player: " + player);


            setLoggedIn(true);
            setAnonSignIn(false);
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            System.out.println("no user after all");
            setLoggedIn(false);
        }
    }

    //methods to kick off logout or login process

    /**
     * Methode zum Ausloggen aus Firebase
     * -> nicht eingeloggt => triggert anonymous login in MenuActivity
     */
    public void logout(Context c){
        AuthUI.getInstance()
                .signOut(c)
                .addOnCompleteListener(task -> setLoggedIn(false));
    }

    /**
     * Methode zum Start des Firebase Signin Intent
     */
    public Intent login(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build() // E-Mail will do for now
                //new AuthUI.IdpConfig.PhoneBuilder().build(),
                //new AuthUI.IdpConfig.GoogleBuilder().build()
                /*new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()*/);

        // Create and launch sign-in intent
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
    }

    /**
     * Methode für anonymous login Firebase
     */
    public void signInAnon(TextView login_status, Button login_button){
        System.out.println("Trying anon sign-in");
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //Logged in as Anon
                        setLoggedIn(true);
                        setAnonSignIn(true);
                        System.out.println("finally anon");
                        //There's an anon user authenticated via Firebase
                        changeTextAnonUser(login_status, login_button);

                    }
                    else { //should never happen..
                        setAnonSignIn(false);
                        changeDefaultText(login_status, login_button);
                    }
                });
    }

    // Methoden um UI Texte anzupassen (sollte in eine eigene Klasse..)

    /**
     * Methode für Textanpassung an anonymous login
     * @param status Login Status aus MenuActivity erhalten
     * @param button Login Button aus MenuActivity erhalten
     */
    public void changeTextAnonUser(TextView status, Button button){
        // Changing login status and button for anonymous user
        System.out.println("changing text to anon...");
        //Setting new strings from strings.xml
        String loginTxt = menuContext.getString(R.string.login);
        String anonLoginStatus = menuContext.getString(R.string.login_status_anon);
        status.setText(anonLoginStatus);
        button.setText(loginTxt);
    }

    /**
     * Methode für Textanpassung an gar kein login -> Fehler, darf nicht sein
     * @param status Login Status aus MenuActivity erhalten
     * @param button Login Button aus MenuActivity erhalten
     */
    public void changeDefaultText(TextView status, Button button){
        //Changing login status and button for no user -> should not happen
        System.out.println("changing text to default...");
        System.out.println("anon login not working for whatevs");
        //Setting new strings from strings.xml
        String defaultLoginBtn = menuContext.getString(R.string.login);
        String defaultLoginStatus = menuContext.getString(R.string.login_status0);
        status.setText(defaultLoginStatus);
        button.setText(defaultLoginBtn);
    }

    //methods to get user data (eigene Klasse..)

    /**
     * Methode um Nutzernamen auszulesen -> anon login = leer
     * @return Current user name if logged in
     */
    public String getUserName(){
        return this.currentUser.getDisplayName();
    }

    public String getFirebaseId(){
        return this.currentUser.getUid();
    }
}
