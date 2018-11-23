package com.datalabor.soporte.arke.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.adapters.CategoryAdapter;
import com.datalabor.soporte.arke.adapters.HerramientasAdapter;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.models.Catalogo;
import com.datalabor.soporte.arke.models.Herramienta;
import com.datalabor.soporte.arke.models.Herramienta_Simple;
import com.datalabor.soporte.arke.models.IViewHolderClick;
import com.datalabor.soporte.arke.models.Mantenimiento;
import com.datalabor.soporte.arke.utils.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link subCategoria.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link subCategoria#newInstance} factory method to
 * create an instance of this fragment.
 */
public class subCategoria extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String TAG = "subCategory";
    private FragmentActivity myContext;
    private View _view;
    private Catalogo curCatalogo;
    private HerramientasAdapter _adapter;

    private RecyclerView _recyclerview;
    private LinearLayoutManager _linearLayoutManager;
    private TextView _labelTipo;


    private ArrayList<Herramienta_Simple> _herramientas;



    private OnFragmentInteractionListener mListener;

    public subCategoria() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment subCategoria.
     */
    // TODO: Rename and change types and number of parameters




    public static subCategoria newInstance(Catalogo curCatalogo) {
        subCategoria fragment = new subCategoria();
        Bundle args = new Bundle();
        args.putSerializable("curCatalogo", curCatalogo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curCatalogo = (Catalogo) getArguments().getSerializable("curCatalogo");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate( R.layout.fragment_sub_categoria, container, false );
        _recyclerview = (RecyclerView) _view.findViewById(R.id.recycler2);
        _labelTipo = (TextView) _view.findViewById(R.id.labelTipo);


        _herramientas = new ArrayList<>();
        _labelTipo.setText(curCatalogo.get_desc());

        Load_Herramientas();

        _adapter = new HerramientasAdapter(getActivity(), _herramientas, new IViewHolderClick() {
            @Override
            public void onClick(int position) {


                Herramienta_Simple curHerramienta =  _herramientas.get(position);
                Log.d(TAG, String.valueOf(curHerramienta.get_id()));
                //subCategoria _subCategory = subCategoria.newInstance(curCatalogo);
               // myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container,_subCategory, "Sub Categoria" ).commit();


                herramienta _herramienta = herramienta.newInstance(curHerramienta.get_id());

                // myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container,_herramienta, "Herramienta" ).commit();

                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_herramienta, "Herramiemta" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();





            }
        });



        //////////

        _linearLayoutManager = new LinearLayoutManager( getActivity() );

        _recyclerview.setHasFixedSize( true );
        _recyclerview.setAdapter( _adapter );
        _recyclerview.setLayoutManager( _linearLayoutManager );




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
        myContext=(FragmentActivity) context;

        super.onAttach(context);
/*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
  */
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

    ///////
    private void Load_Herramientas()
    {


        new HerramientasLoad(myContext, curCatalogo).execute();
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

                    _adapter.notifyDataSetChanged();
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



    private class HerramientasLoad extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        Context _context;
        Catalogo _curCatalogo;

        public HerramientasLoad( Context context ,Catalogo curCatalogo )
        {
            _curCatalogo = curCatalogo;
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


            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "getHerramientaTipo/" + String.valueOf(curCatalogo.get_id()), jsonParam );
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

    ///////


    //////////////

}
