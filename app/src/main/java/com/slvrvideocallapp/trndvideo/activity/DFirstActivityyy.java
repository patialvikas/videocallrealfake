package com.slvrvideocallapp.trndvideo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes.Variables;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_ADMOB_INTERSTITIAL_FREQUENCY;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_APP_ID_AD_UNIT_ID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNER;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNERMAINR;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNERMAINRS;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNERMAINS;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIALMAINR;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVEID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVEIDD;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE_ADS_ENABLED;

public class DFirstActivityyy extends AppCompatActivity {
    Button startCardView;
    private SessionManager sessonManager;


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
    public static com.google.android.gms.ads.InterstitialAd mInterstitialAdr;

    //facebookads
    private LinearLayout bannerAdContainer;
    private com.facebook.ads.AdView bannerAdView, bannerAdViewnew;

    //facebookads
//    private static final String TAG = DisplayUserActivity.class.getSimpleName();

    public static InterstitialAd interstitialAd;

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
        setContentView(R.layout.dactivity_firstttt);
        startCardView= (Button) findViewById(R.id.startCardView);

        sessonManager = new SessionManager(this);
        AudienceNetworkAds.initialize(this);

        context = getApplicationContext();

        prf = new PrefManager(context);

        //realvideocall
        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);
        Variables.user_id = Variables.sharedPreferences.getString(Variables.u_id, "");
        Variables.user_name = Variables.sharedPreferences.getString(Variables.u_name, "");
        Variables.user_pic = Variables.sharedPreferences.getString(Variables.u_pic, "");

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

            mAdView.setAdUnitId(prf.getString(TAG_BANNERMAINRS));
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


        newreleasenative(DFirstActivityyy.this);

        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            mInterstitialAdr = new com.google.android.gms.ads.InterstitialAd(this);
            mInterstitialAdr.setAdUnitId(prf.getString(TAG_INTERSTITIALMAINR));

            mInterstitialAdr.setAdListener(new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }
            });

            requestNewInterstitial();
        }
        startCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
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
                                    public void onAdClosed() {
                                        requestNewInterstitial();
                                        nextActivity();
                                    }
                                });
                            } else {
                                nextActivity();
                            }
                        } else {
                            nextActivity();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {

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

                                    nextActivity();
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
                            nextActivity();
                        }
                    } else {
                        nextActivity();
                    }
                } else {
                    nextActivity();
                }



            }
        });

    }
    public static void requestNewInterstitial() {
//        if (DSplashScrnActvty.prf.getString("rd").contains("1") && DSplashScrnActvty.rajan == 1) {
        if (prf.getString("SUBSCRIBED").equals("FALSE")) {
            mInterstitialAdr.loadAd(adRequest);
        }
//        }
    }

    private void nextActivity(){
        if(sessonManager.getBooleanValue(Const.IS_ENTER_NAME)){
            startActivity(new Intent(DFirstActivityyy.this, DStartActivityyyy.class));
        }else{
            startActivity(new Intent(DFirstActivityyy.this, MaleFemaleActivity.class));
        }
    }


    private void newreleasenative(Activity activity) {
        if (prf.getString(TAG_NATIVE_ADS_ENABLED).equalsIgnoreCase("yes")){

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("admob")) {

                frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholderr);
                AdLoader.Builder builder = new AdLoader.Builder(context, prf.getString(TAG_NATIVEIDD));

                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    // OnUnifiedNativeAdLoadedListener implementation.
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

//                        pDialog.setVisibility(View.GONE);

                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                .inflate(R.layout.ad_unifiedr_frst, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }

                });

                VideoOptions videoOptions = new VideoOptions.Builder()
                        .setStartMuted(true)
                        .build();

                NativeAdOptions adOptions = new NativeAdOptions.Builder()
                        .setVideoOptions(videoOptions)
                        .build();

                builder.withNativeAdOptions(adOptions);

                this.adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError var1) {
//                        pDialog.setVisibility(View.GONE);
//                        addtorrents.setVisibility(View.VISIBLE);

                    }
                }).build();

                if (prf.getString("SUBSCRIBED").equals("FALSE")) {
                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            }

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("fb")) {
                //fb
                nativeAdfb = new NativeAd(context, prf.getString(TAG_NATIVEID));
                nativeAdfb.setAdListener(new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {
                        // Native ad finished downloading all assets
                        System.out.println("Rajan_Native ad finished downloading all assets.");
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Native ad failed to load
                        System.out.println("Rajan_Native ad failed to load: " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Native ad is loaded and ready to be displayed
                        System.out.println("Rajan_Native ad is loaded and ready to be displayed!");
                        // Race condition, load() called again before last ad was displayed
                        if (nativeAdfb == null || nativeAdfb != ad) {
                            return;
                        }
                       /* NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                                .setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark))
                                .setTitleTextColor(Color.WHITE)
                                .setDescriptionTextColor(Color.WHITE)
                                .setButtonColor(Color.WHITE);

                        View adView = NativeAdView.render(activity, nativeAdfb, NativeAdView.Type.HEIGHT_300, viewAttributes);

                        LinearLayout nativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
                        nativeAdContainer.addView(adView);*/
                        // Inflate Native Ad into Container
                        inflateAd(nativeAdfb);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Native ad clicked
                        System.out.println("Rajan_Native ad clicked!");
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Native ad impression
                        System.out.println("Rajan_Native ad impression logged!");
                    }
                });

                if (prf.getString("SUBSCRIBED").equals("FALSE")) {
                    // Request an ad
                    nativeAdfb.loadAd();
                }
            }
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdContainer = findViewById(R.id.native_ad_containerr);
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout_1r, nativeAdContainer, false);
        nativeAdContainer.addView(adView);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }

    public static boolean checkfbAd() {
        if (prf.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        System.out.println("Rajan_fb_counter"+sInterstitialCounter);
        if(prf.getString(TAG_ADMOB_INTERSTITIAL_FREQUENCY) != "") {
            if (Integer.parseInt(prf.getString(TAG_ADMOB_INTERSTITIAL_FREQUENCY)) > 0 && sInterstitialCounter % Integer.parseInt(prf.getString(TAG_ADMOB_INTERSTITIAL_FREQUENCY)) == 0) {
                sInterstitialCounter++;
                return true;
            } else {
                sInterstitialCounter++;
                return false;
            }
        } else {
            if (DFirstActivityyy.ADMOB_INTERSTITIAL_FREQUENCY > 0 && sInterstitialCounter % DFirstActivityyy.ADMOB_INTERSTITIAL_FREQUENCY == 0) {
                sInterstitialCounter++;
                return true;
            } else {
                sInterstitialCounter++;
                return false;
            }
        }
    }

    private void loadinterstrialAdView() {

        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
//        setLabel("Loading interstitial ad...");

        // Create the interstitial unit with a placement ID (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        interstitialAd = new InterstitialAd(DFirstActivityyy.this,prf.getString(TAG_INTERSTITIALMAINR));

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