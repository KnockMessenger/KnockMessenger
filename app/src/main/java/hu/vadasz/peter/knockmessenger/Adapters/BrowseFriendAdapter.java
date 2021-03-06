package hu.vadasz.peter.knockmessenger.Adapters;

import android.view.View;

import java.util.List;

import hu.vadasz.peter.knockmessenger.Activities.BaseActivity;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.R;
import lombok.Setter;

/**
 * This class is the adapter of the RecyclerView which represents users (not friends).
 */

public class BrowseFriendAdapter extends AbstractFriendAdapter {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This interface is implemented by the Activity class which contains the RecyclerView.
     */

    public interface FriendBrowserListener {

        boolean isFriend(Friend friend);
        BaseActivity getActivity();
        void saveFriend(Friend friend);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Setter
    private FriendBrowserListener listener;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public BrowseFriendAdapter(List<Friend> friends, FriendBrowserListener listener) {
        super(friends);
        this.listener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AbstractFriendAdapter OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void setViewItems(ViewHolder holder, final int position) {
        holder.removeFriend.setVisibility(View.GONE);
        holder.sendMessage.setVisibility(View.GONE);

        if (listener.isFriend(filteredFriends.get(position))) {
            holder.mainPanel.setBackgroundResource(R.drawable.friend_card_rounded_corner_dark);
            holder.addFriend.setVisibility(View.GONE);
        } else {
            holder.addFriend.setVisibility(View.VISIBLE);
            holder.mainPanel.setBackgroundResource(R.drawable.friend_card_rounded_corner);
        }

        holder.addFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveFriend(filteredFriends.get(position));
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AbstractFriendAdapter OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method saves a user as a friend.
     * @param friend
     */

    private void saveFriend(Friend friend) {
        listener.saveFriend(friend);
        notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
