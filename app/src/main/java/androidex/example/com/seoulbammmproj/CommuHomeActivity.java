package androidex.example.com.seoulbammmproj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CommuHomeActivity extends AppCompatActivity {

    final String TAG = "CommuHommmm";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    // 이걸로 하면 거꾸로 나오는 함수 빨간줄 뜸
    //private RecyclerView.LayoutManager mLayoutManager;
    //private GridLayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManager;
    ArrayList<Post> postsList;
    int height, width;
    public int flagRefresh; // 0이면 새로고침, 1이면 X
    public static boolean flagFirstLoad;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int visiblePosts = 0;
    final int VISIBLE_PLUS = 5;

    //private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView.OnScrollListener scrollListener;

    ImageView ivAddPost, ivReFresh;
    Toolbar toolbar = null;

    FirebaseDatabase database;
    DatabaseReference myRef;
    //Query mNewPostsQuery;

    Display display;
    Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_home);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        flagFirstLoad = true;
        flagRefresh = 0;

        mRecyclerView = findViewById(R.id.rvPostList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CommuHomeActivity.this);
        //mLayoutManager = new GridLayoutManager(CommuHomeActivity.this, 2);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        /*scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("CommuHommmmm", "onLoadMore: Scrolled~~~");
                loadNextDataFromApi(page);
            }
        };*/
        /*scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(-1)) {
                    Log.d(TAG, "Top of List");
                } else if (!mRecyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "End of List");
                    loadNextDataFromApi();
                } else {
                    Log.d(TAG, "idle");
                }
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);*/

        postsList = new ArrayList<>();
        mAdapter = new PostListAdapter(postsList, width, height);
        mRecyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                Log.d(TAG, "onChildAdded: postsList = " + post.toString());
                postsList.add(post);
                Log.d(TAG, "onChildAdded: postsList.size() = " + postsList.size());

                if (flagFirstLoad) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            posting(postsList.size());
                            flagFirstLoad = false;
                        }
                    }, 1500);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                String delete = post.getDate();
                for (int i = 0; i < postsList.size(); i++) {
                    if (postsList.get(i).getDate().equals(delete)) {
                        postsList.remove(postsList.get(i));
                        mAdapter.notifyItemRemoved(i);
                    }
                }
                Log.d(TAG, "onChildRemoved: postsList = " + post.toString());
                Log.d(TAG, "onChildRemoved: postsList.size() = " + postsList.size());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*visiblePosts += VISIBLE_PLUS;
                mNewPostsQuery = myRef.limitToLast(visiblePosts);
                Log.d("CommuHommmmm", "visiblePosts : " + visiblePosts);
                mNewPostsQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Post post = dataSnapshot.getValue(Post.class);
                postsList.add(post);
                Log.d(TAG, post.toString());
                posting(postsList.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ivAddPost = findViewById(R.id.btnAddPost);
        ivAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommuHomeActivity.this, PostWritingActivity.class);
                startActivity(intent);
            }
        });

        ivReFresh = findViewById(R.id.btnReFresh);
        ivReFresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagRefresh = 0;
                posting(postsList.size());
                Log.d(TAG, "Refresh Button is pushed. Then flag = " + flagRefresh);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flagRefresh = 1;
                        Log.d(TAG, "3초 지남. flag가 1로 다시 세팅. flag = " + flagRefresh);
                    }
                }, 3000);
            }
        });

    }

    public void posting(int size) {
        if (flagRefresh == 0) {
            mRecyclerView.scrollToPosition(size - 1);
            mAdapter.notifyItemChanged(size - 1);
        }
    }

    public void loadNextDataFromApi() {

    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(CommuHomeActivity.this, LoginActivity.class);
        intent.putExtra("sign", LoginActivity.RC_SIGN_OUT);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(logout);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.itemSignOut:
                signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewPostDetail(String date) {
        Intent intent = new Intent(CommuHomeActivity.this, PostViewActivity.class);
        intent.putExtra("date", date);
        startActivity(intent);
    }


}
