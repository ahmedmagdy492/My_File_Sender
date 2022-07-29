package com.magdyradwan.my_file_sender.helpers;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {


    public String postRequest(String url, String data) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url)
                .openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
        out.write(data);
        out.flush();
        out.close();

        Log.d("HttpClient", "post body: ");

        httpURLConnection.connect();

        Log.d("HttpClient", "after connection: " +
                httpURLConnection.getResponseCode());

        if(!String.valueOf(httpURLConnection.getResponseCode()).startsWith("2")) {
            String response = readFromInputStream(httpURLConnection.getInputStream());
            Log.d("HttpClient", "postRequest: " + response);
            throw new IOException(response);
        }

        return readFromInputStream(httpURLConnection.getInputStream());
    }

    private String readFromInputStream(InputStream stream) throws IOException {
        InputStreamReader reader = new InputStreamReader(stream);
        StringBuilder str = new StringBuilder();
        BufferedReader br = new BufferedReader(reader);

        String line = br.readLine();

        while(line != null) {
            str.append(line);
            line = br.readLine();
        }

        br.close();

        return str.toString();
    }
}
