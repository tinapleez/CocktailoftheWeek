/*
 * Copyright (c) 2018. Tina Taylor
 * CREATIVE COMMONS Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * https://creativecommons.org/licenses/by-sa/3.0/
 */

package com.freecbdhomebiz.cocktailoftheweek;

public class Cocktail {
    private String mCocktailName;
    private String mAuthor;
    private String mDate;
    private String mSummary;
    private String mUrl;

    /**
     * Constructs a new Cocktail object
     *
     * @param cocktailName is the name of the drink
     * @param author is the author of the article
     * @param date is the published date of the article
     * @param summary is the summarized body of the article
     * @param url is the website link to the Cocktail article
     */
    public Cocktail(String cocktailName, String author, String date, String summary, String url) {
        mCocktailName = cocktailName;
        mAuthor = author;
        mDate = date;
        mSummary = summary;
        mUrl = url;
    }

    /**
     * Returns the name of the Cocktail
     */
    public String getCocktailName() {
        return mCocktailName;
    }

    /**
     * Returns the name of the author of the article
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the date of the article
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the summarized body of the article
     */
    public String getSummary() {
        return mSummary;
    }

    /**
     * Returns the website URL to find Cocktail article.
     */
    public String getUrl() {
        return mUrl;
    }
}
