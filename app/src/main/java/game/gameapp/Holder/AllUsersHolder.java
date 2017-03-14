package game.gameapp.Holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import game.gameapp.Common.CommonAdapterInterface;
import game.gameapp.Common.CommonInterface;
import game.gameapp.R;
import game.gameapp.RealmModel.Model;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.PreferenceUtil;


public class AllUsersHolder implements CommonAdapterInterface {
    private ImageView deleteIcon;
    private int position;
    private TextView uName;
    private TextView uScore;
    private ImageView icon;
    private Context context;
    private String userName;
    private TextView position_text_view;
    private String user_id;

    public void onClick(View v) {
    }

    public void setView(View row, final CommonInterface commonInterface, Context context) {
        this.uName = (TextView) row.findViewById(R.id.userName);
        this.uScore = (TextView) row.findViewById(R.id.userScore);
        this.icon = (ImageView) row.findViewById(R.id.icon_image_view);
        this.deleteIcon = (ImageView) row.findViewById(R.id.deleteItem);
        this.position_text_view = (TextView) row.findViewById(R.id.position_text_view);
        this.context = context;
        this.deleteIcon.setVisibility(View.GONE);
        this.userName = (String) PreferenceUtil.readPreference(context, CONSTATNTS.USER_NAME, "");
        this.user_id = (String) PreferenceUtil.readPreference(context, CONSTATNTS.USER_UNIQ_ID, "");
    }

    public void reloadRowWithData(Object object, int position) {
        this.position = position;
        Model model = (Model) object;
        if (position == 0) {
            this.icon.setImageResource(R.drawable.gold_medal);
            this.icon.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        } else if (position == 1) {
            this.icon.setImageResource(R.drawable.silver_medal);
            this.icon.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        } else if (position == 2) {
            this.icon.setImageResource(R.drawable.bronze_medal);
            this.icon.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        } else if (position >= 3) {
            this.icon.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray));
        }
        this.uName.setText("Name - " + model.getName());
        this.uScore.setText("Score - " + String.valueOf(model.getScore()));
        this.position_text_view.setText(String.valueOf(position + 1));
        if (model.getName().equals(userName) && model.getId().equals(user_id)) {
            this.uName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            this.uScore.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            this.position_text_view.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            this.uName.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            this.uScore.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            this.position_text_view.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }
}
