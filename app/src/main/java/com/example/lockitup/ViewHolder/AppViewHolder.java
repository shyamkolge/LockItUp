package com.example.lockitup.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lockitup.INterface.AppOnClickListener;
import com.example.lockitup.R;

public class AppViewHolder extends RecyclerView.ViewHolder {

    public ImageView icone_app, lock_app;
    public TextView name_app;

    private AppOnClickListener listener;

    public void setListener(AppOnClickListener listener) {
        this.listener = listener;
    }

    public AppViewHolder(@NonNull View itemView) {
        super(itemView);

        icone_app = itemView.findViewById(R.id.icone_app);
        lock_app =itemView.findViewById(R.id.lock_app);
        name_app = itemView.findViewById(R.id.name_app);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.selectApp(getAdapterPosition());
            }
        });
    }
}
