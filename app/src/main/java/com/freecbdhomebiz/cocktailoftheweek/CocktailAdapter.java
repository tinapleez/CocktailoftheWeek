/*
 * Copyright (c) 2018. Tina Taylor
 * CREATIVE COMMONS Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * https://creativecommons.org/licenses/by-sa/3.0/
 */

package com.freecbdhomebiz.cocktailoftheweek;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CocktailAdapter extends ArrayAdapter {

    public CocktailAdapter(Context context, List<Cocktail> cocktails) {
        super(context, 0, cocktails);
    }

    /**
     * Returns cocktail article data from a given position in the list so that it can be displayed
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


        // Get the name of the Cocktail article
        String cocktailName = currentCocktail.getCocktailName();
        //Find the TextView with id of cocktail_name
        TextView cocktailView = listItemView.findViewById(R.id.cocktail_name);
        // Display the cocktail_name of the current cocktail article in that TextView
        cocktailView.setText(cocktailName);


        // Get the author of the Cocktail article
        String author = currentCocktail.getAuthor();
        // Find the TextView with view ID author
        TextView authorView = listItemView.findViewById(R.id.author);
        // Display the author of the current Cocktail article in that TextView
        authorView.setText(author);


        // Create a new Date object
        Date dateObject = new Date(currentCocktail.getDate());
        // Find the TextView with id of date
        TextView dateView = listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984") using helper method below
        String formattedDate = formatDate(dateObject);
        //Display the date of that current Cocktail article in that TextView
        dateView.setText(formattedDate);


        // Get the summary of the body of the Cocktail article
        String summary = currentCocktail.getSummary();
        // Find the TextView with view ID author
        TextView summaryView = listItemView.findViewById(R.id.summary);
        // Display the summary of the current Cocktail article in that TextView
        authorView.setText(summary);


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }


    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
}
