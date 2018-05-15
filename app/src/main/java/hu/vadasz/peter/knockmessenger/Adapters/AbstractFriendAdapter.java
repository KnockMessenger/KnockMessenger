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
 * This class is the base class of the RecyclerViews which represent friends and other users.
 */

public abstract class AbstractFriendAdapter extends RecyclerView.Adapter<AbstractFriendAdapter.ViewHolder> {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Setter
    protected List<Friend> friends;

    protected List<Friend> filteredFriends;

    protected String filterText;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public AbstractFriendAdapter(List<Friend> friends) {
        this.friends = friends;
        filteredFriends = friends;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// RecyclerView.Adapter OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// RecyclerView.Adapter OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ViewHolder PATTERN
    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ViewHolder PATTERN -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This methods organizes the cards elements.
     * @param holder the viewHolder to manage.
     * @param position the adapter position of the viewHolder.
     */

    protected abstract void setViewItems(ViewHolder holder, final int position);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method filters the friends. Only those fiends will be shown which names or telephones
     * contains the filterText.
     * @param filterText the base text of the filter.
     */

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

    /**
     * The Adapter can be notified that the data changed by this method.
     */

    public void dataSetChanged() {
        filter(filterText);
    }

    /**
     * This method is called when a user is deleted.
     * @param friend the friend to remove.
     */

    public void friendRemoved(Friend friend) {
        friends.remove(friend);
        dataSetChanged();
    }

    /**
     * This method is called when new user or friend data arrives.
     * @param friend the new user or friend.
     */

    public void friendAdded(Friend friend) {
        friends.add(friend);
        dataSetChanged();
    }

    /**
     * This method is called when one of the user's data changed.
     * @param friend
     */

    public void friendChanged(Friend friend) {
        for (int i = 0; i < friends.size(); ++i) {
            if (friends.get(i).equals(friend)) {
                friends.set(i, friend);
                break;
            }
        }
        dataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
