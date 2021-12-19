package com.uts_pbp_d_9229.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.uts_pbp_d_9229.R;
import com.uts_pbp_d_9229.dao.Category;
import com.uts_pbp_d_9229.databinding.ActivityRecyclerViewCategoryBinding;

import java.util.List;

public class RecyclerViewCategory extends RecyclerView.Adapter<RecyclerViewCategory.MyViewHolder> {

    ActivityRecyclerViewCategoryBinding binding;
    private Context context;
    private List<Category> Categorys;
    private OnItemClickListener listener;
    private String selectedCategory;

    public RecyclerViewCategory(){}

    public RecyclerViewCategory(Context context, List<Category> result, String selectedCategory,
                                OnItemClickListener listener){
        this.context = context;
        this.Categorys = result;
        this.listener = listener;
        this.selectedCategory = selectedCategory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_recycler_view_category, parent, false);
        final MyViewHolder holder = new MyViewHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Category category = Categorys.get(position);
        holder.binding.setCategory(category);
        holder.bind(category, listener);
        if(selectedCategory.contains(category.getName())){
            holder.btnCategory.setCardBackgroundColor(Color.BLUE);
            holder.textView.setTextColor(Color.WHITE);
        }else{
            holder.btnCategory.setCardBackgroundColor(Color.WHITE);
            holder.textView.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return (Categorys != null) ? Categorys.size() : 0; }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ActivityRecyclerViewCategoryBinding binding;
        private MaterialCardView btnCategory;
        private TextView textView;

        public MyViewHolder(@NonNull ActivityRecyclerViewCategoryBinding bind){
            super(bind.getRoot());
            this.binding = bind;
        }

        public void bind(final Category category, final OnItemClickListener listener) {
            btnCategory = itemView.findViewById(R.id.btn_category);
            textView = itemView.findViewById(R.id.category_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    selectedCategory = category.getName();
                    listener.onItemClick(category);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
}