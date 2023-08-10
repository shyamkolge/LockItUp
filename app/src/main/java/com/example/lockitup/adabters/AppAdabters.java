package com.example.lockitup.adabters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lockitup.INterface.AppOnClickListener;
import com.example.lockitup.Model.AppItem;
import com.example.lockitup.R;
import com.example.lockitup.ViewHolder.AppViewHolder;
import com.example.lockitup.utils.Utils;

import java.util.List;

public class AppAdabters extends RecyclerView.Adapter<AppViewHolder> {

    private Context mContext;
    private List<AppItem> appItemList;
    private Utils utils;
    public AppAdabters(Context mContext, List<AppItem> appItemList) {
        this.mContext = mContext;
        this.appItemList = appItemList;
        this.utils = new Utils(mContext);
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_apps,parent,false);

        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {

        holder.name_app.setText(appItemList.get(position).getName());
        holder.icone_app.setImageDrawable(appItemList.get(position).getIcone());

        String pk = appItemList.get(position).getPackageName();

        if (utils.isLock(pk)){
            holder.lock_app.setImageResource(R.drawable.outline_lock_24);

        }else {
            holder.lock_app.setImageResource(R.drawable.baseline_lock_open_24);
        }

        holder.setListener(new AppOnClickListener() {
            @Override
            public void selectApp(int pos) {

                if (utils.isLock(pk)){

                    holder.lock_app.setImageResource(R.drawable.baseline_lock_open_24);
                    utils.unLock(pk);

                }else {
                    holder.lock_app.setImageResource(R.drawable.outline_lock_24);
                    utils.lock(pk);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return appItemList.size();
    }
}
