package game.gameapp.Request;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import game.gameapp.R;


public class RequestLoadingMontion extends ProgressBar {
    private RelativeLayout relativeLayout;

    public RequestLoadingMontion(Context context) {
        super(context);
    }

    public RequestLoadingMontion(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RequestLoadingMontion(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RequestLoadingMontion(ViewGroup topView, Context context) {
        super(context);
        this.relativeLayout = new RelativeLayout(context);
        this.relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        this.relativeLayout.setGravity(17);
        setLayoutParams(new LayoutParams(-2, -2));
        this.relativeLayout.addView(this);
        topView.addView(this.relativeLayout);
        this.relativeLayout.setClickable(true);
        this.relativeLayout.setVisibility(4);
        this.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.loadingBackground));
    }

    public void show() {
        this.relativeLayout.setVisibility(0);
    }

    public void hide() {
        this.relativeLayout.setVisibility(4);
    }
}
