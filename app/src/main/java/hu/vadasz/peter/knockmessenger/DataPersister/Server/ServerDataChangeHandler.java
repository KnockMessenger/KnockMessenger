package hu.vadasz.peter.knockmessenger.DataPersister.Server;

import java.util.ArrayList;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;

/**
 * This class represents an action which is fired when a user's data changed or new message arrived.
 */

public class ServerDataChangeHandler {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This interface represents a listener which is listening for user data change.
     */

    public interface FriendChangeListener {
        void friendChanged(Friend friend);
        void friendAdded(Friend friend);
        void friendRemoved(Friend friend);
    }

    /**
     * This interface represents a listener which is listening for message arriving.
     */

    public interface MessageReceivedListener {
        void messageReceived();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private ArrayList<FriendChangeListener> friendChangeListeners;
    private ArrayList<MessageReceivedListener> messageReceivedListeners;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ServerDataChangeHandler() {
        friendChangeListeners = new ArrayList<>();
        messageReceivedListeners = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method registers a new FriendDataChangeLister.
     * @param friendChangeListener the listener to register.
     */

    public synchronized void addFriendDataChangeListener(FriendChangeListener friendChangeListener) {
        friendChangeListeners.add(friendChangeListener);
    }

    /**
     * This method removes a FriendChangeListener.
     * @param friendChangeListener the listener to remove.
     */

    public synchronized void removeFriendDataChangeListener(FriendChangeListener friendChangeListener) {
        friendChangeListeners.remove(friendChangeListener);
    }

    /**
     * This method registers a MessageReceivedListener.
     * @param messageReceivedListener the listener to register.
     */

    public synchronized void addMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
        messageReceivedListeners.add(messageReceivedListener);
    }

    /**
     * This method removes a MessageReceivedListener.
     * @param messageReceivedListener the listener to be removed.
     */

    public synchronized void removeMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
        messageReceivedListeners.remove(messageReceivedListener);
    }

    /**
     * This method is called when a friend's data changed.
     * @param friend the changed friend.
     */

    public synchronized void friendChanged(Friend friend) {
        for (FriendChangeListener listener : friendChangeListeners) {
            listener.friendChanged(friend);
        }
    }

    /**
     * This method is called when a friend deleted.
     * @param friend the deleted friend.
     */

    public synchronized void friendDeleted(Friend friend) {
        for (FriendChangeListener listener : friendChangeListeners) {
            listener.friendRemoved(friend);
        }
    }

    /**
     * This method is called when a new friend arrives.
     * @param friend the new friend.
     */

    public synchronized void friendAdded(Friend friend) {
        for (FriendChangeListener listener : friendChangeListeners) {
            listener.friendAdded(friend);
        }
    }

    /**
     * This method is called when a new message receives.
     */

    public void messageReceived() {
        for (MessageReceivedListener listener: messageReceivedListeners) {
            listener.messageReceived();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
