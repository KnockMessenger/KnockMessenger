package hu.vadasz.peter.knockmessenger.Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.R;

/**
 * This class is the adapter of the Message's RecyclerView.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This interface is implemented by the class which contains the Message's RecyclerView
     */

    public interface MessageAdapterListener {
        Activity getActivity();
        Friend getActualFriend();
        boolean isFriend(String tel);
        String getUserTel();
        User getUser();
        void noMessages();
        void loading();
        void dataLoaded();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private List<Message> allMessages;
    private List<Message> messages;

    private MessageAdapterListener listener;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MessageAdapter(List<Message> allMessages, MessageAdapterListener listener) {
        this.allMessages = allMessages;
        this.messages = new ArrayList<>();
        this.listener = listener;
        filter();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// RecyclerView.Adapter OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView card = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        return new MessageAdapter.ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Message message = messages.get(position);
        holder.message.setText(message.getMessage());
        DateTime dateTime = message.convertDateTimeAsToTimestamp();
        DateTime now = new DateTime();
        if (dateTime.getYear() == now.getYear() && dateTime.getMonthOfYear() == now.getMonthOfYear() && dateTime.getDayOfMonth() == now.getDayOfMonth()) {
            holder.timestamp.setText(message.convertDateTimeAsToTimestamp().
                    toString(DateTimeFormat.forPattern(listener.getActivity().getString(R.string.messageCard_today_timestamp))));
        } else {
            holder.timestamp.setText(message.convertDateTimeAsToTimestamp().
                    toString(DateTimeFormat.forPattern(listener.getActivity().getString(R.string.messageCard_old_timestamp))));
        }
        if (message.getMessageType() == Message.MessageType.IN) {
            holder.mainPanel.setBackgroundResource(R.drawable.message_card_rounded_corners);
            setCardMargin(holder.card,
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_incomingMessageLeftMargin),
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_incomingMessageBottomMargin),
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_incomingMessageRightMargin),
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_incomingMessageTopMargin));
        } else {
            holder.mainPanel.setBackgroundResource(R.drawable.message_card_rounded_corners_dark);
            setCardMargin(holder.card,
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_sentMessageLeftMargin),
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_sentMessageBottomMargin),
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_sentMessageRightMargin),
                    listener.getActivity().getResources().getInteger(R.integer.messageCard_sentMessageTopMargin));
        }

        if (listener.getActualFriend() == null && message.getMessageType() != Message.MessageType.OUT) {
            holder.from.setText(message.getFromTelephone());
        }

        showTimestamp(message, holder);

        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setTimestampVisibility(holder, messages.get(position));
                showTimestamp(message, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// RecyclerView.Adapter OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ViewHolder PATTERN
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.messageCard_messageText)
        TextView message;

        @BindView(R.id.messageCard_timestamp)
        TextView timestamp;

        @BindView(R.id.messageCard_from)
        TextView from;

        @BindView(R.id.messageCard_mainPanel)
        RelativeLayout mainPanel;

        private CardView card;

        public ViewHolder(CardView card) {
            super(card);
            this.card = card;
            ButterKnife.bind(this, card);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ViewHolder PATTERN -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method is called when the data messages changed, f. e. new message arrived.
     */

    public void dataSetChanged() {
        listener.loading();
        filter();
        notifyDataSetChanged();
        listener.dataLoaded();
    }

    /**
     * This method us used to style the message cards.
     * @param card the card to be styled.
     * @param leftPx the left margin of the card in pixel.
     * @param bottomPx the bottom margin of the card in pixel.
     * @param rightPx the right margin of the card in pixel.
     * @param topPx the top margin of the card in pixel.
     */

    private void setCardMargin(CardView card, int leftPx, int bottomPx, int rightPx, int topPx) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        layoutParams.setMargins(dpToPx(leftPx), dpToPx(topPx), dpToPx(rightPx), dpToPx(bottomPx));
        card.requestLayout();
    }

    /**
     * This method converts a dp value to pixel.
     * @param dp the dp to be converted.
     * @return the pixel value od the given dp.
     */

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = listener.getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * This method sets the timestamp of the sending of the message.
     * @param holder the holder of the chosen message.
     * @param message the chosen message.
     */

    private void setTimestampVisibility(ViewHolder holder, Message message) {
        if (message.isShowTimeStamp()) {
            message.setShowTimeStamp(!Message.SHOW_TIMESTAMP);
        } else {
            message.setShowTimeStamp(Message.SHOW_TIMESTAMP);
        }
    }

    /**
     * This method sets the timestamp of the sending of the message.
     * @param holder the holder of the chosen message.
     * @param message the chosen message.
     */

    private void showTimestamp(Message message, ViewHolder holder) {
        if (message.isShowTimeStamp()) {
            holder.timestamp.setVisibility(View.VISIBLE);
        } else {
            holder.timestamp.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This message filter the messages based on telephone.
     */

    private void filter() {
        messages.clear();
        if (listener.getUser() == null) {
            return;
        }

        for (Message message : allMessages) {
            if (listener.getActualFriend() != null) {
                if (!message.getDeleted() && (message.getFromTelephone().equals(listener.getActualFriend().getTel())
                        || message.getToTelephone().equals(listener.getActualFriend().getTel()))) {
                    messages.add(message);
                }
            } else {
                if (!message.getDeleted() && !message.getFromTelephone().equals(listener.getUserTel()) && !listener.isFriend(message.getFromTelephone())) {
                    messages.add(message);
                }
            }
        }
        if (messages.isEmpty()) {
            listener.noMessages();
        }
        sort();
    }

    /**
     * This method sorts the messages based on timestamp.
     */

    private void sort() {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o2.getDateTime() - o1.getDateTime());
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
