package ir.timurid.smarttask.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ir.timurid.smarttask.databinding.RowTodoBinding;
import ir.timurid.smarttask.extra.BindingAdapters;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;

public class TodoAdapter extends ListAdapter<Todo, TodoAdapter.ViewHolder> {

    private OnTodoItemListener onTodoItemListener;
    private int[] prioritiesColorRes;
    @Setter
    private boolean doneList;


    public TodoAdapter(OnTodoItemListener onTodoItemListener, int[] prioritiesColors) {
        super(new ItemCallBack<>());
        this.onTodoItemListener = onTodoItemListener;
        this.prioritiesColorRes = prioritiesColors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowTodoBinding binding = RowTodoBinding.inflate(inflater, parent, false);
        binding.setIsDoneList(doneList);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo item = getItem(position);
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Getter
        private RowTodoBinding binding;

        public ViewHolder(RowTodoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> onTodoItemListener.onTodoClick(binding.getTodo()));
            binding.getRoot().setOnLongClickListener(v -> {
                onTodoItemListener.onTodoOptionsClick(binding.getTodo());
                return true;
            });
        }

        public void bind(Todo todo) {
            binding.setTodo(todo);

            ColorStateList priorityColor = ColorUtils.getColorStateList(prioritiesColorRes[todo.getInfo().getPriority()]);
            binding.setPriorityColor(priorityColor);

            int priorityIconRes = BindingAdapters.prioritiesIconsRes[todo.getInfo().getPriority()];
            Drawable priorityIcon = ContextCompat.getDrawable(itemView.getContext(), priorityIconRes);
            binding.setPriorityIcon(priorityIcon);
            binding.setCategoryAvailable(todo.getCategoryColor() != null);
            binding.setCategoryColor(ColorUtils.getColorStateList(todo.getCategoryColor()));

            binding.executePendingBindings();
        }


    }


    public interface OnTodoItemListener {

        void onTodoClick(Todo todo);

        void onTodoOptionsClick(Todo todo);

    }

}


