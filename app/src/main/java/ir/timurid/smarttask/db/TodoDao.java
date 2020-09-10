package ir.timurid.smarttask.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.model.TodoInfo;

@Dao
public interface TodoDao {

    String GET_BASE_QUERY = "SELECT TodoInfo.*, Category.title AS categoryTitle, Category.color AS categoryColor " +
            "FROM TodoInfo INNER JOIN Category ON TodoInfo.categoryId == Category.categoryId AND ";



    @Query(GET_BASE_QUERY + "TodoInfo.doneDate IS NULL")
    LiveData<List<Todo>> getAllUndone();

    @Query(GET_BASE_QUERY + "TodoInfo.doneDate IS NOT NULL ORDER BY TodoInfo.doneDate DESC")
    LiveData<List<Todo>> getAllDone();

    @Query(GET_BASE_QUERY + "TodoInfo.todoId == :todoId")
    LiveData<Todo> getTodoById(long todoId);

    @Query("SELECT * FROM TodoInfo WHERE title == :title")
    TodoInfo getTodoInfoByTitle(String title);

    @Insert
    long insert(TodoInfo todoInfo);

    @Update
    void update(TodoInfo info);

    @Delete
    void delete(TodoInfo info);
}
