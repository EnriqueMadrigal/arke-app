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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.models.Herramienta;
import com.datalabor.soporte.arke.models.Mantenimiento;
import com.datalabor.soporte.arke.utils.HttpClient;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link herramienta.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link herramienta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class herramienta extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button moverButton;
    private Button responsableButton;
    private Button fotoButton;

    private TextView labelClave;
    private TextView labelModelo;
    private TextView labelMarca;

    private TextView labelDescripcion;
    private TextView labelUbicacionActual;
    private TextView labelResponsable;
    private TextView labelMantenimientos;

    private ImageView fotoImage;

    private FragmentActivity myContext;
    private View _view;

    private Herramienta curHerramienta;
    private Integer id_herramienta = 0;
    private String TAG = "herramienta";

    private OnFragmentInteractionListener mListener;

    public herramienta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment herramienta.
     */
    // TODO: Rename and change types and number of parameters
    public static herramienta newInstance(Integer id_herramienta) {
        herramienta fragment = new herramienta();
        Bundle args = new Bundle();

        //args.putSerializable("herramienta",_herramienta);
        args.putInt("id",id_herramienta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

         //curHerramienta = (Herramienta) getArguments().getSerializable("herramienta");
            id_herramienta = getArguments().getInt("id");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _view = inflater.inflate( R.layout.fragment_herramienta, container, false );

        labelClave = (TextView) _view.findViewById(R.id.lblClave);
        labelMarca = (TextView) _view.findViewById(R.id.lblMarca);
        labelModelo = (TextView) _view.findViewById(R.id.lblModelo);

        labelDescripcion = (TextView) _view.findViewById(R.id.lblDescripcion);

        labelUbicacionActual = (TextView) _view.findViewById(R.id.lblUbicacionActual);
       labelResponsable = (TextView) _view.findViewById(R.id.lblResponsable);

        labelMantenimientos = (TextView) _view.findViewById(R.id.lblMantenimientos);

        moverButton = (Button) _view.findViewById(R.id.btnUbicacion);
        responsableButton = (Button) _view.findViewById(R.id.btnResponsable);
        fotoButton = (Button) _view.findViewById(R.id.btnFotoggrafia);

        fotoImage = (ImageView) _view.findViewById(R.id.image_herramienta);


        Load_Herramienta();




        moverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Mover");

                ubicacion _ubicacion = ubicacion.newInstance(curHerramienta);

                // myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container,_herramienta, "Herramienta" ).commit();

                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_ubicacion, "Ubicacion" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }

        });


        responsableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }

        });


        fotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Foto");
            }

        });

        // Inflate the layout for this fragment
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

    private void LoadValues()
    {
        labelClave.setText(curHerramienta.get_clave());
        labelMarca.setText(curHerramienta.get_marca());
        labelModelo.setText(curHerramienta.get_modelo());

        labelResponsable.setText(curHerramienta.get_responsable());
        labelDescripcion.setText(curHerramienta.get_desc());
        labelUbicacionActual.setText(curHerramienta.get_ubicacion_actual());
        // labelUbicacionAnterior.setText(curHerramienta.get_ubicacion_anterior());


        Picasso.with(myContext)
                .load(curHerramienta.get_imagelink())
                .placeholder(R.drawable.image_notavailable)
                .into(fotoImage);


        StringBuilder mantenimientos = new StringBuilder();

        Integer curContador = 1;
        for(Mantenimiento mantenimiento: curHerramienta.get_mantenimientos())
        {
            mantenimientos.append(String.valueOf(curContador));
            mantenimientos.append(".");
            mantenimientos.append("-");
            mantenimientos.append(" ");
            mantenimientos.append(android.text.format.DateFormat.format("dd/MM/yyyy", mantenimiento.get_fecha()));
            mantenimientos.append(" ");
            mantenimientos.append(" ");
            mantenimientos.append(mantenimiento.get_desc());
            mantenimientos.append(" ");
            mantenimientos.append(System.getProperty("line.separator"));
            curContador ++;

        }

        labelMantenimientos.setText(mantenimientos.toString());

    }


    /////////////
    private void Load_Herramienta()
    {

        new HerramientaLoad(myContext, id_herramienta).execute();

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
                    Herramienta newHerramienta = new Herramienta();
                    JSONObject cur_herramienta = json.getJSONObject( "message" );
                    //JSONArray catalogos = json.getJSONArray("message");

                    int id = cur_herramienta.getInt("id");
                    String link = cur_herramienta.getString("image");
                    String desc = cur_herramienta.getString("desc");
                    String clave = cur_herramienta.getString("clave");
                    String marca = cur_herramienta.getString("marca");
                    String modelo = cur_herramienta.getString("modelo");
                    String responsable = cur_herramienta.getString("responsable");
                    String ubicacion = cur_herramienta.getString("ubicacion");

                    newHerramienta.set_id(id);
                    newHerramienta.set_imagelink(link);
                    newHerramienta.set_desc(desc);
                    newHerramienta.set_clave(clave);
                    newHerramienta.set_marca(marca);
                    newHerramienta.set_modelo(modelo);
                    newHerramienta.set_responsable(responsable);
                    newHerramienta.set_ubicacion_actual(ubicacion);


                    JSONArray mantenimientos = cur_herramienta.getJSONArray("mantenimientos");
                    ArrayList<Mantenimiento> _mantenimientos;
                    _mantenimientos = new ArrayList<>();

                    //Integer UserId = user.getInt("id");
                    //Integer permiso = user.getInt("permiso");
                    //Integer estado = user.getInt("estado");
                    //String nombre = user.getString("nombre");
                    //String apellidos = user.getString("apellidos");


                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    for (int i = 0; i < mantenimientos.length(); i++) {

                        Mantenimiento _mantenimiento = new Mantenimiento();
                        JSONObject row = mantenimientos.getJSONObject(i);
                        String descripcion = row.getString("desc");
                        String fecha = row.getString("fecha");

                        _mantenimiento.set_desc(descripcion);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date convertedDate = new Date();
                        try {
                            convertedDate = dateFormat.parse(fecha);
                            _mantenimiento.set_fecha(convertedDate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }



                        _mantenimientos.add(_mantenimiento);



                    }

                    newHerramienta.set_mantenimientos(_mantenimientos);

                    curHerramienta = newHerramienta;
                    LoadValues();

                }




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



    private class HerramientaLoad extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        Context _context;
        Integer _idHerramienta;

        public HerramientaLoad( Context context ,Integer idHerramienta )
        {
            _idHerramienta = idHerramienta;
            _context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( _context, "Espera un momento..", "Obteniendo resultado..", true );


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


            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "getHerramienta/" + String.valueOf(_idHerramienta), jsonParam );
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


    ///////////


}