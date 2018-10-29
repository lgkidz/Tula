package com.odiousrainbow.leftovers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DishesGridAdapter extends RecyclerView.Adapter<DishesGridAdapter.DishViewHolder> {

    private Context mContext;
    private List<Recipe> mData;
    private StorageReference mStorageRef;
    private final String KEY_IMAGES_FOLDER = "images";

    public DishesGridAdapter(Context context, List<Recipe> data){
        mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);
        this.mContext = context;
        this.mData = data;

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
        Log.d("Rec", "onBindViewHolder: " + i);
        dishViewHolder.tvDishName.setText(mData.get(i).getName());
        mStorageRef.child(mData.get(i).getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("uri", uri.toString());
                Picasso.get().load(uri).into(dishViewHolder.ivDishThumbnail);
            }
        });
        dishViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dishDetailsIntent = new Intent(mContext,DishDetailsActivity.class);
                dishDetailsIntent.putExtra("name",mData.get(i).getName());
                mContext.startActivity(dishDetailsIntent);
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

            tvDishName = (TextView) itemView.findViewById(R.id.dish_name);
            ivDishThumbnail = (ImageView) itemView.findViewById(R.id.dish_thumbnail);
            ibFavorite = (Button) itemView.findViewById(R.id.btn_fav);
            cardView = (CardView) itemView.findViewById(R.id.card_view_dish);
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
