package com.slvrvideocallapp.trndvideo.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slvrvideocallapp.trndvideo.Anylitecs;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.adapters.BottomViewPagerAdapter;
import com.slvrvideocallapp.trndvideo.adapters.CommentAdapter;
import com.slvrvideocallapp.trndvideo.adapters.EmojiAdapter;
import com.slvrvideocallapp.trndvideo.databinding.ActivityVideoBinding;
import com.slvrvideocallapp.trndvideo.databinding.PopUpShowAdsBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.CommentRoot;
import com.slvrvideocallapp.trndvideo.models.EmojiIconRoot;
import com.slvrvideocallapp.trndvideo.models.EmojicategoryRoot;
import com.slvrvideocallapp.trndvideo.models.ThumbRoot;
import com.slvrvideocallapp.trndvideo.models.UserRoot;
import com.slvrvideocallapp.trndvideo.models.VideoRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;
import com.slvrvideocallapp.trndvideo.utils.Functions;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.animation.ValueAnimator.INFINITE;
import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.interstitialAd;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.mInterstitialAdr;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.requestNewInterstitial;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_APP_ID_AD_UNIT_ID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNER;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNERMAINR;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;

public class VideoActivity extends AppCompatActivity {
    private boolean isOpenAd=false;
    private static final int SHEET_OPEN = 1;
    private static final int SHEET_CLOSE = 2;
    private static final String TAG = "vidact";
    private static final String WEBSITE = "WEBSITE";
    private final boolean playWhenReady = true;
    private final int currentWindow = 0;
    private final long playbackPosition = 0;
    private final boolean ggnativeloded = false;
    int adsDiffTime = 15;
    ActivityVideoBinding binding;
    BottomSheetBehavior sheetBehavior;
    Animation animZoomIn;
    CommentAdapter commentAdapter;
    Handler handler;
    Runnable runnable;
    SessionManager sessionManager;
    PlayerView playerView;
    String url;
    String path;
    String name;
    int currentPos = 0;
    AlertDialog aleartdilog;
    boolean cleartext = false;
    List<EmojicategoryRoot.Datum> finelCategories = new ArrayList<>();
    EmojicategoryRoot.Datum tempobj;
    long position = 0;
    boolean paused = false;
    long adPosition = 0;
    boolean isMute = false;
    ProgressDialog progress;
    boolean googleorfbrunning = false;
    boolean flagAds = false;
    boolean videoAdShow = false;
    Random rand = new Random();
    private EmojiAdapter emojiAdapter;
    private String videoURL = "";
    private int count = 0;
    private List<CommentRoot.Datum> comments = new ArrayList<>();
    private String countryid;
    private boolean showAds = false;
    private int time = 0;
    private boolean start = false;
    private SimpleExoPlayer player;
    private String userId;
    private AlertDialog aleart;
    private boolean fetched;
    private String adid;
    private String ownAdRewardUrl = "";
    private String ownAdBannerUrl = "";
    private String ownAdInstarUrl = "";
    private String ownWebUrl = "";
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private int rendtemp = 0;
    private Dialog dialog;
    private String thumbImage;
    private boolean ownLoded = false;
    private SimpleExoPlayer adPlayer;
    private List<EmojicategoryRoot.Datum> categories;
    private boolean googleNativeLoded = false;
//    GGInterstitialAd mAd;



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



    private void chkConnection2() {
        aleart = new androidx.appcompat.app.AlertDialog.Builder(VideoActivity.this).
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

        int randInt1 = rand.nextInt((600 - 450) + 1) + 450;

        rendtemp = randInt1;

        binding.tvCoin.setText(String.valueOf(rendtemp));
        fetched = true;
        addRandomCoins();
        initView();
        initListnear();

    }



