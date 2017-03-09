package game.gameapp.UIActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import game.gameapp.R;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.PreferenceUtil;

public class StartAcivity extends AppCompatActivity {
    private Point point;
    private EditText userNameEditText;
    private AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_start_acivity);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        this.userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        checkingUserName();
        gettingScreenSizes();
    }

    private void checkingUserName() {
        if (((String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "")).isEmpty() || ((String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "")).equals(CONSTATNTS.UNNAMED)) {
            this.userNameEditText.setVisibility(View.GONE);
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

    private void goToMainActivity() {
        if (this.userNameEditText.getText().toString().isEmpty()) {
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
        goToMainActivity();
    }

    public void showAlert() {
        Builder adb = new Builder(this);
        adb.setTitle((CharSequence) "Gamer checking .");
        final String uName = (String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "");
        adb.setMessage("Are you " + uName + " ?");
        adb.setIcon((int) R.drawable.user);
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
}
