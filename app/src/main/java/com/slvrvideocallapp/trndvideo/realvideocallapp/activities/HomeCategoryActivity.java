package com.slvrvideocallapp.trndvideo.realvideocallapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy;
import com.slvrvideocallapp.trndvideo.activity.DStartActivityyyy;
import com.slvrvideocallapp.trndvideo.activity.RoomListActivity;
import com.slvrvideocallapp.trndvideo.realvideocallapp.adapter.HomeCatAdapter;
import com.slvrvideocallapp.trndvideo.realvideocallapp.models.HomeCat;
import com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes.ApiRequest;
import com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes.Callback;
import com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes.Variables;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.interstitialAd;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.mInterstitialAdr;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.requestNewInterstitial;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_APP_ID_AD_UNIT_ID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNER;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNERMAINRS;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE_ADS_ENABLED;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE_ADS_FREQUENCY;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_NATIVE_ADS_FREQUENCY_MAX;

public class HomeCategoryActivity extends AppCompatActivity {
    private static final String TAG = "HomeCategoryActivity";
    private Context context =this;

    private RecyclerView catRv;

    HomeCatAdapter homeCatAdapter;
    List<HomeCat> homeCatList= new ArrayList<>();

    ImageView logout_iv;

    //Prefrance
    private static PrefManager prf;

    // ads
    private AdView mAdView, mAdViewnew;
    private static AdRequest adRequest;

    //facebookads
    private LinearLayout bannerAdContainer;

    private static Integer item = 0 ;
    private static Integer item_max = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_category);

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

            mAdView.setAdUnitId(prf.getString(TAG_BANNERMAINRS));
            if (prf.getString("SUBSCRIBED").equals("FALSE")) {
                mAdView.loadAd(adRequest);
            }
            ((LinearLayout) bannerAdContainer).removeAllViews();
            ((LinearLayout) bannerAdContainer).addView(mAdView);
        }

        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);
        Variables.user_id = Variables.sharedPreferences.getString(Variables.u_id, "");
        Variables.user_name = Variables.sharedPreferences.getString(Variables.u_name, "");
        Variables.user_pic = Variables.sharedPreferences.getString(Variables.u_pic, "");


        logout_iv = findViewById(R.id.logout_iv);
        logout();
        catRv = findViewById(R.id.cat_rv);
        catRv.setLayoutManager(new LinearLayoutManager(context));
        homeCatAdapter = new HomeCatAdapter(HomeCategoryActivity.this, context, homeCatList, new HomeCatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, HomeCat item, View view) {

                Log.e(TAG, "onItemClick: item "+item.countryName );
                Intent intent = new Intent(context, CountryUsersActivity.class);
                intent.putExtra("country_id",item.itemId);
                intent.putExtra("country_name",item.getCountryName());
                intent.putExtra("country_iv",item.getItemImage());



                //for loginintent
                Intent loginIntent = new Intent(context,LoginActivity.class);
                loginIntent.putExtra("country_id",item.itemId);
                loginIntent.putExtra("country_name",item.getCountryName());
                loginIntent.putExtra("country_iv",item.getItemImage());
                loginIntent.putExtra("from","category");

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
                                        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                                            startActivity(intent);
                                        }
                                        else {
                                            startActivity(loginIntent);
                                        }
                                    }
                                });
                            } else {
                                if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                                    startActivity(intent);
                                }
                                else {
                                    startActivity(loginIntent);
                                }
                            }
                        } else {
                            if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                                startActivity(intent);
                            }
                            else {
                                startActivity(loginIntent);
                            }
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

                                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                                        startActivity(intent);
                                    }
                                    else {
                                        startActivity(loginIntent);
                                    }
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
                            if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                                startActivity(intent);
                            }
                            else {
                                startActivity(loginIntent);
                            }
                        }
                    } else {
                        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                            startActivity(intent);
                        }
                        else {
                            startActivity(loginIntent);
                        }
                    }
                } else {
                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                        startActivity(intent);
                    }
                    else {
                        startActivity(loginIntent);
                    }
                }

            }
        });

        catRv.setAdapter(homeCatAdapter);

        callhomeCat();
    }

    private void callhomeCat() {

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("home_cat","home_cat");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_categories, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_cat_data(resp);
            }
        });



    }

    private void parse_cat_data(String responce) {
        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                //Log.e(TAG, "Parse_data: click action time is"+jsonObject.optString("action_time") );
                //   String time  = jsonObject.optString("action_time");
                // Log.e(TAG, "Parse_data: click action time is "+time);
                // Variables.action_time = Integer.parseInt(time);
                JSONArray msgArray = jsonObject.getJSONArray("msg");

                ArrayList<HomeCat> temp_list=new ArrayList();

                for (int i = 0; i < msgArray.length(); i++) {

                    JSONObject catData = msgArray.optJSONObject(i);
                    HomeCat homeCat = new HomeCat();
                    homeCat.itemId = catData.optString("id");
                    homeCat.countryName = catData.optString("CategoryName");
                    homeCat.itemDesc = catData.optString("Description");
                    homeCat.itemImage = catData.optString("CategoryImg");

                    //  Log.e(TAG, "Parse_data: total_pages are from server "+totalPages );

                    temp_list.add(homeCat);

                    //native ads
                    if (prf.getString(TAG_NATIVE_ADS_ENABLED).equalsIgnoreCase("yes")){
                        item++;
                        if (item == Integer.parseInt(prf.getString(TAG_NATIVE_ADS_FREQUENCY)) && item_max < Integer.parseInt(prf.getString(TAG_NATIVE_ADS_FREQUENCY_MAX))){
                            item= 0;
                            item_max++;
                            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("fb")) {
                                temp_list.add(new HomeCat().setViewType(2));
                            }else if (prf.getString(TAG_NATIVE).equalsIgnoreCase("admob")){
                                temp_list.add(new HomeCat().setViewType(1));
                            }
                        }
                    }
                }

                if (!temp_list.isEmpty()) {
                    homeCatList.addAll(temp_list);
                    //  Log.e(TAG, "Parse_data: " );
                    homeCatAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logout(){
        logout_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
                editor.putString(Variables.u_id, "").clear();
                editor.putString(Variables.user_name, "").clear();
                editor.putString(Variables.u_pic, "").clear();
                editor.putBoolean(Variables.islogin, false).clear();
                // Google sign out

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                //google Implimentation
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
                mGoogleSignInClient.signOut();

                editor.commit();
                editor.clear();
                FirebaseAuth.getInstance().signOut();

                // Google sign out
                finish();
                startActivity(new Intent(HomeCategoryActivity.this, LoginActivity.class));
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