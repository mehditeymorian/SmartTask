package ir.timurid.smarttask.db;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import ir.timurid.smarttask.model.Category;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.model.TodoInfo;
import ir.timurid.smarttask.util.LiveDataTestUtil;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private TodoDao todoDao;
    private CategoryDao categoryDao;
    private Database database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        categoryDao = database.categoryDao();
        todoDao = database.todoDao();

    }

    @After
    public void close() {
        database.close();
    }

    @Test
    public void insertCategory() {
        Category category = new Category(-1, "A", "#112233");
        long id = categoryDao.insert(category);


        Category inserted = categoryDao.getCategoryByTitle("A");


        assertEquals(category.getTitle(), inserted.getTitle());
        assertEquals(id, inserted.getCategoryId());
    }

    @Test
    public void insertTodoInfo() {
        String title = "A Very Long Title";
        TodoInfo todoInfo = TodoInfo.builder().categoryId(1)
                .deadline(Calendar.getInstance().getTime())
                .description("This is description of todo")
                .title(title)
                .priority(2)
                .build();

        long id = todoDao.insert(todoInfo);


        TodoInfo inserted = todoDao.getTodoInfoByTitle(title);
        assertEquals(inserted.getTitle(), todoInfo.getTitle());
        assertEquals(id, inserted.getTodoId());
    }

    @Test
    public void readTodo() throws InterruptedException {
        String categoryTitle = "Category A";
        Category category = new Category(-1, categoryTitle, "#12aah3");
        long categoryId = categoryDao.insert(category);


        String todoTitle = "Do School Homework";
        TodoInfo todoInfo = TodoInfo.builder()
                .title(todoTitle)
                .description("Math - Literature - Biology")
                .priority(1)
                .deadline(Calendar.getInstance().getTime())
                .categoryId(categoryId)
                .build();

        todoDao.insert(todoInfo);

        List<Todo> list = LiveDataTestUtil.getValue(todoDao.getAllUndone());



        assertEquals(1, list.size());

        Todo retrievedTodo = list.get(0);

        System.out.println("retrievedTodo = " + retrievedTodo);

        assertEquals(todoTitle, retrievedTodo.getInfo().getTitle());
        assertEquals(categoryTitle, retrievedTodo.getCategoryTitle());


    }


}
