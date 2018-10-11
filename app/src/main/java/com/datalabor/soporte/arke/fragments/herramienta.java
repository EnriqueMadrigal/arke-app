package com.datalabor.soporte.arke.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import com.datalabor.soporte.arke.BuildConfig;
import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.activity.uploadImage;
import com.datalabor.soporte.arke.adapters.MyPageAdapter;
import com.datalabor.soporte.arke.adapters.Obras_SpinnerAdapter;
import com.datalabor.soporte.arke.adapters.Responsables_SpinnerAdapter;
import com.datalabor.soporte.arke.adapters.UbicacionesAdapter;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.models.Herramienta;
import com.datalabor.soporte.arke.models.IViewHolderClick;
import com.datalabor.soporte.arke.models.Mantenimiento;
import com.datalabor.soporte.arke.models.Obra;
import com.datalabor.soporte.arke.models.Responsable;
import com.datalabor.soporte.arke.models.Ubicacion;
import com.datalabor.soporte.arke.utils.CircleIndicator;
import com.datalabor.soporte.arke.utils.HttpClient;
import com.datalabor.soporte.arke.utils.ImageUtils;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.datalabor.soporte.arke.utils.ImageUtils.createImageFile;

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

    private ImageButton cameraButton;
    private ImageButton galeriaButton;
    private ImageButton refreshButton;
    private ImageButton lupaButton;

    private Button mantenimientosButton;

    private TextView labelClave;
    private TextView labelModelo;
    private TextView labelMarca;

    private TextView labelDescripcion;

   // private ImageView fotoImage;

    private FragmentActivity myContext;
    private View _view;

    private ViewPager mPager;
    private MyPageAdapter myPageAdapter;

    private Herramienta curHerramienta;
    private Integer id_herramienta = 0;
    private String TAG = "herramienta";


    private UbicacionesAdapter _adapter;
    private RecyclerView _recyclerview;
    private LinearLayoutManager _linearLayoutManager;

    private ArrayList<Ubicacion> _ubicaciones;
    private List<Fragment> fList;

    private OnFragmentInteractionListener mListener;
///////Modal

    private Integer _curObra = -1;
    private Integer _curResponsable = -1;


    private ArrayList<Obra> _obras;
    private ArrayList<Responsable> _responsables;


    private Obras_SpinnerAdapter _obrasAdapter;
    private Responsables_SpinnerAdapter _responsablesAdapter;

    private Integer curUbicacionItemSelected = -1;

    private String curNombreResponsable = "";
    private String curNombreUbicacion = "";



    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_PICKER_IMAGE = 102;
    private static final int REQUEST_CAMERA = 101;

    String imageURL;

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID+".provider";

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

        cameraButton = (ImageButton) _view.findViewById(R.id.btnCamera);
        galeriaButton = (ImageButton) _view.findViewById(R.id.btnGaleria);
        refreshButton = (ImageButton) _view.findViewById(R.id.btnRefresh);
        lupaButton = (ImageButton) _view.findViewById(R.id.btnLupa);

        mantenimientosButton = (Button) _view.findViewById(R.id.btnMantenimientos);

       // fotoImage = (ImageView) _view.findViewById(R.id.image_herramienta);
        _recyclerview = (RecyclerView) _view.findViewById(R.id.recycler5);
        mPager = (ViewPager) _view.findViewById(R.id.ViewPager);

        _ubicaciones = new ArrayList<>();
        Load_Herramienta();
        //Load_Equipos();

        _obras = new ArrayList<>();
        Load_Obras();

        _responsables = new ArrayList<>();
        Load_Responsables();

        _obrasAdapter = new Obras_SpinnerAdapter(myContext, R.layout.spinner_item, _obras);
        _obrasAdapter.setDropDownViewResource(R.layout.spinner_item);

        _responsablesAdapter = new Responsables_SpinnerAdapter(myContext, R.layout.spinner_item, _responsables);
        _responsablesAdapter.setDropDownViewResource(R.layout.spinner_item);


        lupaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//--------------

                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Ver Imagen");

