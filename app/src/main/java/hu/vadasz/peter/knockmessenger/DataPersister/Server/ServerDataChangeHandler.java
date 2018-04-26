package hu.vadasz.peter.knockmessenger.DataPersister.Server;

import java.util.ArrayList;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;

/**
 * Created by Peti on 2018. 04. 15..
 */

public class ServerDataChangeHandler {

    public interface FriendChangeListener {
        void friendChanged(Friend friend);
        void friendAdded(Friend friend);
        void friendRemoved(Friend friend);
    }

    public interface MessageReceivedListener {
        void messageReceived();
    }

    private ArrayList<FriendChangeListener> friendChangeListeners;
    private ArrayList<MessageReceivedListener> messageReceivedListeners;

    public ServerDataChangeHandler() {
        friendChangeListeners = new ArrayList<>();
        messageReceivedListeners = new ArrayList<>();
    }

    public synchronized void addFriendDataChangeListener(FriendChangeListener friendChangeListener) {
        friendChangeListeners.add(friendChangeListener);
    }

    public synchronized void removeFriendDataChangeListener(FriendChangeListener friendChangeListener) {
        friendChangeListeners.remove(friendChangeListener);
    }

    public synchronized void addMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
        messageReceivedListeners.add(messageReceivedListener);
    }

    public synchronized void removeMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
        messageReceivedListeners.remove(messageReceivedListener);
    }

    public synchronized void friendChanged(Friend friend) {
        for (FriendChangeListener listener : friendChangeListeners) {
            listener.friendChanged(friend);
        }
    }

    public synchronized void friendDeleted(Friend friend) {
        for (FriendChangeListener listener : friendChangeListeners) {
            listener.friendRemoved(friend);
        }
    }

    public synchronized void friendAdded(Friend friend) {
        for (FriendChangeListener listener : friendChangeListeners) {
            listener.friendAdded(friend);
        }
    }

    public void messageReceived() {
        for (MessageReceivedListener listener: messageReceivedListeners) {
            listener.messageReceived();
        }
    }
}
