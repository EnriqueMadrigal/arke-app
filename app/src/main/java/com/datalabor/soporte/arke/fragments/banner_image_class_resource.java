package com.datalabor.soporte.arke.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;


import com.datalabor.soporte.arke.R;

public class banner_image_class_resource extends Fragment {


    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private FragmentActivity myContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int ResId = getArguments().getInt(EXTRA_MESSAGE);

        View v = inflater.inflate(R.layout.banner_image, container, false);

        ImageView curImage = (ImageView) v.findViewById(R.id.curImage);
        curImage.setImageResource(ResId);



        // ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.banner_image, container, false);

        //float curHeight = Common.getCurWidth();
        //float newHeight = curHeight * (float) 0.666667;

        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) newHeight);
        // curImage.setLayoutParams(params);
        // curImage.requestLayout();


        return v;
    }


    public static final banner_image_class_resource newInstance(int ResId)

    {

        banner_image_class_resource f = new banner_image_class_resource();

        Bundle bdl = new Bundle();
        bdl.putInt(EXTRA_MESSAGE, ResId);

        f.setArguments(bdl);

        return f;

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

}