    private void addRandomCoins() {

        Handler handler2 = new Handler();
        Runnable runnable2 = new Runnable() {

            @Override
            public void run() {
                try {
                    int randInt1 = rand.nextInt((50 - 10) + 1) + 10;

                    rendtemp = rendtemp + randInt1;


                    double coin1;
                    if (rendtemp >= 1000) {
                        coin1 = (double) rendtemp / 1000;
                        DecimalFormat df = new DecimalFormat("#.##");

                        binding.tvCoin.setText(df.format(coin1).concat("K"));
                    } else {
                        coin1 = rendtemp;
                        binding.tvCoin.setText(String.valueOf((int) coin1));
                    }
                    Log.d(TAG, "run: coin " + coin1);
                    Log.d(TAG, "run: rendm  " + rendtemp);
                    handler2.postDelayed(this, 5000);

                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };

        handler2.postDelayed(runnable2, 5000);

    }




    private void closeOwnAds() {
        videoAdShow = false;
        binding.tvTime.setVisibility(View.GONE);
        binding.btnClose.setVisibility(View.VISIBLE);
        Log.d(TAG, "closeOwnAds: videoad");
        binding.lytOwnAds.setVisibility(View.GONE);
        binding.videoView.setVisibility(View.VISIBLE);
        showAds = false;
        start = true;
        time = 0;
        if (adPlayer != null) {
            adPlayer.release();
            adPlayer = null;
        }
        if (player != null) {
            player.seekTo(position);
            player.setPlayWhenReady(true);
        }

        dialog.dismiss();
        time = 0;



    }

    private void getIntentdata() {
        Intent intent = getIntent();
        if (intent != null) {
            String cid = intent.getStringExtra("cid");
            String model = intent.getStringExtra("model");
            if (model != null && !model.equals("")) {
                binding.lytthumb.setVisibility(View.VISIBLE);
                ThumbRoot.Datum object = new Gson().fromJson(model, ThumbRoot.Datum.class);
                if (object != null) {
                    Log.d(TAG, "getIntentdata:image " + object.getImage());
                    Log.d(TAG, "getIntentdata:image " + object.getName());
                    thumbImage = object.getImage();
                    Glide.with(getApplicationContext())
                            .load(new SessionManager(VideoActivity.this).getStringValue(Const.IMAGE_URL) + thumbImage)
                            .transform(new BlurTransformation(25), new CenterCrop())
                            .circleCrop()
                            .into(binding.imgthumb);
                    Glide.with(getApplicationContext())
                            .load(new SessionManager(VideoActivity.this).getStringValue(Const.IMAGE_URL) + thumbImage)
                            .circleCrop()
                            .into(binding.imgprofile);
                    String s = String.valueOf(object.getName().charAt(0)).toUpperCase();
                    binding.tvName.setText(s.concat(object.getName().substring(1)));
                }

            }
            if (cid != null && !cid.equals("")) {
                countryid = cid;
                getVideo(cid);
                getComments(cid);
            }
        }
    }

    private void getComments(String cid) {
        Call<CommentRoot> call = RetrofitBuilder.create(this).getComment(cid);
        call.enqueue(new Callback<CommentRoot>() {
            @Override
            public void onResponse(Call<CommentRoot> call, Response<CommentRoot> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() == 200 && !response.body().getData().isEmpty()) {

                    comments = response.body().getData();
                    addDataTOCommentAdapter();


                }
            }

            @Override
            public void onFailure(Call<CommentRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void setRandomViews() {
        Log.d(TAG, "setRandomViews: ");
        int randInt1 = rand.nextInt((1500 - 1100) + 1) + 1100;
        binding.tvviews.setText(String.valueOf(randInt1));
    }

    private void addDataTOCommentAdapter() {
        final Timer timer = new Timer();
        count = 0;
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (count < comments.size()) {
                        commentAdapter.additem(comments.get(count));
                        binding.rvComments.scrollToPosition(count);
                        count++;
                    } else {
                        count = 0;
                        timer.cancel();
                        getComments(countryid);
                    }


                    setRandomViews();
                });


            }
        }, 0, 3000);


    }

    private void initView() {
        flagAds = false;
        getIntentdata();

        playerView = findViewById(R.id.video_view);

        updetUI(SHEET_CLOSE);

        setUI();

        getGiftsCategories();


        commentAdapter = new CommentAdapter();
        binding.rvComments.setAdapter(commentAdapter);


        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.imggift2, "rotationY", 0.0f, 360f);
        animation.setDuration(5000);
        animation.setRepeatCount(INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }


