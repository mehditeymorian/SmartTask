package ir.timurid.smarttask.adapter;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.databinding.RowTodoBinding;
import ir.timurid.smarttask.extra.BindingAdapters;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.ColorUtils;
import ir.timurid.smarttask.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

public class TodoAdapter extends ListAdapter<Todo, TodoAdapter.ViewHolder> {
    public static final int DEADLINE_NOT_SET = -2;
    public static final int DEADLINE_PASSED = -1;
    public static final int DEADLINE_TODAY = 0;
    public static final int DEADLINE_TOMORROW = 1;

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
                onTodoItemListener.onTodoOptionsClick(binding.getTodo(), getAdapterPosition());
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
            binding.setDeadlineMeta(getDeadlineMeta(todo.getInfo().getDeadline()));

            binding.executePendingBindings();
        }

        private String getDeadlineMeta(Date deadline) {
            int remainingDays = DateUtils.getRemainingDays(deadline);
            Resources resources = getBinding().getRoot().getResources();


            switch (remainingDays) {
                case DEADLINE_NOT_SET:
                    return "";
                case DEADLINE_PASSED:
                    return resources.getString(R.string.title_passed);
                case DEADLINE_TODAY:
                    return resources.getString(R.string.title_today);
                case DEADLINE_TOMORROW:
                    return resources.getString(R.string.title_tomorrow);
                default:
                    String extension = resources.getString(R.string.title_daySymbol);
                    return String.format(Locale.getDefault(),"%d %s", remainingDays, extension);
            }
        }


    }


    public interface OnTodoItemListener {

        void onTodoClick(Todo todo);

        void onTodoOptionsClick(Todo todo, int position);

    }


    public void deleteItemAt(int position) {
        // TODO: 9/10/2020 double check for any huge calculation
        List<Todo> list = new ArrayList<>(getCurrentList());
        list.remove(position);
        submitList(list);
    }

    public void addItemAt(Todo todo, int position) {
        // TODO: 9/10/2020 double check for any huge calculation
        List<Todo> list = new ArrayList<>(getCurrentList());
        list.add(position, todo);
        submitList(list);
    }

}


