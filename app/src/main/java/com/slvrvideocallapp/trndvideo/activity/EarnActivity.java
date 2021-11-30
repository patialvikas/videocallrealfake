package com.slvrvideocallapp.trndvideo.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.JsonObject;
import com.slvrvideocallapp.trndvideo.Anylitecs;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.databinding.ActivityEarnBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.HitAdsRoot;
import com.slvrvideocallapp.trndvideo.models.OwnAdsRoot;
import com.slvrvideocallapp.trndvideo.models.ProfileRoot;
import com.slvrvideocallapp.trndvideo.models.UserRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitService;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.interstitialAd;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.mInterstitialAdr;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.requestNewInterstitial;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_APP_ID_AD_UNIT_ID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNER;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNERMAINR;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;

public class EarnActivity extends AppCompatActivity {
    private static final String TAG = "adsactivity";
    private static final String WEBSITE = "WEBSITE";

    ActivityEarnBinding binding;
    RetrofitService service;
    boolean ownLoded = false;
    VideoView videoView;
    private String adid;
    private SessionManager sessionManager;
    private String ownAdRewardUrl = "";
    private String ownAdBannerUrl = "";
    private String ownAdInstarUrl = "";
    private String userId;
    private String ownWebUrl = "";
    private AlertDialog aleart;
    private boolean fetched;
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private MediaPlayer mp;
    private boolean isreawrdfbreward = false;
    private LinearLayout adContainer;
//    private GGInterstitialAd mAd;

    //new
    public static Context context;

    //new
    private Toolbar toolbar;

    //Prefrance
    private static PrefManager prf;

    //rajanads fenil
    // ad will be shown after each x url loadings or clicks on navigation drawer menu
//    public static final int ADMOB_INTERSTITIAL_FREQUENCY = 3;
//    public static final int TAG_WHATSAPP_SHARE_FREQUENCY =3;


    //rajanads
    // ad will be shown after each x url loadings or clicks on navigation drawer menu
    public static final int ADMOB_INTERSTITIAL_FREQUENCY = 3;
    private static int sInterstitialCounter = 1;

    public static final int WHATSAPP_SHARE_FREQUENCY = 3;
    public static int sWhatsappCounter = 1;
    private static final String TAG_WHATSAPP_COUNTER = "whatsapp_counter";

    // ads
    private AdView mAdView, mAdViewnew;
    private static AdRequest adRequest;

    //facebookads
    private LinearLayout bannerAdContainer;
    private com.facebook.ads.AdView bannerAdView, bannerAdViewnew;

    //facebookads
//    private static final String TAG = DisplayUserActivity.class.getSimpleName();

    //Native
    //admob
    private AdLoader adLoader;
    private UnifiedNativeAd nativeAd;
    private FrameLayout frameLayout;

    //fb
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private NativeAd nativeAdfb;
    
    
    
    public static void sendImpression(Context context, String adid) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ad_id", adid);
        jsonObject.addProperty("country", new SessionManager(context).getStringValue(Const.Country));
        Call<HitAdsRoot> call = RetrofitBuilder.create(context).sendImpression(jsonObject);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        MainActivity.setStatusBarGradiant(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_earn);

        service = RetrofitBuilder.create(this);
        sessionManager = new SessionManager(this);
        chkConnection2();
        initMain();

        binding.btnClose.setBackgroundResource(R.drawable.dbg_whitebtnroundddd);
        binding.btnVolume.setBackgroundResource(R.drawable.dbg_whitebtnroundddd);

        AudienceNetworkAds.initialize(this);

        context = getApplicationContext();

        prf = new PrefManager(context);

        if(prf.getString(TAG_APP_ID_AD_UNIT_ID) != "")
            MobileAds.initialize(this, prf.getString(TAG_APP_ID_AD_UNIT_ID));

        bannerAdContainer = (LinearLayout) findViewById(R.id.banner_container);
        if(prf.getString(TAG_BANNER).equalsIgnoreCase("admob") || prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            adRequest = new AdRequest.Builder().build();
        }

