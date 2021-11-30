/*
 *  Copyright 2015 The WebRTC Project Authors. All rights reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package com.slvrvideocallapp.trndvideo.realvideocallapp.apprtc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.LoadAdError;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy;
import com.slvrvideocallapp.trndvideo.realvideocallapp.activities.CountryUsersActivity;
import com.slvrvideocallapp.trndvideo.utils.PrefManager;

import org.webrtc.RendererCommon;

import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.interstitialAd;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.mInterstitialAdr;
import static com.slvrvideocallapp.trndvideo.activity.DFirstActivityyy.requestNewInterstitial;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.TAG_INTERSTITIAL;
import static com.slvrvideocallapp.trndvideo.activity.DSplashScrnActvty.getQueryMap;
import static org.webrtc.ContextUtils.getApplicationContext;


/**
 * Fragment for call control.
 */
public class CallFragment extends Fragment {

  //new
  public static Context context;

  //Prefrance
  private static PrefManager prf;

  private TextView contactView;
  private ImageButton cameraSwitchButton;
  private ImageButton videoScalingButton;
  private ImageButton toggleMuteButton;
  private TextView captureFormatText;
  private SeekBar captureFormatSlider;
  private OnCallEvents callEvents;
  private RendererCommon.ScalingType scalingType;
  private boolean videoCallEnabled = true;
  private ImageView id_triangle;

  /**
   * Call control interface for container activity.
   */
  public interface OnCallEvents {
    void onCallHangUp();
    void onCameraSwitch();
    void onVideoScalingSwitch(RendererCommon.ScalingType scalingType);
    void onCaptureFormatChange(int width, int height, int framerate);
    boolean onToggleMic();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View controlView = inflater.inflate(R.layout.fragment_call, container, false);

    context = getApplicationContext();

    prf = new PrefManager(context);

    // Create UI controls.
    id_triangle=controlView.findViewById(R.id.id_triangle);
    contactView = controlView.findViewById(R.id.contact_name_call);
    ImageButton disconnectButton = controlView.findViewById(R.id.button_call_disconnect);
    cameraSwitchButton = controlView.findViewById(R.id.button_call_switch_camera);
    videoScalingButton = controlView.findViewById(R.id.button_call_scaling_mode);
    toggleMuteButton = controlView.findViewById(R.id.button_call_toggle_mic);
    captureFormatText = controlView.findViewById(R.id.capture_format_text_call);
    captureFormatSlider = controlView.findViewById(R.id.capture_format_slider_call);

    id_triangle.setOnClickListener(new View.OnClickListener() {
      //@RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onClick(View v) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog,null);

        LinearLayout li_porn =(LinearLayout)mView.findViewById(R.id.li_porn);
        LinearLayout li_fake =(LinearLayout)mView.findViewById(R.id.li_fake);
        LinearLayout li_cheat =(LinearLayout)mView.findViewById(R.id.li_cheat);
        LinearLayout li_lang =(LinearLayout)mView.findViewById(R.id.li_lang);
        LinearLayout li_other =(LinearLayout)mView.findViewById(R.id.li_other);
        LinearLayout li_cancel =(LinearLayout)mView.findViewById(R.id.li_cancel);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();
        li_porn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(getActivity(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
            alertDialog.cancel();

          }
        });
        li_fake.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(getActivity(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
            alertDialog.cancel();

          }
        });

        li_cheat.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(getActivity(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
            alertDialog.cancel();

          }
        });
        li_lang.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(getActivity(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
            alertDialog.cancel();

          }
        });
        li_other.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(getActivity(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
            alertDialog.cancel();

          }
        });
        li_cancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //Toast.makeText(getApplicationContext(),"ThankYou For Report.We Will Take Action As Soon As Possible.",Toast.LENGTH_LONG).show();
            alertDialog.cancel();

          }
        });




      }
    });


    // Add buttons click events.
    disconnectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
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
                  public void onAdClosed() {
                    requestNewInterstitial();
                    callEvents.onCallHangUp();
                  }
                });
              } else {
                callEvents.onCallHangUp();
              }
            } else {
              callEvents.onCallHangUp();
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        } else if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {

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

                  callEvents.onCallHangUp();
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
              callEvents.onCallHangUp();
            }
          } else {
            callEvents.onCallHangUp();
          }
        } else {
          callEvents.onCallHangUp();
        }

      }
    });

    cameraSwitchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        callEvents.onCameraSwitch();
      }
    });

    videoScalingButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (scalingType == RendererCommon.ScalingType.SCALE_ASPECT_FILL) {
          videoScalingButton.setBackgroundResource(R.drawable.ic_action_full_screen);
          scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FIT;
        } else {
          videoScalingButton.setBackgroundResource(R.drawable.ic_action_return_from_full_screen);
          scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
        }
        callEvents.onVideoScalingSwitch(scalingType);
      }
    });
    scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;

    toggleMuteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean enabled = callEvents.onToggleMic();
        toggleMuteButton.setAlpha(enabled ? 1.0f : 0.3f);
      }
    });

    return controlView;
  }

  @Override
  public void onStart() {
    super.onStart();

    boolean captureSliderEnabled = false;
    Bundle args = getArguments();
    if (args != null) {
      String contactName = args.getString(CallActivity.EXTRA_ROOMID);
//      contactView.setText(contactName);
      videoCallEnabled = args.getBoolean(CallActivity.EXTRA_VIDEO_CALL, true);
      captureSliderEnabled = videoCallEnabled
          && args.getBoolean(CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, false);
    }
    if (!videoCallEnabled) {
      cameraSwitchButton.setVisibility(View.INVISIBLE);
    }
    if (captureSliderEnabled) {
      captureFormatSlider.setOnSeekBarChangeListener(
          new CaptureQualityController(captureFormatText, callEvents));
    } else {
      captureFormatText.setVisibility(View.GONE);
      captureFormatSlider.setVisibility(View.GONE);
    }
  }

  // TODO(sakal): Replace with onAttach(Context) once we only support API level 23+.
  @SuppressWarnings("deprecation")
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    callEvents = (OnCallEvents) activity;
  }
}
