package com.datalabor.soporte.arke.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.models.Catalogo;
import com.datalabor.soporte.arke.models.IViewHolderClick;
import com.datalabor.soporte.arke.models.Obra;
import com.datalabor.soporte.arke.models.Responsable;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ResponsablesAdapter extends RecyclerView.Adapter<ResponsablesAdapter.ViewHolder> {
    private Context _context;
    private ArrayList<Responsable> _items;
    private IViewHolderClick _listener;
    private Bitmap _placeHolder;
    Integer row_index = -1;

    public ResponsablesAdapter(Context context, ArrayList<Responsable> items, IViewHolderClick listener) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.responsables_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, new IViewHolderClick() {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Responsable curResponsable = _items.get(position);
        holder.get_labelName().setText(curResponsable.get_name());
        holder.get_labelDesc().setText(curResponsable.get_desc());
        holder.setIndex(position);

        ////////
        holder.get_imageIcon().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position) {
            holder.get_imageIcon().setImageResource(R.drawable.check_on);
            _listener.onClick(position);
            //sClickListener.onItemClick(getAdapterPosition());
            //holder.get_linearLayout().setBackgroundColor(Color.parseColor("#567845"));

        }
        else
        {
            //holder.get_linearLayout().setBackgroundColor(Color.parseColor("#ffffff"));
            holder.get_imageIcon().setImageResource(R.drawable.check_off);
        }



        ////////


    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView _labelName;

        public ImageView get_imageIcon() {
            return _imageIcon;
        }

        public void set_imageIcon(ImageView _imageIcon) {
            this._imageIcon = _imageIcon;
        }

        private ImageView _imageIcon;

        public TextView get_labelName() {
            return _labelName;
        }

        public TextView get_labelDesc() {
            return _labelDesc;
        }
        public LinearLayout get_linearLayout() {return _linearLayout;}

        private TextView _labelDesc;
        private LinearLayout _linearLayout;

        private int _index;
        private IViewHolderClick _listener;

        public ViewHolder(View view, IViewHolderClick listener) {
            super(view);

            view.setOnClickListener(this);

            _labelName = (TextView) view.findViewById(R.id.lblNameResponsableItem);
            _labelDesc = (TextView) view.findViewById(R.id.lblDescResponsableItem);
            _linearLayout = (LinearLayout) view.findViewById(R.id.LinearResponsableItem);
            _imageIcon = (ImageView) view.findViewById(R.id.ImgResponsableItem);

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


