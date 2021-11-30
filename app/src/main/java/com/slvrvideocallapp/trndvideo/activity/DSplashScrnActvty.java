package com.slvrvideocallapp.trndvideo.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.auth.AuthMethod;
import com.anchorfree.partner.api.response.User;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.HydraTransportConfig;
import com.anchorfree.sdk.NotificationConfig;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.TransportConfig;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.UnifiedSDKConfig;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.callbacks.TrafficListener;
import com.anchorfree.vpnsdk.callbacks.VpnStateListener;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.firebase.messaging.FirebaseMessaging;
import com.northghost.caketube.CaketubeTransport;
import com.northghost.caketube.OpenVpnTransportConfig;
import com.slvrvideocallapp.trndvideo.AppOpenManager;
import com.slvrvideocallapp.trndvideo.ApplicationCls;
import com.slvrvideocallapp.trndvideo.BuildConfig;
import com.slvrvideocallapp.trndvideo.ClearService;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.models.OwnAdsRoot;
import com.slvrvideocallapp.trndvideo.models.SettingsRoot;
import com.slvrvideocallapp.trndvideo.models.ThumbRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;
import com.slvrvideocallapp.trndvideo.utils.JSONParser;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DSplashScrnActvty extends AppCompatActivity implements TrafficListener, VpnStateListener {
    private static final String TAG = "spppp";
    SessionManager sessionManager;
    boolean adloded = false;
    boolean ownAdLoded = false;
    OwnAdsRoot.Datum ownAd;
    boolean settingLoded = false;
    private boolean finish = false;
    private String sabNam;
    private AlertDialog aleart;
    private boolean fetched = false;
    private boolean isnotification;
    private String countryid;
    private ThumbRoot.Datum datum;

    //Prefrance
    public static PrefManager prf;

    // Building Parameters
    Map<String, String> params = new HashMap<>();

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    // url to get all products list
    private static final String url = "https://tiktok.myappadmin.xyz/videocallnew/"+"videocall.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";

    //user
    public static final String TAG_PKG = "pkg";
    public static final String TAG_NEWVERSION = "newversion";
    public static final String TAG_NEWAPPURL = "newappurl";
    public static final String TAG_NEWAPPURL_VIDEO = "newappurl_video";
    public static final String TAG_NEWAPPURL_REPORT = "newappurl_report";

    public static final String TAG_APP_ID_AD_UNIT_ID = "app_id_ad_unit_id";
    public static final String TAG_BANNER = "banner";
    public static final String TAG_BANNERMAIN = "bannermain";
    public static final String TAG_BANNERMAINS = "bannermains";
    public static final String TAG_BANNERMAINR = "bannermainr";
    public static final String TAG_BANNERMAINRS = "bannermainrs";

    public static final String TAG_INTERSTITIAL = "interstitial";
    public static final String TAG_INTERSTITIALMAIN = "interstitialmain";
    public static final String TAG_INTERSTITIALMAINS = "interstitialmains";
    public static final String TAG_INTERSTITIALMAINR = "interstitialmainr";
    public static final String TAG_SPLASH = "splash";
    public static final String TAG_INTERSTITIALSPLASH = "interstitialsplash";
    public static final String TAG_INTERSTITIALSPLASHID = "interstitialsplashid";

    public static final String TAG_OPENAPP_ADS_ENABLED = "openapp_ads_enabled";
    public static final String TAG_OPENAPPID = "openappid";

    public static final String TAG_NATIVE_ADS_ENABLED = "native_ads_enabled";
    public static final String TAG_NATIVE = "native";
    public static final String TAG_NATIVEID = "nativeid";
    public static final String TAG_NATIVEIDD = "nativeidd";
    public static final String TAG_NATIVE_ADS_FREQUENCY = "NATIVE_ADS_FREQUENCY";
    public static final String TAG_NATIVE_ADS_FREQUENCY_MAX = "NATIVE_ADS_FREQUENCY_MAX";

    public static final String TAG_ADMOB_INTERSTITIAL_FREQUENCY = "ADMOB_INTERSTITIAL_FREQUENCY";
    public static final String TAG_ADMOB_INTERSTITIAL_FREQUENCY_SWIPE = "ADMOB_INTERSTITIAL_FREQUENCY_SWIPE";
    public static final String TAG_WHATSAPP_SHARE_FREQUENCY = "WHATSAPP_SHARE_FREQUENCY";

    private int success = 0;
    private String newversion;

    // ads
//    private AdView mAdView;
    private AdRequest adRequest;
    private static com.google.android.gms.ads.InterstitialAd mInterstitialAdr;

    //facebookads
//    private static final String TAG = DisplayUserActivity.class.getSimpleName();

    private static InterstitialAd interstitialAd;

    private Intent i;

    private static AppOpenManager appOpenManager;
    public AppOpenAd appOpenAd;

    public static String main_domain="rrrrr";

    //vpn
    private static final String CHANNEL_ID = "vpn";
    UnifiedSDK unifiedSDK;
    private boolean loginstatus;
    private String selectedCountry = "us";

    private NotificationManager notificationManager;
    public static final String NOTIFICATION_CHANNEL_ID = "4655";
    public static final String NOTIFICATION_CHANNEL_NAME = "NAME4655";

    private Map<String, String> queryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.dactivity_splshee);
        sessionManager = new SessionManager
                (this);

        AudienceNetworkAds.initialize(this);

        prf = new PrefManager(DSplashScrnActvty.this);

        i = new Intent(DSplashScrnActvty.this, DFirstActivityyy.class);

