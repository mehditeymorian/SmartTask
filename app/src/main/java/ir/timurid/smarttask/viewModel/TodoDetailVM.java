package ir.timurid.smarttask.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ir.timurid.smarttask.db.Repository;
import ir.timurid.smarttask.model.Todo;
import lombok.Getter;

public class TodoDetailVM extends AndroidViewModel {
    private Repository repository;


    @Getter
    private LiveData<Todo> todo;


    public TodoDetailVM(@NonNull Application application, long todoId) {
        super(application);
        repository = new Repository(application);
        todo = repository.getTodoById(todoId);
    }


    public void deleteTodo() {
        repository.deleteTodo(todo.getValue());
    }
}
