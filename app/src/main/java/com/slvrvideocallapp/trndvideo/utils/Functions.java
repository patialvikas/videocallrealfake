package com.slvrvideocallapp.trndvideo.utils;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Functions {
    public static void showAlertDialog(Context context,SweetAlertDialog.OnSweetClickListener confirmListener,SweetAlertDialog.OnSweetClickListener cancelListener){

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Required Login")
                .setContentText("Use this function you need to login")
                .setConfirmButton("Login", confirmListener)
                .setCancelButton("Cancel", cancelListener)
                .show();

    }
}
