package com.slvrvideocallapp.trndvideo.realvideocallapp.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy;
import com.slvrvideocallapp.trndvideo.realvideocallapp.adapter.ItemAdapter;
import com.slvrvideocallapp.trndvideo.realvideocallapp.apprtc.ConnectActivity;
import com.slvrvideocallapp.trndvideo.realvideocallapp.models.CatItem;
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

public class CountryUsersActivity extends AppCompatActivity {

    private static final String TAG = "CountryUsersActivity";
    private Context context =this;

    // intent data
    String countryName,countryIv,countryId;
    TextView country_name_txt;
    ImageView country_iv;


    private RecyclerView userRv;
    ItemAdapter itemAdapter;
    List<CatItem> catItemList= new ArrayList<>();

    private Toolbar toolbar;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);

       /* new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View menuItemView = findViewById(R.id.report);

                menuItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("xxxxxxxxxxx","kelected");
                    }
                });
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.e("xxxxxxxxxxx","kelected");
        switch (item.getItemId()) {
            case R.id.report:
Log.e("xxxxxxxxxxx","selected");

                final AlertDialog.Builder alert = new AlertDialog.Builder(CountryUsersActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);

                LinearLayout li_porn =(LinearLayout)mView.findViewById(R.id.li_porn);
                LinearLayout li_fake =(LinearLayout)mView.findViewById(R.id.li_fake);
                LinearLayout li_cheat =(LinearLayout)mView.findViewById(R.id.li_cheat);
                LinearLayout li_lang =(LinearLayout)mView.findViewById(R.id.li_lang);
                LinearLayout li_other =(LinearLayout)mView.findViewById(R.id.li_other);
                LinearLayout li_cancel =(LinearLayout)mView.findViewById(R.id.li_cancel);

                LinearLayout li_mail =(LinearLayout)mView.findViewById(R.id.li_mail);
                EditText mail_text=(EditText)mView.findViewById(R.id.edit_gmail) ;
                Button send_mail=(Button)mView.findViewById(R.id.send_mail) ;


                //LinearLayout li_porn =(LinearLayout)mView.findViewById(R.id.li_porn);
               /* final EditText txt_inputText = (EditText)mView.findViewById(R.id.txt_input);
                Button btn_cancel = (Button)mView.findViewById(R.id.btn_cancel);
                Button btn_okay = (Button)mView.findViewById(R.id.btn_okay);*/
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                alertDialog.show();
                li_porn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        li_mail.setVisibility(View.VISIBLE);
                        mail_text.requestFocus();
                        send_mail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(mail_text.getText().toString()!=null){
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "abc@gmail.com"});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "Pornographic");
                                    email.putExtra(Intent.EXTRA_TEXT, mail_text.getText().toString());

//need this to prompts email client only
                                    email.setType("message/rfc822");

                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                    alertDialog.cancel();

                                }else{
                                    Toast.makeText(getApplicationContext(),"Please Write Review.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });
                li_fake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        li_mail.setVisibility(View.VISIBLE);
                        mail_text.requestFocus();
                        send_mail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(mail_text.getText().toString()!=null){
                                    Intent email =  new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "abc@gmail.com"});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "Fake Gender.");
                                    email.putExtra(Intent.EXTRA_TEXT, mail_text.getText().toString());

//need this to prompts email client only
                                    email.setType("message/rfc822");

                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                    alertDialog.cancel();

                                }else{
                                    Toast.makeText(getApplicationContext(),"Please Write Review.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                       // Toast.makeText(getApplicationContext(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
                        //alertDialog.cancel();

                    }
                });

                li_cheat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        li_mail.setVisibility(View.VISIBLE);
                        mail_text.requestFocus();
                        send_mail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(mail_text.getText().toString()!=null){
                                    Intent email =  new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "abc@gmail.com"});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "Cheating");
                                    email.putExtra(Intent.EXTRA_TEXT, mail_text.getText().toString());

//need this to prompts email client only
                                    email.setType("message/rfc822");

                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                    alertDialog.cancel();

                                }else{
                                    Toast.makeText(getApplicationContext(),"Please Write Review.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });
                li_lang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        li_mail.setVisibility(View.VISIBLE);
                        mail_text.requestFocus();
                        send_mail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(mail_text.getText().toString()!=null){
                                    Intent email =  new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "abc@gmail.com"});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "Language Violence");
                                    email.putExtra(Intent.EXTRA_TEXT, mail_text.getText().toString());

//need this to prompts email client only
                                    email.setType("message/rfc822");
                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                    alertDialog.cancel();

                                }else{
                                    Toast.makeText(getApplicationContext(),"Please Write Review.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });
                li_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        li_mail.setVisibility(View.VISIBLE);
                        mail_text.requestFocus();
                        send_mail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(mail_text.getText().toString()!=null){
                                    Intent email =  new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "abc@gmail.com"});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "Others Issue");
                                    email.putExtra(Intent.EXTRA_TEXT, mail_text.getText().toString());

