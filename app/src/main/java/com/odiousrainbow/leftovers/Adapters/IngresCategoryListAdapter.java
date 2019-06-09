package com.odiousrainbow.leftovers.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.odiousrainbow.leftovers.DataModel.IngredientCategory;
import com.odiousrainbow.leftovers.Helpers.CircleTransform;
import com.odiousrainbow.leftovers.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;

public class IngresCategoryListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<IngredientCategory> categoryList;
    private StorageReference mStorageRef;
    private final String KEY_IMAGES_FOLDER = "ingreCateImages";

    public IngresCategoryListAdapter(Context context, List<IngredientCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryList.get(groupPosition).getProds().size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return categoryList.get(groupPosition).getName();
    }

    public String getGroupName(int groupPosition){
        return categoryList.get(groupPosition).getName();
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return categoryList.get(groupPosition).getProds().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        IngredientCategory cate = categoryList.get(groupPosition);
        String headerTitle = getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ingres_cate_header,null);
        }


        TextView cateName = convertView.findViewById(R.id.ingres_cate_header);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        cateName.setText(headerTitle);
        mStorageRef.child(cate.getImageURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransform()).into(imageView);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ingres_cate_item,null);
        }
            TextView ingreName = convertView.findViewById(R.id.ingres_cate_item);
            ingreName.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
