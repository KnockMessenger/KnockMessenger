package hu.vadasz.peter.knockmessenger.Activities.DragAndDrop;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.vadasz.peter.knockmessenger.Adapters.CodeAdapter;

/**
 * Created by Peti on 2018. 03. 21..
 */

public class CodeCardTouchHelper extends ItemTouchHelper.Callback {

    public static final boolean DRAG_ENABLED = true;
    public static final boolean SWIPE_ENABLED = true;

    private CodeAdapter adapter;

    public CodeCardTouchHelper(CodeAdapter adapter) {
        this.adapter = adapter;
    }

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
        int dragFlags = 0;
        boolean canBeDeleted = adapter.getFilteredCodes().get(viewHolder.getAdapterPosition()).isInputSymbol();
        int swipeFlags = canBeDeleted ? ItemTouchHelper.START | ItemTouchHelper.END : ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        } else {
            adapter.showSettings(viewHolder.getAdapterPosition());
        }

    }
}
