package ir.timurid.smarttask.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.Date;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.db.Repository;
import ir.timurid.smarttask.model.Category;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.model.TodoInfo;
import lombok.Getter;


public class AddTodoVM extends AndroidViewModel {


    private Repository repository;

    @Getter
    private String[] prioritiesRes;

    @Getter
    private ObservableField<String> titleField = new ObservableField<>();

    @Getter
    private ObservableField<Date> deadlineField = new ObservableField<>();
    @Getter
    private ObservableField<Category> categoryField = new ObservableField<>();
    @Getter
    private ObservableInt priorityField;
    @Getter
    private ObservableField<String> descriptionField = new ObservableField<>();

    private LiveData<Category> defaultCategory;
    private final Observer<Category> categoryObserver;

    private Todo editTodo;
    @Getter
    private ObservableBoolean editMode = new ObservableBoolean();



    public AddTodoVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        prioritiesRes = application.getResources().getStringArray(R.array.priorities);
        priorityField = new ObservableInt(getPriorityDefaultVal());

        defaultCategory = repository.getDefaultCategory();
        categoryObserver = categoryField::set;
        defaultCategory.observeForever(categoryObserver);

    }

    public void insertTodo() {
        long categoryId = 1;
        Category category = categoryField.get();
        if (category != null) categoryId = category.getCategoryId();

        TodoInfo todoInfo = TodoInfo.builder()
                .title(titleField.get())
                .deadline(deadlineField.get())
                .priority(priorityField.get())
                .categoryId(categoryId)
                .description(descriptionField.get())
                .build();


        if (editMode.get()) {
            todoInfo.setTodoId(editTodo.getInfo().getTodoId());
            repository.updateTodo(todoInfo);

        }else repository.insertTodoInfo(todoInfo);

        dismissEditMode();
    }

    public void setDeadline(Date date) {
        if (date == null)
            deadlineField.set(null);
        else
            deadlineField.set(date);
    }

    public void clearFields() {
        titleField.set(null);
        deadlineField.set(null);
        priorityField.set(getPriorityDefaultVal());
        categoryField.set(defaultCategory.getValue());
        descriptionField.set(null);
    }

    private int getPriorityDefaultVal() {
        return prioritiesRes.length - 1;
    }


    //region Edit Mode
    public void setEditTodo(Todo todo) {
        this.editTodo = todo;

        editMode.set(true);

        titleField.set(todo.getInfo().getTitle());
        deadlineField.set(todo.getInfo().getDeadline());
        priorityField.set(todo.getInfo().getPriority());
        descriptionField.set(todo.getInfo().getDescription());

        Category category = new Category(todo.getInfo().getCategoryId(), todo.getCategoryTitle(), todo.getCategoryColor());
        categoryField.set(category);
    }

    public void dismissEditMode() {
        editTodo = null; // nullity of editTodo indicate Create Mode
        editMode.set(false);
        clearFields();
    }

    public boolean isEditMode() {
        return editMode.get();
    }
    //endregion

    @Override
    protected void onCleared() {
        super.onCleared();
        defaultCategory.removeObserver(categoryObserver);
    }
}
