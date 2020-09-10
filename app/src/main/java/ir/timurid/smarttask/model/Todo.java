package ir.timurid.smarttask.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

import lombok.Data;

@Data
public class Todo implements Model{
    @Embedded
    TodoInfo info;
    @ColumnInfo(name = "categoryTitle")
    private String categoryTitle;
    @ColumnInfo(name = "categoryColor")
    private String categoryColor;


    @Override
    public long getModelId() {
        return info.getTodoId();
    }
}
