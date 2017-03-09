package game.gameapp.Request;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

public class BaseActivity extends AppCompatActivity implements RequestLoadingMontionInterface {
    public static int navBarHeight;
    RequestLoadingMontion requestLoadingMontion;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        navBarHeight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    public RequestLoadingMontion getRequestLoadingMontion() {
        if (this.requestLoadingMontion == null && findViewById(16908290) != null) {
            this.requestLoadingMontion = new RequestLoadingMontion((ViewGroup) findViewById(16908290), getApplicationContext());
        }
        return this.requestLoadingMontion;
    }
}
