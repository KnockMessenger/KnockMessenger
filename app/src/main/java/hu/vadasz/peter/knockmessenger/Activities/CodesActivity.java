package hu.vadasz.peter.knockmessenger.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Activities.DragAndDrop.CodeCardTouchHelper;
import hu.vadasz.peter.knockmessenger.Adapters.CodeAdapter;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.CodeDataManager;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.morsecodedecoder.Code.Code;
import hu.vadasz.peter.morsecodedecoder.Decoder.Utils.MorseCodeTable;

/**
 * This activity provide code operations. Codes can be listed, deleted, updated.
 */

public class CodesActivity extends BaseActivity implements CodeAdapter.CodeSettingsListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.code_recyclerView)
    RecyclerView codes;

    private SearchView searchView;
    private String searchedText;

    private CodeAdapter codeAdapter;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes);
        ButterKnife.bind(this);

        codes.setLayoutManager(new LinearLayoutManager(this));
        codeAdapter = new CodeAdapter(codeDataManager.getActualCodes(), this);
        codes.setAdapter(codeAdapter);
        setTitle(EMPTY_TEXT);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CodeCardTouchHelper(codeAdapter));
        itemTouchHelper.attachToRecyclerView(codes);

        codes.setHasFixedSize(RECYCLER_VIEW_HAS_FIXED_SIZE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeSettingActivity.CODE_UPDATE_REQUEST && resultCode == CodeSettingActivity.CODE_UPDATE_SUCCESS) {
            updateCode((Code) data.getParcelableExtra(CodeSettingActivity.EXTRA_CODE));
            codeAdapter.setCodes(codeDataManager.getActualCodes());
            showMessage(getString(R.string.save_success));
        } else if (requestCode == CodeSettingActivity.CODE_CREATE_REQUEST && resultCode == CodeSettingActivity.CODE_CREATE_SUCCESS) {
            saveCode((Code) data.getParcelableExtra(CodeSettingActivity.EXTRA_CODE));
            codeAdapter.setCodes(codeDataManager.getActualCodes());
            showMessage(getString(R.string.save_success));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.codes_activity_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.codesActivity_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                searchedText = newText;
                codeAdapter.filter(newText);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchedText = newText;
                codeAdapter.filter(newText);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.codesActivity_addCode:
                Intent intent = new Intent(this, CodeSettingActivity.class);
                intent.putExtra(CodeSettingActivity.EXTRA_IS_CREATE_CODE,true);
                startActivityForResult(intent, CodeSettingActivity.CODE_CREATE_REQUEST);
                break;
            case R.id.codesActivity_resetCodes:
                resetCodes();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CodeAdapter.CodeSettingsListener INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void deleteCode(Code code) {
        conFirmDelete(code);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CodeAdapter.CodeSettingsListener INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method provides opportunity to reset the default Morse codes.
     */

    private void resetCodes() {
        codeDataManager.saveAllCodesJson(MorseCodeTable.getDefaultCodeTable(), CodeDataManager.CODE_TABLE_NAME);
        if (sharedPreferenceManager.getBoolean(SharedPreferenceManager.MODE_PREFERENCE_KEY)) {
            codeDataManager.loadMorseCodes();
        } else {
            codeDataManager.loadHuffmanCodes();
        }

        codeAdapter.setCodes(codeDataManager.getActualCodes());
        codeAdapter.sortCodes();
        codeAdapter.notifyDataSetChanged();
    }

    /**
     * This method persists a new code.
     * @param code the code to save.
     */

    private void saveCode(Code code) {
        codeDataManager.addCode(code);
    }

    /**
     * This method updates an existing code.
     * @param code the code to update.
     */

    private void updateCode(Code code) {
        codeDataManager.updateCode(code);
    }

    /**
     * This method shows a message and requires confirmation before deleting a code.
     * @param code the code to be deleted.
     */

    private void conFirmDelete(final Code code) {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.codesActivity_confirm_delete_text), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.codesActivity_confirm_delete_yes_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        codeDataManager.deleteCode(code);
                        codeAdapter.sortCodes();
                    }
                })
                .setActionTextColor(getColor(android.R.color.holo_red_light))
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        codeAdapter.setCodes(codeDataManager.getActualCodes());
                        codeAdapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
