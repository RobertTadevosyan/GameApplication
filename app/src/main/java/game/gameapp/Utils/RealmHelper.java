package game.gameapp.Utils;

import java.util.List;

import game.gameapp.RealmModel.Model;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {

    /* renamed from: square.com.avoidsquare.Utils.RealmHelper.1 */
    static class C02871 implements Runnable {
        final /* synthetic */ RealmObject val$object;

        C02871(RealmObject realmObject) {
            this.val$object = realmObject;
        }

        public void run() {
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(this.val$object);
                realm.commitTransaction();
                if (realm != null) {
                    realm.close();
                }
            } catch (Throwable th) {
                if (realm != null) {
                    realm.close();
                }
            }
        }
    }

    public static void saveOrUpdate(RealmObject object) {
        new Thread(new C02871(object)).start();
    }

    public static List<Model> findAllModels() {
        return Realm.getDefaultInstance().where(Model.class).findAll();
    }

    public static void removeFromDatBase(RealmObject object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        object.removeFromRealm();
        realm.commitTransaction();
    }

    public static void removeAllModelsFomRealm() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Model> results = realm.where(Model.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }
}
