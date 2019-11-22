package com.example.cookapptheme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomePageAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<AllRecipeByUser> allRecipeList;


    public HomePageAdapter(Context context, int layout, ArrayList<AllRecipeByUser> allRecipeList) {
        this.context = context;
        this.layout = layout;
        this.allRecipeList = allRecipeList;
    }

    @Override
    public int getCount() {
        return allRecipeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView txtUserName, txtRecipeName, txtSummary, txtDate;
        ImageView imgUserImage, imgRecipeImage;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            // Anh xa
            holder.txtUserName      = view.findViewById(R.id.txtTenTacGia);
            holder.txtRecipeName    = view.findViewById(R.id.textViewTenMonAn);
            holder.txtSummary       = view.findViewById(R.id.textViewTomTatNoiDung);
            holder.txtDate          = view.findViewById(R.id.textViewThoiGianDang);
            holder.imgUserImage     = view.findViewById(R.id.imageViewTacGia);
            holder.imgRecipeImage   = view.findViewById(R.id.imageViewMonAn);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // gọi đối tượng recipe by user
        final AllRecipeByUser allRecipeByUser =allRecipeList.get(position);

        holder.txtUserName.setText(allRecipeByUser.getUserName());
        holder.txtRecipeName.setText(allRecipeByUser.getRecipeName());
        holder.txtSummary.setText(allRecipeByUser.getRecipeSummary());
        holder.txtDate.setText(allRecipeByUser.getRecipeDate());
        if(allRecipeByUser.getUserImage().equals("null")){
            holder.imgUserImage.setImageResource(R.drawable.ic_launcher_background);
        }else{
            Picasso.get().load(allRecipeByUser.getUserImage()).into(holder.imgUserImage);
        }
        Picasso.get().load(allRecipeByUser.getRecipeImage()).into(holder.imgRecipeImage);

        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, allRecipeByUser.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
