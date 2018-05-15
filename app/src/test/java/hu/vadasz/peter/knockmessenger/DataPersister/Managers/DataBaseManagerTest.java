package hu.vadasz.peter.knockmessenger.DataPersister.Managers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.DaoSession;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.FriendDao;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.MessageDao;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.UserDao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataBaseManagerTest {

    public static final boolean OK = true;
    public static final int MAX_TIME = 1000;

    @Mock
    private DaoSession daoSession;

    @Mock
    private UserDao userDao;

    @Mock
    private FriendDao friendDao;

    @Mock
    private MessageDao messageDao;

    private DataBaseManager dataBaseManager;

    @Test(timeout = MAX_TIME)
    public void testFindUser_ShouldReturnNull() {
        when(userDao.loadAll()).thenReturn(new ArrayList<User>());
        when(daoSession.getUserDao()).thenReturn(userDao);
        dataBaseManager = new DataBaseManager(daoSession);
        assertEquals(OK, dataBaseManager.findUser() == null);
    }

    @Test(timeout = MAX_TIME)
    public void testFindUser_ShouldReturnCorrectUser() {
        List<User> testList = new ArrayList<User>();
        testList.add(new User(1L, "","",""));
        when(userDao.loadAll()).thenReturn(testList);
        when(daoSession.getUserDao()).thenReturn(userDao);
        dataBaseManager = new DataBaseManager(daoSession);
        assertThat(dataBaseManager.findUser(), is(testList.get(0)));
    }

    @Test(timeout = MAX_TIME)
    public void testLoadAllFriends_ShouldReturnEmptyList() {
        when(friendDao.loadAll()).thenReturn(new ArrayList<Friend>());
        when(daoSession.getFriendDao()).thenReturn(friendDao);
        dataBaseManager = new DataBaseManager(daoSession);
        assertEquals(OK, dataBaseManager.loadAllFriends().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testLoadAllFriends_ShouldReturnListWithFriends() {
        List<Friend> testList = new ArrayList<>();
        testList.add(new Friend(1L, "", ""));
        when(friendDao.loadAll()).thenReturn(testList);
        when(daoSession.getFriendDao()).thenReturn(friendDao);
        dataBaseManager = new DataBaseManager(daoSession);
        assertThat(dataBaseManager.loadAllFriends(), is(testList));
    }

    @Test(timeout = MAX_TIME)
    public void testLoadAllMessages_ShouldReturnEmptyList() {
        when(messageDao.loadAll()).thenReturn(new ArrayList<Message>());
        when(daoSession.getMessageDao()).thenReturn(messageDao);
        dataBaseManager = new DataBaseManager(daoSession);
        assertEquals(OK, dataBaseManager.loadAllMessages().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testLoadAllMessages_ShouldReturnListWithMessages() {
        List<Message> testList = new ArrayList<>();
        testList.add(new Message());
        when(messageDao.loadAll()).thenReturn(testList);
        when(daoSession.getMessageDao()).thenReturn(messageDao);
        dataBaseManager = new DataBaseManager(daoSession);
        assertThat(dataBaseManager.loadAllMessages(), is(testList));
    }
}