        if (prf.getString(TAG_BANNER).equalsIgnoreCase("admob")) {
//        mAdView = (AdView) findViewById(R.id.adView_view);
            mAdView = new AdView(this);

            mAdView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
//            mAdView.setAdSize(adSize);

            mAdView.setAdUnitId(prf.getString(TAG_BANNERMAINR));
            if (prf.getString("SUBSCRIBED").equals("FALSE")) {
                mAdView.loadAd(adRequest);
            }
            ((LinearLayout) bannerAdContainer).removeAllViews();
            ((LinearLayout) bannerAdContainer).addView(mAdView);
        }

        
        
        

       
    }

    private void chkConnection2() {
        aleart = new androidx.appcompat.app.AlertDialog.Builder(EarnActivity.this).
                setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Internet Connection Alert")
                .setMessage("Please Turn on Internet Connection")
                .setPositiveButton("Close", (dialog, which) -> finishAffinity()).create();
        aleart.setCancelable(false);
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        if (isConnected()) {
                            aleart.dismiss();
                            if (!fetched) {
                                initMain();
                            }
                        } else {
                            if (!aleart.isShowing()) {
                                fetched = false;
                                aleart.show();
                            }

                        }

                    } catch (Exception e) {
                        //mm
                    }
                });
            }

            private boolean isConnected() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }

        };
        timer.schedule(doAsynchronousTask, 0, 1000);
    }

    private void initMain() {


//        getOwnAds();
        fetched = true;
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            userId = sessionManager.getUser().get_id();
            getUser();
        }


    }

    private void getUser() {
        Call<ProfileRoot> call = RetrofitBuilder.create(this).getUser(sessionManager.getUser().get_id());
        call.enqueue(new Callback<ProfileRoot>() {
            @Override
            public void onResponse(Call<ProfileRoot> call, Response<ProfileRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200 && response.body().getData() != null) {
                    binding.tvcoin.setText(String.valueOf(response.body().getData().getCoin()));
                }
            }

            @Override
            public void onFailure(Call<ProfileRoot> call, Throwable t) {
//ll
            }
        });
    }

    public void addMoney(String coin) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("coin", coin);
        Call<UserRoot> call = RetrofitBuilder.create(this).addCoin(jsonObject);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200 && response.body().getData() != null) {
                    binding.tvcoin.setText(String.valueOf(response.body().getData().getCoin()));
                    sessionManager.saveUser(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
//ll
            }
        });
    }

    private void getOwnAds() {
        RetrofitBuilder.create(this).getOwnAds().enqueue(new Callback<OwnAdsRoot>() {
            @Override
            public void onResponse(Call<OwnAdsRoot> call, Response<OwnAdsRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200 && !response.body().getData().isEmpty()) {
                    ownLoded = true;
                    ownAdRewardUrl = response.body().getData().get(0).getReward();
                    ownAdBannerUrl = response.body().getData().get(0).getBanner();
                    ownAdInstarUrl = response.body().getData().get(0).getInterstitial();
                    ownWebUrl = response.body().getData().get(0).getWebsite();
                    adid = response.body().getData().get(0).get_id();
                    Log.d(TAG, "onResponse:b " + ownAdBannerUrl);
                    Log.d(TAG, "onResponse:i " + ownAdInstarUrl);
                    Log.d(TAG, "onResponse:r " + ownAdRewardUrl);

                    Glide
                            .with(EarnActivity.this)
                            .load(new SessionManager(EarnActivity.this).getStringValue(Const.IMAGE_URL) + ownAdBannerUrl)
                            .into(binding.imgOwnAd);
                    Glide
                            .with(EarnActivity.this)
                            .load(new SessionManager(EarnActivity.this).getStringValue(Const.IMAGE_URL) + ownAdInstarUrl)
                            .into(binding.imgOwnInter);
                }
            }

            @Override
            public void onFailure(Call<OwnAdsRoot> call, Throwable t) {
///mm
            }
        });
    }


    private void setOwnAds() {
        sendImpression(this, adid);
        binding.lytOwnAds.setVisibility(View.VISIBLE);

        videoView = binding.videoView;
        try {
            videoView.setVideoURI(Uri.parse(new SessionManager(EarnActivity.this).getStringValue(Const.IMAGE_URL) + ownAdRewardUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            boolean isMute = false;


            @Override
            public void onPrepared(MediaPlayer m) {
                mp = m;

                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        binding.tvTime.setText(String.valueOf((int) (millisUntilFinished / 1000)));
                        binding.btnVolume.setOnClickListener(v -> toggleVolume());
                    }

                    @Override
                    public void onFinish() {
                        binding.tvTime.setVisibility(View.GONE);
                        binding.btnClose.setVisibility(View.VISIBLE);
                        binding.btnClose.setOnClickListener(v -> closeOwnAds());
                        addMoney(sessionManager.getStringValue(Const.REWARD_COINS));
                    }
                }.start();
                videoView.start();
            }

            private void closeOwnAds() {

                if (mp != null) {
                    mp.release();
                    mp = null;
                }

                binding.lytOwnAds.setVisibility(View.GONE);

            }

            private void toggleVolume() {
                if (isMute) {
                    binding.btnVolume.setImageDrawable(ContextCompat.getDrawable(EarnActivity.this, R.drawable.ic_round_volume_off_24));
                    mp.setVolume(10000, 10000);
                    isMute = false;
                } else {
                    mp.setVolume(0, 0);
                    binding.btnVolume.setImageDrawable(ContextCompat.getDrawable(EarnActivity.this, R.drawable.ic_round_volume_down_24));
                    isMute = true;
                }
            }
        });
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.d(TAG, "onError:videoeee ");
            if (mp != null) {
                mp.release();
                mp = null;
            }
            binding.lytOwnAds.setVisibility(View.GONE);
            return false;
        });

        binding.videoView.setOnClickListener(v -> {
            Intent intent = new Intent(EarnActivity.this, WebActivity.class);
            intent.putExtra("ADID", String.valueOf(adid));
            intent.putExtra(WEBSITE, ownWebUrl);
            intent.putExtra("type", "ads");
            startActivity(intent);
        });

    }

    public void onClickBack(View view) {
     finish();


    }


    @Override
    public void onBackPressed() {
        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            try {

                if (DFirstActivityyy.checkfbAd()) {
                    if (mInterstitialAdr.isLoaded()) {
                        mInterstitialAdr.show();

                        mInterstitialAdr.setAdListener(new com.google.android.gms.ads.AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError var1) {
                                // Code to be executed when an ad request fails.
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                requestNewInterstitial();
                                bckpressed();
                            }
                        });
                    } else {
                        bckpressed();
                    }
                } else {
                    bckpressed();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {

            if (interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()) {
                if (DFirstActivityyy.checkfbAd()) {
                    interstitialAd.show();

                    // Set listeners for the Interstitial Ad
                    interstitialAd.setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(Ad ad) {
                            // Interstitial ad displayed callback
                        }

                        @Override
                        public void onInterstitialDismissed(Ad ad) {
                            // Interstitial dismissed callback

                            // Load a new interstitial.
                            interstitialAd.loadAd();

                            bckpressed();
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            // Ad error callback
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            // Interstitial ad is loaded and ready to be displayed
                            if (ad == interstitialAd) {
                                System.out.println("Rajan_interstrial" + "Ad loaded. Click show to present!");
                            }
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                            // Ad clicked callback
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                            // Ad impression logged callback
                        }
                    });
                } else {
                    bckpressed();
                }
            } else {
                bckpressed();
            }
        } else {
            bckpressed();
        }
    }



    public void bckpressed() {
      finish();
    }




    @Override
    protected void onDestroy() {
        Anylitecs.removeSesson(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Anylitecs.removeSesson(this);
        Log.d(TAG, "onPause: ");
    }

    public void onclickvid1(View view) {
        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            try {

                if (DFirstActivityyy.checkfbAd()) {
                    if (mInterstitialAdr.isLoaded()) {
                        mInterstitialAdr.show();

                        mInterstitialAdr.setAdListener(new com.google.android.gms.ads.AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError var1) {
                                // Code to be executed when an ad request fails.
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                requestNewInterstitial();
                                addMoney(sessionManager.getStringValue(Const.REWARD_COINS));;
                            }
                        });
                    } else {
                        addMoney(sessionManager.getStringValue(Const.REWARD_COINS));;
                    }
                } else {
                    addMoney(sessionManager.getStringValue(Const.REWARD_COINS));;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {

            if (interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()) {
                if (DFirstActivityyy.checkfbAd()) {
                    interstitialAd.show();

                    // Set listeners for the Interstitial Ad
                    interstitialAd.setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(Ad ad) {
                            // Interstitial ad displayed callback
                        }

                        @Override
                        public void onInterstitialDismissed(Ad ad) {
                            // Interstitial dismissed callback

                            // Load a new interstitial.
                            interstitialAd.loadAd();

                            addMoney(sessionManager.getStringValue(Const.REWARD_COINS));;
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            // Ad error callback
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            // Interstitial ad is loaded and ready to be displayed
                            if (ad == interstitialAd) {
                                System.out.println("Rajan_interstrial" + "Ad loaded. Click show to present!");
                            }
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                            // Ad clicked callback
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                            // Ad impression logged callback
                        }
                    });
                } else {
                    addMoney(sessionManager.getStringValue(Const.REWARD_COINS));;
                }
            } else {
                addMoney(sessionManager.getStringValue(Const.REWARD_COINS));
            }
        } else {
            addMoney(sessionManager.getStringValue(Const.REWARD_COINS));
        }
        
    }


    @Override
    protected void onResume() {
        super.onResume();
        Anylitecs.addUser(this);
    }



}