package hu.vadasz.peter.knockmessenger.Activities.DragAndDrop;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.vadasz.peter.knockmessenger.Adapters.FriendsAdapter;

/**
 * Created by Peti on 2018. 04. 14..
 */

public class FriendCardTouchHelper extends ItemTouchHelper.Callback {

    public static final boolean DRAG_ENABLED = true;
    public static final boolean SWIPE_ENABLED = true;

    private FriendsAdapter friendAdapter;

    public FriendCardTouchHelper(FriendsAdapter friendAdapter) { this.friendAdapter = friendAdapter; }

    @Override
    public boolean isLongPressDragEnabled(){
        return !DRAG_ENABLED;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return SWIPE_ENABLED;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        friendAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
