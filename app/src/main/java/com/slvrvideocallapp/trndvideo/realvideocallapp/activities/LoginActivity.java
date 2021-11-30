package com.slvrvideocallapp.trndvideo.realvideocallapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.realvideocallapp.apprtc.ConnectActivity;
import com.slvrvideocallapp.trndvideo.realvideocallapp.models.CatItem;
import com.slvrvideocallapp.trndvideo.realvideocallapp.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes.ApiRequest;
import com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes.Callback;
import com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes.Variables;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Context context = this;

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 11;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    SharedPreferences sharedPreferences;

    private Button skipBtn;

    // intent data
    String countryName,countryIv,countryId;
    private String intentFrom;

    private CatItem bundleCatItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginnn);


        mAuth = FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);
        skipBtn = findViewById(R.id.skipBtn);


        if (getIntent()!=null && getIntent().getStringExtra("country_name")!=null &&
                getIntent().getStringExtra("country_iv")!=null &&
                getIntent().getStringExtra("country_id")!=null) {
            countryName = getIntent().getStringExtra("country_name");
            countryIv = getIntent().getStringExtra("country_iv");
            countryId = getIntent().getStringExtra("country_id");
            Log.e(TAG, "onCreate: country name " + countryName + " iv " + countryIv + " id " + countryId);
        }

        if (getIntent().getSerializableExtra("catItem") != null) {
            bundleCatItem = (CatItem) getIntent().getSerializableExtra("catItem");
        }


        if (getIntent().getStringExtra("from")!=null){
            intentFrom = getIntent().getStringExtra("from");
            if (intentFrom.equals("category")){
                skipBtn.setVisibility(View.VISIBLE);
            }
        }

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CountryUsersActivity.class);
                intent.putExtra("country_id",countryId);
                intent.putExtra("country_name",countryName);
                intent.putExtra("country_iv",countryIv);
                startActivity(intent);
                finish();
            }
        });

        if(mAuth.getCurrentUser() != null && Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
            goToNextActivity();
        }


        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    void goToNextActivity() {
        startActivity(new Intent(LoginActivity.this, HomeCategoryActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult();
            authWithGoogle(account);
        }
    }

    void authWithGoogle(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            final int random = new Random().nextInt(3000) + 1000;
                            String username = googleSignInAccount.getGivenName()+String.valueOf(random);

                            User firebaseUser = new User(user.getUid(), username, user.getPhotoUrl().toString(), "Unknown", 500);

                            Call_Api_For_Signup(user.getUid(),username,user.getPhotoUrl().toString(),"gmail",user,firebaseUser);


                            //Log.e("profile", user.getPhotoUrl().toString());
                        } else {
                            Log.e("err", task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    // this function call an Api for Signin
    private void Call_Api_For_Signup(String id,
                                     String username,
                                     String picture,
                                     String singnup_type, FirebaseUser user, User firebaseUser) {


        PackageInfo packageInfo = null;
        try {
            packageInfo =getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appversion=packageInfo.versionName;

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("fb_id", id);
            parameters.put("username",""+username);
            parameters.put("version",appversion);
            parameters.put("signup_type",singnup_type);

            JSONObject file_data=new JSONObject();
            file_data.put("file_data",picture);
            parameters.put("image",file_data);

            Log.e(TAG, "Call_Api_For_Signup: parameters "+parameters.toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(this, Variables.sign_up, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Log.e(TAG, "Responce: response is "+resp );

                Parse_signup_data(resp,user,firebaseUser);

            }
        });

    }

    // if the signup successfull then this method will call and it store the user info in local
    public void Parse_signup_data(String loginData, FirebaseUser user, User firebaseUser){
        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String code=jsonObject.optString("code");
            if(code.equals("200")){


                JSONArray jsonArray=jsonObject.getJSONArray("msg");
                JSONObject userdata = jsonArray.getJSONObject(0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(Variables.u_id,userdata.optString("fb_id"));
                editor.putString(Variables.user_name,userdata.optString("username"));
                editor.putString(Variables.u_pic,userdata.optString("profile_pic"));
                editor.putBoolean(Variables.islogin,true);
                editor.commit();
                Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);
                Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");

                sendDataToFirebase(user,firebaseUser);





            }else {
                Toast.makeText(this, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendDataToFirebase(FirebaseUser user, User firebaseUser) {

        database.getReference()
                .child("profiles")
                .child(user.getUid())
                .setValue(firebaseUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    if (intentFrom != null) {
                        if (intentFrom.equals("user")) {
                            Intent connectingIntent = new Intent(context, ConnectActivity.class);
                            connectingIntent.putExtra("catItem", bundleCatItem);
                            startActivity(connectingIntent);
                            finish();
                        } else {
                            startActivity(new Intent(LoginActivity.this, HomeCategoryActivity.class));
//                            finish();
                        }
                    } else {
                        startActivity(new Intent(LoginActivity.this, HomeCategoryActivity.class));
                    }
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}