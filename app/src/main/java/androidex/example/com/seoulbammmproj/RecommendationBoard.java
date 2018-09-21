package androidex.example.com.seoulbammmproj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RecommendationBoard  extends AppCompatActivity {
    final static String TAG = "Thread";

    private PopIntent popIntent;

    String _location;
    String region;

    Toolbar toolbar;

    ImageView moon_image;
    ImageView img;
    ImageView share_btn;

    TextView moon_num;
    TextView name_kor, gu, region_detail, explanation;

    int click_num = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recommendation_board);

        // toolbar 설정
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        moon_num = findViewById(R.id.recommendation_num);
        moon_image = findViewById(R.id.moon_btn);

        img = findViewById(R.id.img);
        name_kor = findViewById(R.id.name_kor);
        gu = findViewById(R.id.gu);
        region_detail = findViewById(R.id.region_detail);
        explanation = findViewById(R.id.explanation);
        share_btn = findViewById(R.id.share_btn);

//        Uri uri = getIntent().getData();
//
//        if(uri != null){
//            String msg = uri.getQueryParameter("msg");
//            Log.d("recommend_param", "?????");
//            if(msg.equals("goMain")){
//                Log.d("recommend_param", "1");
//                Intent intent_commu_menu = new Intent(RecommendationBoard.this,LoginActivity.class);
//                startActivity(intent_commu_menu);
//            }
//            else {
//                Log.d("recommend_param", "2");
//                InputStream is = getResources().openRawResource(R.raw.region_data);
//                InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader reader = new BufferedReader(isr);
//
//                StringBuffer sb = new StringBuffer();
//                String line = null;
//                try {
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    Log.i(TAG, "sb : " + sb.toString());
//
//                    JSONObject jsonObject = new JSONObject(sb.toString());
//                    JSONArray jsonArray = new JSONArray(jsonObject.getString("region_info"));
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//                        String _name = jsonObject1.getString("name");
//                        Log.i(TAG, "_name : " + _name);
//
//                        final String _name_kor = jsonObject1.getString("name_kor");
//                        Log.i(TAG, "_name_kor : " + _name_kor);
//
//                        if (!msg.equals(_name_kor)) {
//                            continue;
//                        } else {
//                            _location = jsonObject1.getString("location");
//                            Log.i(TAG, "_location : " + _location);
//
//                            final String _gu = jsonObject1.getString("gu");
//                            Log.i(TAG, "_gu : " + _gu);
//
//                            final String _img = "@drawable/" + jsonObject1.getString("img");
//
//                            final int redID = getResources().getIdentifier(_img, "drawble", getPackageName());
//                            Log.i(TAG, "_img : " + _img);
//                            //
//
//                            JSONObject jsonObject2 = jsonObject1.getJSONObject("detail");
//                            final String _region_detail = jsonObject2.getString("region_detail");
//                            Log.i(TAG, "_region_detail : " + _region_detail);
//
//                            final String _explanation = jsonObject2.getString("explanation");
//                            Log.i(TAG, "_explanation : " + _explanation);
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    img.setImageResource(redID);
//                                    name_kor.setText(_name_kor);
//                                    gu.setText(_gu);
//                                    region_detail.setText(_region_detail);
//                                    explanation.setText(_explanation);
//                                }
//                            });
//                            break;
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (reader != null)
//                            reader.close();
//                        if (isr != null)
//                            isr.close();
//                        if (is != null)
//                            is.close();
//                    } catch (Exception e2) {
//                        e2.printStackTrace();
//                    }
//                }
//            }
//        }

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedTemplate params = FeedTemplate
                        .newBuilder(ContentObject.newBuilder(name_kor.getText().toString(),
                                "https://postfiles.pstatic.net/MjAxODA5MjBfMjcx/MDAxNTM3NDI5ODk1OTIw.CBfFHdRT8tBqiqoU6SHBpMeexQvVQJd3FiJwV8qNhSIg.MFR0ZFvHglk-0u66lByBP3ZIOWa-Q1wCZMAAVQQk7QAg.PNG.ros008/1537429880238.png?type=w966",
                                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                        .setMobileWebUrl("https://developers.kakao.com").build())
                                .setDescrption(explanation.getText().toString())
                                .build())
                        .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                                .setWebUrl("'https://developers.kakao.com")
                                .setMobileWebUrl("'https://developers.kakao.com")
                                .setAndroidExecutionParams("msg="+"1"+region)
                                .setIosExecutionParams("key1=value1")
                                .build()))
                        .build();

                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                serverCallbackArgs.put("user_id", "${current_user_id}");
                serverCallbackArgs.put("product_id", "${shared_product_id}");

                KakaoLinkService.getInstance().sendDefault(RecommendationBoard.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
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

        moon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_num == 0){
                    String num = (String) moon_num.getText();
                    int num_1 = Integer.parseInt(num);
                    num_1++;
                    num = Integer.toString(num_1);
                    moon_num.setText(num);
                    click_num = 1;
                    moon_image.setImageResource(R.drawable.moon_full);
                }
                else{
                    String num = (String) moon_num.getText();
                    int num_1 = Integer.parseInt(num);
                    num_1--;
                    num = Integer.toString(num_1);
                    moon_num.setText(num);
                    click_num = 0;
                    moon_image.setImageResource(R.drawable.moon_empty);
                }
            }
        });

        popIntent = new PopIntent();
        popIntent.start();
    }


    public class PopIntent extends Thread {
        @Override
        public void run() {
            Log.d("recommend_param", "here");
            Intent intent = getIntent();
            region = intent.getStringExtra("region");
            Log.d("recommend_param", region);
            InputStream is = getResources().openRawResource(R.raw.region_data);
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
                JSONArray jsonArray = new JSONArray(jsonObject.getString("region_info"));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    String _name = jsonObject1.getString("name");
                    Log.i(TAG, "_name : " + _name);

                    if (!region.equals(_name)) {
                        continue;
                    } else {
                        final String _name_kor = jsonObject1.getString("name_kor");
                        Log.i(TAG, "_name_kor : " + _name_kor);

                        _location = jsonObject1.getString("location");
                        Log.i(TAG, "_location : " + _location);

                        final String _gu = jsonObject1.getString("gu");
                        Log.i(TAG, "_gu : " + _gu);

                        final String _img = "@drawable/"+jsonObject1.getString("img");

                        final int redID = getResources().getIdentifier(_img, "drawble", getPackageName());
                        Log.i(TAG, "_img : " + _img);
                        //

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("detail");
                        final String _region_detail = jsonObject2.getString("region_detail");
                        Log.i(TAG, "_region_detail : " + _region_detail);

                        final String _explanation = jsonObject2.getString("explanation");
                        Log.i(TAG, "_explanation : " + _explanation);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageResource(redID);
                                name_kor.setText(_name_kor);
                                gu.setText(_gu);
                                region_detail.setText(_region_detail);
                                explanation.setText(_explanation);
                            }
                        });
                        break;
                    }
                }

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
