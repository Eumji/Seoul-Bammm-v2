package androidex.example.com.seoulbammmproj;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    String currentDate;
    String viewFlag;

    ImageView ivAddPost, ivReFresh;
    TextView tvCommuTitle;
    Toolbar toolbar = null;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Display display;
    Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_commu_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ivAddPost = findViewById(R.id.btnAddPost);
        ivReFresh = findViewById(R.id.btnReFresh);
        tvCommuTitle = findViewById(R.id.tvCommuTitle);

        viewFlag = getIntent().getStringExtra("commu");
        if(viewFlag.equals("today")){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = df.format(c.getTime());

            //글 추가와 새로고침은 오늘만 가능
            ivAddPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CommuHomeActivity.this, PostWritingActivity.class);
                    startActivity(intent);
                }
            });

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
        } else if (viewFlag.equals("yesterday")){
            tvCommuTitle.setText("어제의 서울");
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = df.format(c.getTime());

            // 버튼 안보이게
            ivAddPost.setImageResource(R.drawable.nothingbtn);
            ivReFresh.setImageResource(R.drawable.nothingbtn);

            toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        postsList = new ArrayList<>();
        mAdapter = new PostListAdapter(postsList, width, height);
        mRecyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts").child(currentDate);

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
                int sz=postsList.size();
                for (int i = 0; i < sz; i++) {
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


        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    } //onCreate()

    public void posting(int size) {
        if (flagRefresh == 0) {
            mRecyclerView.scrollToPosition(size - 1);
            mAdapter.notifyItemChanged(size - 1);
        }
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
        if(viewFlag.equals("today")){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.logout, menu);
            return true;
        }
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
            case R.id.itemYesterday:
                viewYesterdayFeed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewPostDetail(String date) {
        Intent intent = new Intent(CommuHomeActivity.this, PostViewActivity.class);
        String day = date.split(";;")[0];
        String time = date.split(";;")[1];
        intent.putExtra("day", day);
        intent.putExtra("time",time);
        startActivity(intent);
    }

    // 어제의 서울 피드 열기
    private void viewYesterdayFeed(){
        if (viewFlag.equals("today")){
            Intent intent = new Intent(CommuHomeActivity.this,CommuHomeActivity.class);
            intent.putExtra("commu","yesterday");
            startActivity(intent);
        }

    }


}
