package hu.vadasz.peter.knockmessenger.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Activities.BaseActivity;
import hu.vadasz.peter.knockmessenger.Activities.CodeSettingActivity;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.morsecodedecoder.Code.Code;
import lombok.Getter;

import static hu.vadasz.peter.knockmessenger.Activities.CodeSettingActivity.CODE_UPDATE_REQUEST;

/**
 * This class provides data and manages the codes recycler view.
 */

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.ViewHolder> {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface CodeSettingsListener {
        BaseActivity getActivity();
        void deleteCode(Code code);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean ENABLED = true;

    /// CONSTANTS -- END

    private List<Code> codes;

    @Getter
    private List<Code> filteredCodes;
    private CodeSettingsListener codeSettingsListener;
    private String filterText;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public CodeAdapter(List<Code> codes, CodeSettingsListener codeSettingsListener) {
        this.codeSettingsListener = codeSettingsListener;
        this.codes = codes;
        filteredCodes = codes;

        sortCodes();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// RecyclerView.Adapter OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView card = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.code_card, parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.code.setText(filteredCodes.get(position).getCode());
        holder.text.setText(filteredCodes.get(position).getText());
        if (!filteredCodes.get(position).isInputSymbol()) {
            holder.deleteButton.setEnabled(!ENABLED);
            holder.deleteButton.setVisibility(View.GONE);
            holder.mainPanel.setBackgroundResource(R.drawable.code_card_rounded_corner_dark);
        } else {
            holder.deleteButton.setEnabled(ENABLED);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.mainPanel.setBackgroundResource(R.drawable.code_card_rounded_corner);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    codeSettingsListener.deleteCode(codes.get(position));
                }
            });
        }

        holder.settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSettings(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredCodes.size();
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
        sortCodes();
        filter(filterText);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// RecyclerView.Adapter OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void sortCodes() {
        Collections.sort(this.codes, new Comparator<Code>() {

            @Override
            public int compare(Code lhs, Code rhs) {
                return lhs.getText().compareTo(rhs.getText());
            }
        });
    }

    public void onItemDismiss(int position) {
        if (filteredCodes != codes) {
            filteredCodes.remove(position);
        }
        codeSettingsListener.deleteCode(codes.get(position));
    }

    public void showSettings(int position) {
        Intent intent = new Intent(codeSettingsListener.getActivity(), CodeSettingActivity.class);
        intent.putExtra(CodeSettingActivity.EXTRA_IS_UPDATE_CODE,true);
        intent.putExtra(CodeSettingActivity.EXTRA_CODE, filteredCodes.get(position));
        codeSettingsListener.getActivity().startActivityForResult(intent, CODE_UPDATE_REQUEST);
    }

    public void filter(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            filteredCodes = codes;
            this.filterText = null;
        } else {
            List<Code> filteredTmp = new ArrayList<>();
            for (Code code :  codes) {
                if (code.getText().toLowerCase().contains(filterText.toLowerCase())) {
                    filteredTmp.add(code);
                }
            }

            filteredCodes = filteredTmp;
            this.filterText = filterText;
        }

        notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// VIEW HOLDER PATTERN
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView card;

        @BindView(R.id.codeCard_code)
        TextView code;

        @BindView(R.id.codeCard_text)
        TextView text;

        @BindView(R.id.codeCard_settingsButton)
        ImageButton settingsButton;

        @BindView(R.id.codeCard_deleteButton)
        ImageButton deleteButton;

        @BindView(R.id.codeCard_mainPanel)
        RelativeLayout mainPanel;

        public ViewHolder(CardView card) {
            super(card);
            this.card = card;
            ButterKnife.bind(this, card);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// VIEW HOLDER PATTERN -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
