package space.game.tictactoe.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import space.game.tictactoe.menu.MenuActivity;
import space.game.tictactoe.R;
import space.game.tictactoe.models.Player;

/** Class to handle Firebase Authentication
 * called from MenuActivity, checks for logged in person
 * provides methods to get login status and user data
 * provides method to kickoff firebaseUI account creation/login
 * @author fs for help
 */
public class FirebaseLoginHandler {

    final private Context menuContext;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public Player player;
    public StatisticsHandler statisticsHandler = StatisticsHandler.getStatisticsHandler();


    //CONSTRUCTOR METHOD
    public FirebaseLoginHandler(Context menuContext, FirebaseAuth mAuth, FirebaseUser currentUser){
        this.menuContext = menuContext; // Context benötigt für UI Änderungen je nach Login -> potentiell in eigene Klasse bei Gelegenheit
        this.mAuth = mAuth;
        this.currentUser = currentUser;
        this.player = Player.getPlayer();
    }

    public void updatingMyKnowledge(FirebaseAuth mAuth, FirebaseUser currentUser){
        System.out.println("updating what I should know about firebase");
        this.mAuth = mAuth;
        this.currentUser = currentUser;
    }


    //methods to kick off logout or login process
    /**
     * Methode zum Ausloggen aus Firebase
     * -> nicht eingeloggt => triggert anonymous login in MenuActivity
     */
    public void logout(Context c, MenuActivity menu) throws Exception {
        statisticsHandler.setPlayerData();
        try {
            AuthUI.getInstance()
                    .signOut(c)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            System.out.println("successful logout");
                            menu.signInAnon(menu.findViewById(R.id.login_status), menu.findViewById(R.id.button_login));
                            try {
                                menu.updateUserAndUI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            menu.updatePlayerFirebaseStatus();
                        }
                    });
        }
        catch (Exception e){
            System.out.println("logout-error");
            e.printStackTrace();
        }
    }

    /**
     * Methode zum Start des Firebase Signin Intent
     */
    public Intent login() throws Exception {
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
        ((Activity)menuContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("change running well");
                status.setText(anonLoginStatus);
                button.setText(loginTxt);
            }
        });
    }

    /**
     * Methode für Textanpassung an gar kein login -> Fehler, darf nicht sein
     * @param status Login Status TextView aus MenuActivity erhalten
     * @param button Login Button aus MenuActivity erhalten
     */
    public void changeDefaultText(TextView status, Button button){
        //Changing login status and button for no user -> should not happen. Ever.
        System.out.println("changing text to default...");
        System.out.println("anon login not working for whatevs");
        //Setting new strings from strings.xml
        String defaultLoginBtn = menuContext.getString(R.string.login);
        String defaultLoginStatus = menuContext.getString(R.string.login_status0);
        status.setText(defaultLoginStatus);
        button.setText(defaultLoginBtn);
    }

    /**
     * Methode um Nutzernamen auszulesen -> anon login = leer
     * @return Current user name if logged in
     */
    public String getUserName(){
        try{
            return this.currentUser.getDisplayName();
        }
        catch (Exception e){
            System.out.println("uhoh");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @return Uid of the current firebase user
     */
    public String getFirebaseId(){
        return this.currentUser.getUid();
    }

    /**
     * @return E-mail of the current firebase user
     */
    public String getEmail() { return  this.currentUser.getEmail(); }

    /**
     * @param mAuth On signInAnon with FirebaseAuth set mAuth-Value for encapsulation
     */
    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    /**
     * @param user On signInAnon or onResume with FirebaseAuth (login) set ccurrent firebase user for encapsulation
     */
    public void setCurrentUser(FirebaseUser user) {
        this.currentUser = user;
    }
}
