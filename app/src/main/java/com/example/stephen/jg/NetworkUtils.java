package com.example.stephen.jg;
//The networking is based on T02.05 - Github Search Query.

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class NetworkUtils {
    // Use scanner to 'catch' the data.
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            String output = "";
            Scanner scanner = new Scanner(in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output += line;
            }
            return output;
        } finally {
            urlConnection.disconnect();
        }
    }
}
