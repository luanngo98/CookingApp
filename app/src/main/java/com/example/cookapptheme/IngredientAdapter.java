package com.example.cookapptheme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class IngredientAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Ingredient> ingredientList;

    public IngredientAdapter(Context context, int layout, List<Ingredient> ingredientList) {
        this.context = context;
        this.layout = layout;
        this.ingredientList = ingredientList;
    }

    @Override
    public int getCount() {
        return ingredientList.size();
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
        TextView txtIngredientName;
        ImageView imgDelete;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            // Anh xa
            holder.txtIngredientName = view.findViewById(R.id.txtTenNguyenLieu);
            holder.imgDelete = view.findViewById(R.id.imgDeleteIngredient);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // gọi đối tượng ingredient
        final Ingredient ingredient = ingredientList.get(position);
        holder.txtIngredientName.setText(ingredient.getIngredientName());

        // bắt sự kiện xóa
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ingredientList.remove(i);
//                notifyDataSetChanged();

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("Bạn có muốn xóa '" + ingredient.getIngredientName() + "' không?");
                dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ingredientList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        return view;
    }
}
