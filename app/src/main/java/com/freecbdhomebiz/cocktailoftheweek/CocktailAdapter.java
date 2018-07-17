/*
 * Copyright (c) 2018. Tina Taylor
 * CREATIVE COMMONS Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * https://creativecommons.org/licenses/by-sa/3.0/
 */

package com.freecbdhomebiz.cocktailoftheweek;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CocktailAdapter extends ArrayAdapter {

    public CocktailAdapter(Context context, List<Cocktail> cocktailArticleList) {
        super(context, 0, cocktailArticleList);
    }

    /**
     * Returns Cocktail article data from a given position in the list passed from CocktailLoader
     * so that each item can be displayed
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the Cocktail article in the given position in the list of articles
        Cocktail currentCocktail = (Cocktail) getItem(position);

        // Get the name of the section of the newspaper
        String sectionName = currentCocktail.getSectionName();
        //Find the TextView with id of section_name
        TextView sectionView = listItemView.findViewById(R.id.section_name);
        sectionView.setText(sectionName);

        // Get the name of the Cocktail article
        String cocktailName = currentCocktail.getCocktailName();
        //Find the TextView with id of cocktail_name
        TextView cocktailView = listItemView.findViewById(R.id.cocktail_name);
        cocktailView.setText(cocktailName);

        // Get the author of the Cocktail article
        String author = currentCocktail.getAuthor();
        // Find the TextView with view ID author
        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(author);

        // Get the date of the Cocktail article
        String date = currentCocktail.getDate();
        // Find the TextView with id of date
        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(date);

        // Get the summary of the body of the Cocktail article
        String summary = currentCocktail.getSummary();
        // Find the TextView with view ID author
        TextView summaryView = listItemView.findViewById(R.id.summary);
        summaryView.setText(summary);

        // Return the list_item view that is now showing the extracted JSON data
        return listItemView;
    }
}
