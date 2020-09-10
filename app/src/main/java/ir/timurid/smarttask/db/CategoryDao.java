package ir.timurid.smarttask.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.timurid.smarttask.model.Category;

@Dao
public interface CategoryDao {


    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM Category WHERE title == :title")
    Category getCategoryByTitle(String title);

    @Query("SELECT * FROM Category WHERE categoryId == 1")
    LiveData<Category> getDefaultCategory();


    @Insert
    long insert(Category category);

    @Delete
    void delete(Category category);

    @Update
    void update(Category category);
}
