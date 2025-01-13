package com.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity("tasks")
//@Indexes({@Index(fields = {@Field("description")})})
public class Task {

    @Id
    private String id;
    private LocalDate creationDate;
    private LocalDate deadline;
    private String name;
    private String description;
    private List<SubTask> subTasks = new ArrayList<>();
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
