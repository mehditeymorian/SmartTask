package ir.timurid.smarttask.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity
public class Category implements Model{
    public static final int DEFAULT_CATEGORY_ID = 1;

    @PrimaryKey(autoGenerate = true)
    private long categoryId;
    private String title;
    private String color;


    @Ignore
    public Category() {
    }

    public Category(long categoryId, String title, String color) {
        this.categoryId = categoryId;
        this.title = title;
        this.color = color;
    }


    @Override
    public long getModelId() {
        return getCategoryId();
    }
}
