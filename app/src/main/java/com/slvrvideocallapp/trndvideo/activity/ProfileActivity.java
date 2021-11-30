package com.slvrvideocallapp.trndvideo.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.slvrvideocallapp.trndvideo.Anylitecs;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.databinding.ActivityProfileBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.ProfileRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

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

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "profileact";
    private static final String WEBSITE = "WEBSITE";
    ActivityProfileBinding binding;
    SessionManager sessionManager;
    boolean fetched;
    private String userId;
    private ProfileRoot.Data user;
    private String adid;
    private String ownAdRewardUrl = "";
    private String ownAdBannerUrl = "";
    private String ownAdInstarUrl = "";
    private String ownWebUrl = "";
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private boolean ownLoded = false;
    private LinearLayout adContainer;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.purplepink));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        sessionManager = new SessionManager(this);
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            userId = sessionManager.getUser().get_id();
            getData();
        }



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


    private void getData() {
        binding.pd.setVisibility(View.VISIBLE);
        Call<ProfileRoot> call = RetrofitBuilder.create(this).getUser(userId);
        call.enqueue(new Callback<ProfileRoot>() {
            @Override
            public void onResponse(Call<ProfileRoot> call, Response<ProfileRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200 && response.body().getData() != null) {
                    user = response.body().getData();
                    binding.setUser(user);

                    binding.username.setText("@".concat(user.getUsername()));
                    String fname = user.getFname().substring(0, 1).toUpperCase().concat(user.getFname().substring(1));

                    binding.tvName.setText(fname);
                    setUserImage();

                    binding.tvcopy.setOnClickListener(v -> {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("refercode", binding.tvrefercode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ProfileActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                    });
                }

                binding.pd.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProfileRoot> call, Throwable t) {
                binding.pd.setVisibility(View.GONE);
            }
        });

    }

    private void setUserImage() {
        Log.d(TAG, "setUserImage: " + sessionManager.getStringValue(Const.PROFILE_IMAGE));
        Glide.with(getApplicationContext())
                .load(sessionManager.getStringValue(Const.PROFILE_IMAGE)).error(R.drawable.dbg_whitebtnroundddd)
                .placeholder(R.drawable.dbg_whitebtnroundddd)
                .circleCrop()
                .into(binding.imgprofile);
        if (sessionManager.getStringValue(Const.PROFILE_IMAGE).equals("null")) {
            Log.d(TAG, "setUserImage:22 " + sessionManager.getStringValue(Const.PROFILE_IMAGE));
            binding.profilechar.setVisibility(View.VISIBLE);
            binding.profilechar.setText(String.valueOf(user.getFname().charAt(0)).toUpperCase());
        }
    }



    public void onClickBack(View view) {
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

    public void onClickCoin(View view) {
        startActivity(new Intent(this, EarnActivity.class));
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
}