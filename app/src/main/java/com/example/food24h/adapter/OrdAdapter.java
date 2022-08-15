package com.example.food24h.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food24h.CTFoodActivity;
import com.example.food24h.CTOrdActivity;
import com.example.food24h.Drink;
import com.example.food24h.Food;
import com.example.food24h.R;
import com.example.food24h.SPOrd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.admin.v1.Index;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrdAdapter extends RecyclerView.Adapter<OrdAdapter.OrdViewHolder>{
    List<SPOrd> mlist;
    Context context;
    DatabaseReference databaseReference;

    public OrdAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<SPOrd> list){
        this.mlist = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ord, parent, false);
        return new OrdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdViewHolder holder, int position) {
        SPOrd spOrd = mlist.get(position);
        if (spOrd == null){
            return;
        }
//        holder.tvsl.setText(spOrd.type);
        if (spOrd.type.equals("1")){
            databaseReference= FirebaseDatabase.getInstance().getReference("Food").child(spOrd.key_sp_ord);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Food food = snapshot.getValue(Food.class);

                    Picasso.with(holder.imageView.getContext()).load(food.image).into(holder.imageView);
                    holder.tvsl.setText(spOrd.sl_sp_ord);
                    holder.tvtime.setText(spOrd.time_ord);
                    holder.tvname.setText(food.name_food);
                    holder.tvprice.setText(food.price);

                    if (spOrd.status.equals("0")){
                        holder.tvstt.setText("Chờ xác nhận");
                    }
                    if (spOrd.status.equals("1")){
                        holder.tvstt.setText("Đã xác nhận");
                        holder.tvstt.setBackgroundColor(Color.parseColor("#CF9F9F"));
                    }
                    if (spOrd.status.equals("2")){
                        holder.tvstt.setText("Đang giao hàng");
                        holder.tvstt.setBackgroundColor(Color.parseColor("#CF6F6F"));
                    }
                    if (spOrd.status.equals("3")){
                        holder.tvstt.setText("Đã nhận hàng");
                        holder.tvstt.setBackgroundColor(Color.parseColor("#4CAF50"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            databaseReference= FirebaseDatabase.getInstance().getReference("Drink").child(spOrd.key_sp_ord);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Drink drink = snapshot.getValue(Drink.class);

                    Picasso.with(holder.imageView.getContext()).load(drink.image).into(holder.imageView);
                    holder.tvsl.setText(spOrd.sl_sp_ord);
                    holder.tvtime.setText(spOrd.time_ord);
                    holder.tvname.setText(drink.name_drink);
                    holder.tvprice.setText(drink.price);

                    if (spOrd.status.equals("0")){
                        holder.tvstt.setText("Chờ xác nhận");
                    }
                    if (spOrd.status.equals("1")){
                        holder.tvstt.setText("Đã xác nhận");
                        holder.tvstt.setBackgroundColor(Color.parseColor("#CF9F9F"));
                    }
                    if (spOrd.status.equals("2")){
                        holder.tvstt.setText("Đang giao hàng");
                        holder.tvstt.setBackgroundColor(Color.parseColor("#CF6F6F"));
                    }
                    if (spOrd.status.equals("3")){
                        holder.tvstt.setText("Đã nhận hàng");
                        holder.tvstt.setBackgroundColor(Color.parseColor("#4CAF50"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, CTOrdActivity.class);
                intent.putExtra("key_ord",spOrd.temp);
                intent.putExtra("key_user",spOrd.key_user_ord);
                context.startActivities(new Intent[]{intent});
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mlist != null){
            return mlist.size();
        }
        return 0;
    }

    public class OrdViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView tvname,tvsl,tvprice,tvstt,tvtime;

        public OrdViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_ord);
            tvname = itemView.findViewById(R.id.tvname_ord);
            tvprice = itemView.findViewById(R.id.tvprice_ord);
            tvsl = itemView.findViewById(R.id.tvsl_ord);
            tvstt = itemView.findViewById(R.id.tvstt_ord);
            tvtime = itemView.findViewById(R.id.tvtime_ord);
        }
    }

}
