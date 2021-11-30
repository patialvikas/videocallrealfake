package com.slvrvideocallapp.trndvideo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.tabs.TabLayout;
import com.slvrvideocallapp.trndvideo.Anylitecs;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.adapters.CountrtyAdapter;
import com.slvrvideocallapp.trndvideo.adapters.ViewPagerAdapter;
import com.slvrvideocallapp.trndvideo.databinding.ActivityMainBinding;
import com.slvrvideocallapp.trndvideo.databinding.PopUpShowAdsBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.CountryRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitService;
import com.slvrvideocallapp.trndvideo.utils.Functions;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

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
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIALMAINR;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainact";
    private static final String WEBSITE = "WEBSITE";
    ActivityMainBinding binding;
    RetrofitService service;
    AlertDialog aleart;
    boolean fetched;
    Dialog dialog;
    CountryRoot.Datum tempobj;
    List<CountryRoot.Datum> finelCountry = new ArrayList<>();
    private String adid;
    private String ownAdRewardUrl = "";
    private String ownAdBannerUrl = "";
    private String ownAdInstarUrl = "";
    private String ownWebUrl = "";
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private SessionManager sessionManager;
    private List<CountryRoot.Datum> countries;
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

    public MainActivity() {
    }

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.chatprimary));
            window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setStatusBarGradiant(this);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        service = RetrofitBuilder.create(this);
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

        try {
            //facebookads
            if(prf.getString(TAG_BANNER).equalsIgnoreCase("fb")) {
//                loadAdView();
            }

            //facebookadsinterstrial
            if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
                loadinterstrialAdView();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        initView();
        binding.drawerlayout.setViewScale(GravityCompat.START, 0.9f); //set height scale for main view (0f to 1f)
        binding.drawerlayout.setViewElevation(GravityCompat.START, 30); //set main view elevation when drawer open (dimension)
        binding.drawerlayout.setViewScrimColor(GravityCompat.START, ContextCompat.getColor(this, R.color.colorPrimary)); //set drawer overlay coloe (color)
        binding.drawerlayout.setDrawerElevation(30); //set drawer elevation (dimension)
        //  binding.drawerLayout.setContrastThreshold(0); //set maximum of contrast ratio between white text and background color.
        binding.drawerlayout.setRadius(GravityCompat.START, 25);
        binding.drawerlayout.setViewRotation(GravityCompat.START, 0);
        binding.drawerlayout.closeDrawer(GravityCompat.START);
        binding.drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setStatusBarGradiant(MainActivity.this);
            }
        });
        initDrawerListners();


        Anylitecs.addUser(this);
    }


    private void initDrawerListners() {
        binding.appbarImgmenu.setOnClickListener(v -> binding.drawerlayout.openDrawer(GravityCompat.START, true));
        binding.navToolbar.navhome.setOnClickListener(v -> binding.drawerlayout.closeDrawer(GravityCompat.START));
        binding.navToolbar.navabout.setOnClickListener(v -> startActivity(new Intent(this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/about.html").putExtra("title", "About Us")));
        binding.navToolbar.navprivacy.setOnClickListener(v -> {
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
                                     startActivity(new Intent(MainActivity.this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/terms.html").putExtra("title", "Privacy Policy"));;
                                }
                            });
                        } else {
                             startActivity(new Intent(this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/terms.html").putExtra("title", "Privacy Policy"));;
                        }
                    } else {
                         startActivity(new Intent(this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/terms.html").putExtra("title", "Privacy Policy"));;
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

                                 startActivity(new Intent(MainActivity.this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/terms.html").putExtra("title", "Privacy Policy"));;
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
                         startActivity(new Intent(this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/terms.html").putExtra("title", "Privacy Policy"));;
                    }
                } else {
                     startActivity(new Intent(this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/terms.html").putExtra("title", "Privacy Policy"));;
                }
            } else {
                 startActivity(new Intent(this, WebActivity.class).putExtra(WEBSITE, "https://codderlab.com/images/terms.html").putExtra("title", "Privacy Policy"));;
            }
            
           
        });
        binding.navToolbar.navProfile.setOnClickListener(v -> {
            sessionManager = new SessionManager(MainActivity.this);
            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else {
                Functions.showAlertDialog(MainActivity.this, sweetAlertDialog -> {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    sweetAlertDialog.dismissWithAnimation();
                }, sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
            }
        });

        binding.navToolbar.navEarnCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager = new SessionManager(MainActivity.this);
                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
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
                                            startActivity(new Intent(MainActivity.this, EarnActivity.class));;
                                        }
                                    });
                                } else {
                                    startActivity(new Intent(MainActivity.this, EarnActivity.class));;
                                }
                            } else {
                                startActivity(new Intent(MainActivity.this, EarnActivity.class));;
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

                                        startActivity(new Intent(MainActivity.this, EarnActivity.class));;
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
                                startActivity(new Intent(MainActivity.this, EarnActivity.class));;
                            }
                        } else {
                            startActivity(new Intent(MainActivity.this, EarnActivity.class));;
                        }
                    } else {
                        startActivity(new Intent(MainActivity.this, EarnActivity.class));;
                    }
                } else {
                    Functions.showAlertDialog(MainActivity.this, sweetAlertDialog -> {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        sweetAlertDialog.dismissWithAnimation();
                    }, sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
                }
            }
        });
        binding.navToolbar.navshare.setOnClickListener(v -> {
            final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
            Intent sendInt = new Intent(Intent.ACTION_SEND);
            sendInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sendInt.putExtra(Intent.EXTRA_TEXT, "live video chat  Download Now  " + appLink);
            sendInt.setType("text/plain");
            startActivity(Intent.createChooser(sendInt, "Share"));
        });
        binding.navToolbar.navmore.setOnClickListener(v -> {
            final String appName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }
        });
        binding.navToolbar.navrate.setOnClickListener(v -> {
            final String appName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }
        });
    }

    private void initView() {
        getCountryList();
        // getCountery();

        binding.videobtn.setOnClickListener(v -> {
            sessionManager = new SessionManager(this);
            //startActivity(new Intent(this,LiveActivity.class));


            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
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
                                        openPopup();
                                    }
                                });
                            } else {
                                openPopup();
                            }
                        } else {
                            openPopup();
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

                                    openPopup();
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
                            openPopup();
                        }
                    } else {
                        openPopup();
                    }
                } else {
                    openPopup();
                }

            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

        });
    }

    private void getCountery() {
        Call<CountryRoot> call = service.getCountries();
        call.enqueue(new Callback<CountryRoot>() {
            @Override
            public void onResponse(Call<CountryRoot> call, Response<CountryRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 200 && !response.body().getData().isEmpty()) {
                        CountrtyAdapter countrtyAdapter = new CountrtyAdapter(response.body().getData());
                        binding.rvCountry.setAdapter(countrtyAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<CountryRoot> call, Throwable t) {

            }
        });
    }

    private void openPopup() {
        //ads policy
        return;

//        dialog = new Dialog(this, R.style.customStyle);
//        if (dialog.isShowing()) {
//            return;
//        }
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        PopUpShowAdsBinding popupbinding = DataBindingUtil.inflate(inflater, R.layout.pop_up_show_ads, null, false);
//
//
//        dialog.setContentView(popupbinding.getRoot());
//
//
//
//       /* AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
//                .forUnifiedNativeAd(unifiedNativeAd -> {
//                    // Show the ad.
//                    Log.d(TAG, "showPopup: popup native loded");
//
//                    UnifiedNativeAdView adView =
//                            (UnifiedNativeAdView) getLayoutInflater()
//                                    .inflate(R.layout.native_google_popup, null);
//
//                    ImageView imageView = adView.findViewById(R.id.ad_media);
//                    Glide.with(this).load(unifiedNativeAd.getMediaContent().getMainImage()).circleCrop().transform(new RoundedCorners(20)).into(imageView);
//                    // Set other ad assets.
//                    adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//                    adView.setBodyView(adView.findViewById(R.id.ad_body));
//                    adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//
//                    adView.setPriceView(adView.findViewById(R.id.ad_price));
//                    adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//                    adView.setStoreView(adView.findViewById(R.id.ad_store));
//                    adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//                    // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
//                    ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
//
//
//                    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//                    // check before trying to display them.
//                    if (unifiedNativeAd.getBody() == null) {
//                        adView.getBodyView().setVisibility(View.INVISIBLE);
//                    } else {
//                        adView.getBodyView().setVisibility(View.VISIBLE);
//                        ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
//                    }
//
//                    if (unifiedNativeAd.getCallToAction() == null) {
//                        adView.getCallToActionView().setVisibility(View.INVISIBLE);
//                    } else {
//                        adView.getCallToActionView().setVisibility(View.VISIBLE);
//                        ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
//                    }
//
//
//                    if (unifiedNativeAd.getPrice() == null) {
//                        adView.getPriceView().setVisibility(View.INVISIBLE);
//                    } else {
//                        adView.getPriceView().setVisibility(View.VISIBLE);
//                        ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
//                    }
//
//                    if (unifiedNativeAd.getStore() == null) {
//                        adView.getStoreView().setVisibility(View.INVISIBLE);
//                    } else {
//                        adView.getStoreView().setVisibility(View.VISIBLE);
//                        ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
//                    }
//
//                    if (unifiedNativeAd.getStarRating() == null) {
//                        adView.getStarRatingView().setVisibility(View.INVISIBLE);
//                    } else {
//                        ((RatingBar) adView.getStarRatingView())
//                                .setRating(unifiedNativeAd.getStarRating().floatValue());
//                        adView.getStarRatingView().setVisibility(View.VISIBLE);
//                    }
//
//                    if (unifiedNativeAd.getAdvertiser() == null) {
//                        adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//                    } else {
//                        ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
//                        adView.getAdvertiserView().setVisibility(View.VISIBLE);
//                    }
//
//
//                    adView.setNativeAd(unifiedNativeAd);
//                    popupbinding.flAdplaceholderbanner.removeAllViews();
//                    popupbinding.flAdplaceholderbanner.addView(adView);
//
//
//                })
//                .withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(LoadAdError adError) {
//                        Log.d(TAG, "onAdFailedToLoad: popup nativew  " + adError.getMessage());
//                        // Handle the failure by logging, altering the UI, and so on.
//                    }
//                })
//                .withNativeAdOptions(new NativeAdOptions.Builder()
//                        // Methods in the NativeAdOptions.Builder class can be
//                        // used here to specify individual options settings.
//                        .build())
//                .build();
//        adLoader.loadAd(new AdRequest.Builder().build());
//*/
//
//        if (sessionManager.getStringValue(Const.PROFILE_IMAGE).equals("")) {
//            popupbinding.profilechar.setVisibility(View.VISIBLE);
//            popupbinding.profilechar.setText(String.valueOf(sessionManager.getUser().getFname().charAt(0)).toUpperCase());
//        }
//
//        Glide.with(getApplicationContext())
//                .load(sessionManager.getStringValue(Const.PROFILE_IMAGE)).error(R.drawable.dbg_whitebtnroundddd)
//                .placeholder(R.drawable.dbg_whitebtnroundddd)
//                .circleCrop()
//                .into(popupbinding.imagepopup);
//        if (sessionManager.getUser().getFname() != null) {
//            popupbinding.tv1.setText("Hello Dear, " + sessionManager.getUser().getFname());
//        }
//        popupbinding.textview.setText("Do you want to live with Us?");
//
//        popupbinding.tvPositive.setOnClickListener(v -> {
//
//            dialog.dismiss();
//            Toast.makeText(MainActivity.this, "Your Request has been Sended!!", Toast.LENGTH_SHORT).show();
//        });
//        popupbinding.tvCencel.setOnClickListener(v -> dialog.dismiss());
//
//
//        dialog.show();


    }

    private void getCountryList() {
        Call<CountryRoot> call = service.getCountries();
        call.enqueue(new Callback<CountryRoot>() {
            @Override
            public void onResponse(Call<CountryRoot> call, Response<CountryRoot> response) {
                if (response.code() == 200 && !response.body().getData().isEmpty()) {
                    countries = response.body().getData();
                    for (int i = 0; i < countries.size(); i++) {
                        if (Boolean.TRUE.equals(countries.get(i).getIsTop())) {
                            tempobj = countries.get(i);
                            Log.d(TAG, "onResponse: temp " + tempobj.get_id());
                            Log.d(TAG, "onResponse: temp " + tempobj.getName());
                        } else {
                            finelCountry.add(countries.get(i));
                            Log.d(TAG, "onResponse: obg" + countries.get(i).getName());
                        }
                    }
                    if (tempobj != null) {
                        finelCountry.add(0, tempobj);
                    }

                    ViewPagerAdapter viewPagerAdapter;
                    List<CountryRoot.Datum> filteredList = new ArrayList<>();
                    String id="";
                    if(getIntent().hasExtra("country_id")){
                        id=getIntent().getStringExtra("country_id");
                        for(int i=0;i<finelCountry.size();i++){
                            if(finelCountry.get(i).get_id().equals(id)){
                                filteredList.add(finelCountry.get(i));
                            }
                        }
                        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), filteredList);
                    }else{
                        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), finelCountry);
                    }




                    binding.viewPager.setAdapter(viewPagerAdapter);
                    binding.tablayout1.setupWithViewPager(binding.viewPager);

                    if(getIntent().hasExtra("country_id")){
                        settabLayoutFilter(filteredList);
                    }else{
                        settabLayout(finelCountry);
                    }


                    binding.tablayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
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
                                                    View view = tab.getCustomView();
                                                    TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                                                    selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                                                }
                                            });
                                        } else {
                                            View view = tab.getCustomView();
                                            TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                                            selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                                        }
                                    } else {
                                        View view = tab.getCustomView();
                                        TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                                        selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
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

                                                View view = tab.getCustomView();
                                                TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                                                selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
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
                                        View view = tab.getCustomView();
                                        TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                                        selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                                    }
                                } else {
                                    View view = tab.getCustomView();
                                    TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                                    selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                                }
                            } else {
                                View view = tab.getCustomView();
                                TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                                selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                            }

                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            View view = tab.getCustomView();
                            TextView selectedText = (TextView) view.findViewById(R.id.tvTab);
                            selectedText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
