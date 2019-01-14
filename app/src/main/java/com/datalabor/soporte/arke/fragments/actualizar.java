package com.datalabor.soporte.arke.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link actualizar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link actualizar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class actualizar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context myContext;

    private TextView txtActualizar;
    private Button btnActualizar;


    private String curVersion;

    private final  String TAG = "Actualizar";
    public actualizar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment actualizar.
     */
    // TODO: Rename and change types and number of parameters
    public static actualizar newInstance() {
        actualizar fragment = new actualizar();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_actualizar, container, false);

        View _view = inflater.inflate( R.layout.fragment_actualizar, container, false );
        txtActualizar = (TextView) _view.findViewById(R.id.actualizar_label);
        btnActualizar = (Button) _view.findViewById(R.id.btnActualizar);


        util _util = new util(myContext);
        curVersion = _util.GetCurrentVersion();

        txtActualizar.setText("Versión actual: " +curVersion );


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Actualizar");

                new checkUpdateTask().execute();
            }

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


    private void actualizaApp()
    {
        new AlertDialog.Builder( myContext )
                .setTitle("Nueva versión")
                .setMessage( "¿Desea actualizar el app a la nueva versión?" )
                .setCancelable( false )
                .setPositiveButton( "Si", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id )
                    {
                    ///Download file
                        Log.d(TAG, "Downloading file");

                       Intent newWeb =  new Intent(Intent.ACTION_VIEW, Uri.parse(common.DOWNLOAD_URL_BASE + "arke-app.apk"));
                        startActivity(newWeb);
                        getActivity().finishAffinity();

                    }
                } )
                .setNegativeButton( "No", null )
                .show();



    }


    private static String downloadFile(URL url) throws IOException {
        // Codigo de coneccion, Irrelevante al tema.
        URL ftpurl = url;
        URLConnection c = url.openConnection();


        //c.setRequestMethod("GET");
        c.setReadTimeout(15 * 1000);
        c.setUseCaches(false);
        c.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }


    private class checkUpdateTask extends AsyncTask<Void,Void,String[]> {

        ProgressDialog _progressDialog;

        protected String[] doInBackground(Void... v) {
            long start = System.currentTimeMillis();
            String[] result = new String[2];

            //disableConnectionReuseIfNecessary();

            //// Codigo Insertado


            try {
                String data = downloadFile(new URL(common.DOWNLOAD_URL_BASE + "arke_version.json"));

                JSONObject json = new JSONObject(data);
                int latestVersionCode = json.getInt("version");
                String latestVersionName = json.getString("versionname");


                //downloadURL = json.getString("downloadURL");
                Log.d(TAG, "Datos obtenidos con éxito");


                if (latestVersionName.equals(curVersion)) return null;

                else {

                    result[0] = "have update";
                    result[1] = "arke-app.apk";
                    return result;
                }




            } catch (JSONException e) {
                Log.e("AutoUpdate", "Ha habido un error con el JSON", e);
                return null;
            } catch (IOException e) {
                Log.e("AutoUpdate", "Ha habido un error con la descarga", e);
                return null;
            }



        }



        protected void onPreExecute()
        {
            // show progress bar or something
            Log.v("Actualizar", "checking if there's update on the server");
            _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Revisando si existe una nueva versión..", true );

        }


        protected void onPostExecute(String[] result) {
            // kill progress bar here

            _progressDialog.dismiss();
            if( result != null ) {
                if( result[0].equalsIgnoreCase("have update") ) {

                //    new actualizar.downloadTask().execute();

                    actualizaApp();

                }

                //if (checkDownloadeFileVersion())   raise_notification();


            } else {

                common.showWarningDialog("Mensaje", "El app esta actualizada..", myContext);

                Log.v("Actualizar", "no reply from update server");
            }
        }




    }


}
