package ir.timurid.smarttask.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

public class TodoDetailFactory implements ViewModelProvider.Factory {
    private Application application;
    private long todoId;


    @Inject
    public TodoDetailFactory(Application application, long todoId) {
        this.application = application;
        this.todoId = todoId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TodoDetailVM(application, todoId);
    }
}
