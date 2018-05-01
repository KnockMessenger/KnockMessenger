package hu.vadasz.peter.knockmessenger.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Activities.DragAndDrop.FriendCardTouchHelper;
import hu.vadasz.peter.knockmessenger.Adapters.AbstractFriendAdapter;
import hu.vadasz.peter.knockmessenger.Adapters.FriendsAdapter;
import hu.vadasz.peter.knockmessenger.Controllers.Validators.InternetConnectionValidator;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerDataChangeHandler;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.TimeoutHandler;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.morsecodedecoder.Code.Code;

public class FriendsActivity extends BaseActivity implements FriendsAdapter.FriendListener,
        ServerDataChangeHandler.FriendChangeListener, ValueEventListener, TimeoutHandler.TimeoutListener {

    @BindView(R.id.friendsActivity_recyclerView)
    RecyclerView friendsRecyclerView;

    @BindView(R.id.friendsActivity_progressbar)
    ProgressBar progressBar;

    @BindView(R.id.friendsActivity_image)
    ImageView friendsImage;

    private SearchView searchView;

    private AbstractFriendAdapter friendsAdapter;

    private InternetConnectionValidator internetConnectionValidator;

    private TimeoutHandler timeoutHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);

        initRecyclerView();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new FriendCardTouchHelper((FriendsAdapter) friendsAdapter));
        itemTouchHelper.attachToRecyclerView(friendsRecyclerView);

        internetConnectionValidator = new InternetConnectionValidator();

        timeoutHandler = new TimeoutHandler(TimeoutHandler.DEFAULT_LONG_TIME_OUT, this);

        setTitle(EMPTY_TEXT);

        friendsRecyclerView.setHasFixedSize(RECYCLER_VIEW_HAS_FIXED_SIZE);

        if (friendsAdapter.getItemCount() != 0) {
            friendsImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        serverDataChangeHandler.addFriendDataChangeListener(this);
        syncFriends();

    }

    @Override
    public void onPause() {
        super.onPause();
        timeoutHandler.stop();
        serverDataChangeHandler.removeFriendDataChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_activity_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.friendsActivity_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                friendsAdapter.filter(newText);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                friendsAdapter.filter(newText);

                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.friendsActivity_browse:
                showBrowseFriendsActivity();
                break;
            case R.id.friendsActivity_sync:
                syncFriends();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void removeFriend(Friend friend) {
        userDataManager.deleteFriend(friend);
        if (friendsAdapter.getItemCount() == 0) {
            friendsImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sendMessage(String telephone) {
        showMessageSendingActivity(telephone);
    }

    private void showBrowseFriendsActivity() {
        if (internetConnectionValidator.validateConnection()) {
            startActivity(new Intent(this, BrowseFriendsActivity.class));
        } else {
            showErrorMessage(getString(R.string.device_offline_error));
        }
    }

    private void showMessageSendingActivity(String telephone) {
        Intent intent = new Intent(this, MessageSendingActivity.class);
        intent.putExtra(MessageSendingActivity.EXTRA_FRIEND_TELEPHONE_KEY, telephone);
        startActivity(intent);
    }

    private void initRecyclerView() {
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsAdapter = new FriendsAdapter(userDataManager.getFriends(), this);
        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    private void syncFriends() {
        if (internetConnectionValidator.validateConnection()) {
            userDataManager.requestAllContacts(this);
            progressBar.setVisibility(View.VISIBLE);
            timeoutHandler.start();
        } else {
            showErrorMessage(getString(R.string.syncError));
        }
    }

    @Override
    public void confirmDelete(final Friend friend) {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.friendsActivity_confirm_delete_text), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.friendsActivity_confirm_delete_yes_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFriend(friend);
                        friendsAdapter.dataSetChanged();
                    }
                })
                .setActionTextColor(getColor(android.R.color.holo_red_light))
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        friendsAdapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    @Override
    public void friendChanged(Friend friend) {
        friendsAdapter.dataSetChanged();
    }

    @Override
    public void friendAdded(Friend friend) {}

    @Override
    public void friendRemoved(Friend friend) {
        friendsAdapter.dataSetChanged();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        timeoutHandler.stop();
        List<Friend> tmp = new ArrayList<>();

        if (dataSnapshot.exists()) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Friend friend = new Friend(snapshot.getValue(User.class));
                if (userDataManager.isFriend(friend)) {
                    userDataManager.updateFriend(friend);
                    tmp.add(friend);
                }
            }
        }

        for (Friend friend : new ArrayList<>(userDataManager.getFriends())) {
            if (!tmp.contains(friend)) {
                userDataManager.deleteFriend(friend);
            }
        }
        friendsAdapter.dataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void timeout() {
        progressBar.setVisibility(View.GONE);
        showErrorMessage(getString(R.string.syncError));
    }
}
