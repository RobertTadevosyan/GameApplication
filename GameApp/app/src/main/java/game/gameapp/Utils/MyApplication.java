package game.gameapp.Utils;

import android.app.Application;
import android.content.Context;
import io.realm.Realm;
import io.realm.RealmConfiguration.Builder;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Realm.setDefaultConfiguration(new Builder((Context) this).build());
    }
}
