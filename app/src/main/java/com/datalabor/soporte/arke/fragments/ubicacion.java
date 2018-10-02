package com.datalabor.soporte.arke.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.adapters.ObrasAdapter;
import com.datalabor.soporte.arke.adapters.Obras_SpinnerAdapter;
import com.datalabor.soporte.arke.adapters.ResponsablesAdapter;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.models.Catalogo;
import com.datalabor.soporte.arke.models.Herramienta;
import com.datalabor.soporte.arke.models.Herramienta_Simple;
import com.datalabor.soporte.arke.models.IViewHolderClick;
import com.datalabor.soporte.arke.models.Obra;
import com.datalabor.soporte.arke.models.Responsable;
import com.datalabor.soporte.arke.utils.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ubicacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ubicacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ubicacion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private FragmentActivity myContext;
    private View _view;


    private String TAG = "ubicacion";
    private Herramienta curHerramienta;
    private int curEquipo;

     private Button buttonAceptar;
    private Button buttonCancelar;

    private Integer _curObra = -1;
    private Integer _curResponsables = -1;


    private ArrayList<Obra> _obras;
    private ArrayList<Responsable> _responsables;

    private Spinner Spinner_obras;
    private Spinner Spinner_responsables;
    private TextView lblNumEquipos;


    private Obras_SpinnerAdapter _obrasAdapter;

    public ubicacion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ubicacion.
     */
    // TODO: Rename and change types and number of parameters
    public static ubicacion newInstance(Herramienta curHerramienta, int _numEquipo) {
        ubicacion fragment = new ubicacion();
        Bundle args = new Bundle();
        args.putSerializable("herramienta" , curHerramienta);
        args.putInt("numEquipo", _numEquipo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           curHerramienta = (Herramienta) getArguments().getSerializable("herramienta");
           curEquipo = (int) getArguments().getInt("numEquipo");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate( R.layout.fragment_ubicacion, container, false );

        buttonAceptar  = (Button) _view.findViewById(R.id.btnActualizarUbicacion);
        buttonCancelar = (Button) _view.findViewById(R.id.btnCancelarUbicacion);
        lblNumEquipos = (TextView) _view.findViewById(R.id.lblUbicacionNumEquipo);

        Spinner_obras = (Spinner) _view.findViewById(R.id.ubicacion_obra);
        Spinner_responsables = (Spinner) _view.findViewById(R.id.ubicacion_responsable);

        lblNumEquipos.setText("Equipo: #" +  String.valueOf(curEquipo));

        _obras = new ArrayList<>();
        Load_Obras();

        _responsables = new ArrayList<>();
        Load_Responsables();


        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"Mover");
                Log.d(TAG, String.valueOf(_curObra));
//public Actualizar( Context _context, Integer idHerramienta,Integer idObra, Integer idResponsable, Integer idUsuario )

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( myContext );

                Integer user_id = sharedPref.getInt(common.VAR_USER_ID, 0);


                if (_curObra == -1 || _curResponsables == -1)
                {
                    common.showWarningDialog("! Selección invalida ¡", "Favor seleccionar el responsable u Obra", myContext);
                }

                else {
                    new Actualizar(myContext, curHerramienta.get_id(), _curObra, _curResponsables, user_id).execute();
                }



            }

        });


        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cancelar");
                getActivity().onBackPressed();
                //getActivity().getFragmentManager().popBackStack();

            }

        });


/////////// Spinners

        _obrasAdapter = new Obras_SpinnerAdapter(myContext, R.layout.spinner_item, _obras);
        _obrasAdapter.setDropDownViewResource(R.layout.spinner_item);
        Spinner_obras.setAdapter(_obrasAdapter);



        Spinner_obras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position

                Obra curObra = _obrasAdapter.getItem(position);
                // Here you can do the action you want to...
                Log.d(TAG,String.valueOf(curObra.get_id()));

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        /////////


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
        myContext = (FragmentActivity) context;
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


    private void Load_Obras()
    {


        new ObrasLoad(myContext).execute();
    }


    private void handleSent( HttpClient.HttpResponse response )
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
                    JSONArray obras = json.getJSONArray("message");

                    //Integer UserId = user.getInt("id");
                    //Integer permiso = user.getInt("permiso");
                    //Integer estado = user.getInt("estado");
                    //String nombre = user.getString("nombre");
                    //String apellidos = user.getString("apellidos");


                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    for (int i = 0; i < obras.length(); i++) {
                        JSONObject row = obras.getJSONObject(i);
                        int id = row.getInt("id");
                        String nombre = row.getString("nombre");
                        String desc = row.getString("desc");

                        Obra newObra = new Obra();
                        newObra.set_desc(desc);
                        newObra.set_name(nombre);
                        newObra.set_id(id);
                        _obras.add(newObra);


                    }
                // Spinner Obras
                    _obrasAdapter.notifyDataSetChanged();


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



    private class ObrasLoad extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        Context _context;

        public ObrasLoad( Context context)
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


            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "obras", jsonParam );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSent( result );


        }
    }

///////////////


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

////////////////

    private void handleSent3( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d("Actualizar", "resultado");

                getActivity().onBackPressed();





            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                common.showWarningDialog("! No valido ¡", "No se pudo actualizar", myContext);
            }
        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
        }
    }


    private class Actualizar extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;

        Integer id_herramienta;
        Integer id_obra;
        Integer id_responsable;
        Integer id_usuario;
        Context context;



        public Actualizar( Context _context, Integer idHerramienta,Integer idObra, Integer idResponsable, Integer idUsuario )
        {
            id_herramienta = idHerramienta;
            id_obra = idObra;
            id_responsable = idResponsable;
            id_usuario = idUsuario;
            context = _context;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( context, "Espera un momento..", "Actualizando..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            JSONObject jsonParam = new JSONObject();

            try {


                jsonParam.put("id_herramienta", id_herramienta);
                jsonParam.put("id_obra", id_obra);
                jsonParam.put("id_responsable", id_responsable);
                jsonParam.put("id_usuario", id_usuario);

            }


            catch (Exception e)
            {
                return null;
            }


            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "actualizarUbicacion", jsonParam );
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
