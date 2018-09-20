package androidex.example.com.seoulbammmproj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Intro1 extends AppCompatActivity {

    // 시작 화면 띄우는 activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startIntro();
    }

    private void startIntro(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent_intro2 = new Intent(Intro1.this, Intro2.class);
                startActivity(intent_intro2);
                finish();
            }
        },2000);

    }
}
