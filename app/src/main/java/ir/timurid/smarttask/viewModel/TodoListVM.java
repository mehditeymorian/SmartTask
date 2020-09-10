package ir.timurid.smarttask.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.db.Repository;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.model.TodoInfo;
import ir.timurid.smarttask.utils.DateUtils;
import lombok.Getter;

public class TodoListVM extends AndroidViewModel {

    private Repository repository;

    @Getter
    private int[] prioritiesColors;

    @Getter
    private LiveData<List<Todo>> undoneTodoList;

    @Getter
    private LiveData<List<Todo>> doneTodoList;

    @Getter
    private ObservableBoolean isListEmpty = new ObservableBoolean();

    @Getter
    private MutableLiveData<Boolean> doneTodoNotification;




    public TodoListVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        prioritiesColors = application.getResources().getIntArray(R.array.prioritiesColors);
        undoneTodoList = repository.getAllUndoneTodos();
        doneTodoList = repository.getAllDoneTodos();
        doneTodoNotification = new MutableLiveData<>(false);


    }

    public void setTodoDone(Todo todoDone) {
        TodoInfo info = todoDone.getInfo();
        info.setDoneDate(DateUtils.getToday());
        repository.setTodoDone(info);
        doneTodoNotification.setValue(true);
    }

    public void delete(Todo todo) {
        repository.deleteTodo(todo);
    }


}
