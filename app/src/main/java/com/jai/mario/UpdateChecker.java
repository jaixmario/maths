package com.jai.mario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    private static final String VERSION_URL = "https://raw.githubusercontent.com/jaixmario/database/main/version.json";

    public static void checkForUpdate(Activity activity) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(VERSION_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();

            InputStream is = connection.getInputStream();
            Scanner scanner = new Scanner(is);
            StringBuilder json = new StringBuilder();
            while (scanner.hasNext()) {
                json.append(scanner.nextLine());
            }

            JSONObject obj = new JSONObject(json.toString());
            int latestVersionCode = obj.getInt("versionCode");
            String latestVersionName = obj.getString("versionName");
            String updateUrl = obj.getString("updateUrl");

            int currentVersionCode = com.jai.mario.BuildConfig.VERSION_CODE; // ✅ FIXED

            if (latestVersionCode > currentVersionCode) {
                new AlertDialog.Builder(activity)
                        .setTitle("Update Available")
                        .setMessage("A new version (" + latestVersionName + ") is available.")
                        .setPositiveButton("Update Now", (dialog, which) -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                            activity.startActivity(browserIntent);
                        })
                        .setNegativeButton("Later", null)
                        .show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Update check failed", Toast.LENGTH_SHORT).show();
        }
    }
}