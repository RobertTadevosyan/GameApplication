package game.gameapp.UIActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.gameapp.Common.CommonAdapter;
import game.gameapp.Common.CommonInterface;
import game.gameapp.Holder.ModelHolder;
import game.gameapp.R;
import game.gameapp.RealmModel.Model;
import game.gameapp.Request.BaseActivity;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.PreferenceUtil;
import game.gameapp.Utils.RealmHelper;
import io.realm.RealmObject;


public class GameOverActivity extends BaseActivity implements CommonInterface {
    //    private AnimationDrawable anim;
    private float check;
    private CommonAdapter commonAdapter;
    private List<Model> list;
    private ListView listView;
    private int positionOfDeletingItem;
    private Button removeAllButton;
    //    private EditText userName;
    private AdView mAdView;
    private AdView secondAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        setContentView((int) R.layout.activity_game_over);
        mAdView = (AdView) findViewById(R.id.adView);
        secondAdView = (AdView) findViewById(R.id.secondAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest secondAdRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        secondAdView.loadAd(secondAdRequest);
        findViewById(R.id.new_game_button).setEnabled(false);
        showProgressDialog();
        this.removeAllButton = (Button) findViewById(R.id.clear_all_list);
        configureViews();
        listViewsConfigurations();
        hideProgressBar();
    }

    private void hideProgressBar() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                GameOverActivity.this.reloadData();
                GameOverActivity.this.findViewById(R.id.new_game_button).setEnabled(true);
            }
        }, 2000);
    }

    private void listViewsConfigurations() {
        this.list = new ArrayList();
        this.listView = (ListView) findViewById(R.id.listView);
        this.commonAdapter = new CommonAdapter(this, this, this.list, ModelHolder.class, R.layout.list_item);
        this.listView.setAdapter(this.commonAdapter);
        reloadData();
    }

    private void reloadData() {
        this.list.clear();
        this.list.addAll(RealmHelper.findAllModels());
        Collections.sort(this.list, new Comparator<Model>() {
            @Override
            public int compare(Model model, Model t1) {
                return t1.getScore().compareTo(model.getScore());
            }
        });
        this.commonAdapter.notifyDataSetChanged();
        if (this.list.isEmpty()) {
            this.removeAllButton.setEnabled(false);
            this.removeAllButton.setTextColor(ContextCompat.getColor(this, R.color.loadingBackground));
            return;
        }
        this.removeAllButton.setEnabled(true);
        this.removeAllButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    }

    private void configureViews() {
    }

    public void newGameButtonOnClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public Context getAdapterApplicationContext() {
        return this;
    }

    public void deleteItem(int position) {
        this.positionOfDeletingItem = position;
        deleteItemAlertShow();
    }

    private void deleteItemAlertShow() {
        Builder adb = new Builder(this);
        adb.setTitle((CharSequence) getString(R.string.delete_item) + " !");
        adb.setMessage(((String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "")) + getString(R.string.do_you_want_to_delete_this_score));
        adb.setIcon((int) R.drawable.user);
        adb.setCancelable(false);
        adb.setPositiveButton((CharSequence) getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RealmHelper.removeFromDatBase((RealmObject) GameOverActivity.this.list.get(GameOverActivity.this.positionOfDeletingItem));
                GameOverActivity.this.reloadData();
            }
        });
        adb.setNegativeButton((CharSequence) getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        adb.show();
    }

    public void onListItemClick(View v) {
    }

    public void removeAllHistory(View view) {
        Builder adb = new Builder(this);
        adb.setTitle((CharSequence) getString(R.string.clear_all_history) + " !");
        adb.setMessage(((String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "")) + getString(R.string.do_you_want_to_clear_all_history_list));
        adb.setIcon((int) R.drawable.user);
        adb.setCancelable(false);
        adb.setPositiveButton((CharSequence) getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RealmHelper.removeAllModelsFomRealm();
                GameOverActivity.this.reloadData();
            }
        });
        adb.setNegativeButton((CharSequence) getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        adb.show();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.list = null;
        this.listView = null;
        this.commonAdapter = null;
        this.removeAllButton = null;
        System.gc();
    }


    public void allUsersScorePage(View view) {
        String highestScore = "";
        if (list.get(0) != null && list.get(0).getScore() != null) {
            highestScore = String.valueOf(list.get(0).getScore());
        } else {
            highestScore = "0.0";
        }
        Intent intent = new Intent(this, AllUsersScoreActivity.class);
        intent.putExtra(CONSTATNTS.HIGH_SCORE, highestScore);
        startActivity(intent);
    }
}
