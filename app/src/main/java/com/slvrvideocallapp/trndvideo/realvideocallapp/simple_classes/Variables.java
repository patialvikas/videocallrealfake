package com.slvrvideocallapp.trndvideo.realvideocallapp.simple_classes;

import android.content.SharedPreferences;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by AQEEL on 2/15/2019.
 */

public class Variables {


    public static final String device = "android";
    public static String tag = "Condell";
    public static final boolean is_secure_info = false;

    public static SharedPreferences sharedPreferences;
    public static final String pref_name = "pref_name";
    public static final String u_id = "u_id";
    public static final String u_name = "u_name";
    public static final String u_pic = "u_pic";
    public static final String f_name = "f_name";
    public static final String l_name = "l_name";
    public static final String gender = "u_gender";
    public static final String contact = "contact";
    public static final String islogin = "is_login";
    public static final String device_token = "device_token";
    public static final String api_token = "api_token";
    public static final String device_id = "device_id";

    public static String user_id;
    public static String user_name;
    public static String user_pic;

    public static final String privacy_policy = "https://sites.google.com/view/";

    //    public static final  String main_domain="http://161.97.174.181/";
    //  public static final  String main_domain="http://kicktak.net/ind/";
//    public static final  String main_domain="http://consolerider.com/indiagram/";
    public static final String main_domain = "http://sameer1.myappadmin.xyz/condell/";


    public static final String base_url = main_domain + "api/";
    public static final String get_categories = base_url + "get_categories.php";
    public static final String get_items = base_url + "get_items.php";
    public static final String sign_up = base_url + "sign_up.php";


}
