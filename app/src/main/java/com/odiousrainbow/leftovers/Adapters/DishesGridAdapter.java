package com.odiousrainbow.leftovers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Activities.DishDetailsActivity;
import com.odiousrainbow.leftovers.R;
import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DishesGridAdapter extends RecyclerView.Adapter<DishesGridAdapter.DishViewHolder> {

    private Context mContext;
    private List<Recipe> mData;
    private List<Recipe> mFavoriteDishes;
    private StorageReference mStorageRef;
    private final String KEY_IMAGES_FOLDER = "images";
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Type type;
    private SharedPreferences.Editor editor;

    public DishesGridAdapter(Context context, List<Recipe> data){
        this.mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);
        this.mContext = context;
        this.mData = data;
        mFavoriteDishes = new ArrayList<>();
        sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        gson = new Gson();
        type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        String json = sharedPreferences.getString(mContext.getString(R.string.prefernece_favorite_key),null);
        if(json != null && !json.equals("[]")){
            mFavoriteDishes = gson.fromJson(json,type);
        }

    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.cardview_dish,viewGroup,false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DishViewHolder dishViewHolder,final int i) {
        if(mFavoriteDishes.contains(mData.get(i))){
           dishViewHolder.ibFavorite.setBackgroundResource(R.drawable.ic_favorite_24dp);
        }
        else{
            dishViewHolder.ibFavorite.setBackgroundResource(R.drawable.ic_favorite_border_24dp);
        }
        dishViewHolder.tvDishName.setText(mData.get(i).getName());
        mStorageRef.child(mData.get(i).getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(dishViewHolder.ivDishThumbnail);
            }
        });

        dishViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dishDetailsIntent = new Intent(mContext,DishDetailsActivity.class);
                Recipe r = mData.get(i);
                dishDetailsIntent.putExtra("dish",r);
                mContext.startActivity(dishDetailsIntent);
            }
        });

        dishViewHolder.ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavoriteDishes.contains(mData.get(i))){
                    mFavoriteDishes.remove(mData.get(i));
                    editor = sharedPreferences.edit();
                    String toJson = gson.toJson(mFavoriteDishes);
                    editor.putString(mContext.getString(R.string.prefernece_favorite_key),toJson);
                    editor.apply();
                    dishViewHolder.ibFavorite.setBackgroundResource(R.drawable.ic_favorite_border_24dp);
                }
                else{
                    mFavoriteDishes.add(mData.get(i));
                    editor = sharedPreferences.edit();
                    String toJson = gson.toJson(mFavoriteDishes);
                    editor.putString(mContext.getString(R.string.prefernece_favorite_key),toJson);
                    editor.apply();
                    dishViewHolder.ibFavorite.setBackgroundResource(R.drawable.ic_favorite_24dp);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder{

        TextView tvDishName;
        ImageView ivDishThumbnail;
        CardView cardView;
        Button ibFavorite;


        public DishViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            ivDishThumbnail = itemView.findViewById(R.id.dish_thumbnail);
            ibFavorite = itemView.findViewById(R.id.btn_fav);
            cardView = itemView.findViewById(R.id.card_view_dish);
            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToFav(v);
                }
            });
        }

        public void addToFav(View v){
            Log.d("fav", "addToFav: oke " + this.tvDishName.getText());

            ibFavorite.setBackgroundResource(R.drawable.ic_favorite_24dp);
        }
    }
}
