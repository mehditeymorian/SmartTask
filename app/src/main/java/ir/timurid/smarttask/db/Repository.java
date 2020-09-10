package ir.timurid.smarttask.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ir.timurid.smarttask.model.Category;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.model.TodoInfo;

import static android.os.AsyncTask.execute;

public class Repository {

    private Database database;


    public Repository(Context context) {
        database = Database.getInstance(context);
    }

    //region TodoInfo
    public void insertTodoInfo(TodoInfo todoInfo) {
        execute(() -> database.todoDao().insert(todoInfo));
    }

    public LiveData<List<Todo>> getAllUndoneTodos() {
        return database.todoDao().getAllUndone();
    }

    public LiveData<List<Todo>> getAllDoneTodos() {
        return database.todoDao().getAllDone();
    }

    public LiveData<Todo> getTodoById(long todoId) {
        return database.todoDao().getTodoById(todoId);
    }

    public void setTodoDone(TodoInfo info) {
        execute(() -> database.todoDao().update(info));
    }

    public void updateTodo(TodoInfo todoInfo) {
        execute(() -> database.todoDao().update(todoInfo));
    }

    public void deleteTodo(Todo todo) {
        execute(() -> database.todoDao().delete(todo.getInfo()));

    }

    //endregion

    //region Category

    public void insertCategory(Category category) {
        execute(() -> database.categoryDao().insert(category));
    }

    public LiveData<List<Category>> getAllCategories() {
        return database.categoryDao().getAll();
    }

    public void deleteCategory(Category category) {
        execute(() -> database.categoryDao().delete(category));
    }

    public LiveData<Category> getDefaultCategory() {
        return database.categoryDao().getDefaultCategory();
    }

    public void updateCategory(Category category) {
        execute(() -> database.categoryDao().update(category));
    }
    //endregion
}