//        sessionManager.saveStringValue(Const.BASE_URL, BuildConfig.TMDB_API_KEY);
//        sessionManager.saveStringValue(Const.IMAGE_URL, BuildConfig.TMDB_API_KEY);

        try {
            startService(new Intent(getBaseContext(), ClearService.class));
            checkInstallReferrer();
            checkcntry();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new BackgroundSplashTask().execute();

    }

    private void checkcntry() {

        String locale = "none";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = getResources().getConfiguration().getLocales().get(0).getCountry();
            } else {
                locale = getResources().getConfiguration().locale.getCountry();
            }
            params.put("cntry", locale);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Rajan_cntry"+locale);
    }

    private void checkInstallReferrer()  {

        InstallReferrerClient referrerClient;

        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();
                        } catch (RemoteException e) {
//                            System.out.println("Rajan_ex"+e.getMessage());
                            e.printStackTrace();
                        }

                        String referrerUrl = response.getInstallReferrer();
//                        long referrerClickTime = response.getReferrerClickTimestampSeconds();
//                        long appInstallTime = response.getInstallBeginTimestampSeconds();
//                        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        try {
                            params.put("referrerUrl", referrerUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (referrerUrl != null) {
                            queryMap = getQueryMap(referrerUrl);

                            Iterator myVeryOwnIterator = queryMap.keySet().iterator();
                            while (myVeryOwnIterator.hasNext()) {
                                String key = (String) myVeryOwnIterator.next();
                                String value = (String) queryMap.get(key);
                                System.out.println("Rajan_" + "Key: " + key + " Value: " + value);
                                try {
                                    params.put(key, value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Rajan_referrerUrl" + referrerUrl);
//                        Toast.makeText(DSplashScrnActvty.this, referrerUrl, Toast.LENGTH_LONG).show();
                        }

                        referrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        System.out.println("Rajan_FEATURE_NOT_SUPPORTED");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        System.out.println("Rajan_SERVICE_UNAVAILABLE");
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });



    }

    public static Map<String, String> getQueryMap(String query) {
        String[] par = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : par) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    public void initHydraSdk() {
//        createNotificationChannel();

        SharedPreferences prefs = getPrefs();
        ClientInfo clientInfo = ClientInfo.newBuilder()
                .baseUrl(prefs.getString(prf.getString("STORED_HOST_URL_KEY"), prf.getString("BASE_HOST")))
                .carrierId(prefs.getString(prf.getString("STORED_CARRIER_ID_KEY"), prf.getString("BASE_CARRIER_ID")))
                .build();
        List<TransportConfig> transportConfigList = new ArrayList<>();
        transportConfigList.add(HydraTransportConfig.create());
        transportConfigList.add(OpenVpnTransportConfig.tcp());
        transportConfigList.add(OpenVpnTransportConfig.udp());
        UnifiedSDK.update(transportConfigList, CompletableCallback.EMPTY);
        UnifiedSDKConfig config = UnifiedSDKConfig.newBuilder().idfaEnabled(false).build();
        unifiedSDK = UnifiedSDK.getInstance(clientInfo, config);
        NotificationConfig notificationConfig = NotificationConfig.newBuilder()
                .title(getResources().getString(R.string.app_name))
//                .channelId(CHANNEL_ID)
                .build();
        UnifiedSDK.update(notificationConfig);
//        UnifiedSDK.setLoggingLevel(Log.VERBOSE);
    }

    public void setNewHostAndCarrier(String hostUrl, String carrierId) {
        SharedPreferences prefs = getPrefs();
        if (TextUtils.isEmpty(hostUrl)) {
            prefs.edit().remove(prf.getString("STORED_HOST_URL_KEY")).apply();
        } else {
            prefs.edit().putString(prf.getString("STORED_HOST_URL_KEY"), hostUrl).apply();
        }
        if (TextUtils.isEmpty(carrierId)) {
            prefs.edit().remove(prf.getString("STORED_CARRIER_ID_KEY")).apply();
        } else {
            prefs.edit().putString(prf.getString("STORED_CARRIER_ID_KEY"), carrierId).apply();
        }
        initHydraSdk();
    }

    public SharedPreferences getPrefs() {
        return getSharedPreferences(prf.getString("SHARED_PREFS"), Context.MODE_PRIVATE);
    }

    protected void loginToVpn() {
        Log.e(TAG, "login: 1111");
        if(prf.getString("vpn").equalsIgnoreCase("yes")) {
            AuthMethod authMethod = AuthMethod.anonymous();
            UnifiedSDK.getInstance().getBackend().login(authMethod, new com.anchorfree.vpnsdk.callbacks.Callback<User>() {
                @Override
                public void success(@NonNull User user) {
                    //                updateUI();
                    loginstatus = true;
                    connectToVpn();
                }

                @Override
                public void failure(@NonNull VpnException e) {
                    //                updateUI();
                    //                handleError(e);
                    loginstatus = false;
                    startActivity(i);
                }
            });
        } else {
            startActivity(i);
        }
    }

    protected void connectToVpn() {

//        final List<String> apps = new ArrayList<String();
//        apps.add("com.google.chrome");
//        final SessionConfig session = new SessionConfig.Builder()
//                .withVirtualLocation(UnifiedSDK.COUNTRY_OPTIMAL)
//                .withReason(TrackingConstants.GprReasons.M_UI)
//                .appPolicy(AppPolicy.newBuilder().policy(AppPolity.POLICY_FOR_LIST).appList(apps).build())
//                .build();

        if (loginstatus) {
            List<String> fallbackOrder = new ArrayList<>();
            fallbackOrder.add(HydraTransport.TRANSPORT_ID);
            fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_TCP);
            fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_UDP);
//            showConnectProgress();
            List<String> bypassDomains = new LinkedList<>();
            bypassDomains.add("*facebook.com");
            bypassDomains.add("*wtfismyip.com");
            UnifiedSDK.getInstance().getVPN().start(new SessionConfig.Builder()
                    .withReason(TrackingConstants.GprReasons.M_UI)
                    .withTransportFallback(fallbackOrder)
                    .withVirtualLocation(selectedCountry)
                    .withTransport(HydraTransport.TRANSPORT_ID)
                    .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                    .build(), new CompletableCallback() {
                @Override
                public void complete() {
//                    uploading_state_animation.playAnimation();
//                    downloading_state_animation.playAnimation();
//                    hideConnectProgress();
//
//                    startUIUpdateTask();
//                    Toast.makeText(DSplashScrnActvty.this, "RConnected", Toast.LENGTH_LONG).show();
                    startActivity(i);
                }

                @Override
                public void error(@NonNull VpnException e) {
//                    hideConnectProgress();
//                    updateUI();
//                    handleError(e);
                    startActivity(i);
                }
            });
        } else {
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        UnifiedSDK.addTrafficListener(this);
        UnifiedSDK.addVpnStateListener(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        UnifiedSDK.removeVpnStateListener(this);
        UnifiedSDK.removeTrafficListener(this);
    }

    @Override
    public void onTrafficUpdate(long bytesTx, long bytesRx) {
//        updateUI();
//        updateTrafficStats(bytesTx, bytesRx);
    }

    @Override
    public void vpnStateChanged(@NonNull VPNState vpnState) {
//        updateUI();
    }

    @Override
    public void vpnError(@NonNull VpnException e) {
//        updateUI();
//        handleError(e);
    }

    public void ShowAppOpenAds() {
        if (appOpenAd != null) {
            // handler.removeCallbacks(r);
            //isActive = false;
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd = null;

                            if (prf.getString("BASE_CARRIER_ID") != null) {
                                loginToVpn();
                            } else {
                                startActivity(i);
                            }

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            appOpenAd = null;

                            if (prf.getString("BASE_CARRIER_ID") != null) {
                                loginToVpn();
                            } else {
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                        }
                    };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(DSplashScrnActvty.this);
        } else {
            if (prf.getString("BASE_CARRIER_ID") != null) {
                loginToVpn();
            } else {
                startActivity(i);
            }
        }
    }

    private void getCredential() {
        sessionManager.saveStringValue(Const.REWARD_COINS, "200");
        sessionManager.saveStringValue(Const.REWARD_COINS, "200");

        Call<SettingsRoot> call = RetrofitBuilder.create(this).getSettings();
        call.enqueue(new Callback<SettingsRoot>() {
            @Override
            public void onResponse(Call<SettingsRoot> call, Response<SettingsRoot> response) {
                if (response.code() == 200) {
                    if (response != null) {
                        if (response.body().getStatus() == 200 && !response.body().getData().isEmpty()) {
                            if (response.body().getData().get(0).getSecond() != null) {
                                sessionManager.saveStringValue(Const.ADS_TIME, String.valueOf(response.body().getData().get(0).getSecond()));
                            }
                            if (response.body().getData().get(0).getVideoBonus() != null) {
                                sessionManager.saveStringValue(Const.REWARD_COINS, String.valueOf(response.body().getData().get(0).getVideoBonus()));
                            }

                        }
                        settingLoded = true;
                    }

                }
            }

            @Override
            public void onFailure(Call<SettingsRoot> call, Throwable t) {

            }
        });
    }

    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

