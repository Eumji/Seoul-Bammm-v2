package androidex.example.com.seoulbammmproj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Constellation extends AppCompatActivity {
    TextView tv_name, tv_view, tv_story;
    ImageView iv;
    Toolbar toolbar=null;
    final static String TAG = "constellationMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_name = findViewById(R.id.cs_name);
        tv_view = findViewById(R.id.cs_view);
        tv_story = findViewById(R.id.cs_story);
        iv = findViewById(R.id.cs_d_img);

        Intent intent = getIntent();
        int m=intent.getIntExtra("month", 0);
        int w=intent.getIntExtra("whi", 0);
        int img = intent.getIntExtra("img", 0);

        int _num = (m-1)*4+w;
        setView(_num);
        iv.setImageResource(img);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public boolean onOptionsItemSelected(MenuItem item) {//메뉴에서 선택 되었을 때
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:  //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
        }
        return false;
    }

    public void setView(int num) {
        InputStream is = getResources().openRawResource(R.raw.constellation_data);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);

        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.i(TAG, "sb : " + sb.toString());

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("constellation_info"));

            JSONObject jsonObject1 = (JSONObject) jsonArray.get(num-1);

            String _name = jsonObject1.getString("name");
            Log.i(TAG, "_name : " + _name);
            tv_name.setText(_name+"자리");

            String _view = jsonObject1.getString("view");
            Log.i(TAG, "_view : " + _name);
            tv_view.setText("보이는 시각, 방향: "+_view);

            String _story = jsonObject1.getString("story");
            Log.i(TAG, "_story : " + _name);
            tv_story.setText("설화: "+ _story);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


}
