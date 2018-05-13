package hu.vadasz.peter.knockmessenger.DataPersister.Server;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;

/**
 * Created by Peti on 2018. 05. 13..
 */

public class TestFriendAndMessageChangeListener implements ServerDataChangeHandler.FriendChangeListener,
        ServerDataChangeHandler.MessageReceivedListener {

    public static final boolean CALLED = true;

    private boolean friendChangedCalled;
    private boolean friendAddedCalled;
    private boolean friendRemovedCalled;
    private boolean messageReceivedCalled;

    @Override
    public void friendChanged(Friend friend) {
        friendChangedCalled = CALLED;
    }

    @Override
    public void friendAdded(Friend friend) {
        friendAddedCalled = CALLED;
    }

    @Override
    public void friendRemoved(Friend friend) {
        friendRemovedCalled = CALLED;
    }

    @Override
    public void messageReceived() {
        messageReceivedCalled = CALLED;
    }

    public boolean isFriendChangedCalled() {
        return friendChangedCalled;
    }

    public boolean isFriendAddedCalled() {
        return friendAddedCalled;
    }

    public boolean isFriendRemovedCalled() {
        return friendRemovedCalled;
    }

    public boolean isMessageReceivedCalled() {
        return messageReceivedCalled;
    }

}
