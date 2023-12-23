package com.ramish.spotit;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeaturedItemAdapter extends RecyclerView.Adapter<FeaturedItemAdapter.MyViewHolder> {

    List<Item> featuredList;
    Context context;
    String userId;

    public FeaturedItemAdapter(List<Item> featuredList, Context context, String userId) {

        this.featuredList = featuredList;
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View component = LayoutInflater.from(context).inflate(R.layout.featured_item_component, parent, false);
        return new MyViewHolder(component);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String itemId = featuredList.get(position).getItemId();
        String ownerId = featuredList.get(position).getOwner();

        holder.name.setText(featuredList.get(position).getItemName());
        holder.rate.setText("PKR " + featuredList.get(position).getRate().toString() + "/hr");
        holder.city.setText(featuredList.get(position).getCity());
        holder.date.setText("23"+ " " + "Nov");

        Picasso.get().load(featuredList.get(position).getItemImageUrl()).into(holder.img);

        holder.featuredItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                  Intent intent = new Intent(context, ItemDetails.class);
                  intent.putExtra("itemId", itemId);
                  context.startActivity(intent);
                }
        });
    }

    @Override
    public int getItemCount() {

        return (featuredList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, rate, city, date;
        ImageView img;
        CardView featuredItem;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            rate = itemView.findViewById(R.id.rate);
            city = itemView.findViewById(R.id.city);
            date = itemView.findViewById(R.id.date);
            img = itemView.findViewById(R.id.img);
            featuredItem = itemView.findViewById(R.id.featuredItem);
        }
    }
}
