package com;

import com.entity.Category;
import com.entity.SubTask;
import com.entity.Task;
import com.entity.dao.TaskDao;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class TaskManager {

    public static void main(String[] args) {

        // Create a new task
        SubTask subtask = new SubTask("Sub task 1", "Sub task 1 description");
        Task task = Task.builder()
                .id(new ObjectId())
                .creationDate(LocalDateTime.now())
                .deadline(LocalDate.now())
                .name("Task 1")
                .description("Task 1 description")
                .subTasks(Arrays.asList(subtask))
                .category(Category.HOME)
                .build();

        try (MongoDBProvider provider = new MongoDBProvider("mongodb://localhost:27017")) {
            MongoDatabase database = provider.getDatabase("taskDatabase");
            MongoCollection<Task> collection = database.getCollection("tasks", Task.class);
            collection.insertOne(task);

            TaskDao taskDao = new TaskDao(collection);
            List<Task> all = taskDao.findAll();
            all.forEach(System.out::println);


        }
    }
}