package androidex.example.com.seoulbammmproj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConstellationMenu extends AppCompatActivity {
    Toolbar toolbar=null;
    TextView tv_month=null;
    ImageView cs01, cs02, cs03, cs04;
    int nowMonth=1;

    //현재 시간 가져오기
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf= new SimpleDateFormat("MM", Locale.KOREA);
    String str_date = sdf.format(new Date());
    int[][]imgs=new int[13][5];//이미지 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cs01=findViewById(R.id.cs_01);
        cs02=findViewById(R.id.cs_02);
        cs03=findViewById(R.id.cs_03);
        cs04=findViewById(R.id.cs_04);
//        cs05=findViewById(R.id.cs_05);
//        cs06=findViewById(R.id.cs_06);
        tv_month = findViewById(R.id.tv_month);

        new Thread(new Runnable(){
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    public void run(){
                        getArray();
                        setView(str_date);
                    }
                });
            }
        }).start();

        cs01.setOnClickListener(listener);
        cs02.setOnClickListener(listener);
        cs03.setOnClickListener(listener);
        cs04.setOnClickListener(listener);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void getArray(){//이미지 매열에 이미지 넣는 함수 i=월, j=번째
        for(int i=1;i<=12;i++) {
            for (int j = 1; j <= 4; j++) {
                imgs[i][j] = getApplicationContext().getResources().getIdentifier("c" + i + j, "drawable", "androidex.example.com.seoulbammmproj");
            }
        }
        return;
    }

    void setCs(int month){//해당 월의 별자리 사진 넣어주는 함수
        cs01.setImageResource(imgs[month][1]);
        cs02.setImageResource(imgs[month][2]);
        cs03.setImageResource(imgs[month][3]);
        cs04.setImageResource(imgs[month][4]);
    }


    View.OnClickListener listener = new View.OnClickListener(){//사진이 클릭 되었을 때
        @Override
        public void onClick(View v){
            int whi=1;
            switch (v.getId()){
                case R.id.cs_01:
                    whi=1;
                    break;
                case R.id.cs_02:
                    whi=2;
                    break;
                case R.id.cs_03:
                    whi=3;
                    break;
                case R.id.cs_04:
                    whi=4;
                    break;
            }

            Intent intent = new Intent(ConstellationMenu.this, Constellation.class);
            intent.putExtra("month", nowMonth);
            intent.putExtra("whi", whi);
            intent.putExtra("img", imgs[nowMonth][whi]);
            startActivity(intent);
        }
    };

    void setView(String str){
        switch(str){
            case "01":
                tv_month.setText("일월");
                setCs(1);
                nowMonth=1;
                break;
            case "02":
                tv_month.setText("이월");
                setCs(2);
                nowMonth=2;
                break;
            case "03":
                tv_month.setText("삼월");
                setCs(3);
                nowMonth=3;
                break;
            case "04":
                tv_month.setText("사월");
                setCs(4);
                nowMonth=4;
                break;
            case "05":
                tv_month.setText("오월");
                setCs(5);
                nowMonth=5;
                break;
            case "06":
                tv_month.setText("유월");
                setCs(6);
                nowMonth=6;
                break;
            case "07":
                tv_month.setText("칠월");
                setCs(7);
                nowMonth=7;
                break;
            case "08":
                tv_month.setText("팔월");
                setCs(8);
                nowMonth=8;
                break;
            case "09":
                tv_month.setText("구월");
                setCs(9);
                nowMonth=9;
                break;
            case "10":
                tv_month.setText("시월");
                setCs(10);
                nowMonth=10;
                break;
            case "11":
                tv_month.setText("십일월");
                setCs(11);
                nowMonth=11;
                break;
            case "12":
                tv_month.setText("십이월");
                setCs(12);
                nowMonth=12;
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.month, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//메뉴에서 선택 되었을 때
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:  //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.january:
                setView("01");
                return true;
            case R.id.february:
                setView("02");
                return true;
            case R.id.march:
                setView("03");
                return true;
            case R.id.april:
                setView("04");
                return true;
            case R.id.may:
                setView("05");
                return true;
            case R.id.june:
                setView("06");
                return true;
            case R.id.july:
                setView("07");
                return true;
            case R.id.august:
                setView("08");
                return true;
            case R.id.september:
                setView("09");
                return true;
            case R.id.october:
                setView("10");
                return true;
            case R.id.november:
                setView("11");
                return true;
            case R.id.december:
                setView("12");
                return true;
        }
        return false;
    }

}
