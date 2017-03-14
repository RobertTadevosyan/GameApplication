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


public class ModelHolder implements CommonAdapterInterface {
    private ImageView deleteIcon;
    private int position;
    private TextView uName;
    private TextView uScore;
    private ImageView icon;
    private Context context;

    public void onClick(View v) {
    }

    public void setView(View row, final CommonInterface commonInterface, Context context) {
        this.uName = (TextView) row.findViewById(R.id.userName);
        this.uScore = (TextView) row.findViewById(R.id.userScore);
        this.icon = (ImageView) row.findViewById(R.id.icon_image_view);
        this.deleteIcon = (ImageView) row.findViewById(R.id.deleteItem);
        row.findViewById(R.id.position_text_view).setVisibility(View.GONE);
        this.context = context;
        this.deleteIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commonInterface.deleteItem(ModelHolder.this.position);
            }
        });
    }

    public void reloadRowWithData(Object object, int position) {
        this.position = position;
        Model model = (Model) object;
        if(position == 0) {
            this.icon.setImageResource(R.drawable.gold_medal);
            this.icon.setColorFilter(ContextCompat.getColor(context,android.R.color.transparent));
        }else if(position == 1) {
            this.icon.setImageResource(R.drawable.silver_medal);
            this.icon.setColorFilter(ContextCompat.getColor(context,android.R.color.transparent));
        }else if(position == 2) {
            this.icon.setImageResource(R.drawable.bronze_medal);
            this.icon.setColorFilter(ContextCompat.getColor(context,android.R.color.transparent));
        }else if(position >= 3){
            this.icon.setColorFilter(ContextCompat.getColor(context,android.R.color.darker_gray));
        }
        this.uName.setText(context.getResources().getString(R.string.name) + model.getName());
        this.uScore.setText(context.getResources().getString(R.string.score) + String.valueOf(model.getScore()));
    }
}
