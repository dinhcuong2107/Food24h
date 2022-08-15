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

import com.example.food24h.CTDrinkActivity;
import com.example.food24h.CTFoodActivity;
import com.example.food24h.Drink;
import com.example.food24h.Food;
import com.example.food24h.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListDrinkAdapter extends ArrayAdapter<Drink> {
    final String TAG = getClass().getSimpleName();
    Context context;
    int resource;
    ListDrinkAdapter.SaveView saveView;
    ArrayList<Drink> arrayListDrink;

    public ListDrinkAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Drink> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayListDrink = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_kos1,parent,false);
            saveView = new ListDrinkAdapter.SaveView();
            saveView.imageView = (ImageView) convertView.findViewById(R.id.item_img1);
            saveView.name = (TextView) convertView.findViewById(R.id.item_name1);
            saveView.price = (TextView) convertView.findViewById(R.id.item_price1);

            convertView.setTag(saveView);
        }else {
            saveView = (ListDrinkAdapter.SaveView) convertView.getTag();
        }

        final Drink drink = arrayListDrink.get(position);
        Picasso.with(context).load(drink.image).into(saveView.imageView);
        saveView.name.setText(drink.name_drink);
        saveView.price.setText(drink.price+" VNĐ");

        saveView.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, CTDrinkActivity.class);
                intent.putExtra("key_sp",drink.text);
                intent.putExtra("key_user",drink.sl);
                context.startActivities(new Intent[]{intent});
            }
        });
        Log.d(TAG,"stt"+position);
        return convertView;
    }
    public class SaveView {
        ImageView imageView;
        TextView price,name;
    }
}
