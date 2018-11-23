package com.datalabor.soporte.arke.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.adapters.Responsables_SpinnerAdapter;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.models.Herramienta_Simple;
import com.datalabor.soporte.arke.models.Responsable;
import com.datalabor.soporte.arke.utils.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link busquedaxresponsable.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link busquedaxresponsable#newInstance} factory method to
 * create an instance of this fragment.
 */
public class busquedaxresponsable extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private Context myContext;

    private Integer _curResponsable = -1;
    private int _curPos = 0;
    private ArrayList<Responsable> _responsables;
    private Spinner Spinner_responsables;

    private Responsables_SpinnerAdapter _responsablesAdapter;
    private Button buttonMostrar;


    private ArrayList<Herramienta_Simple> _herramientas;
    private OnFragmentInteractionListener mListener;

    private final String TAG = "busquedaxresp";

    public busquedaxresponsable() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment busquedaxresponsable.
     */
    // TODO: Rename and change types and number of parameters
    public static busquedaxresponsable newInstance() {
        busquedaxresponsable fragment = new busquedaxresponsable();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_busquedaxresponsable, container, false);
        View _view;
        _view = inflater.inflate( R.layout.fragment_busquedaxresponsable, container, false );


        buttonMostrar = (Button) _view.findViewById(R.id.btnResponsableMostrar);
        Spinner_responsables = (Spinner) _view.findViewById(R.id.responsable_responsables);


        buttonMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Mostrar Responsable");

                _herramientas = new ArrayList<>();
                Load_Herramientas();
            }
        });


        _responsables = new ArrayList<>();
        Load_Responsables();

        _responsablesAdapter = new Responsables_SpinnerAdapter(myContext, R.layout.spinner_item, _responsables);
        _responsablesAdapter.setDropDownViewResource(R.layout.spinner_item);
        Spinner_responsables.setAdapter(_responsablesAdapter);

        Spinner_responsables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position

                Responsable curResponsable = _responsablesAdapter.getItem(position);
                // Here you can do the action you want to...
                Log.d(TAG,String.valueOf(curResponsable.get_id()));
                _curResponsable = curResponsable.get_id();
                _curPos = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });




        return _view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
       myContext = context;
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void Load_Responsables()
    {


        new ResponsablesLoad(myContext).execute();
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
                    common.showWarningDialog("! No se pudo cargar el contenido ¡", "Favor de revisar la conexión de datos", myContext);
                    return;
                }

                if (json.getInt("error") == 0) // No hay errores continuar
                {

                    // JSONObject catalogos = json.getJSONObject( "message" );
                    JSONArray Responsables = json.getJSONArray("message");

                    //Integer UserId = user.getInt("id");
                    //Integer permiso = user.getInt("permiso");
                    //Integer estado = user.getInt("estado");
                    //String nombre = user.getString("nombre");
                    //String apellidos = user.getString("apellidos");


                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    for (int i = 0; i < Responsables.length(); i++) {
                        JSONObject row = Responsables.getJSONObject(i);
                        int id = row.getInt("id");
                        String nombre = row.getString("nombre");
                        String desc = row.getString("desc");

                        Responsable newResponsable = new Responsable();
                        newResponsable.set_desc(desc);
                        newResponsable.set_name(nombre);
                        newResponsable.set_id(id);
                        _responsables.add(newResponsable);


                    }
                    _responsablesAdapter.notifyDataSetChanged();
                }


                /*
                if( json.getString( "user" ).equals( "not_exists" ) )
                    Common.alert( this, "Vendedor no existente." );
                else if( json.getString( "user" ).equals( "already_registered" ) )
                    Common.alert( this, "Vendedor ya está registrado." );
                else
                {
                    JSONObject user = json.getJSONObject( "user" );
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( this );
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString( Common.VAR_USER_UID, user.getString( "uid" ) );
                    editor.putString( Common.VAR_USER_NAME, user.getString( "name" ) );
                    editor.putString( Common.VAR_USER_EMPLOYEE_NO, user.getString( "employee_no" ) );
                    editor.putString( Common.VAR_USER_SHOP_ID, user.getString( "shop_id" ) );
                    editor.putString( Common.VAR_USER_SHOP_NAME, user.getString( "shop_name" ) );
                    editor.putString( Common.VAR_USER_SHOP_NUMBER, user.getString( "shop_number" ) );
                    editor.putString( Common.VAR_USER_SHOP_RETAILER_ID, user.getString( "retailer_id" ) );
                    editor.putString( Common.VAR_USER_SHOP_RETAILER_NAME, user.getString( "retailer_name" ) );
                    editor.commit();

                    Intent intent = new Intent( this, MainActivity.class );
                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity( intent );


                }
                */
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



    private class ResponsablesLoad extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        Context _context;

        public ResponsablesLoad( Context context)
        {
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


            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "responsables", jsonParam );
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

    // Herramientas


    private void Load_Herramientas()
    {


        new HerramientasLoad(myContext, _curResponsable).execute();
    }


    private void handleSent3( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d("LOGin", "resultado");



                if (json.getInt("error") == 1)
                {
                    common.showWarningDialog("! No se pudo cargar el contenido ¡", "Favor de revisar la conexión de datos", myContext);
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

                FragmentManager fragmentManager = getFragmentManager();
                resultados _resultados = resultados.newInstance("Herramientas en:" + _responsables.get(_curPos).get_name(), _herramientas);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_resultados, "Herramiemta" );
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
        int _curObra;

        public HerramientasLoad( Context context ,int obra )
        {
            _curObra = obra;
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

            Log.d(TAG,common.API_URL_BASE + "getHerramientaTipo/" + String.valueOf(_curObra));
            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "getHerramientaTipo/" + String.valueOf(_curObra), jsonParam );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSent3( result );


        }
    }


}