/////////////
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.ver_imagen, (ViewGroup) getView(), false);
// Set up the input
               ImageView imageHerramienta = (ImageView) viewInflated.findViewById(R.id.imageHerramienta);
                Integer curPage = mPager.getCurrentItem();

                ArrayList<String> images = curHerramienta.get_images();
                if (images.size()>=1)
                {

                    String curImage = images.get(curPage);

                    if (curImage.length()>8)
                    {

                        Picasso.with(myContext)
                                .load(curImage)
                                .placeholder(R.drawable.image_notavailable)
                                .into(imageHerramienta);

                    }


                }


                //////////


                builder.setView(viewInflated);

// Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });


                /////////////
                builder.show();



//-----------

            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Load_Herramienta();

                _obras = new ArrayList<>();
                Load_Obras();

                _responsables = new ArrayList<>();
                Load_Responsables();

            }
        });

                cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"camara");

                /////////

                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(myContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(myContext,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                } else {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    File file = null;
                    try {
                        file = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("error al crear: ", "imagen");
                        //Toast.makeText(NoteCreationActivity.this,"No se pudo crear Imagen",Toast.LENGTH_SHORT).show();
                    }
                    try {


                        //Uri outputFileUri = Uri.fromFile(file);
                        //Uri outputFileUri = Uri.parse(file.getAbsolutePath());
                       //Uri contentUri = FileProvider.getUriForFile(getContext(), "com.mydomain.fileprovider", newFile);
                        Uri outputFileUri = FileProvider.getUriForFile(myContext, AUTHORITY , file);


                        String path = outputFileUri.toString();
                        imageURL = path;
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } catch (Exception e) {
                        Log.e("ERROR ", "  " + e);
                    }


                }



///////////////
            }

        });


        galeriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"galeria");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Selecciona la imagén"), REQUEST_PICKER_IMAGE);



            }

        });


        mantenimientosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"mantenimientos");


         //////////////////
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Mantenimientos Realizados");

/////////////
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.ver_mantenimientos, (ViewGroup) getView(), false);
// Set up the input
                 TextView lblMantenimientos = (TextView) viewInflated.findViewById(R.id.lblMantenimientos);

///////////////
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

                lblMantenimientos.setText(mantenimientos.toString());


                //////////


                builder.setView(viewInflated);

// Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                /////////////
                builder.show();



                ///////////////////


            }

        });


        /////////

        _adapter = new UbicacionesAdapter(getActivity(), _ubicaciones, new IViewHolderClick() {
            @Override
            public void onClick(int position) {


                Ubicacion curUbicacion =  _ubicaciones.get(position);
                Log.d(TAG, String.valueOf(curUbicacion.get_id()));

                curUbicacionItemSelected = position;
                final Integer curNum = curUbicacion.get_num();

                /*
                ubicacion _ubicacion = ubicacion.newInstance(curHerramienta , curUbicacion.get_num());

                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_ubicacion, "Ubicacion" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
*/


                //////////////////
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( myContext );

                final Integer user_id = sharedPref.getInt(common.VAR_USER_ID, 0);


                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Actualizar ubicación");

/////////////
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.cambiar_ubicacion, (ViewGroup) getView(), false);
// Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.ModalinputPassword);
                TextView lblNumEquipo = (TextView) viewInflated.findViewById(R.id.ModalUbicacionNumEquipo);

                lblNumEquipo.setText(curNum.toString());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                ///////////Spinners
                Spinner Spinner_obras = (Spinner) viewInflated.findViewById(R.id.Modalubicacion_obra);
                Spinner Spinner_responsables = (Spinner) viewInflated.findViewById(R.id.Modalubicacion_responsable);

                Spinner_obras.setAdapter(_obrasAdapter);
                Spinner_responsables.setAdapter(_responsablesAdapter);


                Spinner_obras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position

                        Obra curObra = _obrasAdapter.getItem(position);
                        // Here you can do the action you want to...
                        Log.d(TAG,String.valueOf(curObra.get_id()));

                        _curObra = curObra.get_id();
                        curNombreUbicacion = curObra.get_name();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });


                Spinner_responsables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position

                        Responsable curResponsable = _responsablesAdapter.getItem(position);
                        // Here you can do the action you want to...
                        Log.d(TAG,String.valueOf(curResponsable.get_id()));
                        _curResponsable = curResponsable.get_id();
                         curNombreResponsable = curResponsable.get_name();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });





                //////////


                builder.setView(viewInflated);

// Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String  m_Text = input.getText().toString();
                        new Actualizar(myContext, curHerramienta.get_id(), _curObra, _curResponsable, curNum, user_id, m_Text).execute();

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                /////////////
                builder.show();



                ////////////



            }
        });



        //////////

        _linearLayoutManager = new LinearLayoutManager( getActivity() );

        _recyclerview.setHasFixedSize( true );
        _recyclerview.setAdapter( _adapter );
        _recyclerview.setLayoutManager( _linearLayoutManager );

        ////// Images


        fList = new ArrayList<Fragment>();
      //  fList.add(banner_image_class.newInstance(R.drawable.banner1));
      //  fList.add(banner_image_class.newInstance(R.drawable.banner2));

        myPageAdapter = new MyPageAdapter(myContext.getSupportFragmentManager(), fList);


        mPager.setAdapter(myPageAdapter);
        mPager.setCurrentItem(0);
        myPageAdapter.notifyDataSetChanged();


        CircleIndicator indicator = (CircleIndicator)_view.findViewById( R.id.CircleIndicator );
        indicator.setViewPager( mPager );


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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// perform your action here
        //Bitmap bitmap = null;
        ////////////

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                Log.d(TAG, "Received Camera");

                Uri uri = Uri.parse(imageURL);

/*
                try {
                    bitmap = ImageUtils.decodeUriIntoBitmap(myContext, uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

*/

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( myContext );

                final Integer user_id = sharedPref.getInt(common.VAR_USER_ID, 0);
                Intent intent = new Intent();


                //intent2.setClass(context, VisitsActivity.class);
                intent.setClass(myContext, uploadImage.class);
                intent.putExtra("imageUrl", imageURL);
                intent.putExtra("id_usuario", user_id);
                intent.putExtra("id_herramienta", id_herramienta);

                startActivity(intent);





            } else if (requestCode == REQUEST_PICKER_IMAGE) {
                Log.d(TAG, "Received Gallery");
                // Get the picked user picture
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( myContext );
                final Integer user_id = sharedPref.getInt(common.VAR_USER_ID, 0);


                Uri pickedImageURI = data.getData();
                String path = pickedImageURI.toString();


                Intent intent = new Intent();
                //intent2.setClass(context, VisitsActivity.class);
                intent.setClass(myContext, uploadImage.class);
                intent.putExtra("imageUrl", path);
                intent.putExtra("id_usuario", user_id);
                intent.putExtra("id_herramienta", id_herramienta);


                startActivity(intent);



                /////
/*


                // Set the picked user picture in the bitmap variable
                try {

                    bitmap = ImageUtils.getCorrectlyOrientedImage(this, pickedImageURI);
//					userPhotoBitmap = decodeUriIntoBitmap(pickedImageURI);

                } catch (FileNotFoundException e1) {
                } catch (IOException e) {
                } catch (NullPointerException e) {
                    // TODO: handle exception
                    return;
                }
                imagesURLS.add(path);
                //notSentImages.add(bitmap);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Tipo", "Bitmap");
                map.put("Objecto", path);
                objectsItems.add(map);

                mAdapter.notifyDataSetChanged();

                // Set the picked user picture in the image view

                */
            }
        }

        ///////////////



    }

    private void LoadValues()
    {
        labelClave.setText(curHerramienta.get_clave());
        labelMarca.setText(curHerramienta.get_marca());
        labelModelo.setText(curHerramienta.get_modelo());

        labelDescripcion.setText(curHerramienta.get_desc());
        // labelUbicacionAnterior.setText(curHerramienta.get_ubicacion_anterior());

        Log.d(TAG,"loading images");

        String  curUrl = curHerramienta.get_imagelink();
/*
        if (curUrl.length()>8) {
            Picasso.with(myContext)
                    .load(curUrl)
                    .placeholder(R.drawable.image_notavailable)
                    .into(fotoImage);
        }
        else
        {
            fotoImage.setImageResource(R.drawable.image_notavailable);
        }
*/
       ArrayList<String> images = curHerramienta.get_images();
       fList.clear();
        for (String curImage: images)
        {

            fList.add(banner_image_class.newInstance(curImage));

        }


        if (images.size()==0)
        {
            fList.add(banner_image_class_resource.newInstance(R.drawable.image_notavailable));
        }

        myPageAdapter.notifyDataSetChanged();



    }


    /////////////
    private void Load_Herramienta()
    {

        new HerramientaLoad(myContext, id_herramienta).execute();

    }

    private void handleSentHerramienta( HttpClient.HttpResponse response )
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
                    JSONArray images = cur_herramienta.getJSONArray("images");


                    ArrayList<Mantenimiento> _mantenimientos;
                    ArrayList<String> _images;

                    _mantenimientos = new ArrayList<>();
                    _images = new ArrayList<>();

                    //Integer UserId = user.getInt("id");
                    //Integer permiso = user.getInt("permiso");
                    //Integer estado = user.getInt("estado");
                    //String nombre = user.getString("nombre");
                    //String apellidos = user.getString("apellidos");


                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    for (int i = 0; i < images.length(); i++) {
                        String curImage = images.getString(i);
                        _images.add(curImage);

                    }



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
                    newHerramienta.set_images(_images);

                    curHerramienta = newHerramienta;
                    LoadValues();
                    Load_Equipos();

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
            handleSentHerramienta( result );


        }
    }


    ///////////

    private void Load_Equipos()
    {
        new EquiposLoad(myContext, curHerramienta.get_id()).execute();
    }

    private void handleSentEquipos( HttpClient.HttpResponse response )
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

                   JSONArray ubicaciones = json.getJSONArray("message");

                    for (int i = 0; i < ubicaciones.length(); i++) {

                        Ubicacion newUbicacion = new Ubicacion();

                        JSONObject row = ubicaciones.getJSONObject(i);
                        int id = row.getInt("id");
                        int num_equipo = row.getInt("num_equipo");

                        String  ubicacion = row.getString("ubicacion_actual");
                        String responsable = row.getString("responsable");

                        newUbicacion.set_id(id);
                        newUbicacion.set_num(num_equipo);
                        newUbicacion.set_ubicacion(ubicacion);
                        newUbicacion.set_responsable(responsable);

                        _ubicaciones.add(newUbicacion);

                    }

                        _adapter.notifyDataSetChanged();


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



    private class EquiposLoad extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        Context _context;
        Integer _idEquipo;

        public EquiposLoad( Context context ,Integer idEquipo )
        {
            _idEquipo = idEquipo;
            _context = context;
            _ubicaciones.clear();
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


            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "getEquipos/" + String.valueOf(_idEquipo), jsonParam );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentEquipos( result );


        }
    }



    //////// Modal ///////////

    private void Load_Obras()
    {


        new ObrasLoad(myContext).execute();
    }


    private void handleSentObras( HttpClient.HttpResponse response )
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
            handleSentObras( result );


        }
    }

