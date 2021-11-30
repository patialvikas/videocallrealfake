package com.slvrvideocallapp.trndvideo;

import android.app.Application;
import android.content.Context;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;

public class ApplicationCls extends Application {

    private static final String TAG = "BrowserApp";

    //    private static AppOpenManager appOpenManager;

    private static ApplicationCls a;

    public ApplicationCls() {
        a = this;
    }

    public static Context getContext() {
        return a;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        FirebaseApp.initializeApp(this);

//        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
//        status.getPermissionStatus().getEnabled();
//
//        status.getSubscriptionStatus().getSubscribed();
//        status.getSubscriptionStatus().getUserSubscriptionSetting();
//        status.getSubscriptionStatus().getUserId();
//        status.getSubscriptionStatus().getPushToken();
//        OneSignal.setEmail(prf.getString(TAG_EMAIL));

//        if (AudienceNetworkAds.isInAdsProcess(this)) {
//            return;
//        } // else execute default application initialization code

//        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

//        AudienceNetworkInitializeHelper.initialize(this);

        MobileAds.initialize(this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });

//        appOpenManager = new AppOpenManager(this);

//        List<String> testDeviceIds = Arrays.asList("96FF10EC5E219F344E81CE3DE437426E");
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        MobileAds.setRequestConfiguration(configuration);
    }
}
