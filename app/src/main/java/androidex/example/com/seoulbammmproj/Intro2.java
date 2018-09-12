package androidex.example.com.seoulbammmproj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Intro2 extends AppCompatActivity {

    private final int REQ_WIDTH = 1080;
    private final int REQ_HEIGHT = 1080;
    int width, height;

    RelativeLayout ll = null;
    ImageView iv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ll = findViewById(R.id.ll_intro2);
        iv = findViewById(R.id.iv_best);

        // 어제의 서울 사진 사이즈 조절
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.night_view_ex,options);

        options.inSampleSize = setSimpleSize(options);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night_view_ex,options);
        iv.setImageBitmap(bitmap);


        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        iv.getLayoutParams().height=width;
        iv.getLayoutParams().width=width;
        iv.requestLayout();
    }

    private int setSimpleSize(BitmapFactory.Options options){
        int originWidth = options.outWidth;
        int originHeight = options.outHeight;

        int size = 1;

        while (REQ_WIDTH<originWidth||REQ_HEIGHT<originHeight){
            originWidth = originWidth/2;
            originHeight = originHeight/2;

            size*=2;
        }
        return size;
    }
}