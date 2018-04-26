package hu.vadasz.peter.knockmessenger.Activities.SearchSuggesters;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Peti on 2018. 03. 23..
 */

public class CodeSearchSuggestionProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =  "hu.vadasz.peter.knockmessenger.Activities.SearchSuggesters.CodeSearchSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public CodeSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
