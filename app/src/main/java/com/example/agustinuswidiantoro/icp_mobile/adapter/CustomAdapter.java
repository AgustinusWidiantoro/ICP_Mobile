package com.example.agustinuswidiantoro.icp_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.agustinuswidiantoro.icp_mobile.R;
import com.example.agustinuswidiantoro.icp_mobile.activity.BookActivity;
import com.example.agustinuswidiantoro.icp_mobile.activity.LoginActivity;
import com.example.agustinuswidiantoro.icp_mobile.activity.UpdateBookActivity;
import com.example.agustinuswidiantoro.icp_mobile.model.DataBook;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends ArrayAdapter<DataBook> {

    private ArrayList<DataBook> dataBooks;
    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCreateAt;
        TextView txtFullname;
        TextView txtNameBook;
        TextView txtDescriptionBook;
    }

    public CustomAdapter(Context context, int textViewResourceId,
                         ArrayList<DataBook> dataBooks) {
        super(context, textViewResourceId, dataBooks);
        this.dataBooks = dataBooks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataBooks.size();
    }

    @Override
    public DataBook getItem(int position) {
        return dataBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_row, null, true);

            viewHolder.txtCreateAt = (TextView) convertView.findViewById(R.id.createAt);
            viewHolder.txtFullname = (TextView) convertView.findViewById(R.id.fullname);
            viewHolder.txtNameBook = (TextView) convertView.findViewById(R.id.name_book);
            viewHolder.txtDescriptionBook = (TextView) convertView.findViewById(R.id.description_book);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtCreateAt.setText(dataBooks.get(position).getCreateAt());
        viewHolder.txtFullname.setText(dataBooks.get(position).getFullname());
        viewHolder.txtNameBook.setText(dataBooks.get(position).getName_book());
        viewHolder.txtDescriptionBook.setText(dataBooks.get(position).getDescription_book());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, dataBooks.get(position).getId(), Toast.LENGTH_LONG).show();
                String id = dataBooks.get(position).getId();
                String name = dataBooks.get(position).getName_book();
                String description = dataBooks.get(position).getDescription_book();

                context.startActivity(new Intent(context, UpdateBookActivity.class)
                        .putExtra("Id_book", id)
                        .putExtra("Name_book", name)
                        .putExtra("Description_book", description)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
//        viewHolder.txtCreateAt.setTag(position);


        // Return the completed view to render on screen
        return convertView;
    }
}
