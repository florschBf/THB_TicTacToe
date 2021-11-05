package space.game.tictactoe;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

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

    protected void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    //CONSTRUCTOR METHOD
    public FirebaseLoginHandler(){
        //init Firebase
        mAuth = FirebaseAuth.getInstance();

        //check if we're logged in and save current user / non-user
        currentUser = mAuth.getCurrentUser();
    }

    protected void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            System.out.println(user);
            setLoggedIn(true);
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
    public void logout(Context c){
        AuthUI.getInstance()
                .signOut(c)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        setLoggedIn(false);
                    }
                });
    }

    public Intent login(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build() // E-Mail will do for now
                /*new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()*/);

        // Create and launch sign-in intent
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
    }

    public boolean signInAnon(){
        System.out.println("Trying anon sign-in");
        final boolean[] anonSignIn = new boolean[1];
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Logged in as Anon
                            setLoggedIn(true);
                            anonSignIn[0] = true;
                            System.out.println("logged in as anon: " + anonSignIn[0] + mAuth.getCurrentUser());
                        }
                    }
                });
        return anonSignIn[0];
    }

    //method to get user name
    public String getUserName(){
        return this.currentUser.getDisplayName();
    }
}