//ll
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<CountryRoot> call, Throwable t) {
//ll
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.drawerlayout.closeDrawer(GravityCompat.START);
        Anylitecs.addUser(this);
    }

    private void settabLayout(List<CountryRoot.Datum> countries) {
        binding.tablayout1.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tablayout1.removeAllTabs();
        for (int i = 0; i < countries.size(); i++) {

            binding.tablayout1.addTab(binding.tablayout1.newTab().setCustomView(createCustomView(this.finelCountry.get(i), i)));

        }
    }


    private void settabLayoutFilter(List<CountryRoot.Datum> countries) {
        binding.tablayout1.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tablayout1.removeAllTabs();
        for (int i = 0; i < countries.size(); i++) {

            binding.tablayout1.addTab(binding.tablayout1.newTab().setCustomView(createCustomView(countries.get(i), i)));

        }
    }


    private View createCustomView(CountryRoot.Datum datum, int pos) {
        Log.d(TAG, "settabLayout: " + datum.getName());
        Log.d(TAG, "settabLayout: " + datum.getLogo());
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tabhorizontol, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(datum.getName());
        ImageView img = (ImageView) v.findViewById(R.id.imagetab);

        if (pos == 0) {
            tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

        }
        Glide.with(getApplicationContext())
                .load(new SessionManager(MainActivity.this).getStringValue(Const.IMAGE_URL) + datum.getLogo()).error(R.drawable.dcoinssss)
                .placeholder(R.drawable.dic_giftttt)
                .circleCrop()
                .into(img);
        return v;

    }


    public void onClickProfile(View view) {
        sessionManager = new SessionManager(this);
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
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
                                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                }
                            });
                        } else {
                            startActivity(new Intent(this, ProfileActivity.class));
                        }
                    } else {
                        startActivity(new Intent(this, ProfileActivity.class));
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

                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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
                        startActivity(new Intent(this, ProfileActivity.class));
                    }
                } else {
                    startActivity(new Intent(this, ProfileActivity.class));
                }
            } else {
                startActivity(new Intent(this, ProfileActivity.class));
            }



        } else {
            Functions.showAlertDialog(MainActivity.this, sweetAlertDialog -> {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                sweetAlertDialog.dismissWithAnimation();
            }, sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());


        }


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
        super.onBackPressed();
    }
    private void loadinterstrialAdView() {

        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
//        setLabel("Loading interstitial ad...");

        // Create the interstitial unit with a placement ID (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        interstitialAd = new InterstitialAd(MainActivity.this,prf.getString(TAG_INTERSTITIALMAINR));

//        AdSettings.addTestDevice("3cbb5876-713c-4967-ba8e-9da6222c0afa");
        // Set a listener to get notified on changes or when the user interact with the ad.
//        interstitialAd.setAdListener(this);
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
//                // Cleanup.
//                interstitialAd.destroy();
//                interstitialAd = null;

                // Load a new interstitial.
                interstitialAd.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                if (ad == interstitialAd) {
                    System.out.println("Rajan_interstrial"+"Interstitial ad failed to load: " + adError.getErrorMessage());
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                if (ad == interstitialAd) {
                    System.out.println("Rajan_interstrial"+"Ad loaded. Click show to present!");
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                System.out.println("Rajan_interstrial_onLoggingImpression");
            }
        });

        // Load a new interstitial.
        if (prf.getString("SUBSCRIBED").equals("FALSE")) {
            interstitialAd.loadAd();
        }

    }

}