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
 * Created by Peti on 2018. 04. 22..
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

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

    private List<Message> allMessages;
    private List<Message> messages;

    private MessageAdapterListener listener;

    public MessageAdapter(List<Message> allMessages, MessageAdapterListener listener) {
        this.allMessages = allMessages;
        this.messages = new ArrayList<>();
        this.listener = listener;
        filter();
    }

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

    private void sort() {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o2.getDateTime() - o1.getDateTime());
            }
        });
    }

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

    public void dataSetChanged() {
        listener.loading();
        filter();
        notifyDataSetChanged();
        listener.dataLoaded();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                filter();
                listener.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        listener.dataLoaded();
                    }
                });
            }
        }).start();*/
    }

    private void setCardMargin(CardView card, int leftPx, int bottomPx, int rightPx, int topPx) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        layoutParams.setMargins(dpToPx(leftPx), dpToPx(topPx), dpToPx(rightPx), dpToPx(bottomPx));
        card.requestLayout();
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = listener.getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void setTimestampVisibility(ViewHolder holder, Message message) {
        if (message.isShowTimeStamp()) {
            message.setShowTimeStamp(!Message.SHOW_TIMESTAMP);
        } else {
            message.setShowTimeStamp(Message.SHOW_TIMESTAMP);
        }
    }

    private void showTimestamp(Message message, ViewHolder holder) {
        if (message.isShowTimeStamp()) {
            holder.timestamp.setVisibility(View.VISIBLE);
        } else {
            holder.timestamp.setVisibility(View.INVISIBLE);
        }
    }
}
