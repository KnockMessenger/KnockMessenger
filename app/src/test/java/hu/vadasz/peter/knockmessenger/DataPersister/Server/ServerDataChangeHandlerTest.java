package hu.vadasz.peter.knockmessenger.DataPersister.Server;

import org.junit.Before;
import org.junit.Test;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;

import static org.junit.Assert.*;

/**
 * Created by Peti on 2018. 05. 13..
 */
public class ServerDataChangeHandlerTest {

    public static final boolean OK = true;

    public static final int MAX_TIME = 1000;

    private ServerDataChangeHandler serverDataChangeHandler;

    private TestFriendAndMessageChangeListener testFriendAndMessageChangeListener;

    @Before
    public void init() {
        testFriendAndMessageChangeListener = new TestFriendAndMessageChangeListener();
        serverDataChangeHandler = new ServerDataChangeHandler();
        serverDataChangeHandler.addFriendDataChangeListener(testFriendAndMessageChangeListener);
        serverDataChangeHandler.addMessageReceivedListener(testFriendAndMessageChangeListener);
    }

    @Test(timeout = MAX_TIME)
    public void testFriendAdded_ListenerMethodShouldBeCalled() {
        serverDataChangeHandler.friendAdded(new Friend());
        assertEquals(OK, testFriendAndMessageChangeListener.isFriendAddedCalled());
    }

    @Test(timeout = MAX_TIME)
    public void testFriendChanged_ListenerMethodShouldBeCalled() {
        serverDataChangeHandler.friendChanged(new Friend());
        assertEquals(OK, testFriendAndMessageChangeListener.isFriendChangedCalled());
    }

    @Test(timeout = MAX_TIME)
    public void testFriendRemoved_ListenerMethodShouldBeCalled() {
        serverDataChangeHandler.friendDeleted(new Friend());
        assertEquals(OK, testFriendAndMessageChangeListener.isFriendRemovedCalled());
    }

    @Test(timeout = MAX_TIME)
    public void testMessageReceived_ListenerMethodShouldBeCalled() {
        serverDataChangeHandler.messageReceived();
        assertEquals(OK, testFriendAndMessageChangeListener.isMessageReceivedCalled());
    }

}