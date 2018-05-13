package hu.vadasz.peter.knockmessenger.DataPersister.Managers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerSideDatabase;
import lombok.Getter;

/**
 *
 */

public class MessageDataManager {

    public static final boolean MESSAGE_DELETED = true;

    private DataBaseManager dataBaseManager;
    private ServerSideDatabase serverSideDatabase;

    @Getter
    private List<Message> messages;

    public MessageDataManager(DataBaseManager dataBaseManager, ServerSideDatabase serverSideDatabase) {
        this.dataBaseManager = dataBaseManager;
        this.serverSideDatabase = serverSideDatabase;
        loadMessages();
    }

    public synchronized List<Message> getMessages() {
        return messages;
    }

    public void loadMessages() {
        messages = dataBaseManager.loadAllMessages();
    }

    public void requestAllMessages(String telephone, ValueEventListener listener) {
        serverSideDatabase.findAllMessages(telephone, listener);
    }

    public void trySendMessage(Message message, DatabaseReference.CompletionListener listener) {
        serverSideDatabase.newMessage(message, listener);
    }

    public synchronized void cacheMessage(Message message) {
        message.setId(dataBaseManager.insertMessage(message));
        messages.add(message);
    }

    public synchronized void newMessage(Message message) {
        message.setMessageType(Message.MessageType.IN);
        cacheMessage(message);
    }

    public synchronized void deleteAllMessages() {
        for (Message message : messages) {
            message.setDeleted(MESSAGE_DELETED);
            dataBaseManager.updateMessage(message);
        }
    }

}
