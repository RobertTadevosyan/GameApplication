package game.gameapp.Utils;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class Gamer extends ImageView {
    private GameInterface gameInterface;
    private volatile boolean gameIsPlaying;
    public int gamerHeight;
    OnTouchListener gamerOnTouchListener;
    public int gamerWidth;
    public float gamerX;
    public float gamerY;
    private Point screenSizes;
    OnTouchListener second;

    /* renamed from: square.com.avoidsquare.Utils.Gamer.1 */
    class C02851 implements OnTouchListener {
        C02851() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 6) {
                Gamer.this.setOnTouchListener(Gamer.this.second);
                return false;
            }
            Gamer.this.onTouchActionPerformed(motionEvent);
            return true;
        }
    }

    /* renamed from: square.com.avoidsquare.Utils.Gamer.2 */
    class C02862 implements OnTouchListener {
        C02862() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            Gamer.this.setOnTouchListener(Gamer.this.gamerOnTouchListener);
            return true;
        }
    }

    public void setGameInterface(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
    }

    public Gamer(Context context) {
        super(context);
        this.gameIsPlaying = false;
        this.gamerOnTouchListener = new C02851();
        this.second = new C02862();
        initializing(context);
    }

    public Gamer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gameIsPlaying = false;
        this.gamerOnTouchListener = new C02851();
        this.second = new C02862();
        initializing(context);
    }

    public Gamer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.gameIsPlaying = false;
        this.gamerOnTouchListener = new C02851();
        this.second = new C02862();
        initializing(context);
    }

    private void initializing(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        this.screenSizes = point;
        setFocusableInTouchMode(true);
    }

    public boolean onTouchActionPerformed(MotionEvent event) {
        return true;
    }

    public boolean onTouchActionPerformed(float moveX, float moveY) {
        if (!this.gameIsPlaying) {
            this.gameInterface.startGame();
            this.gameIsPlaying = true;
        }
        setX((this.gamerX - ((float) (getLayoutParams().width / 2))) + moveX);
        setY((this.gamerY - ((float) (getLayoutParams().height / 2))) + moveY);
        return true;
    }

    public boolean isBelongToGamer(float x, float y) {
        if ((getX() != 0.0f || getY() != 0.0f) && Math.pow((double) (x - (getX() + ((float) (getWidth() / 2)))), 2.0d) + Math.pow((double) (y - (getY() + ((float) (getHeight() / 2)))), 2.0d) < Math.pow((double) (getWidth() / 2), 2.0d)) {
            return true;
        }
        return false;
    }

    public void move(float x, float y) {
        this.gamerX = getX();
        this.gamerY = getY();
        this.gamerWidth = getLayoutParams().width;
        this.gamerHeight = getLayoutParams().height;
        setX((this.gamerX - ((float) (this.gamerWidth / 2))) + x);
        setY((this.gamerY - ((float) (this.gamerHeight / 2))) + y);
    }
}
