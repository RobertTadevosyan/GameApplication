package game.gameapp.Request;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import game.gameapp.RealmModel.User;
import game.gameapp.UIActivities.EmailPasswordActivity;
import game.gameapp.Utils.RealmHelper;

public class BaseActivity extends AppCompatActivity implements RequestLoadingMontionInterface {
    public static int navBarHeight;
    RequestLoadingMontion requestLoadingMontion;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        navBarHeight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
//        if(User.getLoggedInUser() == null){
//            startActivity(new Intent(this, EmailPasswordActivity.class));
////            Toast.makeText(this, "Please Log in :)", Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }

    @Override
    public RequestLoadingMontion getRequestLoadingMontion() {
        if (null == requestLoadingMontion) {
            if (findViewById(android.R.id.content) != null) {
                requestLoadingMontion = new RequestLoadingMontion((ViewGroup) findViewById(android.R.id.content), getApplicationContext());
            }
        }
        return requestLoadingMontion;
    }

    protected void showProgressDialog() {
        getRequestLoadingMontion().show();
    }

    protected void hideProgressDialog() {
        getRequestLoadingMontion().hide();
    }

    protected boolean checkingInternetConnection() {
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }

    protected void IsLoggedInUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            User user1 = new User();
            user1.setId("current user");
            user1.setEmail(email);
            user1.setPassword(name);
            RealmHelper.saveOrUpdate(user1);
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();
        }
    }
}
