package game.gameapp.UIActivities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import game.gameapp.R;
import game.gameapp.RealmModel.Model;
import game.gameapp.Request.BaseActivity;
import game.gameapp.Utils.CONSTATNTS;
import game.gameapp.Utils.EnemyViews;
import game.gameapp.Utils.GameInterface;
import game.gameapp.Utils.Gamer;
import game.gameapp.Utils.PreferenceUtil;
import game.gameapp.Utils.RealmHelper;

public class MainActivity extends BaseActivity implements GameInterface {
    private double endTime;
    private EnemyViews enemyViewsFirst;
    private EnemyViews enemyViewsFourth;
    private EnemyViews enemyViewsSecond;
    private EnemyViews enemyViewsThird;
    private boolean gameIsPlaying;
    private Gamer gamer;
    OnTouchListener gamerOnTouchListener;
    private float glyuk;
    private Integer id;
    private ImageView joystick;
    private MediaPlayer mediaPlayer;
    private Model model;
    private int screenSizes_x;
    private int screenSizes_y;
    OnTouchListener second;
    private TextView timerTextView;
    private float topAndBottomBorderLength;
    private String uName;

    public MainActivity() {
        this.gameIsPlaying = false;
        this.gamerOnTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity.this.findViewById(R.id.helpTextView).setVisibility(View.GONE);
                if (motionEvent.getAction() == 6) {
                    MainActivity.this.joystick.setOnTouchListener(MainActivity.this.second);
                    return false;
                }
                float jX = MainActivity.this.joystick.getX();
                float jY = MainActivity.this.joystick.getY();
                if (!MainActivity.this.gameIsPlaying) {
                    MainActivity.this.startGame();
                    MainActivity.this.gameIsPlaying = true;
                }
                if (!MainActivity.this.gameIsPlaying) {
                    return true;
                }
                MainActivity.this.gamer.move(motionEvent.getX(), motionEvent.getY());
                MainActivity.this.gameOverBecauseOfOutOfBounds();
                MainActivity.this.joystick.setX((jX - ((float) (MainActivity.this.joystick.getLayoutParams().width / 2))) + motionEvent.getX());
                MainActivity.this.joystick.setY((jY - ((float) (MainActivity.this.joystick.getLayoutParams().height / 2))) + motionEvent.getY());
                return true;
            }
        };

        this.second = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0) {
                    return false;
                }
                MainActivity.this.joystick.setOnTouchListener(MainActivity.this.gamerOnTouchListener);
                return true;
            }
        };
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.model = new Model();
        this.mediaPlayer = MediaPlayer.create(this, R.raw.boom);
        configViews();
        this.topAndBottomBorderLength = getResources().getDimension(R.dimen.top_margin_of_game_view);
        this.glyuk = getResources().getDimension(R.dimen.glyuk);
        this.uName = (String) PreferenceUtil.readPreference(this, CONSTATNTS.USER_NAME, "");
        this.id = (Integer) PreferenceUtil.readPreference(this, CONSTATNTS.ID, Integer.valueOf(0));
        this.id = Integer.valueOf(this.id.intValue() + 1);
    }

    protected void onResume() {
        super.onResume();
        setRandomMarginsToEnemyViews();
    }

    protected void onPause() {
        super.onPause();
    }

    private void configViews() {
        this.gamer = (Gamer) findViewById(R.id.gamer);
        this.joystick = (ImageView) findViewById(R.id.gamerJoystick);
        this.joystick.setOnTouchListener(this.gamerOnTouchListener);
        this.gamer.setGameInterface(this);
        this.enemyViewsFirst = (EnemyViews) findViewById(R.id.first);
        this.enemyViewsSecond = (EnemyViews) findViewById(R.id.second);
        this.enemyViewsThird = (EnemyViews) findViewById(R.id.third);
        this.enemyViewsFourth = (EnemyViews) findViewById(R.id.fourth);
        this.timerTextView = (TextView) findViewById(R.id.top_view);
        this.enemyViewsFirst.setGameOverInterface(this);
        this.enemyViewsSecond.setGameOverInterface(this);
        this.enemyViewsThird.setGameOverInterface(this);
        this.enemyViewsFourth.setGameOverInterface(this);
        configureViewsSizes();
    }

    private void configureViewsSizes() {
        this.screenSizes_x = ((Integer) PreferenceUtil.readPreference(this, CONSTATNTS.POINTS_X, Integer.valueOf(0))).intValue();
        this.screenSizes_y = ((Integer) PreferenceUtil.readPreference(this, CONSTATNTS.POINTS_Y, Integer.valueOf(0))).intValue();
        int firstSize = this.screenSizes_x / 10;
        int secondSize = this.screenSizes_x / 7;
        int thirdSize = this.screenSizes_x / 9;
        int fourthSize = this.screenSizes_x / 6;
        this.enemyViewsFirst.getLayoutParams().width = firstSize;
        this.enemyViewsFirst.getLayoutParams().height = firstSize;
        this.enemyViewsSecond.getLayoutParams().width = secondSize;
        this.enemyViewsSecond.getLayoutParams().height = secondSize;
        this.enemyViewsThird.getLayoutParams().width = thirdSize;
        this.enemyViewsThird.getLayoutParams().height = thirdSize;
        this.enemyViewsFourth.getLayoutParams().width = fourthSize;
        this.enemyViewsFourth.getLayoutParams().height = fourthSize;
        this.gamer.getLayoutParams().width = firstSize;
        this.gamer.getLayoutParams().height = firstSize;
        this.joystick.getLayoutParams().width = firstSize;
        this.joystick.getLayoutParams().height = firstSize;
    }

    @RequiresApi(api = 19)
    public void gameOverBecauseOfEnemies(float enemX, float enemyY, int enemyWidth, int enemyHeight) {
        if (this.gamer.isBelongToGamer(this.glyuk + enemX, this.glyuk + enemyY) || this.gamer.isBelongToGamer(this.glyuk + enemX, (((float) enemyHeight) + enemyY) - this.glyuk) || this.gamer.isBelongToGamer((enemX - this.glyuk) + ((float) enemyWidth), this.glyuk + enemyY) || this.gamer.isBelongToGamer((enemX - this.glyuk) + ((float) enemyWidth), (enemyY - this.glyuk) + ((float) enemyHeight))) {
            stop();
        }
    }

    private void setRandomMarginsToEnemyViews(){
        this.enemyViewsFirst.setMargins();
        this.enemyViewsFirst.setMargins();
        this.enemyViewsFirst.setMargins();
        this.enemyViewsFirst.setMargins();
    }

    public void startGame() {
        this.enemyViewsFirst.move();
        this.enemyViewsSecond.move();
        this.enemyViewsThird.move();
        this.enemyViewsFourth.move();
    }

    public void gameOverBecauseOfOutOfBounds() {
        if (this.gamer.getX() <= 0.0f || this.gamer.getX() >= ((float) (this.screenSizes_x - this.gamer.getWidth())) || this.gamer.getY() <= 0.0f || this.gamer.getY() + ((float) this.gamer.getHeight()) >= ((float) (this.screenSizes_y / 2))) {
            stop();
        }
    }

    public void timerMethod(double milliseconds) {
        this.timerTextView.setText(String.valueOf(milliseconds));
        this.endTime = milliseconds;
    }

    @RequiresApi(api = 19)
    private void stop() {
        this.joystick.setOnTouchListener(null);
        this.gameIsPlaying = false;
        this.gamer.setImageResource(R.drawable.boom_balloon);
        this.enemyViewsFirst.stop();
        this.enemyViewsSecond.stop();
        this.enemyViewsThird.stop();
        this.enemyViewsFourth.stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.this.mediaPlayer != null) {
                    MainActivity.this.mediaPlayer.start();
                }
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.startActivity(new Intent(MainActivity.this, GameOverActivity.class));
                MainActivity.this.saveInRealM();
                MainActivity.this.finish();
            }
        }, 100);
    }

    private void saveInRealM() {
        PreferenceUtil.saveInSharedPreference(this, CONSTATNTS.ID, this.id);
        this.model.setId(String.valueOf(this.id));
        this.model.setName(this.uName);
        this.model.setScore(Double.valueOf(this.endTime));
        RealmHelper.saveOrUpdate(this.model);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.enemyViewsFirst = null;
        this.model = null;
        this.enemyViewsSecond = null;
        this.enemyViewsThird = null;
        this.enemyViewsFourth = null;
        this.gamer = null;
        this.timerTextView = null;
        System.gc();
    }
}
