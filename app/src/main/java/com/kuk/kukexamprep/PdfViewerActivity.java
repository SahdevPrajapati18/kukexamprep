package com.kuk.kukexamprep;

import android.graphics.pdf.PdfRenderer;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PdfViewerActivity extends AppCompatActivity {

    private ImageView pdfImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfImageView = findViewById(R.id.pdfImageView);

        try {
            // Copy PDF from assets to cache
            String fileName = "yourfile.pdf";  // Change to your PDF file name
            File file = new File(getCacheDir(), fileName);
            if (!file.exists()) {
                InputStream asset = getAssets().open(fileName);
                FileOutputStream output = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int size;
                while ((size = asset.read(buffer)) != -1) {
                    output.write(buffer, 0, size);
                }
                asset.close();
                output.close();
            }

            // Render PDF
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer renderer = new PdfRenderer(fileDescriptor);
            PdfRenderer.Page page = renderer.openPage(0);

            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            pdfImageView.setImageBitmap(bitmap);

            page.close();
            renderer.close();
            fileDescriptor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
