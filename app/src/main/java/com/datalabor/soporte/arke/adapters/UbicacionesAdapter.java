package com.datalabor.soporte.arke.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.models.Catalogo;
import com.datalabor.soporte.arke.models.Herramienta_Simple;
import com.datalabor.soporte.arke.models.IViewHolderClick;
import com.datalabor.soporte.arke.models.Ubicacion;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UbicacionesAdapter extends RecyclerView.Adapter<UbicacionesAdapter.ViewHolder> {
    private Context _context;
    private ArrayList<Ubicacion> _items;
    private IViewHolderClick _listener;
    private Bitmap _placeHolder;



    public UbicacionesAdapter(Context context, ArrayList<Ubicacion> items, IViewHolderClick listener) {
        _context = context;
        _items = items;
        _listener = listener;
        // _placeHolder = BitmapFactory.decodeResource( context.getResources(), R.drawable.placeholder );
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public UbicacionesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.equipos_items, parent, false);
        UbicacionesAdapter.ViewHolder viewHolder = new UbicacionesAdapter.ViewHolder(view, new IViewHolderClick() {
            @Override
            public void onClick(int position) {
                if (_listener != null) {
                    _listener.onClick(position);
                }
            }
        });

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(UbicacionesAdapter.ViewHolder holder, int position) {
        Ubicacion curUbicacion = _items.get(position);
        holder.get_labelNum().setText(String.valueOf(curUbicacion.get_num()));
        holder.get_labelResponsable().setText(curUbicacion.get_responsable());
        holder.get_labelUbicacion().setText(curUbicacion.get_ubicacion());


        holder.setIndex(position);


    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView get_labelUbicacion() {
            return _labelUbicacion;
        }

        public TextView get_labelResponsable() {
            return _labelResponsable;
        }

        public TextView get_labelNum() {
            return _labelNum;
        }

        private TextView _labelUbicacion;
        private TextView _labelResponsable;
        private TextView _labelNum;


        private int _index;
        private IViewHolderClick _listener;

        public ViewHolder(View view, IViewHolderClick listener) {
            super(view);

            view.setOnClickListener(this);
            _labelUbicacion = (TextView) view.findViewById(R.id.lblUbicacionEquipo);
            _labelResponsable = (TextView) view.findViewById(R.id.lblResponsableEquipo);
            _labelNum = (TextView) view.findViewById(R.id.lblNumEquipo);

            _listener = listener;
        }





        public int getIndex() {
            return _index;
        }
        public void setIndex(int index) {
            _index = index;
        }

        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onClick(_index);
            }
        }
    }


}
