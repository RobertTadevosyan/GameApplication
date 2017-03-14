package game.gameapp.Request;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

public class BaseActivity extends AppCompatActivity implements RequestLoadingMontionInterface {
    public static int navBarHeight;
    RequestLoadingMontion requestLoadingMontion;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        navBarHeight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    @Override
    public RequestLoadingMontion getRequestLoadingMontion() {
        if (null == requestLoadingMontion){
            if (findViewById(android.R.id.content) != null){
                requestLoadingMontion = new RequestLoadingMontion((ViewGroup)findViewById(android.R.id.content),getApplicationContext());
            }
        }
        return requestLoadingMontion;
    }

    protected void showProgressDialog(){
        getRequestLoadingMontion().show();
    }

    protected void hideProgressDialog(){
        getRequestLoadingMontion().hide();
    }

    protected boolean checkingInternetConnection(){
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ) {
            return true;
        }
        return false;
    }
}
