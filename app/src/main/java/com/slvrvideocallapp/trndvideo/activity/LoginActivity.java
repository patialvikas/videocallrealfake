package com.slvrvideocallapp.trndvideo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.slvrvideocallapp.trndvideo.Anylitecs;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.databinding.ActivityLoginBinding;
import com.slvrvideocallapp.trndvideo.models.User;
import com.slvrvideocallapp.trndvideo.models.ValidationRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String EMAIL = "email";
    private static final String TAG = "loginact";
    private static final int RC_SIGN_IN = 1000;
    private static final int DEFULT = 1;
    private static final int OTPSEND = 2;
    private static final String MOBILE = "mobile";
    ActivityLoginBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    private String mobileNumber = "";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private SessionManager sessonManager;
    private RelativeLayout rlClickWithGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        MainActivity.setStatusBarGradiant(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        Glide.with(getApplicationContext()).load(R.mipmap.ic_launcher).circleCrop().into(binding.imageview);
        sessonManager = new SessionManager(this);

        binding.card1.setVisibility(View.VISIBLE);
        binding.card2.setVisibility(View.GONE);
        rlClickWithGoogle=findViewById(R.id.rlClickWithGoogle);
        rlClickWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkboxSign=findViewById(R.id.checkboxSign);
                if(checkboxSign.isChecked()){
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }else{
                    Toast.makeText(getApplicationContext(), "Please Select All Terms and Conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // The SMS quota for the project has been exceeded
                // ...
                binding.pd.setVisibility(View.GONE);
                if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseTooManyRequestsException) {
                    // Invalid request
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                setUI(OTPSEND);
                binding.pd.setVisibility(View.GONE);
                new CountDownTimer(45000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        binding.tvtimer.setText(millisUntilFinished / 1000 + " seconds");
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        binding.tvtimer.setText("R E S E N D");
                        binding.tvtimer.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                    }

                }.start();
                // ...
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        binding.pd.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        Log.d(TAG, "onComplete: " + user.getPhoneNumber() + "  yess  " + user.getUid());

                        chkVeliditonToServerByMobile(user.getPhoneNumber());

                        binding.pd.setVisibility(View.GONE);
                        // ...
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        binding.pd.setVisibility(View.GONE);
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }


    public void onClickResendOTP(View view) {

        if (binding.tvtimer.getText().toString().equals("R E S E N D")) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    mobileNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    mResendToken);
        } else {
            Toast.makeText(this, "Please wait some time", Toast.LENGTH_SHORT).show();
        }
        binding.pd.setVisibility(View.VISIBLE);

    }

    public void onClickGetOTP(View view) {
        String mobile = binding.etMobile.getText().toString();
        String countryCode = binding.etCountry.getText().toString();
        if (mobile.equals("")) {
            binding.etMobile.setError("Enter Mobile First");
            return;
        }
        Log.d(TAG, "onClickGetOTP: " + mobile.length());
        if (mobile.length() < 10) {
            binding.etMobile.setError("Mobile Number should be 10 digit");
            return;
        }
        if (countryCode.equals("")) {
            Toast.makeText(this, "Enter Country Code", Toast.LENGTH_SHORT).show();
            return;
        }

        mobileNumber = "+" + countryCode + mobile;
        mCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
        binding.pd.setVisibility(View.VISIBLE);
    }

    public void onClickCountinue(View view) {
        String otp = binding.fieldVerificationCode.getText().toString();
        if (otp.equals("")) {
            Toast.makeText(this, "Enter OTP First", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
        binding.pd.setVisibility(View.VISIBLE);
    }

    private void setUI(int state) {
        if (state == 1) {
            binding.tvtimer.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.graydark));
            binding.tvextra.setText("ENTER YOUR MOBILE NO");
            binding.tvmobile.setVisibility(View.GONE);
            binding.tvContinue.setVisibility(View.GONE);
            binding.tvresendOtp.setVisibility(View.GONE);
            binding.fieldVerificationCode.setVisibility(View.GONE);
            binding.tvtimer.setVisibility(View.GONE);
        } else if (state == 2) {
            binding.tvresendOtp.setTextColor(ContextCompat.getColor(this, R.color.graydark));
            binding.tvtimer.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.graydark));
            binding.tvextra.setText("ENTER THE OTP");
            binding.tvmobile.setText("send to : " + mobileNumber);
            binding.tvresendOtp.setVisibility(View.VISIBLE);
            binding.tvContinue.setVisibility(View.VISIBLE);
            binding.tvgetOtp.setVisibility(View.GONE);
            binding.fieldVerificationCode.setVisibility(View.VISIBLE);
            binding.tvtimer.setVisibility(View.VISIBLE);
            binding.tvresendOtp.setText("Please wait for");
        }

    }

    public void onClickWithMobile(View view) {
        binding.card2.setVisibility(View.VISIBLE);
        binding.card1.setVisibility(View.GONE);
        setUI(DEFULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getDisplayName());
                    //   Log.d(TAG, "firebaseAuthWithGoogle:" + account.get());
                    chkVeliditonToServerByEmail(account);

                }

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
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

    private void chkVeliditonToServerByEmail(GoogleSignInAccount acc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(EMAIL, acc.getEmail());
        Call<ValidationRoot> call = RetrofitBuilder.create(this).validionFromEmail(jsonObject);

        call.enqueue(new Callback<ValidationRoot>() {
            @Override
            public void onResponse(Call<ValidationRoot> call, Response<ValidationRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 200 && Boolean.FALSE.equals(response.body().getData().getNotFound())) {
                        User user = response.body().getData().getUser();
                        sessonManager.saveStringValue(Const.PROFILE_IMAGE, String.valueOf(acc.getPhotoUrl()));
                        sessonManager.saveUser(user);
                        sessonManager.saveBooleanValue(Const.IS_LOGIN, true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (response.body().getStatus() == 200 && Boolean.TRUE.equals(response.body().getData().getNotFound())) {
                        sessonManager.saveStringValue(Const.PROFILE_IMAGE, String.valueOf(acc.getPhotoUrl()));
                        gotoUserDetailActivity(EMAIL, acc.getEmail(), acc.getDisplayName());

                    }
                }
            }

            @Override
            public void onFailure(Call<ValidationRoot> call, Throwable t) {
//ll
            }
        });
    }

    private void chkVeliditonToServerByMobile(String phoneNumber) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(MOBILE, phoneNumber);
        Call<ValidationRoot> call = RetrofitBuilder.create(this).validionFromMobile(jsonObject);

        call.enqueue(new Callback<ValidationRoot>() {
            @Override
            public void onResponse(Call<ValidationRoot> call, Response<ValidationRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 200 && Boolean.FALSE.equals(response.body().getData().getNotFound())) {
                        User user = response.body().getData().getUser();
                        sessonManager.saveUser(user);
                        sessonManager.saveBooleanValue(Const.IS_LOGIN, true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (response.body().getStatus() == 200 && Boolean.TRUE.equals(response.body().getData().getNotFound())) {
                        gotoUserDetailActivity(MOBILE, phoneNumber, "");
                    }
                }
            }

            @Override
            public void onFailure(Call<ValidationRoot> call, Throwable t) {
//ll
            }
        });

    }

    private void gotoUserDetailActivity(String type, String data, String name) {
        startActivity(new Intent(this, NewUserActivity.class).putExtra("type", type).putExtra("data", data).putExtra("name", name));
    }

    public void onClickSkip(View view) {
        sessonManager.saveBooleanValue(Const.IS_LOGIN, false);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public void openP(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy)));
        startActivity(browserIntent);
    }
}