package com.example.pulkit.capturista;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private PaginationAdapter pContext;

    private List<Image> images;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context,ArrayList<Image> images) {
        this.context = context;
        this.images = new ArrayList<>(images);
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_list, parent, false);
        viewHolder = new ImageVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        position = ((ImageVH)holder).pos;

        Image image = images.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                ImageVH imageVH = (ImageVH) holder;

                imageVH.textView.setText(image.getTitle());

                try{
                    Glide.with(context).load(image.getUri()).into(imageVH.imageView);
                }catch (Exception ignored){
                }

//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image.getUri());
//                    imageVH.imageView.setImageBitmap(bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == images.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Image mc) {
        images.add(mc);
        notifyItemInserted(images.size() - 1);
    }

    public void addAll(List<Image> mcList) {
        for (Image mc : mcList) {
            add(mc);
        }
    }

    public void remove(Image city) {
        int position = images.indexOf(city);
        if (position > -1) {
            images.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Image());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = images.size() - 1;
        Image item = getItem(position);

        if (item != null) {
            images.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Image getItem(int position) {
        return images.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ImageVH extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        MainActivity mContext;
        int pos;
        public ImageVH(View itemView) {
            super(itemView);
            if(itemView!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(mContext,
                                    Pair.create(v, "selectedImage")
                            ).toBundle();
                            Intent intent = new Intent(context, DetailsActivity.class);
                            intent.putExtra("bg", images.get(pos).getUri());
                            intent.putExtra("cover", images.get(pos).getTitle());
                            v.getContext().startActivity(intent, bundle);
                        }
                    }
                });
            }
            textView = (TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
