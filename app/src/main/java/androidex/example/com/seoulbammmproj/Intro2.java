package androidex.example.com.seoulbammmproj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Intro2 extends AppCompatActivity {

    private final int REQ_WIDTH = 1080;
    private final int REQ_HEIGHT = 1080;
    int width, height;
    int postsize, randPostNum;
    String currentDate;
    ArrayList<Post> postsList;

    RelativeLayout ll = null;
    ImageView iv = null;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ll = findViewById(R.id.ll_intro2);
        iv = findViewById(R.id.iv_best);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(c.getTime());

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
                randPostNum = (int)(Math.random()*postsize);
                Log.d("randomPostNum", "onCreate: size = "+postsize);
                Log.d("randomPostNum", "onCreate: random = "+randPostNum);

                Picasso.get().load(postsList.get(randPostNum).getImage()).fit().centerInside().into(iv);

                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                //BitmapFactory.decodeResource(getResources(), R.drawable.night_view_ex,options);
                //BitmapFactory.decodeFile(postsList.get(randPostNum).getImage(),options);

                options.inSampleSize = setSimpleSize(options);

                options.inJustDecodeBounds = false;
                //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night_view_ex,options);
                Bitmap bitmap = BitmapFactory.decodeFile(postsList.get(randPostNum).getImage(),options);
                iv.setImageBitmap(bitmap);*/
            }
        }, 2000);



        // 어제의 서울 사진 사이즈 조절



        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        iv.getLayoutParams().height=width;
        iv.getLayoutParams().width=width;
        iv.requestLayout();*/
    }

    /*private int setSimpleSize(BitmapFactory.Options options){
        int originWidth = options.outWidth;
        int originHeight = options.outHeight;

        int size = 1;

        while (REQ_WIDTH<originWidth||REQ_HEIGHT<originHeight){
            originWidth = originWidth/2;
            originHeight = originHeight/2;

            size*=2;
        }
        return size;
    }*/
}