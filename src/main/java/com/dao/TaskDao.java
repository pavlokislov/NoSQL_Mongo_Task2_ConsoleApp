package com.dao;

import com.entity.Category;
import com.entity.SubTask;
import com.entity.Task;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.UpdateOptions;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperators;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class TaskDao {

    private Datastore datastore;

    public TaskDao(Datastore datastore) {
        this.datastore = datastore;
    }

    public void save(Task task) {
        datastore.save(task);
    }

    public Task get(String id) {
        return datastore.find(Task.class)
                .filter(Filters.eq("_id", new ObjectId(id)))
                .first();
    }

    public boolean delete(String id) {
        DeleteResult result = datastore.find(Task.class)
                .filter(Filters.eq("_id", new ObjectId(id)))
                .delete();
        return result.wasAcknowledged();
    }

    public List<Task> findAll() {
        return datastore.find(Task.class)
                .stream()
                .toList();
    }

    public List<Task> findOverdueTasks() {
        LocalDate today = LocalDate.now();
        return datastore.find(Task.class).filter(Filters.lt("deadline", today))
                .stream()
                .toList();
    }

    public List<Task> findTasksByCategory(Category category) {
        return datastore.find(Task.class)
                .filter(Filters.eq("category", category))
                .stream()
                .toList();
    }

    public List<SubTask> findSubTasksByCategory(Category category) {
        return findTasksByCategory(category).stream()
                .map(Task::getSubTasks)
                .flatMap(List::stream)
                .toList();
    }

    public boolean update(String id, Task task) {

        UpdateOptions options = new UpdateOptions();
        UpdateResult result = datastore.find(Task.class)
                .filter(Filters.eq("_id", new ObjectId(id)))
                .update(
                        options,
                        UpdateOperators.set("name", task.getName()),
                        UpdateOperators.set("description", task.getDescription()),
                        UpdateOperators.set("deadline", task.getDeadline()),
                        UpdateOperators.set("category", task.getCategory()),
                        UpdateOperators.set("subTasks", task.getSubTasks())
                );
        return result.wasAcknowledged();
    }

    public List<Task> searchByDescription(String searchTerm) {
        return datastore.find(Task.class)
                .filter(Filters.text(searchTerm))
                .stream()
                .toList();
    }

    public List<Task> searchBySubTaskName(String searchTerm) {
        return datastore.find(Task.class)
                .filter(Filters.regex("subTasks.name").pattern(searchTerm).caseInsensitive())
                .stream()
                .toList();
    }

    public boolean deleteSubTaskByOrder(String taskId, int order) {
        Task task = datastore.find(Task.class)
                .filter(Filters.eq("_id", new ObjectId(taskId)))
                .first();
        if (task == null) {
            System.out.println("Task not found");
            return false;
        }
        if (task.getSubTasks().size() >= order && order > 0) {
            task.getSubTasks().remove(order - 1);
            return update(task.getId(), task);
        } else {
            System.out.println("Invalid subtask order.");
            return false;
        }
    }

    public boolean updateSubTaskByOrder(String taskId, int order, SubTask newSubTaskName) {
        Task task = datastore.find(Task.class)
                .filter(Filters.eq("_id", new ObjectId(taskId)))
                .first();
        if (task == null) {
            System.out.println("Task not found");
            return false;
        }
        if (task.getSubTasks().size() >= order && order > 0) {
            task.getSubTasks().get(order-1).setName(newSubTaskName.getName());
            task.getSubTasks().get(order-1).setName(newSubTaskName.getDescription());
            return update(task.getId(), task);
        } else {
            System.out.println("Invalid subtask order.");
            return false;
        }
    }
}
