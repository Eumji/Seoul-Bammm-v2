package androidex.example.com.seoulbammmproj;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Intro2 extends AppCompatActivity {

    private final static int RANGE = 6;
    int postsize, randPostNum;
    String currentDate;
    ArrayList<Post> postsList;
    int maxLike=0;
    int maxLikeWhi=0;

    RelativeLayout ll = null;
    ImageView iv = null;

    FirebaseDatabase database;
    DatabaseReference myRef;

    int height, width;
    Display display;
    Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ll = findViewById(R.id.ll_intro2);
        iv = findViewById(R.id.iv_best);

        // 이미지뷰 크기 조절
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        iv.getLayoutParams().width = width;
        iv.getLayoutParams().height = width;
        iv.requestLayout();

        Calendar c = new GregorianCalendar();
        c.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(c.getTime());
        Log.d("randomPostNum", "currentDate : " +currentDate);


        postsList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts").child(currentDate);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Post post = dataSnapshot.getValue(Post.class);
                        postsList.add(post);
                        int sz=postsList.size();
                        for(int i=0;i<sz;i++){
                            int nowLike = Integer.parseInt(postsList.get(i).getLike());
                            if(nowLike>maxLike){
                                maxLike=nowLike;
                                maxLikeWhi=postsList.size()-1;
                                Log.d("randomPostNum", "onDataChange: max = "+maxLike+maxLikeWhi);
                            }
                        }

                        Log.d("randomPostNum", "onDataChange: count = "+postsList.size());
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
                });
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postsize = postsList.size();
                if(postsize == 0){
                    showRandomExImage();
                } else {
                    randPostNum = (int)(Math.random()*postsize);
                    Log.d("randomPostNum", "onCreate: size = "+postsize);
                    Log.d("randomPostNum", "onCreate: random = "+randPostNum);
                    Log.d("randomPostNum", "onCreate: maxWhi = "+maxLikeWhi);
                    Log.d("randomPostNum", "onCreate: maxLike = "+maxLike);
                    Picasso.get().load(postsList.get(maxLikeWhi).getImage()).fit().centerInside().into(iv);
                }
            }
        }, 2000);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showRandomExImage(){
        randPostNum = (int)(Math.random()*RANGE);
        switch (randPostNum){
            case 0:
                iv.setImageResource(R.drawable.night_view_ex); break;
            case 1:
                iv.setImageResource(R.drawable.building63); break;
            case 2:
                iv.setImageResource(R.drawable.gaeun); break;
            case 3:
                iv.setImageResource(R.drawable.noeul); break;
            case 4:
                iv.setImageResource(R.drawable.namsan); break;
            case 5:
                iv.setImageResource(R.drawable.naksan); break;
        }
    }

}