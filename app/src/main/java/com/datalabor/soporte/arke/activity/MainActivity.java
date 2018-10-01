package com.datalabor.soporte.arke.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.fragments.mainf;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    mainf MainFragment;

    private final  String TAG ="MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back_off);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MainFragment = new mainf();
        getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container, MainFragment, "HOME" ).commit();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        switch(item.getItemId()) {

            /*
            case R.id.show_menu:
               //drawerLayout.openDrawer(GravityCompat.START);
               //drawerLayout.openDrawer(GravityCompat.END);
                return true;
            */

            case android.R.id.home:
            Log.d("main","back pressed");
                manageFragments();
                return true;



            case R.id.go_logout:

                confirmLogout();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }


    public void confirmLogout()
    {
        new AlertDialog.Builder( this )
                .setMessage( "¿Estás seguro de que deseas cerrar tu sesión?" )
                .setCancelable( false )
                .setPositiveButton( "Si", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id )
                    {
                        logout();
                    }
                } )
                .setNegativeButton( "No", null )
                .show();
    }


    private void logout()
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( this );
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(common.VAR_USER_ID, 0);
        editor.putInt(common.VAR_USER_PERMISOS, 0);
        editor.putString(common.VAR_USER_NAME, "");
        editor.putString(common.VAR_USER_APELLIDOS, "");
        editor.commit();

        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }

    private void manageFragments()
    {



    }

}
