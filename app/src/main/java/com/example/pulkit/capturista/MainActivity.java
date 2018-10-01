package com.example.pulkit.capturista;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.example.pulkit.capturista.Image;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_CONSTANT = 234;
    private Uri filePath;

    private StorageReference mstorageReference;

    public static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        loadFirstPage();


        // mocking network delay for API call
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadFirstPage();
//            }
//        }, 1000);

    }

    public void databaseDone(ArrayList<Image> images,boolean isFirst) {
        adapter = new PaginationAdapter(this, images);
        if(isFirst){
            progressBar.setVisibility(View.GONE);

            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
            else isLastPage = true;
        }else {
            adapter.removeLoadingFooter();
            isLoading = false;

//        adapter.addAll(images);

            if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
            else isLastPage = true;
        }


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
        Image.createImages(this,true);

    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        Image.createImages(this,false);

    }

//    public void setCardListener(final List<Image> images){
//
//        rv.setOnClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                //We are passing Bundle to activity, these lines will animate when we laucnh activity
//                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
//                        Pair.create(view,"selectedMovie")
//                ).toBundle();
//
//                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
//                intent.putExtra("bg",images.get(i).getUri());
//                intent.putExtra("cover",images.get(i).getTitle());
//                startActivity(intent,bundle);
//
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == PICK_IMAGE_CONSTANT && resultCode== RESULT_OK && data!=null && data.getData()!=null){
//            filePath = data.getData();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageview.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}