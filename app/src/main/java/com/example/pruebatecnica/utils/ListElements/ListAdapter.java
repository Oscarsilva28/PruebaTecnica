package com.example.pruebatecnica.utils.ListElements;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebatecnica.R;
import com.example.pruebatecnica.activities.AsteroidActivity;
import com.example.pruebatecnica.utils.models.Asteroid;
import com.example.pruebatecnica.utils.shared.IdAsteroidUtility;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() { return mData.size(); }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = mInflater.inflate(R.layout.list_element, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListElement> items) { mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView name, magnitude, isPotentiallyHazardous;

       ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            name = itemView.findViewById(R.id.nameTextView);
            magnitude = itemView.findViewById(R.id.magnitudeTextView);
            isPotentiallyHazardous = itemView.findViewById(R.id.isPotentiallHazardousTextView);
        }

        void bindData(final ListElement item) {
           try {
               URL newUrl = new URL(item.getPhoto());
               Bitmap mIcon_val = BitmapFactory.decodeStream(newUrl.openConnection() .getInputStream());
               iconImage.setImageBitmap(mIcon_val);
           } catch (Exception e) {
               Log.w("photo", e);
           }
            name.setText(item.getName());
           name.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   IdAsteroidUtility.setId(context,item.id);
                   Intent intent = new Intent(context.getApplicationContext(), AsteroidActivity.class);
                   context.startActivity(intent);
               }
           });
            magnitude.setText("Magnitude: " + item.getMagnitude());
            isPotentiallyHazardous.setText(item.getIsPotentiallHazardous() ? "⚠️" : "✅");
        }
    }
}
