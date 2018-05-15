package hu.vadasz.peter.knockmessenger.DataPersister.Server;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;

/**
 * This class is used to access the remote server.
 */

public class ServerSideDatabase {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    private static final String USERS = "users";
    private static final String TELEPHONE = "tels";
    private static final String MESSAGES = "messages";

    /// CONSTANTS -- END

    private DatabaseReference databaseReference;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ServerSideDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method sends a new user to the server.
     * @param user the user to send.
     */

    public void addUser(User user) {
        databaseReference.child(USERS).child(user.getTelephone()).setValue(user);
        databaseReference.child(TELEPHONE).child(user.getTelephone()).setValue(user.getTelephone());
    }

    /**
     * This method deletes the user from the remote server.
     * @param user the user to delete.
     */

    public void deleteUser(User user) {
        databaseReference.child(USERS).child(user.getTelephone()).removeValue();
    }

    /**
     * This method updates a user's data on the remote server.
     * @param user the user to update.
     */

    public void updateUser(User user) {
        addUser(user);
    }

    /**
     * This method sends a request to the server to get users.
     * @param listener the class which receives data from server.
     */

    public void findAllUsers(ValueEventListener listener) {
        databaseReference.child(USERS).addListenerForSingleValueEvent(listener);
    }

    /**
     * This method sends a request to the server to get a specified user.
     * @param telephone the users's telephone.
     * @param listener the class which receives data from the server.
     */

    public void findUser(String telephone, ValueEventListener listener) {
        databaseReference.child(USERS).child(telephone).addListenerForSingleValueEvent(listener);
    }

    /**
     * This method sends a request to the server to get a specified user by telephone.
     * @param telephone the user's telephone.
     * @param listener the class which receives data from server.
     */

    public void findUserByTelephone(String telephone, ValueEventListener listener) {
        databaseReference.child(TELEPHONE).child(telephone).addListenerForSingleValueEvent(listener);
    }

    /**
     * This methods sets the listener which listens for user data changes.
     * @param listener the listener to set.
     */

    public void setUserChangedListener(ChildEventListener listener) {
        databaseReference.child(USERS).addChildEventListener(listener);
    }

    /**
     * This methods removes the listener which listens for user data changes.
     * @param listener to remove.
     */

    public void removeEventListener(ChildEventListener listener) {
        databaseReference.child(USERS).removeEventListener(listener);
    }

    /// MESSAGES OPERATIONS

    /**
     * This method sends a message to the server.
     * @param message the message to send.
     * @param listener the class which receives data from the server.
     */

    public void newMessage(Message message, DatabaseReference.CompletionListener listener) {
        databaseReference.child(MESSAGES).child(message.getToTelephone()).child(message.getKey()).setValue(message, listener);
    }

    /**
     * This method send a request to the server to get messages from a user.
     * @param telephone the telephone of the user.
     * @param listener the class which receives data from the server.
     */
    public void findAllMessages(String telephone, ValueEventListener listener) {
        databaseReference.child(MESSAGES).child(telephone).addListenerForSingleValueEvent(listener);
    }

    /// MESSAGES OPERATIONS -- END

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
