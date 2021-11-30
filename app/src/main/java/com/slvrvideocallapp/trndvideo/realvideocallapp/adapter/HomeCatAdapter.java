package com.slvrvideocallapp.trndvideo.realvideocallapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.realvideocallapp.models.HomeCat;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import java.util.List;
import java.util.Random;

import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVEID;

public class HomeCatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyVideoAdapterTwo";

    //Prefrance
    private static PrefManager prf;

    private Activity activity;

    public Context context;
    private OnItemClickListener listener;
    private List<HomeCat> dataList;

    // private ArrayList<UnifiedNativeAd> unifiedNativeAdArrayList;


    public HomeCatAdapter(Activity activity, Context context, List<HomeCat> dataList,
                          OnItemClickListener listener) {
        this.activity = activity;
        this.context = context;
        this.listener = listener;
        this.dataList = dataList;
        //  this.unifiedNativeAdArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        prf = new PrefManager(activity);

        switch (viewType) {
            case 0: {
                View v0 = inflater.inflate(R.layout.item_home_cat, parent, false);
                viewHolder = new HomeCatViewHolder(v0);
                break;
            }
            case 1: {
                View v1  = inflater.inflate(R.layout.item_admob_native_ads, parent, false);
                viewHolder = new AdmobNativeHolder(v1);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 0:
            HomeCat homeCat = dataList.get(position);

            if (homeCat.getItemId()!=null) {

                final HomeCatViewHolder holderd = (HomeCatViewHolder) holder;

                holderd.room_name_txt.setText("Room "+(position+1));
                holderd.country_desc_txt.setText(homeCat.getCountryName()+"  -  "+homeCat.getItemDesc());

                final int random = new Random().nextInt(3700) + 800;

                Glide.with(context)
                        .load(homeCat.getItemImage())
    //                        .skipMemoryCache(true)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                .placeholder(new ColorDrawable(context.getResources().getColor(R.color.yellow))).centerCrop())
                        .into(holderd.country_iv);



                holderd.online_txt.setText(String.valueOf(random)+" online");

                holderd.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(position,homeCat,v);
                    }
                });

            }
                break;
            case 1:
                final AdmobNativeHolder holder_admob = (AdmobNativeHolder) holder;

                holder_admob.adLoader.loadAd(new AdRequest.Builder().build());

                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if(dataList.get(position).getItemId() != null){
            type = 0;
        }
        if (dataList.get(position).getViewType() == 1){
            type = 1;
        }
        return type;
    }

    public interface OnItemClickListener {
        void onItemClick(int postion, HomeCat item, View view);
    }




    // video view holder
    class HomeCatViewHolder extends RecyclerView.ViewHolder {


        ImageView country_iv;

        TextView room_name_txt,country_desc_txt,online_txt;


        public HomeCatViewHolder(View view) {
            super(view);

            country_iv = view.findViewById(R.id.country_iv);
            room_name_txt = view.findViewById(R.id.room_name_txt);
            country_desc_txt = view.findViewById(R.id.country_desc_txt);
            online_txt = view.findViewById(R.id.online_txt);

        }




    }

    public class AdmobNativeHolder extends HomeCatViewHolder {
        private final AdLoader adLoader;
        private UnifiedNativeAd nativeAd;
        private FrameLayout frameLayout;

        public AdmobNativeHolder(@NonNull View itemView) {
            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.fl_adplaceholder);
            AdLoader.Builder builder = new AdLoader.Builder(activity, prf.getString(TAG_NATIVEID));

            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                // OnUnifiedNativeAdLoadedListener implementation.
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;

                    UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                            .inflate(R.layout.ad_unifiedrecycl, null);
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

            this.adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {


                }
            }).build();

        }
    }

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
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




}
