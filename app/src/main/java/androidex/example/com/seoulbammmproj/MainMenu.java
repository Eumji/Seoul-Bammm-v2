package androidex.example.com.seoulbammmproj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button btn_menu1, btn_menu2, btn_menu3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        Uri uri = getIntent().getData();

        if(uri != null) {
            String msg = uri.getQueryParameter("msg");
            if(msg!=null) {
                Log.d("share", msg);
                if (msg.charAt(0) == '1') {
                    msg = msg.substring(1);
                    Intent i = new Intent(this, RecommendationBoard.class);
                    i.putExtra("region", msg);
                    startActivity(i);
                    finish();
                } else if (msg.charAt(0) == '2') {
                    msg = msg.substring(1);
                    Intent intent = new Intent(MainMenu.this, LoginActivity.class);
                    intent.putExtra("date", msg);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                Intent intent_intro1 = new Intent(MainMenu.this, Intro1.class);
                startActivity(intent_intro1);
            }
        }

        else{
            Intent intent_intro1 = new Intent(MainMenu.this, Intro1.class);
            startActivity(intent_intro1);
        }


        btn_menu1 = findViewById(R.id.btn_main_menu01);
        btn_menu2 = findViewById(R.id.btn_main_menu02);
        btn_menu3 = findViewById(R.id.btn_main_menu03);
        btn_menu1.setOnClickListener(listener_mainMenu);
        btn_menu2.setOnClickListener(listener_mainMenu);
        btn_menu3.setOnClickListener(listener_mainMenu);

//        getAppKeyHash();
    }

    View.OnClickListener listener_mainMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_main_menu01:
                    Intent intent_constellaion_menu = new Intent(MainMenu.this, ConstellationMenu.class);
                    startActivity(intent_constellaion_menu);
                    break;
                case R.id.btn_main_menu02:
                    Intent intent_recommendation_menu = new Intent(MainMenu.this, RecommendationMenu.class);
                    startActivity(intent_recommendation_menu);
                    break;
                case R.id.btn_main_menu03:
                    Intent intent_commu_menu = new Intent(MainMenu.this,LoginActivity.class);
                    startActivity(intent_commu_menu);
                    break;
                default:break;
            }
        }
    };
//    해쉬값 찾는 법
//    private void getAppKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                Log.d("Hash key", something);
//            }
//        } catch (Exception e) {
//// TODO Auto-generated catch block
//            Log.e("name not found", e.toString());
//        }
//    }
}

