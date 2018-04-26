package hu.vadasz.peter.knockmessenger.DataPersister.Managers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerSideDatabase;
import lombok.Getter;

/**
 * This class provides cache and data from local database.
 */

public class UserDataManager {

    public static final boolean IS_FRIEND = true;

    private DataBaseManager dataBaseManager;
    private ServerSideDatabase serverSideDatabase;

    @Getter
    private User user;

    @Getter
    private List<Friend> friends;

    public UserDataManager(DataBaseManager dataBaseManager, ServerSideDatabase serverSideDatabase) {
        this.dataBaseManager = dataBaseManager;
        this.serverSideDatabase = serverSideDatabase;
        friends = new ArrayList<>();
    }

    public void trySaveUser(String telephone, ValueEventListener listener) {
        findUser(telephone, listener);
    }

    public void findUser(String telephone, ValueEventListener listener) {
        serverSideDatabase.findUserByTelephone(telephone, listener);
    }

    public void findUser(Friend friend, ValueEventListener listener) {
        serverSideDatabase.findUser(friend.getTel(), listener);
    }

    public void registrate(User user) {
        Long id = dataBaseManager.insertUser(user);
        user.setId(id);
        this.user = user;
        serverSideDatabase.addUser(user);
    }

    public void loadUser() {
        user = dataBaseManager.findUser();
    }

    public void updateUser(User user) {
        dataBaseManager.updateUser(user);
        this.user = user;
        serverSideDatabase.updateUser(user);
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void deleteUser() {
        dataBaseManager.deleteUser(user);
        serverSideDatabase.deleteUser(user);
        user = null;
    }


    /// FRIENDS

    public void requestAllContacts(ValueEventListener listener) {
        serverSideDatabase.findAllUsers(listener);
    }

    public void loadFriends() {
        friends = dataBaseManager.loadAllFriends();
    }

    public synchronized void insertFriend(Friend friend) {
        friend.setId(dataBaseManager.insertFriend(friend));
        friends.add(friend);
    }

    public synchronized void deleteFriend(Friend friend) {
        for (Friend f : friends) {
            if (f.equals(friend)) {
                friend.setId(f.getId());
                break;
            }
        }
        dataBaseManager.deleteFriend(friend);
        friends.remove(friend);
    }

    public void deleteAllFriends() {
        friends.clear();
        dataBaseManager.deleteAllFriend();
    }

    public synchronized boolean isFriend(Friend friend) {
        return friends.contains(friend);
    }

    public boolean isFriend(String tel) {
        for (Friend friend :  friends) {
            if (friend.getTel().equals(tel)) {
                return IS_FRIEND;
            }
        }

        return !IS_FRIEND;
    }

    public Friend getFriendByTelephone(String telephone) {
        for (Friend friend : friends) {
            if (friend.getTel().equals(telephone)) {
                return friend;
            }
        }

        return null;
    }

    public synchronized void updateFriend(Friend friend) {
        for (Friend f : friends) {
            if (f.equals(friend)) {
                f.setName(friend.getName());
                friend.setId(f.getId());
                break;
            }
        }

        dataBaseManager.updateFriend(friend);
    }

    public void setServerDataChangeListener(ChildEventListener listener) {
        serverSideDatabase.setUserChangedListener(listener);
    }

    public void removeServerDataChaneListener(ChildEventListener listener) {
        serverSideDatabase.removeEventListener(listener);
    }

    /// FRIENDS -- END

}
