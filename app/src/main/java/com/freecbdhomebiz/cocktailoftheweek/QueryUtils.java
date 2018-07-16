/*
 * Copyright (c) 2018. Tina Taylor
 * CREATIVE COMMONS Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * https://creativecommons.org/licenses/by-sa/3.0/
 */

package com.freecbdhomebiz.cocktailoftheweek;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for requesting and receiving Cocktail article data.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Cocktail} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Cocktail> extractFeatureFromJson(String cocktailJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(cocktailJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding cocktails to
        List<Cocktail> cocktails = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(cocktailJson);

            // Extract the JSONArray associated with the key called "leadContent",
            // which has the title, the date, and the url of the Cocktail article.
            JSONArray leadContentArray = baseJsonResponse.getJSONArray("leadContent");

            // For each Cocktail article in the leadContentArray, create an {@link Cocktail}
            // object
            for (int i = 0; i < leadContentArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentCocktail = leadContentArray.getJSONObject(i);

                // Extract the value for the key called "webTitle", which is the name of the
                // article.
                String cocktailName = currentCocktail.getString("webTitle");

                // Extract the value for the key called "webPublicationDate"
                String date = currentCocktail.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                String url = currentCocktail.getString("webUrl");


                // The key "fields" holds the byline, which holds the author's name
                JSONObject fields = currentCocktail.getJSONObject("fields");

                String author = fields.getString("byline");


                // The key "blocks" holds the body of the article summary
                JSONObject blocks = currentCocktail.getJSONObject("blocks");

                JSONObject body = blocks.getJSONObject("body");

                String summary = body.getString("bodyTextSummary");


                // Create a new {@link Cocktail} object with the cocktailName, author, date,
                // summary, and url
                Cocktail cocktail = new Cocktail(cocktailName, author, date, summary, url);

                // Add the new {@link Earthquake} to the list of cocktails.
                cocktails.add(cocktail);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of cocktails
        return cocktails;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils", "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem retrieving the Guardian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Query the GUARDIAN and return a list of {@link Cocktail} objects.
     */
    public static List<Cocktail> fetchCocktailData(String requestUrl) {
        Log.i("QueryUtils", "TEST: fetchCocktailData() called ...");

        // Just for fun, can simulate a slow network response by sleeping for 5 seconds
        //try {
        //   Thread.sleep(5000);
        //} catch (InterruptedException e) {
        //   e.printStackTrace();
        //}

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Cocktail}
        // articles
        List<Cocktail> cocktails = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Cocktail} articles
        return cocktails;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
