package hu.vadasz.peter.knockmessenger.DataPersister.Managers;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerSideDatabase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Peti on 2018. 05. 13..
 */

@RunWith(MockitoJUnitRunner.class)
public class MessageDataManagerTest {

    public static final boolean OK = true;
    public static final int MAX_TIME = 1000;

    @Mock
    private DataBaseManager dataBaseManager;

    @Mock
    ServerSideDatabase serverSideDatabase;

    private MessageDataManager messageDataManager;

    @Test(timeout = MAX_TIME)
    public void testCacheMessages_ShouldSaveMessage() {
        Message message = new Message();
        message.setMessage("alma");
        when(dataBaseManager.insertMessage(message)).thenReturn(1L);
        when(dataBaseManager.loadAllMessages()).thenReturn(new ArrayList<Message>());
        messageDataManager = new MessageDataManager(dataBaseManager, serverSideDatabase);
        messageDataManager.cacheMessage(message);
        assertEquals(OK, messageDataManager.getMessages().size() == 1
                && messageDataManager.getMessages().get(0).getMessage().equals(message.getMessage()));
    }

    @Test(timeout = MAX_TIME)
    public void testNewMessages_ShouldSaveMessage() {
        Message message = new Message();
        message.setMessage("alma");
        when(dataBaseManager.insertMessage(message)).thenReturn(1L);
        when(dataBaseManager.loadAllMessages()).thenReturn(new ArrayList<Message>());
        messageDataManager = new MessageDataManager(dataBaseManager, serverSideDatabase);
        messageDataManager.newMessage(message);
        assertEquals(OK, messageDataManager.getMessages().size() == 1
                && messageDataManager.getMessages().get(0).getMessage().equals(message.getMessage())
                && messageDataManager.getMessages().get(0).getMessageType() == Message.MessageType.IN);
    }

    @Test(timeout = MAX_TIME)
    public void testDeleteMessages_ShouldDeleteMessage() {
        Message message = new Message();
        message.setMessage("alma");
        when(dataBaseManager.insertMessage(message)).thenReturn(1L);
        when(dataBaseManager.loadAllMessages()).thenReturn(new ArrayList<Message>());
        messageDataManager = new MessageDataManager(dataBaseManager, serverSideDatabase);
        messageDataManager.newMessage(message);
        messageDataManager.deleteAllMessages();
        assertEquals(OK, messageDataManager.getMessages().size() == 1
                && messageDataManager.getMessages().get(0).getMessage().equals(message.getMessage())
                && messageDataManager.getMessages().get(0).getMessageType() == Message.MessageType.IN
                && messageDataManager.getMessages().get(0).getDeleted());
    }

}