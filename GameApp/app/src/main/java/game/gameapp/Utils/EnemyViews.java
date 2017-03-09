package game.gameapp.Utils;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EnemyViews extends ImageView {
    private ValueAnimator animator;
    int enemyHeight;
    int enemyWidth;
    private float enemyX;
    private float enemyY;
    private GameInterface gameOverInterface;
    private final int limitOfPixels;
    private int moveX;
    private int moveY;
    private final long periodOfSpeed;
    private int screenHeight;
    private int screenWidth;
    private Stopwatch stopwatch;
    private Timer timer;
    private Random random = new Random();

    /* renamed from: square.com.avoidsquare.Utils.EnemyViews.1 */
    class C02831 implements AnimatorUpdateListener {
        C02831() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            EnemyViews.this.animationUpdateActionPerformed();
        }
    }

    private void setMargins() {
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.setMargins(random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100));
            requestLayout();
        }
    }

    public void setGameOverInterface(GameInterface gameOverInterface) {
        this.gameOverInterface = gameOverInterface;
    }

    public EnemyViews(Context context) {
        super(context);
        this.timer = new Timer();
        this.periodOfSpeed = 1700;
        this.limitOfPixels = 30;
        initializing(context);
    }

    public EnemyViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.timer = new Timer();
        this.periodOfSpeed = 1700;
        this.limitOfPixels = 30;
        initializing(context);
    }

    public EnemyViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.timer = new Timer();
        this.periodOfSpeed = 1700;
        this.limitOfPixels = 30;
        initializing(context);
    }

    public void move() {
        this.stopwatch = new Stopwatch();
        this.animator = ValueAnimator.ofFloat(new float[]{0.0f});
        this.animator.setRepeatCount(-1);
        this.animator.setInterpolator(new LinearInterpolator());
        this.animator.setDuration(1);
        this.animator.addUpdateListener(new C02831());
        this.animator.start();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (EnemyViews.this.moveY < 0 && EnemyViews.this.moveY > -30) {
                    EnemyViews.this.moveY = EnemyViews.this.moveY - 1;
                } else if (EnemyViews.this.moveY > 0 && EnemyViews.this.moveY < 30) {
                    EnemyViews.this.moveY = EnemyViews.this.moveY + 1;
                }
                if (EnemyViews.this.moveX < 0 && EnemyViews.this.moveX > -30) {
                    EnemyViews.this.moveX = EnemyViews.this.moveX - 1;
                } else if (EnemyViews.this.moveX > 0 && EnemyViews.this.moveX < 30) {
                    EnemyViews.this.moveX = EnemyViews.this.moveX + 1;
                }
            }
        }, 0, 1700);
    }

    @RequiresApi(api = 19)
    public void stop() {
        if (this.animator != null) {
            this.animator.pause();
        }
    }

    private void initializing(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        this.moveX = 2;
        this.moveY = 2;
        this.screenWidth = point.x;
        this.screenHeight = point.y / 2;
        setMargins();
    }

    private void animationUpdateActionPerformed() {
        this.enemyHeight = getLayoutParams().height;
        this.enemyWidth = getLayoutParams().width;
        this.enemyX = getX();
        this.enemyY = getY();
        if (this.enemyX < 0.0f || this.enemyX + ((float) this.enemyWidth) > ((float) this.screenWidth)) {
            this.moveX = -this.moveX;
        }
        if (this.enemyY < 0.0f || this.enemyY + ((float) this.enemyHeight) > ((float) this.screenHeight)) {
            this.moveY = -this.moveY;
        }
        setX(this.enemyX + ((float) this.moveX));
        setY(this.enemyY + ((float) this.moveY));
        this.gameOverInterface.gameOverBecauseOfEnemies(this.enemyX, this.enemyY, this.enemyWidth, this.enemyHeight);
        this.gameOverInterface.timerMethod(this.stopwatch.elapsedTime());
    }
}