///////////////


    private void Load_Responsables()
    {


        new ResponsablesLoad(myContext).execute();
    }


    private void handleSentResponsables( HttpClient.HttpResponse response )
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
            handleSentResponsables( result );


        }
    }

////////////////

    private void handleSentActualizar( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d("Actualizar", "resultado");

                ////////
                if (json.getInt("error") == 1)
                {
                    common.showWarningDialog("! No se pudo actualizar ¡", "Favor de confirmar los datos", myContext);
                    return;
                }

                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    //getActivity().onBackPressed();
                   // getFragmentManager().popBackStack();
                    //new EquiposLoad(myContext, curHerramienta.get_id()).execute();
                    _ubicaciones.get(curUbicacionItemSelected).set_responsable(curNombreResponsable);
                    _ubicaciones.get(curUbicacionItemSelected).set_ubicacion(curNombreUbicacion);
                    _adapter.notifyDataSetChanged();

                    common.showWarningDialog("! Actualizar ¡", "Actualización exitosa", myContext);



                }


                ///////////








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
        Integer num_Equipo;
        String cur_pass;
        Context context;



        public Actualizar( Context _context, Integer idHerramienta,Integer idObra, Integer idResponsable, Integer numEquipo,Integer idUsuario, String curpass )
        {
            id_herramienta = idHerramienta;
            id_obra = idObra;
            id_responsable = idResponsable;
            id_usuario = idUsuario;
            num_Equipo = numEquipo;
            context = _context;
            cur_pass = curpass;

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
                jsonParam.put("no_equipo", num_Equipo);
                jsonParam.put("user_pass", cur_pass);

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
            handleSentActualizar( result );


        }
    }



    ////// Modal /////////////

}
