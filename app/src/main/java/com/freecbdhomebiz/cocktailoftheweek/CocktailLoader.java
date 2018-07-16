/*
 * Copyright (c) 2018. Tina Taylor
 * CREATIVE COMMONS Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * https://creativecommons.org/licenses/by-sa/3.0/
 */

package com.freecbdhomebiz.cocktailoftheweek;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class CocktailLoader extends AsyncTaskLoader<List<Cocktail>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = CocktailLoader.class.getName();

    private String mUrl;

    /**
     * Constructs a new {@link CocktailLoader}.
     *
     * @param context of the activity
     * @param url to query, sent from MainActivity onCreateLoader
     */
    public CocktailLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Cocktail> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of Cocktail articles.
        // Return list of cocktail articles to CocktailAdapter
        List<Cocktail> cocktailArticleList = QueryUtils.fetchCocktailData(mUrl);
        return cocktailArticleList;
    }
}
