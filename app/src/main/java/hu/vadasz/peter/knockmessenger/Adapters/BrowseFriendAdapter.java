package hu.vadasz.peter.knockmessenger.Adapters;

import android.view.View;

import java.util.List;

import hu.vadasz.peter.knockmessenger.Activities.BaseActivity;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.R;
import lombok.Setter;

/**
 * Created by Peti on 2018. 04. 14..
 */

public class BrowseFriendAdapter extends AbstractFriendAdapter {

    public interface FriendBrowserListener {

        boolean isFriend(Friend friend);
        BaseActivity getActivity();
        void saveFriend(Friend friend);
    }

    @Setter
    private FriendBrowserListener listener;

    public BrowseFriendAdapter(List<Friend> friends, FriendBrowserListener listener) {
        super(friends);
        this.listener = listener;
    }

    private void saveFriend(Friend friend) {
        listener.saveFriend(friend);
        notifyDataSetChanged();
    }

    @Override
    protected void setViewItems(ViewHolder holder, final int position) {
        holder.removeFriend.setVisibility(View.GONE);
        holder.sendMessage.setVisibility(View.GONE);

        //TODO choose another color
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
}
