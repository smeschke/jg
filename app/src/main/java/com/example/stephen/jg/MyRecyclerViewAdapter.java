package com.example.stephen.jg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class MyRecyclerViewAdapter extends
        RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ItemClickListener mClickListener;
    private List<String> mThumbs;
    private List<String> mAuthors;
    private List<String> mTitles;
    private List<String> mUrls;

    private final LayoutInflater mInflater;
    private final Context mContext;

    MyRecyclerViewAdapter(Context context,
                          List<String> thumbs,
                          List<String> urls,
    List<String> authors,
                          List<String> titles){
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mThumbs = thumbs;
        this.mTitles = titles;
        this.mAuthors = authors;
        this.mUrls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get url for poster
        String thumb_url = mThumbs.get(position);
        //set the image view using Picasso
        Picasso.with(mContext).load(thumb_url).into(holder.imageView);
        //set the title and description
        holder.authorTextView.setText(mAuthors.get(position));
        holder.titleTextView.setText(mTitles.get(position));
    }

    @Override
    public int getItemCount() {
        int nThumbs = mThumbs.size();
        int nTitles = mTitles.size();
        int nAuthors = mAuthors.size();
        int nUrls = mUrls.size();
        return Math.min(Math.min(nThumbs, nTitles), Math.min(nAuthors, nUrls));
    }

    //TODO (1) create ViewHolder class - implement OnClickListener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView imageView;
        final TextView titleTextView;
        final TextView authorTextView;

        // Set the click listener to the image view
        // When a user clicks on an image --> something happens based on the image a user clicked.
        ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_poster_image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            itemView.setOnClickListener(this);
        }

        //TODO (4c) 'wire' into ViewHolder
        @Override
        public void onClick(View view){
            mClickListener.onItemClick(getAdapterPosition());
        }
    }

    //TODO (4a) Create interface for the click listener
    public interface ItemClickListener{
        void onItemClick(int position);
    }
    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }
}
