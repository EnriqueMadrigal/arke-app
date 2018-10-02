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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HerramientasAdapter extends RecyclerView.Adapter<HerramientasAdapter.ViewHolder> {
    private Context _context;
    private ArrayList<Herramienta_Simple> _items;
    private IViewHolderClick _listener;
    private Bitmap _placeHolder;



    public HerramientasAdapter(Context context, ArrayList<Herramienta_Simple> items, IViewHolderClick listener) {
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
    public HerramientasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.herramientas_items, parent, false);
        HerramientasAdapter.ViewHolder viewHolder = new HerramientasAdapter.ViewHolder(view, new IViewHolderClick() {
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
    public void onBindViewHolder(HerramientasAdapter.ViewHolder holder, int position) {
        Herramienta_Simple curHerramienta = _items.get(position);
        holder.get_labelDesc().setText(curHerramienta.get_desc());
        holder.get_labelClave().setText((curHerramienta.get_clave()));
       // holder.get_labelUbicacion().setText(curHerramienta.get_ubicacion());

        holder.setIndex(position);

        String curLink = curHerramienta.get_imagelink();

        if (curLink.length()>8) {
            Picasso.with(_context)
                    .load(curLink)
                    .placeholder(R.drawable.image_notavailable)
                    .into(holder.getIconView());
        }

        else
        {
            holder.getIconView().setImageResource(R.drawable.image_notavailable);
        }

        //holder.getIconView().setImageBitmap( _placeHolder );
        //  holder.getIconView().setImageResource(curCategory.getResId());

    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView _iconView;
        private TextView _labelDesc;

        public TextView get_labelDesc() {
            return _labelDesc;
        }

        public TextView get_labelClave() {
            return _labelClave;
        }



        private TextView _labelClave;



        private int _index;
        private IViewHolderClick _listener;

        public ViewHolder(View view, IViewHolderClick listener) {
            super(view);

            view.setOnClickListener(this);
            _iconView = (ImageView) view.findViewById(R.id.imgHerramienta);
            _labelDesc = (TextView) view.findViewById(R.id.lblHerramientaDesc);
            _labelClave = (TextView) view.findViewById(R.id.lblHerramientaClave);


                    _listener = listener;
        }

        public ImageView getIconView() {
            return _iconView;
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
