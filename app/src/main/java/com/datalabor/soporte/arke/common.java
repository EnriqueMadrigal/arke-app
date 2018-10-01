package com.datalabor.soporte.arke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class common {

    private static int _curPage = 0;

    public static final String API_URL_BASE = "http://104.131.34.72/backend/arke/public/api/";
    //public static final String API_URL_BASE = "http://192.168.15.20/arke/public/api/";
    public static final String VAR_USER_ID = "USER_ID";
    public static final String VAR_USER_NAME = "USER_NAME";
    public static final String VAR_LOGIN_NAME = "LOGIN_NAME";
    public static final String VAR_USER_APELLIDOS = "USER_APELLIDOS";
    public static final String VAR_USER_PERMISOS = "USER_PERMISOS";







    // Internet Permission

    public static boolean haveInternetPermissions(Context context, String TAG) {
        Set<String> required_perms = new HashSet<String>();
        required_perms.add("android.permission.INTERNET");
        required_perms.add("android.permission.ACCESS_WIFI_STATE");
        required_perms.add("android.permission.ACCESS_NETWORK_STATE");

        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        int flags = PackageManager.GET_PERMISSIONS;
        PackageInfo packageInfo = null;

        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
            //  versionCode = packageInfo.versionCode;
            //  versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        if( packageInfo.requestedPermissions != null ) {
            for( String p : packageInfo.requestedPermissions ) {
                //Log_v(TAG, "permission: " + p.toString());
                required_perms.remove(p);
            }
            if( required_perms.size() == 0 ) {
                return true;	// permissions are in order
            }
            // something is missing
            for( String p : required_perms ) {
                Log.d(TAG, "required permission missing: " + p);
            }
        }
        Log.d(TAG, "INTERNET/WIFI access required, but no permissions are found in Manifest.xml");
        return false;
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    public static void showWarningDialog(String title ,String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }







}