    private void initListnear() {

        binding.etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//ll
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    //ll
                } else {
                    cleartext = false;
                    binding.lytbuttons.setVisibility(View.GONE);
                    binding.btnsend.setVisibility(View.VISIBLE);
                    binding.etComment.setVisibility(View.VISIBLE);
                    binding.lytShare.setVisibility(View.GONE);
                    binding.lytbuttons.setVisibility(View.GONE);
                    binding.btnsend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//ll
            }
        });
        binding.btnsend.setOnClickListener(v -> {
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
                                    sendComment();
                                }
                            });
                        } else {
                            sendComment();
                        }
                    } else {
                        sendComment();
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

                                sendComment();
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
                        sendComment();
                    }
                } else {
                    sendComment();
                }
            } else {
                sendComment();
            }






        });

        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoomin);

        binding.imggift2.setOnClickListener(v -> {

            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                binding.bottomPage.tvUsereCoin.setText(String.valueOf(sessionManager.getUser().getCoin()));
                binding.tvusercoins.setText(String.valueOf(sessionManager.getUser().getCoin()));
                updetUI(SHEET_OPEN);
            } else {
                binding.bottomPage.tvUsereCoin.setText("00");
                binding.tvusercoins.setText("00");
                updetUI(SHEET_OPEN);
            }
        });
        binding.videoView.setOnClickListener(v -> updetUI(SHEET_CLOSE));
        binding.bottomPage.btnclose.setOnClickListener(v -> updetUI(SHEET_CLOSE));


    }

    private void sendComment() {
        sessionManager = new SessionManager(VideoActivity.this);
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            binding.btnsend.setVisibility(View.VISIBLE);
            binding.lytbuttons.setVisibility(View.VISIBLE);
            if (!binding.etComment.getText().toString().equals("")) {
                Log.d(TAG, "initListnear: comment1");
                CommentRoot.Datum datum = new CommentRoot.Datum();
                datum.setComment(binding.etComment.getText().toString().trim());
                datum.setName(sessionManager.getUser().getFname());
                Log.d(TAG, "initListnear: comment2");
                commentAdapter.additem(datum);
                Log.d(TAG, "initListnear: cmt3");
                binding.rvComments.setAdapter(commentAdapter);
                binding.rvComments.scrollToPosition(commentAdapter.getItemCount());
                Log.d(TAG, "initListnear: cmt4");

                setUI();


                cleartext = true;
                Log.d(TAG, "initListnear: cmt5");

            }
        } else {
            Functions.showAlertDialog(VideoActivity.this, sweetAlertDialog -> {
                startActivity(new Intent(VideoActivity.this, LoginActivity.class));
                sweetAlertDialog.dismissWithAnimation();
            }, sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
        }
    }


    private void getGiftsCategories() {
        Call<EmojicategoryRoot> call = RetrofitBuilder.create(this).getCategories();
        call.enqueue(new Callback<EmojicategoryRoot>() {
            @Override
            public void onResponse(Call<EmojicategoryRoot> call, Response<EmojicategoryRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200 && !response.body().getData().isEmpty()) {

                    categories = response.body().getData();

                    for (int i = 0; i < categories.size(); i++) {
                        if (Boolean.TRUE.equals(categories.get(i).getIsTop())) {
                            tempobj = categories.get(i);
                        } else {
                            finelCategories.add(categories.get(i));
                        }
                    }
                    finelCategories.add(0, tempobj);
                    setGiftList();


                    BottomViewPagerAdapter bottomViewPagerAdapter = new BottomViewPagerAdapter(finelCategories);
                    binding.bottomPage.viewpager.setAdapter(bottomViewPagerAdapter);
                    settabLayout(finelCategories);
                    binding.bottomPage.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.bottomPage.tablayout));
                    binding.bottomPage.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            binding.bottomPage.viewpager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            //ll
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            //ll
                        }
                    });
                    bottomViewPagerAdapter.setEmojiListnerViewPager((bitmap, coin) -> {
                        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                            sendGift(bitmap, coin);
                            updetUI(SHEET_CLOSE);
                        } else {
                            Functions.showAlertDialog(VideoActivity.this, sweetAlertDialog -> {
                                startActivity(new Intent(VideoActivity.this, LoginActivity.class));
                                sweetAlertDialog.dismissWithAnimation();
                            }, sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                        }
                    });
                }
            }

            private void settabLayout(List<EmojicategoryRoot.Datum> categories) {
                binding.bottomPage.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
                for (int i = 0; i < categories.size(); i++) {

                    binding.bottomPage.tablayout.addTab(binding.bottomPage.tablayout.newTab().setCustomView(createCustomView(categories.get(i))));

                }
            }

            @Override
            public void onFailure(Call<EmojicategoryRoot> call, Throwable t) {
//ll
            }
        });
    }

    private View createCustomView(EmojicategoryRoot.Datum datum) {
        Log.d(TAG, "settabLayout: " + datum.getName());
        Log.d(TAG, "settabLayout: " + datum.getIcon());
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tabgift, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(datum.getName());
        ImageView img = (ImageView) v.findViewById(R.id.imagetab);

        Glide.with(getApplicationContext())
                .load(new SessionManager(VideoActivity.this).getStringValue(Const.IMAGE_URL) + datum.getIcon())
                .placeholder(R.drawable.dic_giftttt)
                .into(img);
        return v;

    }

    private void sendGift(Bitmap bitmap, Long coin) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("coin", coin);
        Call<UserRoot> call = RetrofitBuilder.create(this).lessCoin(jsonObject);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {

                    if (response.body().getStatus() == 200) {
                        // long c = rendtemp + coin;

                        rendtemp = (int) (rendtemp + coin);


                        double coin1;
                        if (rendtemp >= 1000) {
                            coin1 = (double) rendtemp / 1000;
                            DecimalFormat df = new DecimalFormat("#.##");

                            binding.tvCoin.setText(df.format(coin1).concat("K"));
                        } else {
                            coin1 = rendtemp;
                            binding.tvCoin.setText(String.valueOf((int) coin1));
                        }
                        // binding.tvCoin.setText(String.valueOf(c));
                        binding.tvusercoins.setText(String.valueOf(response.body().getData().getCoin()));
                        binding.bottomPage.tvUsereCoin.setText(String.valueOf(response.body().getData().getCoin()));

                        Log.d(TAG, "onResponse: success coin minused");
                        sessionManager.saveUser(response.body().getData());

                        binding.imgAnimation.setImageBitmap(bitmap);
                        binding.imgAnimation.setVisibility(View.VISIBLE);
                        binding.imgAnimation.startAnimation(animZoomIn);
                        new Handler().postDelayed(() -> {
                            binding.imgAnimation.setImageBitmap(null);
                            binding.imgAnimation.setVisibility(View.GONE);
                        }, 2000);
                    } else if (response.body().getStatus() == 422) {
                        Toast.makeText(VideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: 452 " + t.toString());
            }
        });


    }

    private void getVideo(String cid) {
        binding.pd.setVisibility(View.VISIBLE);
        Call<VideoRoot> call = RetrofitBuilder.create(this).getVideo(cid);
        call.enqueue(new Callback<VideoRoot>() {
            @Override
            public void onResponse(Call<VideoRoot> call, Response<VideoRoot> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (!response.body().getData().isEmpty()) {
                        name = response.body().getData().get(0).getName();

                    }
                    if (response.body().getVideo() != null) {
                        videoURL = response.body().getVideo().getVideo();
                        Log.d(TAG, "onResponse: " + videoURL);

                        setvideoURL();

                    }
                }
            }

            @Override
            public void onFailure(Call<VideoRoot> call, Throwable t) {
                binding.pd.setVisibility(View.GONE);
            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }


    private void setGiftList() {
        Call<EmojiIconRoot> call1 = RetrofitBuilder.create(this).getEmojiByCategory(finelCategories.get(0).get_id());
        call1.enqueue(new Callback<EmojiIconRoot>() {
            private void onEmojiClick(Bitmap bitmap, Long coin) {
                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                    sendGift(bitmap, coin);
                } else {
                    Functions.showAlertDialog(VideoActivity.this, sweetAlertDialog -> {
                        startActivity(new Intent(VideoActivity.this, LoginActivity.class));
                        sweetAlertDialog.dismissWithAnimation();
                    }, sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                }

            }

            @Override
            public void onResponse(Call<EmojiIconRoot> call, Response<EmojiIconRoot> response) {
                Log.d(TAG, "onResponse: emoji yes" + response.code());
                if (response.code() == 200 && response.body().getStatus() == 200 && !response.body().getData().isEmpty()) {

                    emojiAdapter = new EmojiAdapter(response.body().getData());
                    binding.rvEmogi.setAdapter(emojiAdapter);

                    emojiAdapter.setOnEmojiClickListnear(this::onEmojiClick);


                }
            }

            @Override
            public void onFailure(Call<EmojiIconRoot> call, Throwable t) {
//ll
            }
        });
    }

    private void startTimer() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: " + time);
                    if (start) {
                        if (time == adsDiffTime) {


                            if (player != null) {
                                player.setPlayWhenReady(false);
                            }
                            if (dialog != null) {

                                showPopup();
                            }

                            Log.d(TAG, "run: popup showed");
                            start = false;
                            time = 0;
                        } else {
                            time++;
                        }
                    } else {
                        time = 0;
                    }
                    handler.postDelayed(this, 1000);

                } catch (IllegalStateException ed) {
                    Log.d(TAG, "run: " + ed.toString());
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

    }

    private void setvideoURL() {
        binding.pd.setVisibility(View.VISIBLE);
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setShowBuffering(true);
        Log.d(TAG, "setvideoURL: " + videoURL);
        Uri uri = Uri.parse(DSplashScrnActvty.main_domain + videoURL);
        // Uri uri = Uri.parse( sessionManager.getStringValue(Const.BASE_URL)+videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        Log.d(TAG, "initializePlayer: " + uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case STATE_BUFFERING:
                        Log.d(TAG, "buffer: " + uri);
                        break;
                    case STATE_ENDED:
                        player.setRepeatMode(Player.REPEAT_MODE_ALL);
                        Log.d(TAG, "end: " + uri);
                        break;
                    case STATE_IDLE:
                        Log.d(TAG, "idle: " + uri);
//                        if (player != null) {
//                            player.release();
//                        }
                        //  finish();
                        // getVideo(countryid);
                        break;

                    case STATE_READY:
                        binding.lytthumb.setVisibility(View.GONE);
                        binding.pd.setVisibility(View.GONE);
                        time = 0;
                        start = true;
                        Log.d(TAG, "ready: " + uri);

                        break;
                    default:
                        break;
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        sessionManager = new SessionManager(this);


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

        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            userId = sessionManager.getUser().get_id();
            binding.tvusercoins.setText(String.valueOf(sessionManager.getUser().getCoin()));
            Log.d(TAG, "onCreate: " + sessionManager.getUser().getCoin());
        }else{
            binding.tvusercoins.setText("00");
        }

        if (player != null) {
            player.release();
        }
        dialog = new Dialog(this, R.style.customStyle);
        adsDiffTime = Integer.parseInt(sessionManager.getStringValue(Const.ADS_TIME).equals("") ? "30" : sessionManager.getStringValue(Const.ADS_TIME));
        startTimer();
        initMain();
        chkConnection2();


        binding.bubbleEmitter.emitBubble(20);
        binding.bubbleEmitter.canExplode(true);

        binding.lytusercoin.setOnClickListener(v -> startActivity(new Intent(this, EarnActivity.class)));

        binding.tvusercoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                    startActivity(new Intent(VideoActivity.this, EarnActivity.class));
                } else {
                    Functions.showAlertDialog(VideoActivity.this, sweetAlertDialog -> {
                        startActivity(new Intent(VideoActivity.this, LoginActivity.class));
                        sweetAlertDialog.dismissWithAnimation();
                    }, sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Anylitecs.removeSesson(this);
    }


    private void setUI() {
        binding.etComment.setText(null);
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        binding.btnsend.setVisibility(View.VISIBLE);
        binding.lytbuttons.setVisibility(View.VISIBLE);

        binding.etComment.setVisibility(View.VISIBLE);
        binding.lytShare.setVisibility(View.VISIBLE);
    }

    private void updetUI(int state) {
        if (state == SHEET_OPEN) {
            binding.bottomPage.lyt2.setVisibility(View.VISIBLE);

            binding.rvComments.setVisibility(View.GONE);
            binding.rvEmogi.setVisibility(View.GONE);
            binding.lytbottom.setVisibility(View.GONE);
            binding.lytShare.setVisibility(View.GONE);
            binding.lytusercoin.setVisibility(View.GONE);
        } else {
            binding.bottomPage.lyt2.setVisibility(View.GONE);
            binding.rvComments.setVisibility(View.VISIBLE);
            binding.rvEmogi.setVisibility(View.VISIBLE);
            binding.lytbottom.setVisibility(View.VISIBLE);
            binding.lytShare.setVisibility(View.VISIBLE);
            binding.lytusercoin.setVisibility(View.VISIBLE);
        }
    }

    public void onClickClose(View view) {


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
                                if (player != null) {
                                    player.release();
                                }
                                finish();
                            }
                        });
                    } else {
                        if (player != null) {
                            player.release();
                        }
                        finish();
                    }
                } else {
                    if (player != null) {
                        player.release();
                    }
                    finish();
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

                            if (player != null) {
                                player.release();
                            }
                            finish();
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
                    if (player != null) {
                        player.release();
                    }
                    finish();
                }
            } else {
                if (player != null) {
                    player.release();
                }
                finish();
            }
        } else {
            if (player != null) {
                player.release();
            }
            finish();
        }

    }

    private void showAds() {


        if (player != null) {
            player.setPlayWhenReady(false);
            position = player.getCurrentPosition();
        }
        start = false;
        dialog.dismiss();
        isOpenAd=true;
        Log.d(TAG, "showAds: " + showAds + flagAds);
        if (!showAds) {
            showAds = true;
            flagAds = true;

        } else {
            return;
        }
        Log.d(TAG, "showAds: " + googleNativeLoded);

            Log.d(TAG, "showAds:  nothing loded ");
            flagAds = false;

            showAds = false;
            start = true;
            time = 0;
            if (player != null && player.getPlaybackState() != STATE_IDLE) {
                player.seekTo(position);
                player.setPlayWhenReady(true);
            } else {
                setvideoURL();
                if (player != null && player.getPlaybackState() != STATE_IDLE) {
                    player.seekTo(position);
                    player.setPlayWhenReady(true);
                }
            }

            dialog.dismiss();
            time = 0;
    }

    private void showPopup() {

        if (isFinishing() && dialog.isShowing()) {
            time = 0;
            return;
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        PopUpShowAdsBinding popupbinding = DataBindingUtil.inflate(inflater, R.layout.pop_up_show_ads, null, false);

        dialog.setContentView(popupbinding.getRoot());
        // popupbinding.tvPositive.setVisibility(View.GONE);


        dialog.setCancelable(false);
        Glide.with(getApplicationContext())
                .load(new SessionManager(VideoActivity.this).getStringValue(Const.IMAGE_URL) + thumbImage).placeholder(R.drawable.ddadduserrrrrr)
                .circleCrop()
                .into(popupbinding.imagepopup);
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            popupbinding.tv1.setText("Hello Dear, " + sessionManager.getUser().getFname());
            popupbinding.textview.setText("To countinue with " + binding.tvName.getText().toString() + ",\n watch this Ads");

        }else{
            popupbinding.tv1.setText("Hello Dear, User");
            popupbinding.textview.setText("To countinue with " + binding.tvName.getText().toString() + ",\n watch this Ads");

        }
        popupbinding.tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isOpenAd=true;
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
                                        showAds();
                                    }
                                });
                            } else {
                                showAds();
                            }
                        } else {
                            showAds();
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

                                    showAds();
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
                            showAds();
                        }
                    } else {
                        showAds();
                    }
                } else {
                    showAds();
                }

            }
        });
        popupbinding.tvCencel.setOnClickListener(v -> {
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
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    finish();
                                }
                            });
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            finish();
                        }
                    } else {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        finish();
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

                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                finish();
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
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        finish();
                    }
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    finish();
                }
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                finish();
            }




        });

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Anylitecs.addUser(this);
        if (player != null) {
            player.setVolume(1.0f);
        }
        Log.d(TAG, "onResume: 1");
        binding.lytOwnAds.setVisibility(View.GONE);
        binding.lytOwnInter.setVisibility(View.GONE);
        binding.lytOwnAds.setVisibility(View.GONE);
        binding.videoView.setVisibility(View.VISIBLE);
        Log.d(TAG, "googleorfbrunning   onremused  " + googleorfbrunning);
        Log.d(TAG, "googleor     paused onremused" + paused);
        if (paused) {
            if (player != null) {
                player.setPlayWhenReady(false);
            }
            start = false;
            showAds = false;
            time = 0;
            if (dialog != null) {
                if(!isOpenAd) {
                    showPopup();
                }

            }
        } else {
            if (player != null) {
                player.setPlayWhenReady(true);
            }
            start = true;
            showAds = false;
            time = 0;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Anylitecs.removeSesson(this);
        binding.lytOwnAds.setVisibility(View.GONE);
        binding.lytOwnInter.setVisibility(View.GONE);

        dialog.dismiss();
        if (player != null) {
            Log.d(TAG, "onPause: " + player.getVolume());
            player.setVolume(0);
            position = player.getCurrentPosition();
            player.setPlayWhenReady(false);
        }
        start = false;
        Log.d(TAG, "googleorfbrunning   onpaused  " + googleorfbrunning);
        Log.d(TAG, "googleor     paused onpaused" + paused);
        if (adPlayer != null) {
            adPosition = adPlayer.getCurrentPosition();
            adPlayer.setPlayWhenReady(false);
        }

        paused = !googleorfbrunning;
        Log.d(TAG, "onPause: ");
        time = 0;


        binding.lytOwnAds.setVisibility(View.GONE);
        binding.videoView.setVisibility(View.VISIBLE);
        videoAdShow = false;
        binding.tvTime.setVisibility(View.GONE);
        binding.btnClose.setVisibility(View.VISIBLE);
        binding.btnClose.setOnClickListener(v -> closeOwnAds());
        if (adPlayer != null) {
            adPlayer.release();
            adPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
        }
        if (Util.SDK_INT < 24) {
            if (player != null) {
                position = player.getCurrentPosition();
                player.setPlayWhenReady(false);
            }
            start = false;
        }
        if (Util.SDK_INT >= 24) {
            if (adPlayer != null) {
                adPlayer.setPlayWhenReady(false);
            }
            start = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null) {
            player.release();
        }
        finish();

    }

    public void onclickShare(View view) {
        final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
        Intent sendInt = new Intent(Intent.ACTION_SEND);
        sendInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sendInt.putExtra(Intent.EXTRA_TEXT, "live video chat  Download Now  " + appLink);
        sendInt.setType("text/plain");
        startActivity(Intent.createChooser(sendInt, "Share"));
    }

    public void onClickchat(View view) {
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
                                startActivity(new Intent(VideoActivity.this, ChatActivity.class).putExtra("image", thumbImage).putExtra("name", binding.tvName.getText().toString()));
                            }
                        });
                    } else {
                        startActivity(new Intent(this, ChatActivity.class).putExtra("image", thumbImage).putExtra("name", binding.tvName.getText().toString()));
                    }
                } else {
                    startActivity(new Intent(this, ChatActivity.class).putExtra("image", thumbImage).putExtra("name", binding.tvName.getText().toString()));
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

                            startActivity(new Intent(VideoActivity.this, ChatActivity.class).putExtra("image", thumbImage).putExtra("name", binding.tvName.getText().toString()));
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
                    startActivity(new Intent(this, ChatActivity.class).putExtra("image", thumbImage).putExtra("name", binding.tvName.getText().toString()));
                }
            } else {
                startActivity(new Intent(this, ChatActivity.class).putExtra("image", thumbImage).putExtra("name", binding.tvName.getText().toString()));
            }
        } else {
            startActivity(new Intent(this, ChatActivity.class).putExtra("image", thumbImage).putExtra("name", binding.tvName.getText().toString()));
        }

    }
}