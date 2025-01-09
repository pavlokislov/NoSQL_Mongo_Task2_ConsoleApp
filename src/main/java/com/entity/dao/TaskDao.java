package com.entity.dao;

import com.entity.Task;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TaskDao {

    private final MongoCollection<Task> taskCollection;

    public List<Task> findAll() {
        return taskCollection.find().into(new ArrayList<Task>());
    }

    public List<Task> findOverdueTasks() {
        LocalDate today = LocalDate.now();
        return taskCollection.find(Filters.lt("deadline", today)).into(new ArrayList<>());
    }
}
