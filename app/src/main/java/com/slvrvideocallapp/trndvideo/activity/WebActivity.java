package com.slvrvideocallapp.trndvideo.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;
import com.slvrvideocallapp.trndvideo.Anylitecs;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.databinding.ActivityWebBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.HitAdsRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebActivity extends AppCompatActivity {


    private static final String TAG = "webact";
    private static final String WEBSITE = "WEBSITE";
    WebView wv1;
    MaterialProgressBar progressBar;
    boolean loadingFinished = true;
    boolean redirect = false;
    ImageView imgBack;
    ImageView imgRefresh;
    ActivityWebBinding binding;
    private String url;
    private String adid;

    private String ownAdRewardUrl = "";
    private String ownAdBannerUrl = "";
    private String ownAdInstarUrl = "";

    private String ownWebUrl = "";

    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private boolean showAds = false;
    private SessionManager sessionManager;
    private boolean ownLoded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        MainActivity.setStatusBarGradiant(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        sessionManager = new SessionManager(this);


        progressBar = findViewById(R.id.progressbar);
        TextView textView = findViewById(R.id.tvwebsite);


        progressBar.setIndeterminate(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
        }

        imgBack = findViewById(R.id.imgBack);
        imgRefresh = findViewById(R.id.imgrefresh);
        wv1 = findViewById(R.id.webview);
        wv1.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        url = intent.getStringExtra(WEBSITE);

        String title = intent.getStringExtra("title");
        if (title != null && !title.equals("")) {
            textView.setText(title);
        } else {
            String website = getbaseWebsite(url);
            textView.setText(website);
        }


        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String type = intent.getStringExtra("type");
        if (type != null && !type.equals("") && type.equals("ads")) {
            String adid1 = intent.getStringExtra("ADID");
            if (adid1 != null && !adid1.equals("")) {
                sendHit(adid1);
            }
        }
        loadUrl();
    }

    private void sendHit(String adid) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ad_id", adid);
        jsonObject.addProperty("country", new SessionManager(this).getStringValue(Const.Country));
        Call<HitAdsRoot> call = RetrofitBuilder.create(this).sendImpression(jsonObject);
        call.enqueue(new Callback<HitAdsRoot>() {
            @Override
            public void onResponse(Call<HitAdsRoot> call, Response<HitAdsRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200) {
                    Log.d(TAG, "onResponse: impressonsended");
                }
            }

            @Override
            public void onFailure(Call<HitAdsRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: error 421" + t.toString());
            }
        });


    }

    private void loadUrl() {
        if (url != null) {
            wv1.loadUrl(url);

            wv1.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                    if (!loadingFinished) {
                        redirect = true;
                    }

                    loadingFinished = false;
                    view.loadUrl(urlNewString);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    loadingFinished = false;
                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!redirect) {
                        loadingFinished = true;
                        progressBar.setVisibility(View.GONE);
                    }

                    if (loadingFinished && !redirect) {
                        //HIDE LOADING IT HAS FINISHED
                        progressBar.setVisibility(View.GONE);
                    } else {
                        redirect = false;
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Anylitecs.addUser(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Anylitecs.removeSesson(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Anylitecs.removeSesson(this);
        Log.d(TAG, "onPause: ");
    }

    private String getbaseWebsite(String url) {
        try {
            String[] s = url.split("//");
            String[] s1 = s[1].split("/");


            return s1[0];
        } catch (Exception o) {
            return "";
        }
    }

    public void onBackClick(View view) {

            finish();
    }

    public void onClickRefresh(View view) {
        loadUrl();
    }

    @Override
    public void onBackPressed() {

            finish();
    }





}