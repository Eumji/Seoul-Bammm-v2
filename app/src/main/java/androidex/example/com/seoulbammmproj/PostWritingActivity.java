package androidex.example.com.seoulbammmproj;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class PostWritingActivity extends AppCompatActivity {
    final String TAG = "WritePostPost";

    ImageView ivAddImage;
    EditText etLocation, etCamera;
    Button btnPostWriteCancel, btnPostWriteUpload;
    ProgressBar pbPostUpload;

    Uri downloadUri;
    Bitmap bitmap;
    String strEmail, strName, strUid, strImage, strLocation, strCamera, currentDate;
    FirebaseDatabase database;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_writing);

        ivAddImage = findViewById(R.id.ivAddImage);
        etLocation = findViewById(R.id.etLocation);
        etCamera = findViewById(R.id.etCamera);
        btnPostWriteCancel = findViewById(R.id.btnPostWriteCancel);
        btnPostWriteUpload = findViewById(R.id.btnPostWriteUpload);
        pbPostUpload = findViewById(R.id.pbPostUpload);

        ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        // 취소버튼 클릭 시 다이얼로그 창으로 다시 묻기
        btnPostWriteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder popupCancel = new AlertDialog.Builder(PostWritingActivity.this);
                popupCancel.setMessage("정말 취소하시겠어요?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("아뇨", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(PostWritingActivity.this, "버튼 조심해서 누르세요~~", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        // 완료버튼 클릭 시 다이얼로그 창으로 다시 묻기
        // 완료 확인(?)을 받은 후 파이어베이스에 업로드
        // 사진은 storage에 업로드 후 uri 받아오기, 나머지 텍스트들은 바로 사용
        // 저장 순서 ... storage에 image 업로드 및 uri 추출
        // 실시간 데이터베이스에 정보들 업로드
        btnPostWriteUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder popupUpload = new AlertDialog.Builder(PostWritingActivity.this);
                popupUpload.setTitle("게시글 업로드")
                        .setMessage("완료하시겠어요?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 제일 중요한 부분...!!!

                                // 이메일, 이름, UID
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    strEmail = user.getEmail();
                                    strName = user.getDisplayName();
                                    strUid = user.getUid();
                                }

                                // 업로드 시간
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd;HH;mm;ss");
                                currentDate = df.format(c.getTime());

                                // 장소와 카메라
                                strLocation = etLocation.getText().toString();
                                strCamera = etCamera.getText().toString();


                                Log.d(TAG, "strEmail :  " + strEmail);
                                Log.d(TAG, "strName :  " + strName);
                                Log.d(TAG, "strUid :  " + strUid);
                                Log.d(TAG, "currentDate :  " + currentDate);
                                Log.d(TAG, "strLocation :  " + strLocation);
                                Log.d(TAG, "strCamera :  " + strCamera);

                                if (TextUtils.isEmpty(strLocation)||TextUtils.isEmpty(strCamera)){
                                    Toast.makeText(PostWritingActivity.this, "빈칸을 모두 채워주세요~", Toast.LENGTH_SHORT).show();
                                } else {
                                    pbPostUpload.setVisibility(View.VISIBLE);
                                    mStorageRef = FirebaseStorage.getInstance().getReference();
                                    uploadImage();
                                }



                            }
                        })
                        .setNegativeButton("아뇨", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(PostWritingActivity.this, "버튼 조심해서 누르세요~~", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        // 갤러리 접근 권한 설정
        if (ContextCompat.checkSelfPermission(PostWritingActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PostWritingActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(PostWritingActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

    }

    public void uploadImage() {
        if (bitmap == null) {
            Toast.makeText(this, "사진이 필요해요~", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference mountainsRef = mStorageRef.child("posts").child(currentDate + "_" + strUid + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            Log.d(TAG, "storage 불러오기");

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle unsuccessful uploads
                    pbPostUpload.setVisibility(View.INVISIBLE);
                    Toast.makeText(PostWritingActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Uri를 받아오는 시간을 기다리기 위해서
                    final Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadUri = uriTask.getResult();
                            strImage = String.valueOf(downloadUri);
                            Log.d(TAG, "strImage : " + strImage);

                            // 데이터베이스 불러오기
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("posts").child(currentDate);

                            // 데이터 업로드
                            Hashtable<String, String> posts = new Hashtable<>();
                            posts.put("email", strEmail);
                            posts.put("name", strName);
                            posts.put("uid", strUid);
                            posts.put("like", "0");
                            posts.put("image", strImage);
                            posts.put("location", strLocation);
                            posts.put("camera", strCamera);
                            posts.put("date",currentDate);
                            posts.put("tag", "");
                            myRef.setValue(posts);
                            myRef.child("zzlikepeople").child("0").setValue("");

                            pbPostUpload.setVisibility(View.INVISIBLE);
                            Toast.makeText(PostWritingActivity.this, "업로드 완료! 새로고침 버튼을 눌러주세요~", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri image = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(PostWritingActivity.this.getContentResolver(), image);
            ivAddImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
