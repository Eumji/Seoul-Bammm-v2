package androidex.example.com.seoulbammmproj;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostViewActivity extends AppCompatActivity {
    final static String TAG = "PostViewActivityyy";

    String postDate, postTime;
    String strUserId, strLikeNum, currentUid;
    ArrayList<String> postdetail, likePeople;
    Post post = new Post();

    Toolbar toolbar;

    ImageView ivMoon;
    ImageView img;
    ImageView share_btn;

    TextView tvLikeNum, tvUserId;
    TextView tvGu, tvCamera, tvLocation;

    int click_num = 0;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recommendation_board);
        LinearLayout ll;
        ll=findViewById(R.id.rb_ll);
        ll.setBackgroundResource(R.drawable.background01);

        // toolbar 설정
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvLikeNum = findViewById(R.id.recommendation_num);
        tvUserId = findViewById(R.id.userName);
        ivMoon = findViewById(R.id.moon_btn);

        img = findViewById(R.id.img);
        tvGu = findViewById(R.id.gu);
        tvCamera = findViewById(R.id.filter);
        tvLocation = findViewById(R.id.explanation);
        share_btn = findViewById(R.id.share_btn);

        postDate = getIntent().getStringExtra("day");
        postTime = getIntent().getStringExtra("time");
        Log.d(TAG, "onCreate: " + postDate);
        postdetail = new ArrayList<>();
        likePeople = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUid = user.getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts").child(postDate).child(postTime);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.d(TAG, "size : "+postdetail.size());
                //Log.d(TAG, "onChildAdded.toString(): "+dataSnapshot.getValue().toString());
                if (postdetail.size() < 9) {
                    postdetail.add(dataSnapshot.getValue().toString());
                } else if (postdetail.size() == 9) {
                    viewPost();
                }
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


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                카카오 링크로 사진 공유하기
                FeedTemplate params = FeedTemplate
                        .newBuilder(ContentObject.newBuilder(tvLocation.getText().toString(),
                                postdetail.get(3),
                                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                        .setMobileWebUrl("https://developers.kakao.com").build())
                                .setDescrption(tvCamera.getText().toString())
                                .build())
                        .setSocial(SocialObject.newBuilder().setLikeCount(Integer.parseInt((String) tvLikeNum.getText())).build())
                        .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                                .setWebUrl("'https://developers.kakao.com")
                                .setMobileWebUrl("'https://developers.kakao.com")
                                .setAndroidExecutionParams("msg=" + "2"+postDate+";;"+postTime)
                                .setIosExecutionParams("key1=value1")
                                .build()))
                        .build();

                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                serverCallbackArgs.put("user_id", "${current_user_id}");
                serverCallbackArgs.put("product_id", "${shared_product_id}");


                KakaoLinkService.getInstance().sendDefault(PostViewActivity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                    }
                });
            }
        });


        myRef.child("zzlikepeople").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!dataSnapshot.getValue().toString().equals("")) {
                    likePeople.add(dataSnapshot.getValue().toString());
                    Log.d(TAG, "likePeople : " + likePeople.toString());
                    Log.d(TAG, "currentUid : " + currentUid);
                    if (dataSnapshot.getValue().toString().equals(currentUid)) {
                        click_num = 1;
                        ivMoon.setImageResource(R.drawable.moon_full);
                        Log.d(TAG, "onChildAdded: already clicked");
                    }
                }
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


        ivMoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click_num == 0) {
                    strLikeNum = (String) tvLikeNum.getText();
                    int num_1 = Integer.parseInt(strLikeNum);
                    num_1++;
                    strLikeNum = Integer.toString(num_1);
                    tvLikeNum.setText(strLikeNum);
                    click_num = 1;
                    likePeople.add(currentUid);
                    ivMoon.setImageResource(R.drawable.moon_full);
                } else {
                    strLikeNum = (String) tvLikeNum.getText();
                    int num_1 = Integer.parseInt(strLikeNum);
                    num_1--;
                    strLikeNum = Integer.toString(num_1);
                    tvLikeNum.setText(strLikeNum);
                    click_num = 0;
                    likePeople.remove(currentUid);
                    ivMoon.setImageResource(R.drawable.moon_empty);
                }
                myRef.child("like").setValue(strLikeNum);
                if (likePeople.isEmpty()) {
                    myRef.child("zzlikepeople").setValue("");
                    myRef.child("zzlikepeople").child("0").setValue("");
                } else
                    myRef.child("zzlikepeople").setValue(likePeople);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(logout);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.deletePost:
                deletePost();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void viewPost() {
        post.setCamera(postdetail.get(0));
        post.setDate(postdetail.get(1));
        post.setEmail(postdetail.get(2));
        post.setImage(postdetail.get(3));
        post.setLike(postdetail.get(4));
        post.setLocation(postdetail.get(5));
        post.setName(postdetail.get(7));
        post.setUid(postdetail.get(8));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(post.getImage()).fit().centerInside().into(img, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        // Index 0 is the image view.
                        Log.d(TAG, "Success");
                    }
                });
                tvCamera.setText(post.getCamera());
                tvLocation.setText(post.getLocation());
                tvLikeNum.setText(post.getLike());
                tvUserId.setText(getUserId(post.getEmail()));
            }
        });
    }

    private void deletePost() {
        AlertDialog.Builder popupCancel = new AlertDialog.Builder(PostViewActivity.this);
        popupCancel.setMessage("정말 삭제하시겠어요?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (post.getUid().equals(currentUid)) {
                            mStorageRef = FirebaseStorage.getInstance().getReference().child("posts").child(post.getDate().split(";;")[0]).child(post.getDate().split(";;")[1] + "_" + post.getUid() + ".jpg");;
                            mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    myRef.removeValue();
                                    Toast.makeText(PostViewActivity.this, "삭제 완료~!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostViewActivity.this, "삭제 실패ㅠㅠ", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(PostViewActivity.this, "직접 쓴 게시글만 삭제할 수 있어요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("아뇨", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }


    private String getUserId(String email) {
        strUserId = email.split("@")[0];
        return strUserId;
    }


    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        } else {
            Toast.makeText(this, "External Storage Permission is Grant", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "External Storage Permission is Grant ");
            return true;
        }

    }
}