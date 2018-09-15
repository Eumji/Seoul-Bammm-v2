package androidex.example.com.seoulbammmproj;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final String TAG = "CommuHommmm";

    List<Post> mPost;
    static int currentWidth = 0;
    static int currentHeight = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView ivThumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            ivThumbnail.getLayoutParams().height = currentWidth;
            ivThumbnail.getLayoutParams().width = currentWidth;
            ivThumbnail.requestLayout();
            Log.d("CommuHommmm","MyViewHolder 생성 완료");
            Log.d("CommuHommmm", "currentWidth : "+currentWidth+", currentHeight : "+currentHeight);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostListAdapter(List<Post> posts, int width, int height) {
        this.mPost = posts;
        this.currentWidth = width;
        this.currentHeight = height;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_view, parent, false);
        Log.d(TAG,"onCreateViewHolder()");
        return new MyViewHolder(v);
    }





    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        Picasso.get().load(mPost.get(position).getImage()).fit().centerInside().into(myViewHolder.ivThumbnail);
        Log.d(TAG,"getDate() : "+mPost.get(position).getDate());
        Log.d(TAG,"getImage() : "+mPost.get(position).getImage());
        Log.d(TAG,"onBindViewHolder() ... 썸네일 가져오기");

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post thisPost = mPost.get(position);
                Log.d(TAG, "onClick: "+thisPost.getCamera());
                CommuHomeActivity commuHomeActivity;
                commuHomeActivity = (CommuHomeActivity) view.getContext();
                commuHomeActivity.viewPostDetail(thisPost.getDate());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPost.size();
    }

}
