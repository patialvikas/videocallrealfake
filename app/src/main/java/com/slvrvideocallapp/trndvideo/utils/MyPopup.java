package com.slvrvideocallapp.trndvideo.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.SessionManager;
import com.slvrvideocallapp.trndvideo.databinding.PopUpShowAdsBinding;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyPopup {

    SessionManager sessionManager;
    OnDilogClickListnear onDilogClickListnear;
    private Dialog dialog;
    private Context context;
    private AdvertisementRoot.Google google;

    public MyPopup(Context context) {

        this.context = context;
    }

    public void showPopup(String text1, String text2, String button) {
        sessionManager = new SessionManager(context);
        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        PopUpShowAdsBinding popupbinding = DataBindingUtil.inflate(inflater, R.layout.pop_up_show_ads, null, false);


        dialog.setContentView(popupbinding.getRoot());


        if (sessionManager.getBooleanValue(Const.ADS_Downloded)) {

            if (Boolean.TRUE.equals(sessionManager.getAdsKeys().getGoogle().isShow())) {
                Log.d("TAG", "loadAds: google show yes ");
                google = sessionManager.getAdsKeys().getGoogle();

            }
        }

        if (sessionManager.getStringValue(Const.PROFILE_IMAGE).equals("")) {
            popupbinding.profilechar.setVisibility(View.VISIBLE);
            popupbinding.profilechar.setText(String.valueOf(sessionManager.getUser().getFname().charAt(0)).toUpperCase());
        }

        Glide.with(context)
                .load(sessionManager.getStringValue(Const.PROFILE_IMAGE)).error(R.drawable.dbg_whitebtnroundddd)
                .placeholder(R.drawable.dbg_whitebtnroundddd)
                .circleCrop()
                .into(popupbinding.imagepopup);
        if (sessionManager.getUser().getFname() != null) {
            popupbinding.tv1.setText(text1);
        }
        popupbinding.textview.setText(text2);
        popupbinding.tvPositive.setText(button);
        popupbinding.tvPositive.setOnClickListener(v -> {

            onDilogClickListnear.onDilogPositiveClick();
            //dialog.dismiss();
            // Toast.makeText(context, "Your Request has been Sended!!", Toast.LENGTH_SHORT).show();
        });
        popupbinding.tvCencel.setOnClickListener(v -> dialog.dismiss());


        //dialog.show();

    }

    public void dismissPopup() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public OnDilogClickListnear getOnDilogClickListnear() {
        return onDilogClickListnear;
    }

    public void setOnDilogClickListnear(OnDilogClickListnear onDilogClickListnear) {
        this.onDilogClickListnear = onDilogClickListnear;
    }

    public interface OnDilogClickListnear {
        void onDilogClickClose();

        void onDilogPositiveClick();
    }
}