//            try {
//                Thread.sleep(SPLASH_SHOW_TIME);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            // Building Parameters
            params.put(TAG_PKG, getApplicationContext().getPackageName());
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                params.put("version", version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

//                newversion = json.getString("newversion");
                if(success==1) {
                    // jsonarray found
                    // Getting Array of jsonarray
//                    jsonarray = json.getJSONArray(TAG_RAJANR);
                    prf.setString(TAG_NEWVERSION,json.getString(TAG_NEWVERSION));
                    prf.setString(TAG_NEWAPPURL,json.getString(TAG_NEWAPPURL));
                    main_domain = json.getString("videocallurl");

                    sessionManager.saveStringValue(Const.BASE_URL, main_domain);
                    sessionManager.saveStringValue(Const.IMAGE_URL, main_domain);

                    prf.setString(TAG_APP_ID_AD_UNIT_ID,json.getString(TAG_APP_ID_AD_UNIT_ID));
                    prf.setString(TAG_BANNER,json.getString(TAG_BANNER));
                    prf.setString(TAG_BANNERMAIN,json.getString(TAG_BANNERMAIN));
                    prf.setString(TAG_BANNERMAINS,json.getString(TAG_BANNERMAINS));
                    prf.setString(TAG_BANNERMAINR,json.getString(TAG_BANNERMAINR));
                    prf.setString(TAG_BANNERMAINRS,json.getString(TAG_BANNERMAINRS));
                    prf.setString(TAG_INTERSTITIAL,json.getString(TAG_INTERSTITIAL));
                    prf.setString(TAG_INTERSTITIALMAIN,json.getString(TAG_INTERSTITIALMAIN));
                    prf.setString(TAG_INTERSTITIALMAINS,json.getString(TAG_INTERSTITIALMAINS));
                    prf.setString(TAG_INTERSTITIALMAINR,json.getString(TAG_INTERSTITIALMAINR));
                    prf.setString(TAG_SPLASH,json.getString(TAG_SPLASH));
                    prf.setString(TAG_INTERSTITIALSPLASH,json.getString(TAG_INTERSTITIALSPLASH));
                    prf.setString(TAG_INTERSTITIALSPLASHID,json.getString(TAG_INTERSTITIALSPLASHID));

                    prf.setString(TAG_OPENAPP_ADS_ENABLED, json.getString(TAG_OPENAPP_ADS_ENABLED));
                    prf.setString(TAG_OPENAPPID, json.getString(TAG_OPENAPPID));

                    prf.setString(TAG_NATIVE_ADS_ENABLED, json.getString(TAG_NATIVE_ADS_ENABLED));
                    prf.setString(TAG_NATIVE, json.getString(TAG_NATIVE));
                    prf.setString(TAG_NATIVEID, json.getString(TAG_NATIVEID));
                    prf.setString(TAG_NATIVEIDD, json.getString(TAG_NATIVEIDD));
                    prf.setString(TAG_NATIVE_ADS_FREQUENCY, json.getString(TAG_NATIVE_ADS_FREQUENCY));
                    prf.setString(TAG_NATIVE_ADS_FREQUENCY_MAX, json.getString(TAG_NATIVE_ADS_FREQUENCY_MAX));

                    prf.setString("skipfirstscreen",json.getString("skipfirstscreen"));

                    prf.setString("realvideocall",json.getString("realvideocall"));

                    prf.setString("vpn",json.getString("vpn"));
                    selectedCountry = json.getString("selectedCountry");

                    prf.setString("BASE_HOST",json.getString("BASE_HOST"));
                    prf.setString("BASE_CARRIER_ID",json.getString("BASE_CARRIER_ID"));
                    prf.setString("BASE_OAUTH_METHOD",json.getString("BASE_OAUTH_METHOD"));
                    prf.setString("SHARED_PREFS",json.getString("SHARED_PREFS"));
                    prf.setString("STORED_HOST_URL_KEY",json.getString("STORED_HOST_URL_KEY"));
                    prf.setString("STORED_CARRIER_ID_KEY",json.getString("STORED_CARRIER_ID_KEY"));

                    prf.setString(TAG_ADMOB_INTERSTITIAL_FREQUENCY,json.getString(TAG_ADMOB_INTERSTITIAL_FREQUENCY));
                    prf.setString(TAG_WHATSAPP_SHARE_FREQUENCY,json.getString(TAG_WHATSAPP_SHARE_FREQUENCY));

                    prf.setString("SUBSCRIBED","FALSE");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = tm.getSimCountryIso();
            Log.d("TAG", "onCreate: ttt " + countryCode);

            String localeCountry = tm.getSimCountryIso();
            Log.d("clll", "onCreate: " + localeCountry);
            Locale loc = new Locale("en", localeCountry);
            Log.d("TAG", "onCreate: finel " + loc.getDisplayCountry());
            Log.d("TAG", "onCreate: finel " + loc.getCountry());
            Log.d("TAG", "onCreate: finel  eee  " + loc.getDisplayCountry(loc));
            sessionManager.saveStringValue(Const.Country, loc.getDisplayCountry(loc));


            FirebaseMessaging.getInstance().subscribeToTopic("Users")
                    .addOnCompleteListener(task -> Log.d("nottttt", "onComplete: done" + task.toString()));
            FirebaseMessaging.getInstance().subscribeToTopic("coma")
                    .addOnCompleteListener(task -> Log.d("nottttt", "onComplete: done" + task.toString()));
            FirebaseMessaging.getInstance().subscribeToTopic("test")
                    .addOnCompleteListener(task -> Log.d("nottttt", "onComplete: done" + task.toString()));
            FirebaseMessaging.getInstance().subscribeToTopic("urvi")
                    .addOnCompleteListener(task -> Log.d("nottttt", "onComplete: done" + task.toString()));


            Intent intent = getIntent();
            if (intent != null) {
                Bundle b = intent.getExtras();
                if (b != null) {
                    Set<String> keys = b.keySet();
                    for (String key : keys) {

                        String cid = String.valueOf(getIntent().getExtras().get("countryid"));
                        String thumb = String.valueOf(getIntent().getExtras().get("thumb"));
                        String name = String.valueOf(getIntent().getExtras().get("name"));
                        Log.d("notificationData", "Bundle Contains: key=" + cid);
                        Log.d("notificationData", "Bundle Contains: key=" + thumb);
                        Log.d("notificationData", "Bundle Contains: key=" + name);
                        if (cid != null && !cid.equals("") && !cid.equals("null")) {
                            isnotification = true;
                            countryid = cid;
                            datum = new ThumbRoot.Datum();
                            datum.setImage(thumb);
                            datum.setName(name);
                        } else {
                            isnotification = false;
                        }

                    }
                } else {
                    Log.w("notificationData", "onCreate: BUNDLE is null");
                }
            } else {
                Log.w("notificationData", "onCreate: INTENT is null");
            }

            getCredential();

            //vpn
            if (prf.getString("BASE_CARRIER_ID") != null) {
                initHydraSdk();
            }


            if(prf.getString(TAG_OPENAPP_ADS_ENABLED).equalsIgnoreCase("yes")) {
                appOpenManager = new AppOpenManager((ApplicationCls) ApplicationCls.getContext());
            }

            if(prf.getString(TAG_INTERSTITIALSPLASH).equalsIgnoreCase("yes")) {
                try {

                    if(prf.getString(TAG_SPLASH).equalsIgnoreCase("openapp")) {
                        if (prf.getString(TAG_OPENAPP_ADS_ENABLED).equalsIgnoreCase("yes")) {
                            appOpenManager = new AppOpenManager((ApplicationCls) ApplicationCls.getContext());

                            AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                                @Override
                                public void onAdLoaded(AppOpenAd ad) {
                                    appOpenAd = ad;
                                    ShowAppOpenAds();
                                }

                                @Override
                                public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    ShowAppOpenAds();
                                }
                            };
                            AdRequest request = new AdRequest.Builder().build();
                            AppOpenAd.load(DSplashScrnActvty.this, prf.getString(TAG_OPENAPPID),
                                    request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
                        }
                    } else if (prf.getString(TAG_SPLASH).equalsIgnoreCase("interstitial")) {
                        //ads
                        // Initialize the Mobile Ads SDK.
                        if (prf.getString(TAG_APP_ID_AD_UNIT_ID) != "")
                            MobileAds.initialize(DSplashScrnActvty.this, prf.getString(TAG_APP_ID_AD_UNIT_ID));

                        if (prf.getString(TAG_BANNER).equalsIgnoreCase("admob") || prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
                            adRequest = new AdRequest.Builder().build();
                        }

                        if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
                            mInterstitialAdr = new com.google.android.gms.ads.InterstitialAd(DSplashScrnActvty.this);
                            mInterstitialAdr.setAdUnitId(prf.getString(TAG_INTERSTITIALSPLASHID));

                            mInterstitialAdr.setAdListener(new com.google.android.gms.ads.AdListener() {
                                @Override
                                public void onAdClosed() {
//                            requestNewInterstitial();
                                    // Launch app intro
//                                    startActivity(i);
                                    loginToVpn();
                                }

                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    if (mInterstitialAdr.isLoaded()) {
                                        mInterstitialAdr.show();
                                    }
                                }

                                @Override
                                public void onAdFailedToLoad(LoadAdError adError) {
                                    // Code to be executed when an ad request fails.
                                    // Launch app intro
//                                    startActivity(i);
                                    loginToVpn();
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when the ad is displayed.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }
                            });

                            requestNewInterstitial();
                        }

                        //facebookads
                        // loadAdView();

                        //facebookadsinterstrial
                        if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
                            loadinterstrialAdView();
                        }
                    } else {
//                        startActivity(i);
                        loginToVpn();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                // Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

//                startActivity(i);
                loginToVpn();
            }
        }

    }
    private void requestNewInterstitial() {
        mInterstitialAdr.loadAd(adRequest);
    }

    private void loadinterstrialAdView() {

        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
//        setLabel("Loading interstitial ad...");

        // Create the interstitial unit with a placement ID (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        interstitialAd = new InterstitialAd(this,prf.getString(TAG_INTERSTITIALSPLASHID));

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
//                interstitialAd.loadAd();
                // Launch app intro
//                startActivity(i);
                loginToVpn();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                if (ad == interstitialAd) {
                    System.out.println("Rajan_interstrial"+"Interstitial ad failed to load: " + adError.getErrorMessage());
                    // Launch app intro
//                    startActivity(i);
                    loginToVpn();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                if (ad == interstitialAd) {
                    System.out.println("Rajan_interstrial"+"Ad loaded. Click show to present!");
                    if (interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()) {
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
//                                    interstitialAd.loadAd();

//                                startActivity(i);
                                loginToVpn();
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                // Ad error callback
//                                startActivity(i);
                                loginToVpn();
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
                            }
                        });
                    }else{
//                        startActivity(i);
                        loginToVpn();
                    }
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
        interstitialAd.loadAd();

    }
}