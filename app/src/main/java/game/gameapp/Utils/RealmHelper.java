package game.gameapp.Utils;

import java.util.List;

import game.gameapp.RealmModel.Model;
//import game.gameapp.RealmModel.UsersData;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {

    public static void saveOrUpdate(final RealmObject object) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(object);
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
        }).start();
    }

    public static List<Model> findAllModels() {
        return Realm.getDefaultInstance().where(Model.class).findAll();
    }

//    public static List<UsersData> findAllUsers() {
//        return Realm.getDefaultInstance().where(UsersData.class).findAll();
//    }


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
