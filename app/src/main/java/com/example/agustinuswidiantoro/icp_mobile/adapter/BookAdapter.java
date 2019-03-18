package com.example.agustinuswidiantoro.icp_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agustinuswidiantoro.icp_mobile.R;
import com.example.agustinuswidiantoro.icp_mobile.activity.UpdateBookActivity;
import com.example.agustinuswidiantoro.icp_mobile.model.DataBook;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<DataBook> dataBooks;

    public BookAdapter(Context context, ArrayList<DataBook> dataBooks) {
        this.context = context;
        this.dataBooks = dataBooks;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.list_row, parent, false);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {

        ViewHolder myHolder = (ViewHolder) holder;

        myHolder.txtCreateAt.setText(dataBooks.get(position).getCreateAt());
        myHolder.txtFullname.setText(dataBooks.get(position).getFullname());
        myHolder.txtNameBook.setText(dataBooks.get(position).getName_book());
        myHolder.txtDescriptionBook.setText(dataBooks.get(position).getDescription_book());

        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String id = dataBooks.get(position).getId();
                String name = dataBooks.get(position).getName_book();
                String description = dataBooks.get(position).getDescription_book();

                context.startActivity(new Intent(context, UpdateBookActivity.class)
                        .putExtra("Id_book", id)
                        .putExtra("Name_book", name)
                        .putExtra("Description_book", description)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        };

        myHolder.txtCreateAt.setOnClickListener(clickListener);
        myHolder.txtFullname.setOnClickListener(clickListener);
        myHolder.txtNameBook.setOnClickListener(clickListener);
        myHolder.txtDescriptionBook.setOnClickListener(clickListener);

    }

    @Override
    public int getItemCount() {
        return dataBooks.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtCreateAt;
        TextView txtFullname;
        TextView txtNameBook;
        TextView txtDescriptionBook;

        // create constructor to get widget reference
        public ViewHolder(View itemView) {
            super(itemView);
            txtCreateAt = (TextView) itemView.findViewById(R.id.createAt);
            txtFullname = (TextView) itemView.findViewById(R.id.fullname);
            txtNameBook = (TextView) itemView.findViewById(R.id.name_book);
            txtDescriptionBook = (TextView) itemView.findViewById(R.id.description_book);
        }

    }
}
