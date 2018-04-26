package hu.vadasz.peter.knockmessenger.Controllers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import hu.vadasz.peter.knockmessenger.Controllers.Validators.InternetConnectionValidator;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.TimeoutHandler;
import hu.vadasz.peter.knockmessenger.Models.Interfaces.MessageSenderInterface;
import hu.vadasz.peter.knockmessenger.Models.MessageSender;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.*;

/**
 * This class is responsible for controlling the message sending service, error handling,
 * and exceptions. The message sending activity should have az instance of this class.
 */

public class MessageSendingController implements ValueEventListener, TimeoutHandler.TimeoutListener {

    public interface MessageSendingControllerListener {

        void sendingStarted();
        void userNotExists();
        void timeout();
        void requestUser(ValueEventListener listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    private String CAN_NOT_DELETE_PART_ERROR = "Nothing to delete!";
    private String DEVICE_OFFLINE_ERROR = "Device is offline, please connect to internet!";
    private String EMPTY_MESSAGE_ERROR = "What do you want to send? :)";

    /// CONSTANTS -- END

    private MessageSenderInterface messageSender;

    private InternetConnectionValidator internetConnectionValidator;

    private TimeoutHandler timeoutHandler;

    private MessageSendingControllerListener listener;

    private String fromTelephone;
    private String toTelephone;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MessageSendingController(MessageSender.MessageSendingVisualizer messageSendingVisualizer, MessageDataManager messageDataManager,
                                    MessageSendingControllerListener listener) {
        messageSender = new MessageSender(messageSendingVisualizer, messageDataManager);
        internetConnectionValidator = new InternetConnectionValidator();
        timeoutHandler = new TimeoutHandler(TimeoutHandler.DEFAULT_MEDIUM_TIMEOUT, this);
        this.listener = listener;

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Concatenates the coming part to the message.
     * @param messagePart the new part of the message.
     */

    public void add(String messagePart) {
        messageSender.add(messagePart);
    }

    /**
     * Deletes the last part (character) of te message, if the message is not empty otherwise throws
     * exception.
     * @throws CanNotDeleteCharacterException
     */

    public void delete() throws CanNotDeleteCharacterException {
        if (messageSender.isEmpty() || messageSender.getCursor() == 0) {
            throw new CanNotDeleteCharacterException(CAN_NOT_DELETE_PART_ERROR);
        }

        messageSender.deleteLastPart();
    }

    /**
     * Sends the message to the chosen aim, if there is internet connection, otherwise throws
     * exception.
     * @throws DeviceIsOfflineException
     */

    public void send(String fromTelephone, String toTelephone) throws DeviceIsOfflineException, EmptyMessageException {
        if (internetConnectionValidator.validateConnection() != InternetConnectionValidator.CONNECTION_IS_OK) {
            throw new DeviceIsOfflineException(DEVICE_OFFLINE_ERROR);
        } else if (messageSender.isEmpty()) {
            throw new EmptyMessageException(EMPTY_MESSAGE_ERROR);
        }

        this.fromTelephone = fromTelephone;
        this.toTelephone = toTelephone;
        listener.requestUser(this);
        timeoutHandler.start();
        listener.sendingStarted();
    }

    /**
     * This method sets the model's cursor to the beginning of the text.
     */

    public void home() {
        messageSender.home();
    }

    /**
     * This method sets the cursor to the end of the text.
     */

    public void end() {
        messageSender.end();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        timeoutHandler.stop();
        if (!dataSnapshot.exists()) {
            listener.userNotExists();
        } else {
            messageSender.send(fromTelephone, toTelephone);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}

    @Override
    public void timeout() {
        listener.timeout();
    }
}
