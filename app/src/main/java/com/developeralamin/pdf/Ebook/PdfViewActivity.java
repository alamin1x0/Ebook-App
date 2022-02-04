package com.developeralamin.pdf.Ebook;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.developeralamin.pdf.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity {

    private String url;
    private PDFView pdfView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        url = getIntent().getStringExtra("pdfUrl");

        pdfView = findViewById(R.id.pdfView);

        progressBar = findViewById(R.id.pdfProgress);

        loadFile(url);

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private void loadFile(String url) {

        FileLoader.with(this)
                .load(url)
                .fromDirectory("test4", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File loadedFile = response.getBody();
                        progressBar.setVisibility(View.GONE);
                        pdfView.fromFile(loadedFile)
                                .password(null)
                                .defaultPage(0)
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .enableDoubletap(true)
                                .spacing(5)
                                .load();
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Toast.makeText(PdfViewActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
}