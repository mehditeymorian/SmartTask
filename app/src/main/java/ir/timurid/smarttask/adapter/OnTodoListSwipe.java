package ir.timurid.smarttask.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.pages.TodoListFragment;
import ir.timurid.smarttask.viewModel.TodoListVM;

public class OnTodoListSwipe extends ItemTouchHelper.SimpleCallback {
    private TodoListFragment parent;
    private ColorDrawable background;
    private Drawable icon;


    public OnTodoListSwipe(TodoListFragment parent) {
        super(0, ItemTouchHelper.END);
        this.parent = parent;


        Context context = parent.requireContext();

        icon = ContextCompat.getDrawable(context, R.drawable.ic_done);
        icon.setTint(Color.WHITE);

        background = new ColorDrawable(context.getResources().getColor(R.color.colorPrimary, null));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(parent.getBinding().todoListRecyclerView);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        TodoAdapter.ViewHolder holder = (TodoAdapter.ViewHolder) viewHolder;
        Todo todo = holder.getBinding().getTodo();
        int position = holder.getAdapterPosition();
        parent.doneOption(todo,position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View view = viewHolder.itemView;

        int left = view.getLeft();
        int top = view.getTop();
        int right = view.getLeft() + (int) dX;
        int bottom = view.getBottom();

        background.setBounds(left, top, right, bottom);

        int iconWidth = icon.getMinimumWidth();
        int iconHorizontalPadding = 42;
        int iconVerticalMargin = (bottom - top - iconWidth)/2 - 20;
        int iconLeft = left + iconHorizontalPadding;
        int iconTop = top + iconVerticalMargin;
        int iconRight = iconLeft + (iconWidth) + iconHorizontalPadding;
        int iconBottom = bottom - iconVerticalMargin;

        icon.setBounds(iconLeft,iconTop, iconRight, iconBottom);

        background.draw(c);
        icon.draw(c);
    }

}
