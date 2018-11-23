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
public class resultados extends Fragment {
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

    private HerramientasAdapter _adapter;

    private RecyclerView _recyclerview;
    private LinearLayoutManager _linearLayoutManager;
    private TextView _labelTipo;

    private String curLabel;

    private ArrayList<Herramienta_Simple> _herramientas;



    private OnFragmentInteractionListener mListener;

    public resultados() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment subCategoria.
     */
    // TODO: Rename and change types and number of parameters




    public static resultados newInstance(String label, ArrayList<Herramienta_Simple> herramientas) {
        resultados fragment = new resultados();
        Bundle args = new Bundle();
        args.putString("Label", label);
        args.putSerializable("herramientas", herramientas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _herramientas = new ArrayList<>();

        if (getArguments() != null) {
            _herramientas = (ArrayList<Herramienta_Simple>) getArguments().getSerializable("herramientas");
            curLabel = getArguments().getString("Label");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate( R.layout.fragment_resultados, container, false );
        _recyclerview = (RecyclerView) _view.findViewById(R.id.resultados_recycler);
        _labelTipo = (TextView) _view.findViewById(R.id.resultados_label);



        _labelTipo.setText(curLabel);

       // Load_Herramientas();

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


}
