package com.slvrvideocallapp.trndvideo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

public class ClearService extends Service {

    //new
    public static Context context;

    //Prefrance
    private static PrefManager prf;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();

        prf = new PrefManager(context);

        Log.d("ClearService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearService", "END");
        //Code here
        stopVPN();
        stopSelf();
    }

    private void stopVPN() {
        if(prf.getString("vpn").equalsIgnoreCase("yes")) {
            try {
                UnifiedSDK.getVpnState(new Callback<VPNState>() {
                    @Override
                    public void success(@NonNull VPNState vpnState) {
                        if(vpnState == VPNState.CONNECTED) {
                            UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                                @Override
                                public void complete() {
                                    //done
                                }

                                @Override
                                public void error(@NonNull VpnException e) {
                                    //error
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(@NonNull VpnException e) {
//                                    callback.success(false);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
