package com.slvrvideocallapp.trndvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.slvrvideocallapp.trndvideo.Anylitecs;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.databinding.ActivityNewUserBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.User;
import com.slvrvideocallapp.trndvideo.models.UserRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG = "newuseract";
    private static final CharSequence REQUIRED = "Required";
    private static final String WEBSITE = "WEBSITE";
    ActivityNewUserBinding binding;
    boolean emptymobile = false;
    boolean emptyFname = false;
    boolean emptyLname = false;
    boolean emptyusername = false;
    boolean emptyemail = false;
    boolean emptycountry = false;
    boolean fetched;
    private String gender = "male";
    private SessionManager sessonManager;
    private String adid;
    private String ownAdRewardUrl = "";
    private String ownAdBannerUrl = "";
    private String ownAdInstarUrl = "";
    private String ownWebUrl = "";
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private String countrycode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        MainActivity.setStatusBarGradiant(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_user);
        sessonManager = new SessionManager(this);
        getIntentData();
        initListnear();
        radiolistnear();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Anylitecs.addUser(this);
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

    private void initListnear() {
        binding.etReferCode.setOnEditorActionListener((v, actionId, event) -> true);

        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                // Toast.makeText(NewUserActivity.this, "Updated " + binding.ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
                countrycode = binding.ccp.getSelectedCountryCode();
            }
        });
    }

    private void radiolistnear() {
        gender = "male";
        binding.btnmale.setOnClickListener(v -> {
            binding.btnfemale.setChecked(false);
            binding.btnmale.setChecked(true);
            gender = "male";
        });
        binding.btnfemale.setOnClickListener(v -> {
            binding.btnfemale.setChecked(true);
            binding.btnmale.setChecked(false);
            gender = "female";
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();

        if (intent != null) {
            String type = intent.getStringExtra("type");
            String data = intent.getStringExtra("data");
            String name = intent.getStringExtra("name");
            if (type != null && !type.equals("")) {
                if (type.equals(LoginActivity.EMAIL)) {
                    binding.etEmail.setText(data);
                    binding.etEmail.setEnabled(false);
                } else {

                    if (data != null) {
                        //  binding.etCountry.setText(data.substring(1, 3));
                        binding.etMobile.setText(data.substring(3));
                        binding.etMobile.setEnabled(false);
                    }
                    if (name != null && !name.equals("")) {
                        binding.etFname.setText(name);
                    }
                }
            }
        }
    }

    public void onClickCountinue(View view) {
        emptymobile = false;
        emptyFname = false;

        emptyusername = false;
        emptyemail = false;
        emptycountry = false;

        String country = binding.ccp.getSelectedCountryCode();
        String mobile = binding.etMobile.getText().toString();
        String firstname = binding.etFname.getText().toString();
        String email = binding.etEmail.getText().toString();
        String username = binding.etUsername.getText().toString();
        String refercode = binding.etReferCode.getText().toString();


        if (country.equals("")) {
            emptycountry = true;
            Toast.makeText(this, "Select Country First", Toast.LENGTH_SHORT).show();
        }
        if (mobile.equals("")) {
            emptymobile = true;
            binding.etMobile.setError(REQUIRED);
        }
        Log.d(TAG, "onClickCountinue: " + mobile.length());
        if (mobile.length() != 10) {
            binding.etMobile.setError("Should be 10 digit");
            return;
        }
        if (firstname.equals("")) {
            emptyFname = true;
            binding.etFname.setError(REQUIRED);
        }

        if (email.equals("")) {
            emptyemail = true;
            binding.etEmail.setError(REQUIRED);
        }
        if (!email.contains("@gmail.com")) {
            emptyemail = true;
            binding.etEmail.setError("Enter Valid Email");
            return;
        }
        if (username.equals("")) {
            emptyusername = true;
            binding.etUsername.setError(REQUIRED);
        }

        if (!emptycountry && !emptyFname && !emptymobile && !emptyemail && !emptyusername) {
            binding.pd.setVisibility(View.VISIBLE);
            String mobilenumber = "+" + country + mobile;


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("fname", firstname);
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("mobile", mobilenumber);
            jsonObject.addProperty("gender", gender);
            jsonObject.addProperty("username", username);
            jsonObject.addProperty("referralCode", refercode);

            Call<UserRoot> call = RetrofitBuilder.create(this).signUpUser(jsonObject);
            call.enqueue(new Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {

                    if (response.code() == 200) {

                        if (response.body() != null && response.body().getStatus() != null && response.body().getStatus() == 200) {
                            User user = response.body().getData();
                            sessonManager.saveUser(user);
                            sessonManager.saveBooleanValue(Const.IS_LOGIN, true);

                            startActivity(new Intent(NewUserActivity.this, MainActivity.class));
                            finishAffinity();
                        }
                        if (response.body() != null && response.body().getStatus() != null && response.body().getStatus() == 403) {

                            String msg = response.body().getMessage();
                            if (msg.equals("Email already Exist!")) {
                                binding.etEmail.setEnabled(true);
                                binding.etEmail.setError(msg);
                            } else if (msg.equals("Mobile No already Exist!")) {
                                binding.etMobile.setEnabled(true);
                                binding.etMobile.setError(msg);
                            } else if (msg.equals("Username already Exist!")) {

                                binding.etUsername.setError(msg);
                            } else if (msg.equals("ReferralCode is Not Exist!")) {

                                binding.etReferCode.setError(msg);
                            } else {
                                Toast.makeText(NewUserActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    binding.pd.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<UserRoot> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                }
            });

        }

    }


    private void interAdListnear() {

/*
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
//mm
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                finish();
                // Code to be executed when the interstitial ad is closed.
            }
        });
*/


    }


    @Override
    public void onBackPressed() {
        finish();


    }
}