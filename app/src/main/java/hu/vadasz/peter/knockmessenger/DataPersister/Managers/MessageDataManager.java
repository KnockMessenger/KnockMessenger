package hu.vadasz.peter.knockmessenger.DataPersister.Managers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerSideDatabase;
import lombok.Getter;

/**
 * This class provides cache and access to the messages.
 */

public class MessageDataManager {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean MESSAGE_DELETED = true;

    /// CONSTANTS -- END

    private DataBaseManager dataBaseManager;
    private ServerSideDatabase serverSideDatabase;

    @Getter
    private List<Message> messages;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MessageDataManager(DataBaseManager dataBaseManager, ServerSideDatabase serverSideDatabase) {
        this.dataBaseManager = dataBaseManager;
        this.serverSideDatabase = serverSideDatabase;
        loadMessages();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the list of messages.
     */

    public synchronized List<Message> getMessages() {
        return messages;
    }

    /**
     * Loads the persisted messages to a list.
     */

    public void loadMessages() {
        messages = dataBaseManager.loadAllMessages();
    }

    /**
     * This method send a request to the server to get the messages.
     * @param telephone the telephone of the user.
     * @param listener the class which receives the messages.
     */

    public void requestAllMessages(String telephone, ValueEventListener listener) {
        serverSideDatabase.findAllMessages(telephone, listener);
    }

    /**
     * This method tries to send a message to the server.
     * @param message the message to send.
     * @param listener the class which receives the server's answer.
     */

    public void trySendMessage(Message message, DatabaseReference.CompletionListener listener) {
        serverSideDatabase.newMessage(message, listener);
    }

    /**
     * This method saves the message to the local database.
     * @param message the message to save.
     */

    public synchronized void cacheMessage(Message message) {
        message.setId(dataBaseManager.insertMessage(message));
        messages.add(message);
    }

    /**
     * This method saves a nem incoming message.
     * @param message
     */

    public synchronized void newMessage(Message message) {
        message.setMessageType(Message.MessageType.IN);
        cacheMessage(message);
    }

    /**
     * This method marks all messages to be deleted.
     */

    public synchronized void deleteAllMessages() {
        for (Message message : messages) {
            message.setDeleted(MESSAGE_DELETED);
            dataBaseManager.updateMessage(message);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
