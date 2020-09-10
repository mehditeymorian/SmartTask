package ir.timurid.smarttask.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ir.timurid.smarttask.databinding.ItemColorBinding;
import ir.timurid.smarttask.utils.ColorUtils;

public class ColorPickerAdapter extends ListAdapter<String, ColorPickerAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;


    public ColorPickerAdapter(OnItemClickListener onItemClickListener) {
        super(DIFF_UTIL);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemColorBinding binding = ItemColorBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemColorBinding binding;

        public ViewHolder(ItemColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                String item = getItem(getAdapterPosition());
                onItemClickListener.onItemClick(item);
            });
        }

        public void bind(String color) {
            binding.setColor(ColorUtils.parseColor(color));
            binding.executePendingBindings();
        }
    }

    static final DiffUtil.ItemCallback<String> DIFF_UTIL = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };


    public interface OnItemClickListener {

        void onItemClick(String color);
    }
}

