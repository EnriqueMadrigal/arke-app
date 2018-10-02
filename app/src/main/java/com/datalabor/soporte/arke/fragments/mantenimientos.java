package com.datalabor.soporte.arke.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.models.Herramienta;
import com.datalabor.soporte.arke.models.Mantenimiento;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mantenimientos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mantenimientos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mantenimientos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
 private Herramienta _curHerramienta;

    private OnFragmentInteractionListener mListener;


    private FragmentActivity myContext;
    private View _view;
    private TextView lblMantenimientos;

    private ArrayList<Mantenimiento> _mantenimientos;


    public mantenimientos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment mantenimientos.
     */
    // TODO: Rename and change types and number of parameters
    public static mantenimientos newInstance(Herramienta curHerramienta) {
        mantenimientos fragment = new mantenimientos();
        Bundle args = new Bundle();
       args.putSerializable("curHerramienta", curHerramienta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _curHerramienta = (Herramienta) getArguments().getSerializable("curHerramienta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        _view = inflater.inflate( R.layout.fragment_mantenimientos, container, false );

        lblMantenimientos = (TextView) _view.findViewById(R.id.lblMantenimientos);

                StringBuilder mantenimientos = new StringBuilder();

        Integer curContador = 1;
        for(Mantenimiento mantenimiento: _curHerramienta.get_mantenimientos())
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

          lblMantenimientos.setText(mantenimientos.toString());



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
}
