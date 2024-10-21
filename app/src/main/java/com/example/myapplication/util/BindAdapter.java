package com.example.myapplication.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目封装的Adapter 搭配RecyclerView食用
 * @param <VB> 列表的Item
 * @param <Data>列表数据类型
 */
public abstract class BindAdapter<VB extends ViewBinding, Data> extends RecyclerView.Adapter<BindHolder<VB>> {
    private List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    @NonNull
    @Override
    public BindHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindHolder<VB> vbBindHolder = new BindHolder<>(createHolder(parent));
        vbBindHolder.getVb().getRoot().animate().translationX(100)

                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        vbBindHolder.getVb().getRoot().animate().translationX(0)
                                .setDuration(1000)
                                .start();
                    }
                })
                .start();
        return vbBindHolder;
    }

    public abstract VB createHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull BindHolder<VB> holder, int position) {
        Data d = data.get(position);
        bind(holder.getVb(), d, position);
    }

    public abstract void bind(VB vb, Data data, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

}
