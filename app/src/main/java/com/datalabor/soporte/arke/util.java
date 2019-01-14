
package com.datalabor.soporte.arke;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


public class util {


    private Context context;


    public util(Context newContext)
    {
        context = newContext;

    }

    public String GetCurrentVersion()
    {

        String curVersion="";



        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        int flags = PackageManager.GET_PERMISSIONS;
        PackageInfo packageInfo = null;

        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
            // versionCode = packageInfo.versionCode;
            curVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
            Log.e("Actualizar", e.getMessage());
            return "";
        }




        return curVersion;

    }

}
