package ir.timurid.smarttask.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Builder;
import lombok.Data;


@Data
@Entity
public class TodoInfo {
    @PrimaryKey(autoGenerate = true)
    private long todoId;
    private String title;
    private String description;
    private int priority; // lower value indicate higher priority
    private Date deadline;
    private long categoryId;
    private Date doneDate;

    @Builder
    public TodoInfo(long todoId, String title, String description, int priority, Date deadline, long categoryId, Date doneDate) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.categoryId = categoryId;
        this.doneDate = doneDate;
    }



}
