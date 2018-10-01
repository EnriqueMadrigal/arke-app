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
import com.datalabor.soporte.arke.models.IViewHolderClick;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context _context;
    private ArrayList<Catalogo> _items;
    private IViewHolderClick _listener;
    private Bitmap _placeHolder;

    public CategoryAdapter(Context context, ArrayList<Catalogo> items, IViewHolderClick listener) {
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
        View view = LayoutInflater.from(_context).inflate(R.layout.catalogo_items, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Catalogo curCatalogo = _items.get(position);
        holder.get_labelProduct().setText(curCatalogo.get_desc());

        holder.setIndex(position);

        String curUrl = curCatalogo.get_imagelink();

        if (curUrl.length()>8) {
            Picasso.with(_context)
                    .load(curUrl)
                    .placeholder(R.drawable.image_notavailable)
                    .into(holder.getIconView());
        }
        else {
            holder.getIconView().setImageResource( R.drawable.image_notavailable );
        }
        //  holder.getIconView().setImageResource(curCategory.getResId());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView _iconView;

        private TextView _labelProduct;
        private TextView _labelDesc;

        private int _index;
        private IViewHolderClick _listener;

        public ViewHolder(View view, IViewHolderClick listener) {
            super(view);

            view.setOnClickListener(this);
            _iconView = (ImageView) view.findViewById(R.id.imgIcon);
            _labelProduct = (TextView) view.findViewById(R.id.lblCategoryTitle);

            _listener = listener;
        }

        public ImageView getIconView() {
            return _iconView;
        }


        public TextView get_labelProduct() {
            return _labelProduct;
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


