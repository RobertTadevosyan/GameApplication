package game.gameapp.UIActivities;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.gameapp.Common.CommonAdapter;
import game.gameapp.Common.CommonInterface;
import game.gameapp.Holder.AllUsersHolder;
import game.gameapp.R;
import game.gameapp.RealmModel.Model;
import game.gameapp.Request.BaseActivity;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.PreferenceUtil;

//import game.gameapp.RealmModel.UsersData;

public class AllUsersScoreActivity extends BaseActivity implements CommonInterface {

    private TextView info_text_view;
    private ListView all_scores_list_view;
    private CommonAdapter commonAdapter;
    private List<Model> allUsers;
    private String android_id;
    private AdView mAdView;
    private AdView secondAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_score);
        mAdView = (AdView) findViewById(R.id.adViewAllUsers);
        secondAdView = (AdView) findViewById(R.id.adViewAllUsersSecond);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest secondAdRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        secondAdView.loadAd(secondAdRequest);
        configureViews();
    }

    private void configureViews() {
//        pGif = (GifView) findViewById(R.id.progressBar);
        info_text_view = (TextView) findViewById(R.id.info_text_view);
        all_scores_list_view = (ListView) findViewById(R.id.all_scores_list_view);
        allUsers = new ArrayList<>();
        commonAdapter = new CommonAdapter(getApplicationContext(), this, allUsers, AllUsersHolder.class, R.layout.list_item);
        all_scores_list_view.setAdapter(commonAdapter);
        findViewById(R.id.no_internet_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternetConnectionAndReload();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnectionAndReload();
    }

    private void checkInternetConnectionAndReload() {
        boolean networkIsAvailable = checkingInternetConnection();
        if (networkIsAvailable) {
            findViewById(R.id.no_internet_layout).setVisibility(View.GONE);
            info_text_view.setVisibility(View.VISIBLE);
            all_scores_list_view.setVisibility(View.VISIBLE);
            ifNetworkAvailable();
        } else {
            findViewById(R.id.no_internet_layout).setVisibility(View.VISIBLE);
//            pGif.setImageResource(R.drawable.no_internet);
            info_text_view.setVisibility(View.GONE);
            all_scores_list_view.setVisibility(View.GONE);
            return;
        }
    }

    private void ifNetworkAvailable() {
        String currentUserHighestValue = (String) getIntent().getExtras().get(CONSTATNTS.HIGH_SCORE);
        String userName = (String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "");
        if (currentUserHighestValue == null || currentUserHighestValue.isEmpty()) {
            return;
        }
        info_text_view.setText(R.string.all_scores);
        android_id = Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        PreferenceUtil.saveInSharedPreference(this,CONSTATNTS.USER_UNIQ_ID,android_id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        myRef.child(CONSTATNTS.USERS_DIRECTORY + android_id).child(CONSTATNTS.U_ID).setValue(android_id);
        myRef.child(CONSTATNTS.USERS_DIRECTORY + android_id).child(CONSTATNTS.U_NAME).setValue(userName);
        myRef.child(CONSTATNTS.USERS_DIRECTORY + android_id).child(CONSTATNTS.U_SCORE).setValue(currentUserHighestValue);
        gettingAllUsers(myRef.getRoot());
    }

    private void gettingAllUsers(DatabaseReference databaseRef) {
        showProgressDialog();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Model> users = (HashMap<String, Model>) dataSnapshot.child("Users").getValue();
                Collection<Model> models = users.values();
                allUsers.clear();
                Object[] usersInArray = models.toArray();
                for (int i = 0; i < usersInArray.length; i++) {
                    Model m = new Model();
                    m.setId((String) ((HashMap) usersInArray[i]).get(CONSTATNTS.U_ID));
                    m.setName((String) ((HashMap) usersInArray[i]).get(CONSTATNTS.U_NAME));
                    m.setScore(Double.valueOf((String) ((HashMap) usersInArray[i]).get(CONSTATNTS.U_SCORE)));
                    allUsers.add(m);
                }
                Collections.sort(allUsers, new Comparator<Model>() {
                    @Override
                    public int compare(Model model, Model t1) {
                        return t1.getScore().compareTo(model.getScore());
                    }
                });
//                RealmHelper.saveOrUpdate(new UsersData((RealmList<Model>) allUsers, android_id));
                commonAdapter.notifyDataSetChanged();
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                hideProgressDialog();
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void deleteItem(int i) {

    }

    @Override
    public Context getAdapterApplicationContext() {
        return AllUsersScoreActivity.this;
    }

    @Override
    public void onListItemClick(View view) {

    }
}
