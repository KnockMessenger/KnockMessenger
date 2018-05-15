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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static final boolean IS_FRIEND = true;

    private DataBaseManager dataBaseManager;
    private ServerSideDatabase serverSideDatabase;

    @Getter
    private User user;

    @Getter
    private List<Friend> friends;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public UserDataManager(DataBaseManager dataBaseManager, ServerSideDatabase serverSideDatabase) {
        this.dataBaseManager = dataBaseManager;
        this.serverSideDatabase = serverSideDatabase;
        friends = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method tries to save user data by accessing the server.
     * @param telephone the user's telephone.
     * @param listener the class which receives th server's answer.
     */

    public void trySaveUser(String telephone, ValueEventListener listener) {
        findUser(telephone, listener);
    }

    /**
     * This method send a request to the server to give the data of a specified user.
     * @param telephone the telephone of the requested user.
     * @param listener the class which receivers the server's answer.
     */

    public void findUser(String telephone, ValueEventListener listener) {
        serverSideDatabase.findUserByTelephone(telephone, listener);
    }

    /**
     * This method send a request to the server to give the data of a specified user.
     * @param friend the friend to find.
     * @param listener the class which receivers the server's answer.
     */

    public void findUser(Friend friend, ValueEventListener listener) {
        serverSideDatabase.findUser(friend.getTel(), listener);
    }

    /**
     * This method registrates the user.
     * @param user the user to registrate.
     */

    public void registrate(User user) {
        Long id = dataBaseManager.insertUser(user);
        user.setId(id);
        this.user = user;
        serverSideDatabase.addUser(user);
    }

    /**
     * This method loads the user' data.
     */

    public void loadUser() {
        user = dataBaseManager.findUser();
    }

    /**
     * This method updates the user's data.
     * @param user the new data.
     */

    public void updateUser(User user) {
        dataBaseManager.updateUser(user);
        this.user = user;
        serverSideDatabase.updateUser(user);
    }

    /**
     * @return true if the user already registered.
     */

    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * This method deletes the user's data.
     */

    public void deleteUser() {
        dataBaseManager.deleteUser(user);
        serverSideDatabase.deleteUser(user);
        user = null;
    }

    /// FRIENDS

    /**
     * This method send a request to the server to receive users.
     * @param listener the class which receives the server's answer.
     */

    public void requestAllContacts(ValueEventListener listener) {
        serverSideDatabase.findAllUsers(listener);
    }

    /**
     * This method loads the user's friends.
     */

    public void loadFriends() {
        friends = dataBaseManager.loadAllFriends();
    }

    /**
     * This method saves a new friend.
     * @param friend the friend to save.
     */

    public synchronized void insertFriend(Friend friend) {
        friend.setId(dataBaseManager.insertFriend(friend));
        friends.add(friend);
    }

    /**
     * This method deletes a friend.
     * @param friend the friend to delete.
     */

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

    /**
     * This method deletes all the friends.
     */

    public void deleteAllFriends() {
        friends.clear();
        dataBaseManager.deleteAllFriend();
    }

    /**
     * This method decides whether a user is a friend or not.
     * @param friend the examined user.
     * @return true if the user is a friend.
     */

    public synchronized boolean isFriend(Friend friend) {
        return friends.contains(friend);
    }

    /**
     * This method decides whether a user is a friend or not.
     * @param tel the examined user's telephone.
     * @return true if the user is a friend.
     */

    public boolean isFriend(String tel) {
        for (Friend friend :  friends) {
            if (friend.getTel().equals(tel)) {
                return IS_FRIEND;
            }
        }

        return !IS_FRIEND;
    }

    /**
     * This method queries a friend by telephone.
     * @param telephone the telephone of the friend.
     * @return the found friend, or null if not found.
     */

    public Friend getFriendByTelephone(String telephone) {
        for (Friend friend : friends) {
            if (friend.getTel().equals(telephone)) {
                return friend;
            }
        }

        return null;
    }

    /**
     * This method updates a friend's data.
     * @param friend the friend to update.
     */

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

    /**
     * This method sets the server answer receiver class.
     * @param listener the listener to be set.
     */

    public void setServerDataChangeListener(ChildEventListener listener) {
        serverSideDatabase.setUserChangedListener(listener);
    }

    /**
     * This method deletes the server answer receiver class.
     * @param listener the listener to be deleted.
     */

    public void removeServerDataChaneListener(ChildEventListener listener) {
        serverSideDatabase.removeEventListener(listener);
    }

    /// FRIENDS -- END

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
