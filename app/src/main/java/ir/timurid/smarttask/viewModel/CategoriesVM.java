package ir.timurid.smarttask.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ir.timurid.smarttask.db.Repository;
import ir.timurid.smarttask.model.Category;
import lombok.Getter;

public class CategoriesVM extends AndroidViewModel {
    private Repository repository;

    @Getter
    private LiveData<List<Category>> categories;


    public CategoriesVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        categories = repository.getAllCategories();
    }

    public void deleteCategory(Category category) {
        repository.deleteCategory(category);
    }

}
