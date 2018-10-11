package com.datalabor.soporte.arke.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.common;
import com.datalabor.soporte.arke.utils.HttpClient;
import com.datalabor.soporte.arke.utils.ImageUtils;

import org.json.JSONObject;

import java.io.FileNotFoundException;

public class uploadImage extends AppCompatActivity {


    private ImageView ImagetoUpload;
    private ImageButton closeButton;
    private Button UploadButton;

    private String TAG = "uploadIMage";


    private Context context;

    private String ImageUrl;
    private Integer curIdUsuario=0;
    private Integer curHerramientaId=0;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        context = this;

        ImagetoUpload = (ImageView) findViewById(R.id.ImageToUpload);
        closeButton = (ImageButton) findViewById(R.id.btnClose);
        UploadButton = (Button) findViewById(R.id.btnUpload);



        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           if (bitmap != null)
           {

               ///Decrese size to 480
               Bitmap newBitmap  = ImageUtils.createScaledBitmap(bitmap, 480);
                String base64Bitmap  = ImageUtils.getImage64(newBitmap);


            new AgregarImagen(context , curHerramientaId, curIdUsuario, base64Bitmap).execute();


           }





            }

        });



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }

        });


        Bundle extras = getIntent().getExtras();

        if (getIntent().hasExtra("imageUrl")) {

            ImageUrl = (String) getIntent().getExtras().getString("imageUrl");

        }


        if (getIntent().hasExtra("id_usuario")) {

            curIdUsuario = (Integer) getIntent().getExtras().getInt("id_usuario");

        }




        if (getIntent().hasExtra("id_herramienta")) {

            curHerramientaId = (Integer) getIntent().getExtras().getInt("id_herramienta");

        }


        Uri uri = Uri.parse(ImageUrl);



        try {
            bitmap = ImageUtils.decodeUriIntoBitmap(context, uri);
            ImagetoUpload.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }



    private void handleSentAgregar( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d(TAG, "resultado");

                ////////
                if (json.getInt("error") == 1)
                {
                    common.showWarningDialog("! Atención ¡", "No se pudo subir la imagén", context);
                    return;
                }

                if (json.getInt("error") == 0) // No hay errores continuar
                {



                    new AlertDialog.Builder(context)
                            .setTitle("Agregar imagén")
                            .setMessage("!La imagen se ha subido con exito!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();



                }


                ///////////








            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                common.showWarningDialog("! No valido ¡", "No se pudo actualizar", context);
            }
        }
        else {

        }
    }


    private class AgregarImagen extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        Integer id_herramienta;
        Integer id_usuario;
        String cur_image;
        Context context;



        public AgregarImagen( Context _context, Integer idHerramienta ,Integer idUsuario, String convertedImage )
        {
            id_usuario = idUsuario;
            cur_image = convertedImage;
            context = _context;
            id_herramienta = idHerramienta;


        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( context, "Espera un momento..", "Subiendo imagen..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            JSONObject jsonParam = new JSONObject();

            try {

                jsonParam.put("id_herramienta", id_herramienta);
                jsonParam.put("id_usuario", id_usuario);
                jsonParam.put("image_64", cur_image);

            }


            catch (Exception e)
            {
                return null;
            }


            HttpClient.HttpResponse response = HttpClient.postJson( common.API_URL_BASE + "agregarImagen", jsonParam );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentAgregar( result );


        }
    }





}
