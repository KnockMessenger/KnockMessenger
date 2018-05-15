package hu.vadasz.peter.knockmessenger.Activities.DragAndDrop;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.vadasz.peter.knockmessenger.Adapters.CodeAdapter;

/**
 * This class is responsible for swiping CodeCards. CodeCards can be deleted or edited via swiping.
 */

public class CodeCardTouchHelper extends ItemTouchHelper.Callback {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean DRAG_ENABLED = true;
    public static final boolean SWIPE_ENABLED = true;

    /// CONSTANTS -- END

    private CodeAdapter adapter;

    public CodeCardTouchHelper(CodeAdapter adapter) {
        this.adapter = adapter;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ItemTouchHelper.Callback INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ItemTouchHelper.Callback INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