//need this to prompts email client only
                                    email.setType("message/rfc822");

                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                    alertDialog.cancel();

                                }else{
                                    Toast.makeText(getApplicationContext(),"Please Write Review.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });
                li_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
                        alertDialog.cancel();

                    }
                });
                // Do whatever you want to do on logout click.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_users);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.user_menu);
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

        country_name_txt = findViewById(R.id.country_name_txt);
        country_iv = findViewById(R.id.country_iv);

        if (getIntent()!=null && getIntent().getStringExtra("country_name")!=null &&
                getIntent().getStringExtra("country_iv")!=null &&
                getIntent().getStringExtra("country_id")!=null){
            countryName = getIntent().getStringExtra("country_name");
            countryIv = getIntent().getStringExtra("country_iv");
            countryId = getIntent().getStringExtra("country_id");
            Log.e(TAG, "onCreate: country name "+countryName+" iv "+countryIv +" id "+countryId);
            country_name_txt.setText(countryName);
            Glide.with(context)
                    .load(countryIv)
//                        .skipMemoryCache(true)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                            .placeholder(new ColorDrawable(context.getResources().getColor(R.color.yellow))).centerCrop())
                    .into(country_iv);

        }

        userRv = findViewById(R.id.user_rv);
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        userRv.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(CountryUsersActivity.this, context, catItemList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, CatItem item, View view) {

                Intent connectingIntent = new Intent(context, ConnectActivity.class);
                connectingIntent.putExtra("catItem",item);

                Intent loginIntent = new Intent(context,LoginActivity.class);
                loginIntent.putExtra("country_id",countryId);
                loginIntent.putExtra("country_name",countryName);
                loginIntent.putExtra("country_iv",countryIv);
                loginIntent.putExtra("catItem",item);
                loginIntent.putExtra("from","user");

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
                                            startActivity(connectingIntent);
                                        }
                                        else {
                                            startActivity(loginIntent);
                                        }
                                    }
                                });
                            } else {
                                if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                                    startActivity(connectingIntent);
                                }
                                else {
                                    startActivity(loginIntent);
                                }
                            }
                        } else {
                            if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                                startActivity(connectingIntent);
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
                                        startActivity(connectingIntent);
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
                                startActivity(connectingIntent);
                            }
                            else {
                                startActivity(loginIntent);
                            }
                        }
                    } else {
                        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                            startActivity(connectingIntent);
                        }
                        else {
                            startActivity(loginIntent);
                        }
                    }
                } else {
                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                        startActivity(connectingIntent);
                    }
                    else {
                        startActivity(loginIntent);
                    }
                }

            }
        });

        userRv.setAdapter(itemAdapter);

        callItemList();
    }

    private void callItemList() {

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("cat_id",countryId);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "callItemList: error parameter "+e.getMessage() );
        }

        Log.d("resp parameter ", parameters.toString());

        ApiRequest.Call_Api(context, Variables.get_items, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_cat_data(resp);
            }
        });



    }

    private void parse_cat_data(String responce) {
        Log.e(TAG, "parse_cat_data: responce "+responce );
        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                //Log.e(TAG, "Parse_data: click action time is"+jsonObject.optString("action_time") );
                //   String time  = jsonObject.optString("action_time");
                // Log.e(TAG, "Parse_data: click action time is "+time);
                // Variables.action_time = Integer.parseInt(time);
                JSONArray msgArray = jsonObject.getJSONArray("msg");

                ArrayList<CatItem> temp_list=new ArrayList();

                item = 0;
                item_max = 1;
                for (int i = 0; i < msgArray.length(); i++) {

                    JSONObject itemData = msgArray.optJSONObject(i);
                    CatItem catItem = new CatItem();
                    catItem.itemId = itemData.optString("id");
                    catItem.itemName = itemData.optString("item_name");
                    catItem.itemIv = itemData.optString("item_iv");
                    catItem.videoUrl = itemData.optString("video_url");
                    catItem.videoEnable = itemData.optString("video_enable");

                    //  Log.e(TAG, "Parse_data: total_pages are from server "+totalPages );

                    temp_list.add(catItem);

                    //native ads
                    if (prf.getString(TAG_NATIVE_ADS_ENABLED).equalsIgnoreCase("yes")){
                        item++;
                        if (item == Integer.parseInt(prf.getString(TAG_NATIVE_ADS_FREQUENCY)) && item_max < Integer.parseInt(prf.getString(TAG_NATIVE_ADS_FREQUENCY_MAX))){
                            item= 0;
                            item_max++;
                            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("fb")) {
                                temp_list.add(new CatItem().setViewType(2));
                            }else if (prf.getString(TAG_NATIVE).equalsIgnoreCase("admob")){
                                System.out.println("Rajan_admob_native_1");
                                temp_list.add(new CatItem().setViewType(1));
                            }
                        }
                    }
                }

                if (!temp_list.isEmpty()) {
                    catItemList.addAll(temp_list);
                    //  Log.e(TAG, "Parse_data: " );
                    itemAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
}