package com.kuk.kukexamprep.network;

import android.net.Uri;
import android.util.Log;

import com.kuk.kukexamprep.model.PdfItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {
    private static final String TAG = "ApiClient";

    // Supabase configuration
    private static final String SUPABASE_URL = "https://wrpcjqfnsdcdhztvmlfa.supabase.co";
    private static final String STORAGE_URL = SUPABASE_URL + "/storage/v1/object/public";
    private static final String API_URL = SUPABASE_URL + "/rest/v1";

    // Add your Supabase anon key here
    private static final String SUPABASE_ANON_KEY = "your-supabase-anon-key";

    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    /**
     * Check if a file exists in Supabase storage
     */
    public static void checkFileExists(String fileUrl, ApiCallback<Boolean> callback) {
        Request request = new Request.Builder()
                .url(fileUrl)
                .head()  // HEAD request just checks if the file exists
                .build();

        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                callback.onSuccess(response.isSuccessful());
            }
        });
    }

    /**
     * Get notes path for a specific course, semester and subject
     */
    public static String getNotesPath(String course, String semester, String subject) {
        return STORAGE_URL + "/notes/"
                + Uri.encode(course) + "/"
                + Uri.encode(semester) + "/"
                + Uri.encode(subject) + "/"
                + Uri.encode(subject + ".pdf");
    }

    /**
     * Get syllabus path for a specific course and semester
     */
    public static String getSyllabusPath(String course, String semester) {
        return STORAGE_URL + "/syllabus/"
                + Uri.encode(course) + "/"
                + Uri.encode(semester) + "/"
                + Uri.encode("syllabus.pdf");
    }

    /**
     * Fetch list of all available PDFs from Supabase database (if you have a table for PDFs)
     */
    public static void fetchPdfList(ApiCallback<List<PdfItem>> callback) {
        Request request = new Request.Builder()
                .url(API_URL + "/pdfs?select=*")
                .addHeader("apikey", SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                .build();

        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API request failed", e);
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError(new IOException("Server error: " + response.code()));
                    return;
                }

                try {
                    String jsonData = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    List<PdfItem> pdfItems = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        PdfItem item = new PdfItem(
                                obj.getString("id"),
                                obj.getString("title"),
                                obj.getString("file_path"),
                                obj.optString("description", ""),
                                obj.optString("category", "notes")
                        );
                        pdfItems.add(item);
                    }

                    callback.onSuccess(pdfItems);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error", e);
                    callback.onError(e);
                }
            }
        });
    }

    /**
     * Download PDF bytes from URL
     */
    public static void downloadPdf(String fileUrl, ApiCallback<byte[]> callback) {
        Request request = new Request.Builder()
                .url(fileUrl)
                .build();

        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError(new IOException("Server error: " + response.code()));
                    return;
                }

                try {
                    byte[] bytes = response.body().bytes();
                    callback.onSuccess(bytes);
                } catch (IOException e) {
                    callback.onError(e);
                }
            }
        });
    }
}