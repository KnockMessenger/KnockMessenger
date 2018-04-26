package hu.vadasz.peter.knockmessenger.DataPersister.Server;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import lombok.Setter;

/**
 * Created by Peti on 2018. 04. 10..
 */

public class ServerSideDatabase {

    private static final String USERS = "users";
    private static final String TELEPHONE = "tels";
    private static final String MESSAGES = "messages";

    private DatabaseReference databaseReference;

    public ServerSideDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addUser(User user) {
        databaseReference.child(USERS).child(user.getTelephone()).setValue(user);
        databaseReference.child(TELEPHONE).child(user.getTelephone()).setValue(user.getTelephone());
    }

    public void deleteUser(User user) {
        databaseReference.child(USERS).child(user.getTelephone()).removeValue();
    }

    public void updateUser(User user) {
        addUser(user);
    }

    public void findAllUsers(ValueEventListener listener) {
        databaseReference.child(USERS).addListenerForSingleValueEvent(listener);
    }

    public void findUser(String telephone, ValueEventListener listener) {
        databaseReference.child(USERS).child(telephone).addListenerForSingleValueEvent(listener);
    }

    public void findUserByTelephone(String telephone, ValueEventListener listener) {
        databaseReference.child(TELEPHONE).child(telephone).addListenerForSingleValueEvent(listener);
    }

    public void setUserChangedListener(ChildEventListener listener) {
        databaseReference.child(USERS).addChildEventListener(listener);
    }

    public void removeEventListener(ChildEventListener listener) {
        databaseReference.child(USERS).removeEventListener(listener);
    }

    /// MESSAGES OPERATIONS

    public void newMessage(Message message, DatabaseReference.CompletionListener listener) {
        databaseReference.child(MESSAGES).child(message.getToTelephone()).child(message.getKey()).setValue(message, listener);
    }

    public void setIncomingMessagesListener(String telephone, ChildEventListener listener) {
        databaseReference.child(MESSAGES).child(telephone).addChildEventListener(listener);
    }

    public void removeIncomingMessagesListener(String telephone, ChildEventListener listener) {
        databaseReference.child(MESSAGES).child(telephone).removeEventListener(listener);
    }

    public void findAllMessages(String telephone, ValueEventListener listener) {
        databaseReference.child(MESSAGES).child(telephone).addListenerForSingleValueEvent(listener);
    }

    /// MESSAGES OPERATIONS -- END
}
