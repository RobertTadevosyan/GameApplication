package game.gameapp.RealmModel;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anahit on 3/14/17.
 */

public class UsersData extends RealmObject {
    @PrimaryKey
    private String id;
    private List<Model> allUsers;

    public UsersData() {
    }

    public UsersData(List<Model> allUsers, String id) {
        this.allUsers = allUsers;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Model> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<Model> allUsers) {
        this.allUsers = allUsers;
    }
}
