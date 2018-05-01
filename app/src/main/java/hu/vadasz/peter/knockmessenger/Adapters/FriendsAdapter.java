package hu.vadasz.peter.knockmessenger.Adapters;

import android.view.View;

import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;

/**
 * Created by Peti on 2018. 04. 14..
 */

public class FriendsAdapter extends AbstractFriendAdapter {

    public interface FriendListener {
        void removeFriend(Friend friend);
        void sendMessage(String telephone);
        void confirmDelete(Friend friend);
    }

    private FriendListener listener;

    public FriendsAdapter(List<Friend> friends, FriendListener listener) {
        super(friends);
        this.listener = listener;

    }

    @Override
    public void setViewItems(final ViewHolder holder, final int position) {
        holder.addFriend.setVisibility(View.GONE);
        holder.removeFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeFriend(friends.get(position));
            }
        });

        holder.sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.sendMessage(friends.get(holder.getAdapterPosition()).getTel());
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.sendMessage(friends.get(holder.getAdapterPosition()).getTel());
            }
        });
    }

    public void onItemDismiss(int position) {
        listener.confirmDelete(friends.get(position));
        //removeFriend(friends.get(position));
    }

    private void removeFriend(Friend friend) {
        /*listener.removeFriend(friend);
        if (filteredFriends.contains(friend)) {
            filteredFriends.remove(friend);
        }
        notifyDataSetChanged();*/
        listener.confirmDelete(friend);
    }

}
