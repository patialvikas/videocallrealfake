package com.slvrvideocallapp.trndvideo.realvideocallapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.LoadAdError;
import com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy;
import com.slvrvideocallapp.trndvideo.databinding.ActivityConnectingBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.interstitialAd;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.mInterstitialAdr;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.requestNewInterstitial;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;

public class ConnectingActivity extends AppCompatActivity {

    //new
    public static Context context;

    //Prefrance
    private static PrefManager prf;

    ActivityConnectingBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    boolean isOkay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        context = getApplicationContext();

        prf = new PrefManager(context);

        String profile = getIntent().getStringExtra("profile");
        Glide.with(this)
                .load(profile)
                .into(binding.profile);

        String username = auth.getUid();

        database.getReference().child("users")
                .orderByChild("status")
                .equalTo(0).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() > 0) {
                            isOkay = true;
                            // Room Available
                            for(DataSnapshot childSnap : snapshot.getChildren()) {
                                database.getReference()
                                        .child("users")
                                        .child(childSnap.getKey())
                                        .child("incoming")
                                        .setValue(username);
                                database.getReference()
                                        .child("users")
                                        .child(childSnap.getKey())
                                        .child("status")
                                        .setValue(1);
                                Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
                                String incoming = childSnap.child("incoming").getValue(String.class);
                                String createdBy = childSnap.child("createdBy").getValue(String.class);
                                boolean isAvailable = childSnap.child("isAvailable").getValue(Boolean.class);
                                intent.putExtra("username", username);
                                intent.putExtra("incoming", incoming);
                                intent.putExtra("createdBy", createdBy);
                                intent.putExtra("isAvailable", isAvailable);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Not Available

                            HashMap<String, Object> room = new HashMap<>();
                            room.put("incoming", username);
                            room.put("createdBy", username);
                            room.put("isAvailable", true);
                            room.put("status", 0);

                            database.getReference()
                                    .child("users")
                                    .child(username)
                                    .setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference()
                                            .child("users")
                                            .child(username).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if(snapshot.child("status").exists()) {
                                                if(snapshot.child("status").getValue(Integer.class) == 1) {

                                                    if(isOkay)
                                                        return;

                                                    isOkay = true;
                                                    Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
                                                    String incoming = snapshot.child("incoming").getValue(String.class);
                                                    String createdBy = snapshot.child("createdBy").getValue(String.class);
                                                    boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                                                    intent.putExtra("username", username);
                                                    intent.putExtra("incoming", incoming);
                                                    intent.putExtra("createdBy", createdBy);
                                                    intent.putExtra("isAvailable", isAvailable);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

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