package com.example.pulkit.capturista;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.pulkit.capturista.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Image {

    private String title;
    private String uri;

    public Image() {
        //empty constructor
    }

    public Image(String title, String mUri) {
        this.title = title;
        this.uri = mUri;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String mUri) {
        this.uri = mUri;
    }
//    public static List<Image> createImages(int itemCount) {
//        List<Image> images = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Image image = new Image("Image " + (itemCount == 0 ?
//                    (itemCount + 1 + i) : (itemCount + i)));
//            movies.add(image);
//        }
//        return images;
//        return null;
//    }

    static MainActivity context;
    static boolean isFirst;
    public static List<Image> createImages(MainActivity context, final boolean isFirst) {

        Image.context = context;
        Image.isFirst=isFirst;
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");
        final ArrayList<Image> images = new ArrayList<>();

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Image image = new Image(postSnapshot.getKey(),
                            postSnapshot.getValue().toString());
                    images.add(image);
                    Image.context.databaseDone(images,isFirst);
                    Log.d(MainActivity.TAG, "Image: " + image.title + ", " + image.uri);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(MainActivity.TAG, "Error: " + databaseError.getMessage());
            }
        });

        return images;
    }
}