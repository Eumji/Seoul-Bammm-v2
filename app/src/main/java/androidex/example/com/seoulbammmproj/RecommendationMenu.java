package androidex.example.com.seoulbammmproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class RecommendationMenu  extends AppCompatActivity {
    ImageButton noeul, sunyu, seoullo, building63, bukak, kyeongbok, naksan, dungdung, banpo, ungbong, sukchon, namsan, olympic;
    Intent i;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recommendation_menu);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        noeul = (ImageButton) findViewById(R.id.noeul);
        sunyu = (ImageButton) findViewById(R.id.sunyu);
        seoullo = (ImageButton) findViewById(R.id.seoullo);
        building63 = (ImageButton) findViewById(R.id.building63);
        bukak = (ImageButton) findViewById(R.id.bukak);
        kyeongbok = (ImageButton) findViewById(R.id.kyeongbok);
        naksan = (ImageButton) findViewById(R.id.naksan);
        dungdung = (ImageButton) findViewById(R.id.dungdung);
        banpo = (ImageButton) findViewById(R.id.banpo);
        ungbong = (ImageButton) findViewById(R.id.ungbong);
        sukchon = (ImageButton) findViewById(R.id.sukchon);
        namsan = (ImageButton) findViewById(R.id.namsan);
        olympic = (ImageButton) findViewById(R.id.olympic);



        noeul.setOnClickListener(listener);
        sunyu.setOnClickListener(listener);
        seoullo.setOnClickListener(listener);
        building63.setOnClickListener(listener);
        bukak.setOnClickListener(listener);
        kyeongbok.setOnClickListener(listener);
        naksan.setOnClickListener(listener);
        dungdung.setOnClickListener(listener);
        banpo.setOnClickListener(listener);
        ungbong.setOnClickListener(listener);
        sukchon.setOnClickListener(listener);
        namsan.setOnClickListener(listener);
        olympic.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.noeul:
                    //Toast.makeText(MainActivity.this, "1",Toast.LENGTH_SHORT).show();
                    intent("noeul");
                    break;

                case R.id.sunyu:
                    //Toast.makeText(MainActivity.this, "2",Toast.LENGTH_SHORT).show();
                    intent("sunyu");
                    break;

                case R.id.seoullo:
                    //Toast.makeText(MainActivity.this, "3",Toast.LENGTH_SHORT).show();
                    intent("seoullo");
                    break;

                case R.id.building63:
                    //Toast.makeText(MainActivity.this, "4",Toast.LENGTH_SHORT).show();
                    intent("building63");
                    break;
                case R.id.bukak:
                    //Toast.makeText(MainActivity.this, "5",Toast.LENGTH_SHORT).show();
                    intent("bukak");
                    break;
                case R.id.kyeongbok:
                    //Toast.makeText(MainActivity.this, "6",Toast.LENGTH_SHORT).show();
                    intent("kyeongbok");
                    break;
                case R.id.naksan:
                    //Toast.makeText(MainActivity.this, "7",Toast.LENGTH_SHORT).show();
                    intent("naksan");
                    break;
                case R.id.dungdung:
                    //Toast.makeText(MainActivity.this, "8",Toast.LENGTH_SHORT).show();
                    intent("dungdung");
                    break;
                case R.id.banpo:
                    //Toast.makeText(MainActivity.this, "9",Toast.LENGTH_SHORT).show();
                    intent("banpo");
                    break;
                case R.id.ungbong:
                    //Toast.makeText(MainActivity.this, "10",Toast.LENGTH_SHORT).show();
                    intent("ungbong");
                    break;
                case R.id.sukchon:
                    //Toast.makeText(MainActivity.this, "11",Toast.LENGTH_SHORT).show();
                    intent("sukchon");
                    break;
                case R.id.namsan:
                    //Toast.makeText(MainActivity.this, "12",Toast.LENGTH_SHORT).show();
                    intent("namsan");
                    break;
                case R.id.olympic:
                    //Toast.makeText(MainActivity.this, "c13",Toast.LENGTH_SHORT).show();
                    intent("olympic");
                    break;
            }
        }
    };

    public void intent(String str){
        i = new Intent(this, RecommendationBoard.class);
        i.putExtra("region", str);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

