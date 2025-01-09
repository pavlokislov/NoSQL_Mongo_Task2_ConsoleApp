package com.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @BsonProperty("_id")
    private ObjectId id;
    @BsonProperty("creationDate")
    private LocalDateTime creationDate;
    @BsonProperty("deadline")
    private LocalDate deadline;
    @BsonProperty("name")
    private String name;
    @BsonProperty("description")
    private String description;
    @BsonProperty("subTasks")
    private List<SubTask> subTasks;
    @BsonProperty("category")
    private Category category;

    @Override
    public String toString() {
        return "\n--- Task ---\n"
                + "ID: " + id + "\n"
                + "Creation Date: " + creationDate + "\n"
                + "Deadline: " + deadline + "\n"
                + "Name: " + name + "\n"
                + "Description: " + description + "\n"
                + "Subtasks: " + subTasks + "\n"
                + "Category: " + category + "\n";
    }
}
