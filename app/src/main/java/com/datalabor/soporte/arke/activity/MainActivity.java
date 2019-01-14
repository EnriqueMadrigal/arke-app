package com.datalabor.soporte.arke.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.fragments.acercade;
import com.datalabor.soporte.arke.fragments.actualizar;
import com.datalabor.soporte.arke.fragments.busquedaxobras;
import com.datalabor.soporte.arke.fragments.busquedaxresponsable;
import com.datalabor.soporte.arke.fragments.mainf;
import com.datalabor.soporte.arke.fragments.resultados;
import com.datalabor.soporte.arke.models.Herramienta_Simple;
import com.datalabor.soporte.arke.utils.HttpClient;
import com.datalabor.soporte.arke.utils.ImageUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private ArrayList<Herramienta_Simple> _herramientas;


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
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {

                            case R.id.menu_home:
                                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            break;

                            case R.id.menu_obra:


                                busquedaxobras _busquedaxobras = busquedaxobras.newInstance();


                                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                                fragmentTransaction.replace( R.id.fragment_container,_busquedaxobras, "User Options" );
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();


                                break;

                            case R.id.menu_persona:

                                busquedaxresponsable _busquedaxresponsable = busquedaxresponsable.newInstance();

                                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                                fragmentTransaction.replace( R.id.fragment_container,_busquedaxresponsable, "User Options" );
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();


                                break;


                            case R.id.menu_actualizar:

                                actualizar _actualizar = actualizar.newInstance();
                                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                                fragmentTransaction.replace( R.id.fragment_container,_actualizar, "Acerca de" );
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                                break;

                            case R.id.menu_acerca:

                                acercade _acercade = acercade.newInstance();

                                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                                fragmentTransaction.replace( R.id.fragment_container,_acercade, "Acerca de" );
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();


                                break;


                            case R.id.menu_salir:
                            confirmLogout();
                                break;


                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });

        ImageButton btnClear = (ImageButton) toolbar.findViewById(R.id.btnClear);
        EditText txtSearch = (EditText) toolbar.findViewById(R.id.txtSearch);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearSearchText();
            }
        });
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
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
         Log.d(TAG, "searching: " + search);

            _herramientas = new ArrayList<>();
            new HerramientasLoad(this, search).execute();

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




    private void handleSent2( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d("LOGin", "resultado");



                if (json.getInt("error") == 1)
                {
                    common.showWarningDialog("! No se pudo cargar el contenido ¡", "Favor de revisar la conexión de datos", this);
                    return;
                }

                if (json.getInt("error") == 0) // No hay errores continuar
                {

                    // JSONObject catalogos = json.getJSONObject( "message" );
                    JSONArray catalogos = json.getJSONArray("message");

                    //Integer UserId = user.getInt("id");
                    //Integer permiso = user.getInt("permiso");
                    //Integer estado = user.getInt("estado");
                    //String nombre = user.getString("nombre");
                    //String apellidos = user.getString("apellidos");


                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    for (int i = 0; i < catalogos.length(); i++) {
                        JSONObject row = catalogos.getJSONObject(i);
                        int id = row.getInt("id");
                        String link = row.getString("image");
                        String desc = row.getString("desc");
                        String clave = row.getString("clave");
                        // String ubicacion = row.getString("ubicacion");


                        Herramienta_Simple cat = new Herramienta_Simple();
                        cat.set_desc(desc);
                        cat.set_id(id);
                        cat.set_imagelink(link);
                        cat.set_clave(clave);
                        //cat.set_ubicacion(ubicacion);
                        _herramientas.add(cat);

                    }


                }


                //Cargar el fragment con los resultados
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                resultados _resultados = resultados.newInstance("Resultados de la busqueda", _herramientas);
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_resultados, "User Options" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();





            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }
        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
        }
    }



    private class HerramientasLoad extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        Context _context;
       String _busqueda;

        public HerramientasLoad( Context context ,String busqueda )
        {
            _busqueda = busqueda;
            _context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( _context, "Espera un momento..", "Obteniendo resultados..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            JSONObject jsonParam = new JSONObject();

            try {
            }


            catch (Exception e)
            {
                return null;
            }

            Log.d(TAG,common.API_URL_BASE + "getHerramientaBusqueda/" + _busqueda);
            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "getHerramientaBusqueda/" + _busqueda, jsonParam );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSent2( result );


        }
    }



}
