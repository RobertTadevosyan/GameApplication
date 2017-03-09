package game.gameapp.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import game.gameapp.R;

public class UserAlertHelper {
    public static String uName;

    /* renamed from: square.com.avoidsquare.Utils.UserAlertHelper.1 */
    static class C02881 implements OnClickListener {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ EditText input;

        C02881(EditText editText, Activity activity) {
            this.input = editText;
            this.val$activity = activity;
        }

        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }

    static {
        uName = CONSTATNTS.UNNAMED;
    }

    public static void showSecondAlert(final Activity activity) {
        Builder adb = new Builder(activity);
        final EditText input = new EditText(activity);
        input.setLayoutParams(new LayoutParams(-1, -1));
        adb.setView(input);
        adb.setTitle((CharSequence) "Write your name please !!!");
        adb.setMessage((CharSequence) "What is your name ?");
        adb.setIcon((int) R.drawable.user);
        adb.setCancelable(false);
        adb.setPositiveButton((CharSequence) "OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!input.getText().toString().isEmpty()) {
                    UserAlertHelper.uName = input.getText().toString();
                }
                PreferenceUtil.saveInSharedPreference(activity, CONSTATNTS.USER_NAME, UserAlertHelper.uName);
            }
        });
        adb.show();
    }
}
