package hu.vadasz.peter.knockmessenger.Activities.SearchSuggesters;

import android.content.SearchRecentSuggestionsProvider;

/**
 * This class is responsible for providing search suggestions. This class is planned for later use.
 */

public class CodeSearchSuggestionProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =  "hu.vadasz.peter.knockmessenger.Activities.SearchSuggesters.CodeSearchSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public CodeSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
