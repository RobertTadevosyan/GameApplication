package game.gameapp.UIActivities;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import game.gameapp.Common.CommonAdapter;
import game.gameapp.Common.CommonInterface;
import game.gameapp.Holder.AllUsersHolder;
import game.gameapp.R;
import game.gameapp.RealmModel.Model;
import game.gameapp.Request.BaseActivity;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.PreferenceUtil;
import android.provider.Settings.Secure;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.input;

public class AllUsersScoreActivity extends BaseActivity implements CommonInterface {

    private TextView info_text_view;
    private ListView all_scores_list_view;
    private CommonAdapter commonAdapter;
    private List<Model> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_score);
        info_text_view = (TextView) findViewById(R.id.info_text_view);
        all_scores_list_view = (ListView) findViewById(R.id.all_scores_list_view);
        allUsers = new ArrayList<>();
        commonAdapter = new CommonAdapter(this,this,allUsers, AllUsersHolder.class,R.layout.list_item);
        all_scores_list_view.setAdapter(commonAdapter);
        showProgressDialog();
        String currentUserHighestValue = (String) getIntent().getExtras().get(CONSTATNTS.HIGH_SCORE);
        String userName = (String) PreferenceUtil.readPreference(this,CONSTATNTS.USER_NAME,"");
        if(currentUserHighestValue == null || currentUserHighestValue.isEmpty()){
            return;
        }
        info_text_view.setText("User Name - " + userName + "\n"
                                + "Score - " + currentUserHighestValue );
        String android_id = Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.getParent().child("user_" + android_id).child("user_name").setValue(userName);
        myRef.getParent().child("user_" + android_id).child("user_score").setValue(currentUserHighestValue);
        hideProgressDialog();
        gettingAllUsers(myRef);
    }

    private void gettingAllUsers(DatabaseReference databaseRef) {
        DatabaseReference cities = databaseRef.child("cities");
        Query citiesQuery = cities.orderByKey().startAt(input).endAt(input + "\uf8ff");
        citiesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Model> cities = new ArrayList<Model>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    cities.add((Model) postSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println();
            }
        });
    }

    @Override
    public void deleteItem(int i) {

    }

    @Override
    public Context getAdapterApplicationContext() {
        return null;
    }

    @Override
    public void onListItemClick(View view) {

    }
}
