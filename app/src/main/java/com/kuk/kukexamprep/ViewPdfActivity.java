package com.kuk.kukexamprep;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewPdfActivity extends AppCompatActivity {
    private PDFView pdfView;
    private ProgressBar progressBar;
    private TextView errorText;
    private FloatingActionButton downloadFab;
    private String pdfUrl;
    private String pdfTitle;
    private byte[] pdfBytes;

    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        downloadFab = findViewById(R.id.downloadFab);

        // Get PDF details from intent
        pdfUrl = getIntent().getStringExtra("pdf_url");
        pdfTitle = getIntent().getStringExtra("pdf_title");

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(pdfTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load PDF
        loadPdfFromUrl();

        // Setup download button
        downloadFab.setOnClickListener(v -> {
            if (pdfBytes != null && pdfBytes.length > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ doesn't need storage permission for downloads
                    downloadPdf();
                } else {
                    // Check storage permission for older Android versions
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        downloadPdf();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_CODE);
                    }
                }
            } else {
                Toast.makeText(this, "PDF not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPdfFromUrl() {
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        // Use OkHttp to download the PDF
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(pdfUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    showError("Error downloading PDF: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        showError("Error downloading PDF. Server returned: " + response.code());
                    });
                    return;
                }

                // Save PDF to memory
                try {
                    pdfBytes = response.body().bytes();
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        displayPdf();
                    });
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        showError("Error reading PDF data: " + e.getMessage());
                    });
                }
            }
        });
    }

    private void displayPdf() {
        pdfView.fromBytes(pdfBytes)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAnnotationRendering(false)
                .spacing(10)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        showError("Error rendering PDF: " + t.getMessage());
                    }
                })
                .load();
    }

    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisibility(View.VISIBLE);
    }

    private void downloadPdf() {
        String filename = pdfTitle.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, use MediaStore
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                    if (outputStream != null) {
                        outputStream.write(pdfBytes);
                        Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // For older versions
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, filename);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(pdfBytes);
                Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPdf();
            } else {
                Toast.makeText(this, "Storage permission is required to download PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}