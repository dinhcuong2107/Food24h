package com.example.food24h.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.food24h.CTFoodActivity;
import com.example.food24h.Food;
import com.example.food24h.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListFoodAdapter extends ArrayAdapter<Food> {
    final String TAG = getClass().getSimpleName();
    Context context;
    int resource;
    ListFoodAdapter.SaveView saveView;
    ArrayList<Food> arrayList;
    public ListFoodAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Food> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_kos,parent,false);
            saveView = new SaveView();
            saveView.imageView = (ImageView) convertView.findViewById(R.id.item_img);
            saveView.name = (TextView) convertView.findViewById(R.id.item_name);
            saveView.price = (TextView) convertView.findViewById(R.id.item_price);

            convertView.setTag(saveView);
        }else {
            saveView = (ListFoodAdapter.SaveView) convertView.getTag();
        }

        final Food food = arrayList.get(position);
        Picasso.with(context).load(food.image).into(saveView.imageView);
        saveView.name.setText(food.name_food);
        saveView.price.setText(food.price+" VNĐ");

        saveView.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, CTFoodActivity.class);
                intent.putExtra("key_sp",food.text);
                intent.putExtra("key_user",food.sl);
                context.startActivities(new Intent[]{intent});
            }
        });
        Log.d(TAG,"stt"+position);
        return convertView;
    }
    public class SaveView{
        ImageView imageView;
        TextView price,name;
    }
}
