package com.example.pulkit.capturista;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.NonNull;
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
    private Uri mUri;

    public Image(){
        //empty constructor
    }

    public Image(String title, Uri mUri){
        this.title = title;
        this.mUri = mUri;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
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

    public static List<Image> createImages() {

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");
        final List<Image> images = new ArrayList<>();

        try {

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Image image = postSnapshot.getValue(Image.class);
                        images.add(image);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
//                    Toast.makeText(this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {

        }
        return images;
    }
}
