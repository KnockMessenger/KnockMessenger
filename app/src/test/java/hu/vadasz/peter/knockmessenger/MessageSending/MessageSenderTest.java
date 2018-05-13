package hu.vadasz.peter.knockmessenger.MessageSending;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.TimeoutHandler;

import static org.junit.Assert.*;

/**
 * Created by Peti on 2018. 05. 13..
 */

@RunWith(MockitoJUnitRunner.class)
public class MessageSenderTest {

    public static final boolean OK = true;

    public static final int MAX_TIME = 1000;

    @Mock
    private MessageDataManager messageDataManager;

    @Mock
    private TimeoutHandler timeoutHandler;

    @Mock
    private MessageSender.MessageSendingVisualizer messageSendingVisualizer;

    private MessageSender messageSender;

    @Test(timeout = MAX_TIME)
    public void testAdd_ShouldContainMessagePart() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        messageSender.add("a");
        assertEquals(OK, messageSender.getBuffer().size() == 1
                && messageSender.getBuffer().get(0).equals("a"));
    }

    @Test(timeout = MAX_TIME)
    public void testAdd_MorePartsShouldContainMoreMessageParts() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        messageSender.add("a");
        messageSender.add("b");
        assertEquals(OK, messageSender.getBuffer().size() == 2
                && messageSender.getBuffer().get(0).equals("a")
                && messageSender.getBuffer().get(1).equals("b"));
    }

    @Test(timeout = MAX_TIME)
    public void testClear_MessageSenderShouldNotContainAnyMessagePart() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        messageSender.add("a");
        messageSender.clear();
        assertEquals(OK, messageSender.isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testDeleteLastPart_ShouldDeleteCorrectly() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        messageSender.add("a");
        messageSender.add("b");
        messageSender.deleteLastPart();
        assertEquals(OK, messageSender.getBuffer().size() == 1
                        && messageSender.getBuffer().get(0).equals("a"));
    }

    @Test(timeout = MAX_TIME)
    public void testGetCursor_CursorPositionShouldBeZero() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        assertEquals(OK, messageSender.getCursor() == 0);
    }

    @Test(timeout = MAX_TIME)
    public void testGetCursor_CursorPositionShouldBeOne() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        messageSender.add("a");
        assertEquals(OK, messageSender.getCursor() == 1);
    }

    @Test(timeout = MAX_TIME)
    public void testHome_CursorShouldBeZero() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        messageSender.add("a");
        messageSender.home();
        assertEquals(OK, messageSender.getCursor() == 0);
    }

    @Test(timeout = MAX_TIME)
    public void testHome_CursorShouldBeOne() {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        messageSender.add("a");
        messageSender.home();
        messageSender.end();
        assertEquals(OK, messageSender.getCursor() == 1);
    }
}