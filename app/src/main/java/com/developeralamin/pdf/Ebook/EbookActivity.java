package com.developeralamin.pdf.Ebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developeralamin.pdf.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EbookActivity extends AppCompatActivity {

    private RecyclerView ebookRecyler;
    private DatabaseReference reference;
    private List<EbookData> list;
    private EbookAdapter adapter;

    private AdView adView;

    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout shimmerLayout;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);

        ebookRecyler = findViewById(R.id.ebookRecyler);

        reference = FirebaseDatabase.getInstance().getReference().child("pdf");

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerLayout = findViewById(R.id.shimmer_layout);
        search = findViewById(R.id.searchText);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getData();

        if (!isConnected()) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.wifi)
                    .setTitle("Internet Connection Please")
                    .setMessage("Please Check your Internet Connection?")
                    .setCancelable(false)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }

    }

    private void getData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EbookData data = snapshot.getValue(EbookData.class);
                    list.add(data);
                }

                adapter = new EbookAdapter(EbookActivity.this, list);
                ebookRecyler.setLayoutManager(new LinearLayoutManager(EbookActivity.this));
                ebookRecyler.setAdapter(adapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(EbookActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<EbookData> filterList = new ArrayList<>();
        for (EbookData item : list) {
            if (item.getPdfTitle().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }

        adapter.FilteredList(filterList);
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        shimmerFrameLayout.startShimmer();
        super.onResume();
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}

