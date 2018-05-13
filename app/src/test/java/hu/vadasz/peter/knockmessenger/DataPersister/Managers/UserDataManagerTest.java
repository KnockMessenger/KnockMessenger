package hu.vadasz.peter.knockmessenger.DataPersister.Managers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.invocation.CapturesArgumensFromInvocation;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerSideDatabase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Peti on 2018. 05. 13..
 */

@RunWith(MockitoJUnitRunner.class)
public class UserDataManagerTest {

    public static final boolean OK = true;
    public static final int MAX_TIME = 1000;

    @Mock
    private DataBaseManager dataBaseManager;

    @Mock
    private ServerSideDatabase serverSideDatabase;

    private UserDataManager userDataManager;

    @Test(timeout = MAX_TIME)
    public void testRegistrate_ShouldSaveUser() {
        User user = new User(1L, "XY", "123456789", "");
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.registrate(user);
        assertThat(userDataManager.getUser(), is(user));
    }

    @Test(timeout = MAX_TIME)
    public void testLoadUser_ShouldLoadCorrectUserData() {
        User user = new User(1L, "XY", "123456789", "");
        when(dataBaseManager.findUser()).thenReturn(user);
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadUser();
        assertThat(userDataManager.getUser(), is(user));
    }

    @Test(timeout = MAX_TIME)
    public void testUpdateUser_ShouldUpdateUserData() {
        User user = new User(1L, "XY", "123456789", "");
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.registrate(user);
        User updatedUser = new User(1L, "AB", "333666999", "");
        userDataManager.updateUser(updatedUser);
        assertEquals(OK, userDataManager.getUser().equals(updatedUser)
                            && userDataManager.getUser().getName().equals(updatedUser.getName())
                            && userDataManager.getUser().getPassword().equals(updatedUser.getPassword()));
    }

    @Test(timeout = MAX_TIME)
    public void testIsLoggedIn_ShouldReturnFalse() {
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        assertEquals(!OK, userDataManager.isLoggedIn());
    }

    @Test(timeout = MAX_TIME)
    public void testIsLoggedIn_ShouldReturnTrue() {
        User user = new User(1L, "XY", "123456789", "");
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.registrate(user);
        assertEquals(OK, userDataManager.isLoggedIn());
    }

    @Test(timeout = MAX_TIME)
    public void testDeleteUser_UserDataShouldBeDeleted() {
        User user = new User(1L, "XY", "123456789", "");
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.registrate(user);
        userDataManager.deleteUser();
        assertEquals(!OK, userDataManager.isLoggedIn());
    }

    /// FRIENDS

    @Test(timeout = MAX_TIME)
    public void testLoadFriends_ShouldReturnListWithFriends() {
        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend());
        when(dataBaseManager.loadAllFriends()).thenReturn(friends);
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        assertThat(userDataManager.getFriends(), is(friends));
    }

    @Test(timeout = MAX_TIME)
    public void testInsertFriend_ShouldSaveFriend() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        assertEquals(OK, userDataManager.getFriends().size() == 1
                && userDataManager.getFriends().get(0).equals(new Friend(1L, "XY", "1234456789")));
    }

    @Test(timeout = MAX_TIME)
    public void testDeleteFriend_ShouldDeleteFriend() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        userDataManager.deleteFriend(new Friend(1L, "XY", "1234456789"));
        assertEquals(OK, userDataManager.getFriends().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testDeleteAllFriends_ShouldDeleteAllFriend() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        userDataManager.insertFriend(new Friend(2L, "XY", "1234456789"));
        userDataManager.deleteAllFriends();
        assertEquals(OK, userDataManager.getFriends().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testIsFriend_ShouldReturnTrue() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        assertEquals(OK, userDataManager.isFriend(new Friend(1L, "XY", "1234456789")));
    }

    @Test(timeout = MAX_TIME)
    public void testIsFriend_ShouldReturnFalse() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        assertEquals(!OK, userDataManager.isFriend(new Friend(2L, "XY", "+1234456789")));
    }

    @Test(timeout = MAX_TIME)
    public void testIsFriend_ByTelephoneShouldReturnTrue() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "123456789"));
        assertEquals(OK, userDataManager.isFriend("123456789"));
    }

    @Test(timeout = MAX_TIME)
    public void testIsFriend_ByTelephoneShouldReturnFalse() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        assertEquals(!OK, userDataManager.isFriend("+123456789"));
    }

    @Test(timeout = MAX_TIME)
    public void testGetFriendByTelephone_ShouldReturnNull() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        assertEquals(OK, userDataManager.getFriendByTelephone("+123456789") == null);
    }

    @Test(timeout = MAX_TIME)
    public void testGetFriendByTelephone_ShouldReturnFriend() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        assertEquals(OK, userDataManager.getFriendByTelephone("1234456789").equals(new Friend(1L, "XY", "1234456789")));
    }

    @Test(timeout = MAX_TIME)
    public void testUpdateFriend_FriendShouldBeUpdated() {
        when(dataBaseManager.loadAllFriends()).thenReturn(new ArrayList<Friend>());
        userDataManager = new UserDataManager(dataBaseManager, serverSideDatabase);
        userDataManager.loadFriends();
        userDataManager.insertFriend(new Friend(1L, "XY", "1234456789"));
        userDataManager.updateFriend(new Friend(1L, "XYZ", "1234456789"));
        assertEquals(OK, userDataManager.getFriends().size() == 1
                        && userDataManager.getFriends().get(0).getName().equals("XYZ"));
    }

}