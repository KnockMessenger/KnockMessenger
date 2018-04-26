package hu.vadasz.peter.knockmessenger.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.R;
import lombok.Setter;

/**
 * Created by Peti on 2018. 04. 13..
 */

public abstract class AbstractFriendAdapter extends RecyclerView.Adapter<AbstractFriendAdapter.ViewHolder> {

    @Setter
    protected List<Friend> friends;

    protected List<Friend> filteredFriends;

    protected String filterText;

    public AbstractFriendAdapter(List<Friend> friends) {
        this.friends = friends;
        filteredFriends = friends;
    }

    @Override
    public AbstractFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView card = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_card, parent, false);
        return new AbstractFriendAdapter.ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(AbstractFriendAdapter.ViewHolder holder, final int position) {
        holder.name.setText(filteredFriends.get(position).getName());
        holder.tel.setText(filteredFriends.get(position).getTel());

        setViewItems(holder, position);
    }

    @Override
    public int getItemCount() {
        return filteredFriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected CardView card;

        @BindView(R.id.friendCard_nameText)
        TextView name;

        @BindView(R.id.friendCard_telText)
        TextView tel;

        @BindView(R.id.friendCard_addFriend_button)
        ImageButton addFriend;

        @BindView(R.id.friendCard_removeFriend_button)
        ImageButton removeFriend;

        @BindView(R.id.friendCard_sendMessageToFriend_button)
        ImageButton sendMessage;

        @BindView(R.id.friendCard_mainPanel)
        RelativeLayout mainPanel;

        ViewHolder(CardView card) {
            super(card);
            this.card = card;
            ButterKnife.bind(this, card);
        }

    }

    protected abstract void setViewItems(ViewHolder holder, final int position);

    public void filter(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            filteredFriends = friends;
            this.filterText = null;
        } else {
            List<Friend> filteredTmp = new ArrayList<>();
            for (Friend friend : friends) {
                if (friend.getName().toLowerCase().contains(filterText.toLowerCase()) || friend.getTel().contains(filterText.toLowerCase())) {
                    filteredTmp.add(friend);
                }
            }

            filteredFriends = filteredTmp;
            this.filterText = filterText;
        }

        notifyDataSetChanged();
    }

    public void dataSetChanged() {
        filter(filterText);
    }

    public void friendRemoved(Friend friend) {
        friends.remove(friend);
        dataSetChanged();
    }

    public void friendAdded(Friend friend) {
        friends.add(friend);
        dataSetChanged();
    }

    public void friendChanged(Friend friend) {
        for (int i = 0; i < friends.size(); ++i) {
            if (friends.get(i).equals(friend)) {
                friends.set(i, friend);
                break;
            }
        }
        dataSetChanged();
    }
}
