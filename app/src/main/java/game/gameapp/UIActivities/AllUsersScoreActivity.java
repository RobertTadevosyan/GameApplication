package game.gameapp.UIActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import game.gameapp.R;
import game.gameapp.Request.BaseActivity;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.PreferenceUtil;

public class AllUsersScoreActivity extends BaseActivity {

    TextView info_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_score);
        info_text_view = (TextView) findViewById(R.id.info_text_view);
        showProgressDialog();
        String currentUserHighestValue = (String) getIntent().getExtras().get(CONSTATNTS.HIGH_SCORE);
        String userName = (String) PreferenceUtil.readPreference(this,CONSTATNTS.USER_NAME,"");
        if(currentUserHighestValue == null || currentUserHighestValue.isEmpty()){
            return;
        }
        info_text_view.setText("User Name - " + userName + "\n"
                                + "Score - " + currentUserHighestValue );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.getParent().child("name").setValue(userName);
        myRef.getParent().child("score").setValue(currentUserHighestValue);
        hideProgressDialog();
    }
}
