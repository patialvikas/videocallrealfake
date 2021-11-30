package com.slvrvideocallapp.trndvideo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.slvrvideocallapp.trndvideo.adapters.ChatAdapter;
import com.slvrvideocallapp.trndvideo.adapters.QuestionAdapter;
import com.slvrvideocallapp.trndvideo.databinding.ActivityChatBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.ModelChat;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitService;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.interstitialAd;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.mInterstitialAdr;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.requestNewInterstitial;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_APP_ID_AD_UNIT_ID;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNER;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_BANNERMAINR;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;


public class ChatActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "chatact";
    private static final String WEBSITE = "WEBSITE";

    ActivityChatBinding binding;
    ChatAdapter chatAdapter = new ChatAdapter();
    List<String> messages = new ArrayList<>();
    List<String> photos = new ArrayList<>();
    Random random = new Random();
    RetrofitService service;
    boolean fetched = false;
    private String adid;
    private SessionManager sessionManager;
    private String ownAdRewardUrl = "";
    private String ownAdBannerUrl = "";
    private String ownAdInstarUrl = "";
    private String ownWebUrl = "";
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private boolean ownLoded = false;
    private boolean enable = true;
    private LinearLayout adContainer;
//    private GGInterstitialAd mAd;



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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.chatprimary));
        }
        MainActivity.setStatusBarGradiant(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        sessionManager = new SessionManager(this);


        getInatentData();
        initListner();
        prepareRobot();
        setQuestions();
        binding.rvchats.setAdapter(chatAdapter);

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


    private void setQuestions() {

        List<Questions> questions = new ArrayList<>();
        questions.add(new Questions("Hii", "Heyy"));
        questions.add(new Questions("Send Me your Pic", "IMAGE"));
        questions.add(new Questions("Boyfriend?", "No, Do you like me?"));
        questions.add(new Questions("How Are You?", "I am fine"));
        questions.add(new Questions("Do you like me??", "Yeh of Course"));
        questions.add(new Questions("You are gorgeous", "Awww Thankyou so much"));
        questions.add(new Questions("What is Your name?", binding.tvName.getText().toString()));


        Collections.shuffle(questions);


        QuestionAdapter questionAdapter = new QuestionAdapter(questions, questions1 -> {
            if (!enable) {

                return;
            } else {
                enable = false;

                ModelChat modelChat = new ModelChat(true, questions1.getQuestion(), null, null);
                chatAdapter.addData(modelChat);
                binding.rvchats.scrollToPosition(chatAdapter.getItemCount() - 1);

                int rep = random.nextInt(5);
                Log.d(TAG, "setQuestions: " + rep);
                if (rep == 2 || rep == 4) {
                    new Handler().postDelayed(() -> binding.tvtyping.setVisibility(View.VISIBLE), 2000);

                    if (questions1.getAnswer().equals("IMAGE")) {
                        new Handler().postDelayed(() -> {
                            int i = random.nextInt(photos.size() - 1);
                            String photo = photos.get(i);
                            ModelChat modelChat2 = new ModelChat(false, "", null, photo);
                            chatAdapter.addData(modelChat2);
                            binding.rvchats.scrollToPosition(chatAdapter.getItemCount() - 1);
                            enable = true;
                            binding.tvtyping.setVisibility(View.GONE);
                        }, 3000);

                    } else {
                        new Handler().postDelayed(() -> {


                            ModelChat modelChat2 = new ModelChat(false, questions1.getAnswer(), null, null);
                            chatAdapter.addData(modelChat2);
                            binding.rvchats.scrollToPosition(chatAdapter.getItemCount() - 1);
                            enable = true;
                            binding.tvtyping.setVisibility(View.GONE);
                        }, 3000);

                    }
                }

                new Handler().postDelayed(() -> {
                    enable = true;
                }, 3000);
            }
        });
        binding.rvquestions.setAdapter(questionAdapter);
    }

    private void prepareRobot() {
        messages.add("What's your name ?");
        messages.add("Hey do you want chat with me ?");
        messages.add("Which color would you like ?");
        messages.add("Hmm");
        messages.add("What ?");
        messages.add("Are you kidding with me?");
        messages.add("Do  you have  girlfriend?");
        messages.add("hey Dude I am boring now \n so you can chat with me");
        messages.add("I am busy right now \n talk to you later");
        messages.add("Send me your instagram id");
        messages.add("I am " + binding.tvName.getText().toString() + " who are you ?");
        messages.add("Send me your Number");

        messages.add("Am i looking Sexy??");
        messages.add("wow");
        messages.add("woo...");
        messages.add("wooo");
        messages.add("nice");
        messages.add("cool");
        messages.add("mmm..");
        messages.add("oh");
        messages.add("oooh..");
        messages.add("mmmmm...");


        messages.add("First i need to understand u r my type or not");
        messages.add("Let's give some time here to know each other then maybe we exchange");
        messages.add("I don't know anything about u first communicate here only");
        messages.add("Give some time to develop this relation then may be we share personal");
        messages.add("No personal here first we should know each other");
        messages.add("May be some time later when we know each other");
        messages.add("No personal only fun chat here only");
        messages.add("I can't exchange my details sorry first let's talk here only");
        messages.add("Talk here only i can't share my personal");
        messages.add("No means no that's it");


        photos.add("g1");
        photos.add("g3");
        photos.add("g4");
        photos.add("g5");

        photos.add("g13");

        photos.add("g21");
        photos.add("g22");

        photos.add("g24");
        photos.add("g25");
        photos.add("g26");


    }

    private void initListner() {
        binding.btnsend.setOnClickListener(v -> {
            binding.btnsend.setEnabled(false);
            String msg = binding.etChat.getText().toString();
            if (msg.equals("")) {
                Toast.makeText(this, "Enter Comment First", Toast.LENGTH_SHORT).show();
            } else {
                ModelChat modelChat = new ModelChat(true, msg, null, "g1");
                chatAdapter.addData(modelChat);
                binding.rvchats.scrollToPosition(chatAdapter.getItemCount() - 1);
                binding.etChat.setText("");

                int rep = random.nextInt(5);

                if (rep == 2 || rep == 4) {
                    new Handler().postDelayed(() -> binding.tvtyping.setVisibility(View.VISIBLE), 2000);


                    new Handler().postDelayed(() -> {
                        String message;
                        if (msg.contains("hi") || msg.contains("Hi") || msg.contains("HI")) {
                            message = "hii";
                        } else {
                            int i = random.nextInt(messages.size() - 1);
                            message = messages.get(i);
                        }

                        ModelChat modelChat2 = new ModelChat(false, message, null, null);
                        chatAdapter.addData(modelChat2);
                        binding.rvchats.scrollToPosition(chatAdapter.getItemCount() - 1);
                        binding.btnsend.setEnabled(true);
                        binding.tvtyping.setVisibility(View.GONE);
                    }, 3000);
                }

            }

            new Handler().postDelayed(() -> {
                enable = true;
                binding.btnsend.setEnabled(true);
            }, 3000);
        });
    }

    private void getInatentData() {
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        if (image != null && !image.equals("")) {
            Glide.with(getApplicationContext())
                    .load(new SessionManager(ChatActivity.this).getStringValue(Const.IMAGE_URL) + image).placeholder(R.drawable.ddadduserrrrrr)
                    .circleCrop()
                    .into(binding.imgprofile);

        }
        String name = intent.getStringExtra("name");
        if (name != null && !name.equals("")) {
            binding.tvName.setText(name);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        Anylitecs.addUser(this);
    }

    public void onClickCamara(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE && data != null) {
            Uri selectedImageUri = data.getData();

            ModelChat modelChat = new ModelChat(true, "", selectedImageUri, null);
            chatAdapter.addData(modelChat);
            binding.rvchats.scrollToPosition(chatAdapter.getItemCount() - 1);

            int rep = random.nextInt(5);

            if (rep == 2 || rep == 4) {
                new Handler().postDelayed(() -> binding.tvtyping.setVisibility(View.VISIBLE), 2000);

                new Handler().postDelayed(() -> {
                    int i = random.nextInt(photos.size() - 1);
                    String photo = photos.get(i);
                    ModelChat modelChat2 = new ModelChat(false, "", null, photo);
                    chatAdapter.addData(modelChat2);
                    binding.rvchats.scrollToPosition(chatAdapter.getItemCount() - 1);

                    binding.tvtyping.setVisibility(View.GONE);
                }, 3000);
            }

        }

    }

    public void onClickBack(View view) {

            finish();


    }

    public class Questions {
        String question;
        String answer;

        public Questions(String hii, String heyy) {
            this.answer = heyy;
            this.question = hii;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}