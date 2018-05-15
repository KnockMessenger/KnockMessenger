package hu.vadasz.peter.knockmessenger.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Adapters.AbstractFriendAdapter;
import hu.vadasz.peter.knockmessenger.Adapters.BrowseFriendAdapter;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerDataChangeHandler;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.TimeoutHandler;
import hu.vadasz.peter.knockmessenger.R;

/**
 * This class is responsible for displaying users, not only friends.
 */

public class BrowseFriendsActivity extends BaseActivity implements ValueEventListener,
        BrowseFriendAdapter.FriendBrowserListener, TimeoutHandler.TimeoutListener, ServerDataChangeHandler.FriendChangeListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.browseFriendsActivity_recyclerView)
    RecyclerView friendsRecyclerView;

    @BindView(R.id.browseFriendsActivity_progressbar)
    ProgressBar waiting;

    private SearchView searchView;

    private AbstractFriendAdapter browseFriendAdapter;

    private TimeoutHandler timeoutHandler;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_friends);
        ButterKnife.bind(this);

        timeoutHandler = new TimeoutHandler(TimeoutHandler.DEFAULT_SHORT_TIMEOUT, this);
        serverDataChangeHandler.addFriendDataChangeListener(this);
        friendsRecyclerView.setHasFixedSize(RECYCLER_VIEW_HAS_FIXED_SIZE);
    }

    @Override
    public void onResume() {
        super.onResume();

        timeoutHandler.start();
        userDataManager.requestAllContacts(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        timeoutHandler.stop();
        serverDataChangeHandler.removeFriendDataChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_browser_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.browseFriendsActivity_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                browseFriendAdapter.filter(newText);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                browseFriendAdapter.filter(newText);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ValueEventListener OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        timeoutHandler.stop();
        waiting.setVisibility(View.GONE);
        if (dataSnapshot.exists()) {
            List<Friend> friends = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Friend friend = new Friend(snapshot.getValue(User.class));
                if (!friend.getTel().equals(userDataManager.getUser().getTelephone())) {
                    friends.add(friend);
                }
            }
            initRecyclerView(friends);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ValueEventListener OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// BrowseFriendAdapter.FriendBrowserListener OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isFriend(Friend friend) {
        return userDataManager.isFriend(friend);
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void saveFriend(Friend friend) {
        friend.setId(null);
        userDataManager.insertFriend(friend);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// BrowseFriendAdapter.FriendBrowserListener OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ServerDataChangeHandler.FriendChangeListener OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void friendChanged(Friend friend) {
        if (browseFriendAdapter != null)
            browseFriendAdapter.friendChanged(friend);
    }

    @Override
    public void friendAdded(Friend friend) {
        if (browseFriendAdapter != null)
            browseFriendAdapter.friendAdded(friend);
    }

    @Override
    public void friendRemoved(Friend friend) {
        if (browseFriendAdapter != null)
            browseFriendAdapter.friendRemoved(friend);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ServerDataChangeHandler.FriendChangeListener OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// TimeoutHandler.TimeoutListener OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void timeout() {
        showErrorMessageAndFinish(getString(R.string.connection_timeout_error));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// TimeoutHandler.TimeoutListener OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes the RecyclerView which represents friends with list of friends.
     * @param friends the list of the friends
     */

    private void initRecyclerView(List<Friend> friends) {
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        browseFriendAdapter = new BrowseFriendAdapter(friends, this);
        friendsRecyclerView.setAdapter(browseFriendAdapter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
