package ir.timurid.smarttask.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ir.timurid.smarttask.databinding.RowCategoryBinding;
import ir.timurid.smarttask.model.Category;
import ir.timurid.smarttask.utils.ColorUtils;

public class CategoriesAdapter extends ListAdapter<Category, CategoriesAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;


    public CategoriesAdapter(OnItemClickListener onItemClickListener) {
        super(new ItemCallBack<>());
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowCategoryBinding binding = RowCategoryBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category item = getItem(position);
        holder.bind(item);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowCategoryBinding binding;

        public ViewHolder(@NonNull RowCategoryBinding binding) {
            super(binding.getRoot());
            binding.layout.setOnClickListener(this);
            this.binding = binding;
        }

        public void bind(Category category) {
            ColorStateList categoryColorState = ColorUtils.getColorStateList(category.getColor());
            binding.setTitle(category.getTitle());
            binding.setColor(categoryColorState);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            Category category = getItem(getAdapterPosition());
            onItemClickListener.onItemClick(category);
        }
    }

    @FunctionalInterface
    public interface OnItemClickListener {

        void onItemClick(Category category);

    }

}

