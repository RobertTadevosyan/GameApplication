package game.gameapp.UIActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import game.gameapp.R;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.PreferenceUtil;

public class StartAcivity extends AppCompatActivity {
    private static final String TAG = "FIREBASE_AUTH";
    private Point point;
    private EditText userNameEditText;
    private AdView mAdView;
    private AdView secondAdView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_start_acivity);
        firebaseAuthInit();
        checkingIfIsUserLoggedIn();
        mAdView = (AdView) findViewById(R.id.adView);
        secondAdView = (AdView) findViewById(R.id.secondAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest secondAdRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        secondAdView.loadAd(secondAdRequest);
        this.userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        checkingUserName();
        gettingScreenSizes();
    }

    private void firebaseAuthInit() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void checkingUserName() {
        if (((String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "")).isEmpty() || ((String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "")).equals(CONSTATNTS.UNNAMED)) {
            this.userNameEditText.setVisibility(View.VISIBLE);
            PreferenceUtil.saveInSharedPreference(this, CONSTATNTS.ID, Integer.valueOf(1));
            return;
        }
        showAlert();
    }

    public void gettingScreenSizes() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        this.point = point;
    }

    private void goToMainActivity(String uName) {
        if (uName.isEmpty()) {
            Toast.makeText(this, "Gamer name can not be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        PreferenceUtil.saveInSharedPreference(this, CONSTATNTS.USER_NAME, this.userNameEditText.getText().toString());
        PreferenceUtil.saveInSharedPreference(this, CONSTATNTS.POINTS_X, Integer.valueOf(this.point.x));
        PreferenceUtil.saveInSharedPreference(this, CONSTATNTS.POINTS_Y, Integer.valueOf(this.point.y));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void playGameClick(View view) {
        goToMainActivity(userNameEditText.getText().toString());
    }

    public void showAlert() {
        Builder adb = new Builder(this);
        adb.setTitle((CharSequence) "Gamer checking .");
        final String uName = (String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "");
        adb.setMessage("Are you " + uName + " ?");
        adb.setCancelable(false);
        adb.setPositiveButton((CharSequence) "Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StartAcivity.this.userNameEditText.setText(uName);
                StartAcivity.this.userNameEditText.setFocusable(false);
                StartAcivity.this.userNameEditText.setBackgroundColor(ContextCompat.getColor(StartAcivity.this, android.R.color.transparent));
            }
        });
        adb.setNegativeButton((CharSequence) "No", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StartAcivity.this.userNameEditText.setText("");
                StartAcivity.this.userNameEditText.setFocusable(true);
                StartAcivity.this.userNameEditText.setBackgroundResource(R.drawable.edit_text_background);
            }
        });
        adb.show();
    }

    private void authorizationDialog() {
        final Dialog dialogNext = new Dialog(this);
        dialogNext.setCancelable(false);
        dialogNext.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNext.setContentView(R.layout.auth_dialog_layout);
        final EditText emailField = (EditText) dialogNext.findViewById(R.id.user_email);
        final EditText passField = (EditText) dialogNext.findViewById(R.id.user_password);
        final TextView titleField = (TextView) dialogNext.findViewById(R.id.dialig_title_text_view);
        titleField.setText("Simple authorization");
        final Button auth_button = (Button) dialogNext.findViewById(R.id.auth_button);
        auth_button.setText("Authorize");
        auth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailField.getText().toString().isEmpty() || passField.getText().toString().isEmpty()) {
                    Toast.makeText(StartAcivity.this, "At least of mandatory field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendAuthRequest(emailField.getText().toString(),passField.getText().toString());
//                dialogNext.dismiss();
            }
        });
        dialogNext.show();
    }

    private void sendAuthRequest(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(StartAcivity.this, "Authentication failed. ",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Toast.makeText(StartAcivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onAuthDialogOpenButtonClick(View view) {
        authorizationDialog();
    }

    private void loginDialog() {
        final Dialog dialogNext = new Dialog(this);
        dialogNext.setCancelable(false);
        dialogNext.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNext.setContentView(R.layout.auth_dialog_layout);
        final EditText emailField = (EditText) dialogNext.findViewById(R.id.user_email);
        final EditText passField = (EditText) dialogNext.findViewById(R.id.user_password);
        final TextView titleField = (TextView) dialogNext.findViewById(R.id.dialig_title_text_view);
        titleField.setText("Login to see others result");
        final Button auth_button = (Button) dialogNext.findViewById(R.id.auth_button);
        auth_button.setText("Log in");
        auth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailField.getText().toString().isEmpty() || passField.getText().toString().isEmpty()) {
                    Toast.makeText(StartAcivity.this, "At least of mandatory field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendLoginRequest(emailField.getText().toString(),passField.getText().toString());
//                dialogNext.dismiss();
            }
        });
        dialogNext.show();
    }

    private void sendLoginRequest(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(StartAcivity.this, "Authentication failed. " + task.getResult(),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // ...
                    }
                });
    }


    private void checkingIfIsUserLoggedIn(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            goToMainActivity(name);
            return;
        }
    }

    public void onLoginDialogOpenClick(View view) {
        loginDialog();
    }
}
