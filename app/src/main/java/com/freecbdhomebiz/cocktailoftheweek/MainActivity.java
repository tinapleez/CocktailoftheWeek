/*
 * Copyright (c) 2018. Tina Taylor
 * CREATIVE COMMONS Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * https://creativecommons.org/licenses/by-sa/3.0/
 */

package com.freecbdhomebiz.cocktailoftheweek;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.freecbdhomebiz.cocktailoftheweek.BuildConfig.MY_GUARDIAN_API;

public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Cocktail>> {

    private TextView mImage;
    private TextView mCocktailName;
    private TextView mAuthor;
    private TextView mDate;
    private TextView mSummary;
    private ListView mlistView;
    private TextView mEmptyView;
    private ProgressBar mSpinningCircle;

    private void assignViews() {
        mImage = findViewById(R.id.image);
        mCocktailName = findViewById(R.id.cocktail_name);
        mAuthor = findViewById(R.id.author);
        mDate = findViewById(R.id.date);
        mSummary = findViewById(R.id.summary);
        mlistView = findViewById(R.id.list);
        mEmptyView = findViewById(R.id.empty_view);
        mSpinningCircle = findViewById(R.id.spinning_circle);
    }

    /**
     * URL for querying the Guardian website for JSON response
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/lifeandstyle/series/the-good-mixer?order-by=newest&" +
                    "byline&show-fields=byline&show-blocks=body&api-key=" + MY_GUARDIAN_API;
    
    /**
     * Constant value for the Cocktail loader ID. Only 1 loader.
     */
    private static final int COCKTAIL_LOADER_ID = 1;
    /**
     * Adapter for the list of Cocktail articles
     */
    private CocktailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        mlistView.setEmptyView(mEmptyView);

        // Create a new adapter that takes an empty list of Cocktails as input
        mAdapter = new CocktailAdapter(this, new ArrayList<Cocktail>());

        // Set the adapter on the {@link ListView}
        mlistView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a web page with the article about the Cocktail.
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current Cocktail article that was clicked on
                Cocktail currentCocktail = (Cocktail) mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri cocktailUri = Uri.parse(currentCocktail.getUrl());

                // Create a new intent to view the Cocktail URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, cocktailUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the loader ID, and null for the bundle, and this
            // activity
            loaderManager.initLoader(COCKTAIL_LOADER_ID, null, MainActivity.this);
        } else {
            // Otherwise, display error
            // First, hide ProgressBaar loading indicator so error message will be visible
            mSpinningCircle.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Cocktail>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new CocktailLoader(MainActivity.this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Cocktail>> loader, List<Cocktail> cocktails) {

        // Hide Progress Bar loading indicator
        mSpinningCircle.setVisibility(View.GONE);

        // Set empty state text to display "No cocktails found."
        mEmptyView.setText(R.string.no_cocktails);

        // Clear the adapter of previous cocktail article data
        mAdapter.clear();

        // If there is a valid list of {@link Cocktail}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (cocktails != null && !cocktails.isEmpty()) {
            mAdapter.addAll(cocktails);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Cocktail>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
