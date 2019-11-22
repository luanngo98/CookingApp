package com.example.cookapptheme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class InstructionAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Instruction> instructionList;

    public InstructionAdapter(Context context, int layout, List<Instruction> instructionList) {
        this.context = context;
        this.layout = layout;
        this.instructionList = instructionList;
    }

    @Override
    public int getCount() {
        return instructionList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView txtInstruction;
        ImageView imgDeleteInstruction, imgInstruction;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            // Anh xa
            viewHolder.txtInstruction = view.findViewById(R.id.txtDescribtion);
            viewHolder.imgInstruction = view.findViewById(R.id.imgInstruction);
            viewHolder.imgDeleteInstruction = view.findViewById(R.id.imgDeleteInstruction);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // gọi đối tượng instruction
        final Instruction instruction = instructionList.get(position);
        viewHolder.txtInstruction.setText(instruction.getDescribtion());
        // convert byte[] -> bitmap to assign imageview
        byte[] image =instruction.getDescribetionImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length); // BitmapFactory được sử dụng để decode. Để decode từ mảng -> decodeByteArray
        viewHolder.imgInstruction.setImageBitmap(bitmap);

        // bắt sự kiện xóa
        viewHolder.imgDeleteInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("Bạn có muốn xóa '" + instruction.getDescribtion() + "' không?");
                dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        instructionList.remove(position);
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
