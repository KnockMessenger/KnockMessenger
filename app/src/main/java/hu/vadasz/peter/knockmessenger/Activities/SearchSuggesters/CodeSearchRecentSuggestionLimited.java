package hu.vadasz.peter.knockmessenger.Activities.SearchSuggesters;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.SearchRecentSuggestions;

/**
 * Created by Peti on 2018. 03. 23..
 */

public class CodeSearchRecentSuggestionLimited extends SearchRecentSuggestions {

    private int limit;

    public CodeSearchRecentSuggestionLimited(Context context, String authority, int mode, int limit) {
        super(context, authority, limit);
        this.limit = limit;
    }

    @Override
    protected void truncateHistory(ContentResolver cr, int maxEntries) {
        super.truncateHistory(cr, limit);
    }
}
