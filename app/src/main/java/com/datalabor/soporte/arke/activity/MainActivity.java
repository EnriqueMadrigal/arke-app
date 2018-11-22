package com.datalabor.soporte.arke.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.datalabor.soporte.arke.utils.ImageUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    mainf MainFragment;
    public NavigationView navView;
    public DrawerLayout drawerLayout;
    private boolean _isSearchVisible = false;

    private final  String TAG ="MainActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_PICKER_IMAGE = 102;
    private static final int REQUEST_CAMERA = 101;

    String imageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.navview);


       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back_off);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MainFragment = new mainf();
        getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container, MainFragment, "HOME" ).commit();


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.iconohamburguesa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle( "" );

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        switch (menuItem.getItemId()) {

                            case R.id.menu_home:



                                break;

                            case R.id.menu_actualizar:

                                break;

                            case R.id.menu_acerca:


                                break;


                            case R.id.menu_salir:
                            confirmLogout();
                                break;


                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });



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
                drawerLayout.openDrawer(GravityCompat.START);

                return true;



            case R.id.go_search:
                handleSearch();
               // confirmLogout();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

// search
private void handleSearch()
{
    RelativeLayout pnlSearch = (RelativeLayout) toolbar.findViewById(R.id.pnlSearch);
    EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
    if( pnlSearch.getVisibility() == View.GONE )
    {
        _isSearchVisible = true;
        getSupportActionBar().setDisplayUseLogoEnabled( false );
        pnlSearch.setVisibility( View.VISIBLE );
        txtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.showSoftInput( txtSearch, InputMethodManager.SHOW_IMPLICIT );

    }
    else
    {
        search();
    }
}


    private void search()
    {
        closeSearch();
        EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
        String search = txtSearch.getText().toString().trim();
        txtSearch.setText( "" );
        if( search.length() > 0 )
        {
         Log.d(TAG, "searching");

        }
    }
///////

    private void onClearSearchText()
    {
        EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
        txtSearch.setText( "" );
    }
///////////

    private void closeSearch()
    {
        _isSearchVisible = false;
        getSupportActionBar().setDisplayUseLogoEnabled( true );
        RelativeLayout pnlSearch = (RelativeLayout) toolbar.findViewById( R.id.pnlSearch );
        EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( txtSearch.getWindowToken(), 0 );
        pnlSearch.setVisibility( View.GONE );
    }



    /////
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       }
    }
