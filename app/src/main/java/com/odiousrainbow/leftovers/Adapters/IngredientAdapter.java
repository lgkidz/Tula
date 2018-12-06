package com.odiousrainbow.leftovers.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {
    private List<Ingredient> listIngredient;
    private Gson gson;
    private Context mContext;
    private List<Map<String,String>> stuffsInTula;

    public IngredientAdapter(Context context, List<Ingredient> listIngredient) {
        this.listIngredient = listIngredient;
        this.mContext = context;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.preference_file_key), MODE_PRIVATE);

        String json = sharedPreferences.getString(mContext.getString(R.string.preference_stored_stuff_key),null);
        gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
        if(json != null){
            stuffsInTula = gson.fromJson(json,type);
        }
        else{
            stuffsInTula = new ArrayList<>();
        }

    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rows = LayoutInflater.from(mContext).inflate(R.layout.item_list_ingredient, viewGroup, false);
        return new IngredientHolder(rows);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder ingredientHolder, int i) {
        Ingredient ingredient = listIngredient.get(i);
        ingredientHolder.bindData(ingredient);
        if(!listIngredient.get(i).isSpice()){
            ingredientHolder.tvIngredient.setTextColor(Color.BLACK);
            ingredientHolder.tvQuantity.setTextColor(Color.BLACK);
            for(int j = 0;j<stuffsInTula.size();j++){
                if(stuffsInTula.get(j).get("iName").equals(listIngredient.get(i).getName()) && haveMoreThanNeeded(i)){
                    ingredientHolder.ivCheck.setVisibility(View.VISIBLE);
                    ingredientHolder.haveMoreThanNeeded = true;
                    ingredientHolder.isSpice = false;
                    break;
                }
            }
        }
        else{
            ingredientHolder.tvIngredient.setTextColor(Color.GRAY);
            ingredientHolder.tvQuantity.setTextColor(Color.GRAY);
            //ingredientHolder.ivCheck.setVisibility(View.VISIBLE);
        }
    }

    public boolean haveMoreThanNeeded(int i){
        float quantityHave = 0;
        float quantityNeed;
        String unitNeed = listIngredient.get(i).getUnit().toLowerCase();
        String unitHave = "gram";
        switch (unitNeed){
            case "gram":{
                quantityNeed = Float.parseFloat(listIngredient.get(i).getQuantity());
                break;
            }
            case "kg":{
                quantityNeed = 1000 * Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            case "lạng":{
                quantityNeed = 100 * Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            case "ml":{
                quantityNeed = Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            case "l":{
                quantityNeed = 1000 * Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            default:{
                quantityNeed = Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "countable_unit";
                break;
            }
        }

        for(int j = 0;j<stuffsInTula.size();j++){
            if(stuffsInTula.get(j).get("iName").equals(listIngredient.get(i).getName())){
                String u = stuffsInTula.get(j).get("iUnit").toLowerCase();
                switch (u){
                    case "gram":{
                        quantityHave += Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "kg":{
                        quantityHave += 1000 * Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "lạng":{
                        quantityHave += 100 * Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "ml":{
                        quantityHave += Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "l":{
                        quantityHave += 1000 * Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    default:{
                        quantityHave += Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "countable_unit";
                        break;
                    }
                }
            }
        }



        if(unitHave.equals(unitNeed) && quantityHave >= quantityNeed){
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return listIngredient.size();
    }

    public class IngredientHolder extends RecyclerView.ViewHolder {

        private TextView tvQuantity;
        private TextView tvIngredient;
        private ImageView ivCheck;
        private boolean haveMoreThanNeeded = false;
        private boolean isSpice = true;

        public IngredientHolder(@NonNull View itemView) {
            super(itemView);

            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvIngredient = itemView.findViewById(R.id.tv_ingredient);
            ivCheck = itemView.findViewById(R.id.ic_check);
        }

        public void bindData(Ingredient ingredient) {
            tvQuantity.setText(ingredient.getQuantity() + " " + ingredient.getUnit());
            tvIngredient.setText(ingredient.getName());
        }
    }
}
