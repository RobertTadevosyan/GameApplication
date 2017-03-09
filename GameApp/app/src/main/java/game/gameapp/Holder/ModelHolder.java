package game.gameapp.Holder;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import game.gameapp.Common.CommonAdapterInterface;
import game.gameapp.Common.CommonInterface;
import game.gameapp.R;
import game.gameapp.RealmModel.Model;


public class ModelHolder implements CommonAdapterInterface {
    private ImageView delete;
    private int position;
    private TextView uName;
    private TextView uScore;

    /* renamed from: square.com.avoidsquare.Holder.ModelHolder.1 */
    class C02681 implements OnClickListener {
        final /* synthetic */ CommonInterface val$commonInterface;

        C02681(CommonInterface commonInterface) {
            this.val$commonInterface = commonInterface;
        }

        public void onClick(View view) {
            this.val$commonInterface.deleteItem(ModelHolder.this.position);
        }
    }

    public void onClick(View v) {
    }

    public void setView(View row, CommonInterface commonInterface, Context context) {
        this.uName = (TextView) row.findViewById(R.id.userName);
        this.uScore = (TextView) row.findViewById(R.id.userScore);
        this.delete = (ImageView) row.findViewById(R.id.deleteItem);
        this.delete.setOnClickListener(new C02681(commonInterface));
    }

    public void reloadRowWithData(Object object, int position) {
        this.position = position;
        Model model = (Model) object;
        this.uName.setText(model.getName());
        this.uScore.setText(String.valueOf(model.getScore()));
    }
}
