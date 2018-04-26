package hu.vadasz.peter.knockmessenger.DataPersister.Managers;


import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.DaoSession;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.FriendDao;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.MessageDao;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.UserDao;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.UserSettings;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.UserSettingsDao;

/**
 * This class is responsible for collecting database operations.
 */

public class DataBaseManager {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private UserDao userDao;
    private FriendDao friendDao;
    private MessageDao messageDao;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public DataBaseManager(DaoSession daoSession) {
        userDao = daoSession.getUserDao();
        friendDao = daoSession.getFriendDao();
        messageDao = daoSession.getMessageDao();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DATABASE OPERATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// USER OPERATIONS

    public Long insertUser(User user) { return  userDao.insert(user); }

    public void updateUser(User user) { userDao.update(user); }

    public void deleteUser(User user) { userDao.delete(user); }

    public User findUser() { return userDao.loadAll().isEmpty() ? null : userDao.loadAll().get(0); }

    /// USER OPERATIONS -- END

    /// FRIEND OPERATIONS

    public List<Friend> loadAllFriends() { return friendDao.loadAll(); }

    public Long insertFriend(Friend friend) { return friendDao.insert(friend); }

    public void updateFriend(Friend friend) { friendDao.update(friend); }

    public void deleteFriend(Friend friend) { friendDao.delete(friend); }

    public void deleteAllFriend() { friendDao.deleteAll(); }

    /// FRIEND OPERATIONS -- END

    /// MESSAGES OPERATIONS

    public List<Message> loadAllMessages() { return messageDao.loadAll(); }

    public Long insertMessage(Message message) { return messageDao.insert(message); }

    void deleteMessage(Message message) { messageDao.delete(message); }

    void deleteAllMessages() { messageDao.deleteAll(); }

    void updateMessage(Message message) { messageDao.update(message); }

    /// MESSAGES OPERATIONS -- END

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DATABASE OPERATIONS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
