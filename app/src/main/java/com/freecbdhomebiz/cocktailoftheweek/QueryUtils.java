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
     * Query the GUARDIAN and return a list of {@link Cocktail} objects. Called from CocktailLoader
     */
    public static List<Cocktail> fetchCocktailData(String requestUrl) {

        // Just for fun, can simulate a slow network response by sleeping for 5 seconds
        //try {
        //   Thread.sleep(5000);
        //} catch (InterruptedException e) {
        //   e.printStackTrace();
        //}

        // Create URL object because makeHttpRequest does not accept a String input
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
        List<Cocktail> cocktails = extractFieldsFromJson(jsonResponse);

        // Return the list of {@link Cocktail} articles
        return cocktails;
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
        // Return the String of JSON data to start parsing in extractFieldsFromJson()
        return jsonResponse;
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

    /**
     * Parse the String of JSON response and return a list extracted of {@link Cocktail} objects,
     * Called from fetchCocktailData()
     */
    private static List<Cocktail> extractFieldsFromJson(String cocktailJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(cocktailJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Cocktails article fields to
        List<Cocktail> cocktailList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(cocktailJson);

            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "leadContent",
            JSONArray leadContentArray = responseObject.getJSONArray("leadContent");

            // For each key in the leadContentArray, create an {@link Cocktail}
            // object for the fields needed
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


                // The key "fields" object holds the byline, which is the author's name
                JSONObject fields = currentCocktail.getJSONObject("fields");
                String author = fields.getString("byline");

                // The key "blocks" object holds the "body" array, which first element contains the
                // article summary
                JSONObject blocks = currentCocktail.getJSONObject("blocks");
                JSONArray body = blocks.getJSONArray("body");
                JSONObject bodyFields = body.getJSONObject(0);
                String summary = bodyFields.getString("bodyTextSummary");

                // Create a new {@link Cocktail} object with the cocktailName, author, date,
                // summary, and url
                Cocktail fieldsExtracted = new Cocktail(cocktailName, author, date, summary, url);

                // Add the new object to the list of Cocktail articles.
                cocktailList.add(fieldsExtracted);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of Cocktail articles to the method call inside fetchCocktailData()
        return cocktailList;
    }
}
