package com;

import com.dao.TaskDao;
import com.dto.Response;
import com.entity.Category;
import com.entity.SubTask;
import com.entity.Task;
import com.enums.UserCommand;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import dev.morphia.Datastore;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.util.ConsoleCommandUtil.createSubTaskFromConsole;
import static com.util.ConsoleCommandUtil.createSubTasksFromConsole;
import static com.util.ConsoleCommandUtil.createTaskFromConsole;
import static com.util.ConsoleCommandUtil.getOrderNumberFromConsole;

public class TaskManager {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try (MongoDBProvider provider = new MongoDBProvider("mongodb://localhost:27017")) {
            Datastore taskDatabase = provider.getDatastore("taskDatabase");
            taskDatabase.getMapper().map(Task.class);

            MongoCollection<Document> collection = taskDatabase.getDatabase().getCollection("tasks");
            collection.createIndex(Indexes.text("description"));
            taskDatabase.ensureIndexes();
            TaskDao taskDao = new TaskDao(taskDatabase);

//            SubTask subtask = new SubTask("Sub Create", "Create something");
//            Task task = Task.builder()
//                    .id(new ObjectId())
//                    .creationDate(LocalDate.now())
//                    .deadline(LocalDate.now())
//                    .name("Task 2")
//                    .description("Task 1 description")
//                    .subTasks(Arrays.asList(subtask))
//                    .category(Category.HOME)
//                    .build();
//            taskDatabase.insert(task);

            System.out.println("Please write command");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.println("Enter a command:");
                String userInput = reader.readLine().trim();

                if (userInput.equals(UserCommand.EXIT.getConsoleCommand())) {
                    break;
                }
                executeCommand(userInput, taskDao, reader).printResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Response executeCommand(String userInput, TaskDao taskDao, BufferedReader reader) {

        String[] split = userInput.split("-p");
        String userParameter = null;
        String userCommand;
        if (split.length == 2) {
            userCommand = split[0].trim();
            userParameter = split[1].trim();
        } else {
            userCommand = userInput;
        }

        var command = UserCommand.fromString(userCommand);
        return switch (command) {
            case DISPLAY_ALL -> new Response(taskDao.findAll());
            case DISPLAY_OVERDUE -> new Response(taskDao.findOverdueTasks());
            case DISPLAY_ALL_TASK_BY_CATEGORY_PARAM ->
                    new Response(taskDao.findTasksByCategory(Category.getCategory(userParameter)));
            case DISPLAY_ALL_SUB_TASK_BY_CATEGORY_PARAM ->
                    new Response(taskDao.findSubTasksByCategory(Category.getCategory(userParameter)));
            case INSERT_TASK -> getResponseForInsertNewTask(taskDao, reader);
            case UPDATE_TASK_BY_ID_PARAM -> getResponseForUpdateTask(taskDao, reader, userParameter);
            case GET_TASK_BY_ID_PARAM -> new Response(List.of(taskDao.get(userParameter)));
            case DELETE_TASK_BY_ID_PARAM -> new Response(taskDao.delete(userParameter));
            case INSERT_SUBTASK_BY_TASK_ID_PARAM -> getResponseForInsertSubtask(taskDao, reader, userParameter);
            case UPDATE_SUBTASK_BY_TASK_ID_PARAM -> getResponseForUpdateSubtask(taskDao, reader, userParameter);
            case DELETE_SUBTASK_BY_TASK_ID_PARAM -> getResponseForDeleteSubtask(taskDao, reader, userParameter);
            case SEARCH_TASKS_BY_DESCRIPTION_PARAM -> new Response(taskDao.searchByDescription(userParameter));
            case SEARCH_SUB_TASKS_BY_NAME_PARAM -> new Response(taskDao.searchBySubTaskName(userParameter));
            case HELP -> new Response(UserCommand.getCommands());
            default -> {
                System.out.println("Invalid command");
                yield new Response(false);
            }
        };
    }

    private static @NotNull Response getResponseForDeleteSubtask(TaskDao taskDao, BufferedReader reader, String userParameter) {
        System.out.println("Enter sub-task order number to delete:");
        int orderNumberToDelete = getOrderNumberFromConsole(reader);
        return new Response(taskDao.deleteSubTaskByOrder(userParameter, orderNumberToDelete));
    }

    private static @NotNull Response getResponseForUpdateSubtask(TaskDao taskDao, BufferedReader reader, String userParameter) {
        System.out.println("Enter sub-task order number to update:");
        int orderNumber = getOrderNumberFromConsole(reader);
        return new Response(taskDao.updateSubTaskByOrder(userParameter, orderNumber, createSubTaskFromConsole(reader)));
    }

    private static @NotNull Response getResponseForInsertSubtask(TaskDao taskDao, BufferedReader reader, String userParameter) {
        Task mainTask = taskDao.get(userParameter);
        List<SubTask> subTasksFromConsole = createSubTasksFromConsole(reader);
        mainTask.getSubTasks().addAll(subTasksFromConsole);
        taskDao.save(mainTask);
        return new Response(List.of(mainTask));
    }

    private static @NotNull Response getResponseForUpdateTask(TaskDao taskDao, BufferedReader reader, String userParameter) {
        System.out.println("To use old value, please press enter");
        Task oldTask = taskDao.get(userParameter);
        Task taskFromConsole = createTaskFromConsole(reader, oldTask);
        return new Response(taskDao.update(userParameter, taskFromConsole));
    }

    private static @NotNull Response getResponseForInsertNewTask(TaskDao taskDao, BufferedReader reader) {
        var task = createTaskFromConsole(reader, new Task());
        taskDao.save(task);
        return new Response(List.of(task));
    }
}