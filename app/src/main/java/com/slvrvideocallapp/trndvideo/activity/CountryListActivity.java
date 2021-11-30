package com.slvrvideocallapp.trndvideo.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.adapters.CountryListAdapter;
import com.slvrvideocallapp.trndvideo.models.CountryRoot;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitService;
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
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;

public class CountryListActivity extends AppCompatActivity {
    RecyclerView rvCountrylist;
    CountryListAdapter countryListAdapter;

    RetrofitService service;
    private List<CountryRoot.Datum> countries;
    List<CountryRoot.Datum> finelCountry = new ArrayList<>();
    CountryRoot.Datum tempobj;


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
        setContentView(R.layout.activity_country_list);
        rvCountrylist=findViewById(R.id.rvCountrylist);
        rvCountrylist.setLayoutManager(new LinearLayoutManager(this));

        service = RetrofitBuilder.create(this);
        getCountryList();


        context = getApplicationContext();

        prf = new PrefManager(context);

        //ads
        // Initialize the Mobile Ads SDK.
        if(prf.getString(TAG_APP_ID_AD_UNIT_ID) != "")
            MobileAds.initialize(this, prf.getString(TAG_APP_ID_AD_UNIT_ID));


        if(prf.getString(TAG_BANNER).equalsIgnoreCase("admob") || prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            adRequest = new AdRequest.Builder().build();
        }

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
                        } else {
                            finelCountry.add(countries.get(i));
                        }
                    }
                    if (tempobj != null) {
                        finelCountry.add(0, tempobj);
                    }


                    countryListAdapter=new CountryListAdapter(finelCountry,CountryListActivity.this);
                    rvCountrylist.setAdapter(countryListAdapter);
                    rvCountrylist.setHasFixedSize(true);
                }
            }

            @Override
            public void onFailure(Call<CountryRoot> call, Throwable t) {
//ll
            }
        });


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
}