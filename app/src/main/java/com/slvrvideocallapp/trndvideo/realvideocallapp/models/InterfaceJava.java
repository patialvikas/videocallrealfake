package com.slvrvideocallapp.trndvideo.realvideocallapp.models;

import android.webkit.JavascriptInterface;

import com.slvrvideocallapp.trndvideo.realvideocallapp.activities.CallActivity;

public class InterfaceJava {

    CallActivity callActivity;

    public InterfaceJava(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

    @JavascriptInterface
    public void onPeerConnected(){
        callActivity.onPeerConnected();
    }

}
