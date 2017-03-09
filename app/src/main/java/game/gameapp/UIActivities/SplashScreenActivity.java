package game.gameapp.UIActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import game.gameapp.R;


public class SplashScreenActivity extends AppCompatActivity {
    Thread splashTread;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setFormat(1);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.animation_splash_screen);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);
        this.splashTread = new Thread(new Runnable() {
            @Override
            public void run() {
                int waited = 0;
                while (waited < 3500) {
                    try {
                        Thread.sleep(100);
                        waited += 100;
                    } catch (InterruptedException e) {
                        SplashScreenActivity.this.finish();
                        return;
                    } catch (Throwable th) {
                        SplashScreenActivity.this.finish();
                    }
                }
                Intent intent = new Intent(SplashScreenActivity.this, StartAcivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                SplashScreenActivity.this.startActivity(intent);
                SplashScreenActivity.this.finish();
                SplashScreenActivity.this.finish();
            }
        });
        this.splashTread.start();
    }
}
