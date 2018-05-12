package hu.vadasz.peter.knockmessenger.MessageSending;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.joda.time.DateTime;

import java.util.ArrayList;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.TimeoutHandler;
import hu.vadasz.peter.knockmessenger.MessageSending.Interfaces.MessageSenderInterface;
import lombok.Getter;

/**
 * Main class for message sending. The message is stored in a buffer until it is sent.
 */

public class MessageSender implements MessageSenderInterface, DatabaseReference.CompletionListener,
        TimeoutHandler.TimeoutListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface MessageSendingVisualizer {

        void clearMessage();
        void print(String message);
        void messageSendingInProgress();
        void messageSent();
        void connectionTimeout();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String CURSOR = "_";

    private ArrayList<String> buffer;

    private MessageSendingVisualizer messageSendingVisualizer;

    private MessageDataManager messageDataManager;

    private TimeoutHandler timeoutHandler;

    private Message actualMessage;

    @Getter
    private int cursorPos;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MessageSender(MessageSendingVisualizer messageSendingVisualizer, MessageDataManager messageDataManager) {
        buffer = new ArrayList<>();
        this.messageSendingVisualizer = messageSendingVisualizer;
        this.messageDataManager = messageDataManager;
        timeoutHandler = new TimeoutHandler(TimeoutHandler.DEFAULT_MEDIUM_TIMEOUT, this);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Concatenate the parts of the message from the buffer to a string, and send it to the
     * visualizer.
     */

    private void print() {
        StringBuilder text = new StringBuilder();

        int i = 0;
        for (String character : buffer) {
            if (i == cursorPos) {
                text.append(CURSOR);
            }

            ++i;

            text.append(character);
        }

        if (cursorPos == buffer.size()) {
            text.append(CURSOR);
        }

        messageSendingVisualizer.print(text.toString());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// MessageSenderInterface INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void send(String fromTelephone, String toTelephone) {

        Message message = new Message();
        message.setFromTelephone(fromTelephone);
        message.setToTelephone(toTelephone);
        message.setDateTimeFromTimestamp(new DateTime());
        message.setMessageType(Message.MessageType.OUT);
        message.setKey(message.getDateTime().toString().replace(".", ":") + fromTelephone);
        message.setDeleted(!MessageDataManager.MESSAGE_DELETED);

        StringBuilder sb = new StringBuilder();
        for (String part : buffer) {
            sb.append(part);
        }

        message.setMessage(sb.toString());

        actualMessage = message;
        messageDataManager.trySendMessage(message, this);
        messageSendingVisualizer.messageSendingInProgress();
        timeoutHandler.start();
    }

    @Override
    public void clear() {
        buffer.clear();
        cursorPos = 0;
    }

    @Override
    public void add(String messagePart) {
        buffer.add(cursorPos, messagePart);
        cursorPos++;
        print();
    }

    @Override
    public void deleteLastPart() {
        buffer.remove(cursorPos - 1);
        cursorPos--;
        print();
    }

    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public int getCursor() {
        return getCursorPos();
    }

    @Override
    public void home() {
        cursorPos = 0;
        print();
    }

    @Override
    public void end() {
        cursorPos = buffer.size();
        print();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// MessageSenderInterface INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CompleteListener INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        timeoutHandler.stop();
        clear();
        messageDataManager.cacheMessage(actualMessage);
        messageSendingVisualizer.clearMessage();
        messageSendingVisualizer.messageSent();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CompleteListener INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void timeout() {
        messageSendingVisualizer.connectionTimeout();
    }
}
