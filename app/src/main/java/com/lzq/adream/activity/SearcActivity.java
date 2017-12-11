package com.lzq.adream.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lzq.adream.R;
import com.lzq.adream.adapter.SearchRvAdapter;
import com.lzq.adream.model.bean.ResultBean;
import com.lzq.adream.widget.layout.FlowViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearcActivity extends Activity {
    private static final String TAG = "SearcActivity";
    @BindView(R.id.search_back)
    ImageView mSearchBack;
    @BindView(R.id.search_edittext)
    public EditText mSearchEdittext;
    @BindView(R.id.search_search)
    TextView mSearchSearch;
    @BindView(R.id.search_hot_fl)
    FlowViewGroup mSearchHotFl;
    @BindView(R.id.tv_clear)
    TextView mTvClear;
    @BindView(R.id.search_history)
    RecyclerView mSearchHistory;

    String[] arr = new String[15];
    int num = 0;
    @BindView(R.id.iv_voice)
    ImageView mIvVoice;

    private FlowViewGroup mFlowLayout;
    private List<String> mDatas = new ArrayList<>();
    private Random mRandom = new Random();
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 1;
    private Context mContext;
    private SearchRvAdapter mSearchRvAdapter;
    private String NAME = "history";
    Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mFlowLayout = (FlowViewGroup) findViewById(R.id.search_hot_fl);
        vioceSearch();
        setHot();
    }

    //语音搜索的点击事件
    private void vioceSearch() {
        mIvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkSelfResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);
                if (checkSelfResult != PackageManager.PERMISSION_GRANTED) {
                    //申请RECORD_AUDIO权限
                    ActivityCompat.requestPermissions((SearcActivity) mContext, new String[]{Manifest.permission.RECORD_AUDIO},
                            WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
                    return;
                }
                //android.permission.RECORD_AUDIO   android.permission.CHANGE_NETWORK_STATE

                RecognizerDialog mDialog = new RecognizerDialog(mContext, null);
                //2.设置accent、 language等参数
                mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
                //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
                //结果
                // mDialog.setParameter("asr_sch", "1");
                // mDialog.setParameter("nlp_version", "2.0");
                //3.设置回调接口
                mDialog.setListener(mRecognizerDialogListener);
                //4.显示dialog，接收语音输入
                mDialog.show();
            }
        });
    }

    //语音输入的监听及数据处理
    RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        /**
         *
         * @param recognizerResult
         * @param b 是否是结束 通常标点符号
         */
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if (b) {//过滤掉句号
                return;
            }
            ResultBean resultBean = mGson.fromJson(recognizerResult.getResultString(), ResultBean.class);
            List<ResultBean.WsBean> ws = resultBean.getWs();
            String w = "";
            for (int i = 0; i < ws.size(); i++) {
                ResultBean.WsBean wsBean = ws.get(i);
                List<ResultBean.WsBean.CwBean> cw = wsBean.getCw();
                for (int j = 0; j < cw.size(); j++) {
                    ResultBean.WsBean.CwBean cwBean = cw.get(j);
                    w += cwBean.getW();
                }
            }
            mSearchEdittext.setText(w);
            mSearchEdittext.setSelection(w.length());
        }

        @Override
        public void onError(SpeechError speechError) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        //读取历史记录
        List<String> list = new ArrayList<String>();
        String string = getString(this, NAME);

        if (string == "" || string == null) {
            mSearchRvAdapter = new SearchRvAdapter(mContext, null);
            mSearchHistory.setAdapter(mSearchRvAdapter);
            return;
        }
        arr = string.split("//");
        list = Arrays.asList(arr);
        //显示记录
        mSearchHistory.setLayoutManager(new LinearLayoutManager(mContext));
        mSearchRvAdapter = new SearchRvAdapter(mContext, list);
        mSearchHistory.setAdapter(mSearchRvAdapter);

    }

    //请求热门搜索数据
    private void getData() {

        /*RetrofitUtil.getHttpServiceInstance().requestSearchHot().enqueue(new Callback<HotBean>() {
            @Override
            public void onResponse(Call<HotBean> call, Response<HotBean> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        HotBean hotBean = response.body();
                        mDatas = hotBean.getSearchKeywords();
                        setHot();
                    } else {
                        mDatas.add("时尚女裙");
                        mDatas.add("韩版外套");
                        mDatas.add("韩版秋装");
                        mDatas.add("手机");
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<HotBean> call, Throwable t) {
            }
        });*/
        mDatas.add("时尚时装");
        mDatas.add("最新热报");
        mDatas.add("电子竞技");
        mDatas.add("手机");
    }

    //瀑布流显示热门搜索
    private void setHot() {

        mFlowLayout.setPadding(8, 8, 8, 8);

        for (int i = 0; i < mDatas.size(); i++) {
            TextView view = new TextView(this);
            view.setText(mDatas.get(i));
            //只能通过代码来实现圆角矩形
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(8);
            int a = 200 + mRandom.nextInt(55);
            int r = 50 + mRandom.nextInt(205);
            int g = 50 + mRandom.nextInt(205);
            int b = 50 + mRandom.nextInt(205);
            gradientDrawable.setColor(Color.argb(a, r, g, b));

            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setCornerRadius(8);
            gradientDrawable2.setColor(Color.GRAY);

            //代码实现选择器
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable2);  //按下状态
            stateListDrawable.addState(new int[]{}, gradientDrawable);
            view.setBackgroundDrawable(stateListDrawable);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hotData = (String) ((TextView) v).getText();
                    mSearchEdittext.setText(hotData);
                    mSearchEdittext.setSelection(hotData.length());
                }
            });

            view.setTextColor(Color.WHITE);
            view.setPadding(5, 5, 5, 5);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(14);
            mFlowLayout.addView(view);//触发重绘
        }
    }

    @OnClick({R.id.search_back, R.id.search_search, R.id.tv_clear})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.search_back:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.search_search:
                int number = 1;
                String s = mSearchEdittext.getText().toString().trim();
                if (TextUtils.isEmpty(s)) {
                    Toast.makeText(SearcActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(s);
                if (m.matches() && !TextUtils.isEmpty(s)) {
                    number = Integer.parseInt(s);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("cId", number);
                    intent.putExtra("position", 11);
                    intent.putExtra("id", "1");
                    setResult(111, intent);
                    finish();
                } else if (!TextUtils.isEmpty(s)) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("key", s);
                    intent.putExtra("position", 11);
                    intent.putExtra("id", "2");
                    setResult(111, intent);
                    finish();
                }
                /*else if (TextUtils.isEmpty(s)) {
                    return;
                }*/

                String info = mSearchEdittext.getText().toString().trim();
                if (!TextUtils.isEmpty(info)) {
                    info = info.replace("/", "1");
                    addHistory(info);
                }
                finish();
                break;
            case R.id.tv_clear:
                addHistory();
                break;
        }
    }

    //清空搜索记录
    private void addHistory() {
        setString(this, null, NAME);
        arr = null;
        mSearchRvAdapter = new SearchRvAdapter(mContext, null);
        mSearchHistory.setAdapter(mSearchRvAdapter);
    }

    //添加搜索记录
    private void addHistory(String info) {
        String[] number = new String[15];
        if (info == null || TextUtils.isEmpty(info)) {
            return;
        }
        if (num > 14) {
            num = 0;
        }
        boolean isRepeat = false;
        for (int i = 0; i < number.length; i++) {
            if (arr != null && arr.length != 0) {
                if (arr.length > i && arr[i] != null) {
                    number[i] = arr[i];

                    if (arr[i].equals(info)) {
                        isRepeat = true;
                    }
                }
            }
            if (number[i] == null) {
                num = i;
                break;
            }
        }
        if (!isRepeat) {
            number[num] = info;
        }
        String s1 = "";
        for (String s : number) {
            if (s != null) {
                s1 += (s + "//");
            }
        }
        setString(this, s1, NAME);


    }

    //储存历史搜索记录
    private static SharedPreferences getSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences("history",
                context.MODE_PRIVATE);
        return sp;
    }

    public static void setString(Context context, String value, String key) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = getSP(context);
        return sp.getString(key, null);
    }
}
