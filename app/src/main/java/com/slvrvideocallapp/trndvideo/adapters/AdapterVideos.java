package com.slvrvideocallapp.trndvideo.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.gson.Gson;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy;
import com.slvrvideocallapp.trndvideo.activity.VideoActivity;
import com.slvrvideocallapp.trndvideo.databinding.ItemVideoBinding;
import com.slvrvideocallapp.trndvideo.models.ThumbRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.interstitialAd;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.mInterstitialAdr;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.requestNewInterstitial;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_APP_ID_AD_UNIT_ID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNER;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVEID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE_ADS_ENABLED;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE_ADS_FREQUENCY;

public class AdapterVideos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ADTYPE = 1;
    private static final int VIEWTYPE = 2;
    public List<Object> data = new ArrayList<>();
    private Context context;
    private String cid;
    private int adPosition = 5;
    private int adSetPos = 0;

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

    //fb
    private LinearLayout nativeAdContainer;

    private NativeAd nativeAdfb;

    UnifiedNativeAdView adView  ;

    private final int ITEM_TYPE_VIDEO= 13;
    private final int ITEM_TYPE_AD = 1;

    public AdapterVideos(String cid,Context context) {

        this.cid = cid;
        this.context=context;
        AudienceNetworkAds.initialize(context);


        prf = new PrefManager(context);

        if(prf.getString(TAG_APP_ID_AD_UNIT_ID) != "")
            MobileAds.initialize(context, prf.getString(TAG_APP_ID_AD_UNIT_ID));


        if(prf.getString(TAG_BANNER).equalsIgnoreCase("admob") || prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            adRequest = new AdRequest.Builder().build();
        }


    }

    @Override
    public int getItemViewType(int position) {
        int count=Integer.parseInt(prf.getString(TAG_NATIVE_ADS_FREQUENCY));
        int div=(position+1)%count;
        if(div==0 && position!=0){
            return ITEM_TYPE_AD;
        }
        else {
            return ITEM_TYPE_VIDEO;
        }
    }
    View nativeLayout;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE_VIDEO){
            context = parent.getContext();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new VideoViewHolder(view);

        }else{

            nativeLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_lay_grid,parent,false);
            return new NativeAdViewHolder(nativeLayout);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, int position) {
            if(holderMain instanceof VideoViewHolder){
                VideoViewHolder videoViewHolder = (VideoViewHolder) holderMain;
                videoViewHolder.setData(position);
            }else{
                NativeAdViewHolder holder = (NativeAdViewHolder) holderMain;
                newreleasenative(context,holder);
            }

    }

    public void addData(List<ThumbRoot.Datum> list) {
        for (int i = 0; i < list.size(); i++) {
            this.data.add(list.get(i));
            notifyItemInserted(list.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ItemVideoBinding binding;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemVideoBinding.bind(itemView);
        }

        public void setData(int position) {

            Random rand = new Random();
            int randInt1 = rand.nextInt((9999 - 999) + 1) + 999;

            double coin;
            if (randInt1 >= 1000) {
                coin = (double) randInt1 / 1000;
                DecimalFormat df = new DecimalFormat("#.##");

                binding.tvCoins.setText(String.valueOf(df.format(coin)).concat("K"));
            } else {
                coin = randInt1;
                binding.tvCoins.setText(String.valueOf(coin));
            }
            Log.d("TAG", "onBindViewHolder: " + randInt1);
            Log.d("TAG", "onBindViewHolder: " + coin);

            ThumbRoot.Datum datum = (ThumbRoot.Datum) data.get(position);
            Glide.with(context.getApplicationContext())
                    .load(new SessionManager(context).getStringValue(Const.IMAGE_URL) + datum.getImage())
                    .into(binding.imgThumb);


            String name = datum.getName().substring(0, 1).toUpperCase().concat(datum.getName().substring(1));
            binding.tvName.setText(name);


            ScaleAnimation btnAnimation = new ScaleAnimation(1f, 0.8f, // Start and end values for the X axis scaling
                    1f, 0.8f,
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f);
            btnAnimation.setDuration(2000); //1 second duration for each animation cycle
            btnAnimation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
            btnAnimation.setFillAfter(true);
            btnAnimation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
            binding.livebtn.startAnimation(btnAnimation);

            try {
                binding.imgThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                context.startActivity(new Intent(context, VideoActivity.class).putExtra("cid", cid).putExtra("model", new Gson().toJson(datum)));
                                            }
                                        });
                                    } else {
                                        context.startActivity(new Intent(context, VideoActivity.class).putExtra("cid", cid).putExtra("model", new Gson().toJson(datum)));
                                    }
                                } else {
                                    context.startActivity(new Intent(context, VideoActivity.class).putExtra("cid", cid).putExtra("model", new Gson().toJson(datum)));
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

                                            context.startActivity(new Intent(context, VideoActivity.class).putExtra("cid", cid).putExtra("model", new Gson().toJson(datum)));
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
                                    context.startActivity(new Intent(context, VideoActivity.class).putExtra("cid", cid).putExtra("model", new Gson().toJson(datum)));
                                }
                            } else {
                                context.startActivity(new Intent(context, VideoActivity.class).putExtra("cid", cid).putExtra("model", new Gson().toJson(datum)));
                            }
                        } else {
                            context.startActivity(new Intent(context, VideoActivity.class).putExtra("cid", cid).putExtra("model", new Gson().toJson(datum)));
                        }
                    }
                });
            } catch (Exception e) {
                Log.d("TAG", "setData: cresh inter " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    class NativeAdViewHolder extends RecyclerView.ViewHolder{
        FrameLayout frameLayout;
        UnifiedNativeAd nativeAd ;

        public NativeAdViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


    private void newreleasenative(Context context, NativeAdViewHolder holder) {
        if (prf.getString(TAG_NATIVE_ADS_ENABLED).equalsIgnoreCase("yes")){

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("admob")) {
                holder.frameLayout= nativeLayout.findViewById(R.id.fl_adplaceholderr);
                AdLoader.Builder builder = new AdLoader.Builder(context, prf.getString(TAG_NATIVEID));

                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    // OnUnifiedNativeAdLoadedListener implementation.
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

//                        pDialog.setVisibility(View.GONE);

                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak
                        holder.nativeAd = unifiedNativeAd;

                        UnifiedNativeAdView adView = (UnifiedNativeAdView)LayoutInflater.from(context)
                                .inflate(R.layout.ad_unified_grid, null);
                        populateUnifiedNativeAdView(holder.nativeAd, adView);
                        holder.frameLayout.removeAllViews();
                        holder.frameLayout.addView(adView);
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
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
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
        adView.getAdvertiserView().setVisibility(View.GONE);
        adView.getStarRatingView().setVisibility(View.GONE);
        adView.getPriceView().setVisibility(View.GONE);
        adView.getStoreView().setVisibility(View.GONE);
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


}
