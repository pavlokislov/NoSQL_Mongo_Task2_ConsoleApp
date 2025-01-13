package com.util;

import com.entity.Category;
import com.entity.SubTask;
import com.entity.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleCommandUtil {

    public static String getConsoleInput(BufferedReader reader) {
        try {
            String trim = reader.readLine().trim();
            return trim.isEmpty()
                    ? null
                    : trim;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Task createTaskFromConsole(BufferedReader reader, Task oldTask) {
        System.out.println("Enter task details:");

        System.out.println("Enter Name:");
        String name = Optional.ofNullable(getConsoleInput(reader))
                .orElseGet(oldTask::getName);

        System.out.println("Enter description:");
        String description = Optional.ofNullable(getConsoleInput(reader))
                .orElseGet(oldTask::getDescription);

        System.out.println("Enter creation date (yyyy-mm-dd):");
        LocalDate creationDate = Optional.ofNullable(getConsoleInput(reader))
                .map(LocalDate::parse)
                .orElseGet(oldTask::getCreationDate);


        System.out.println("Enter deadline (yyyy-mm-dd):");
        LocalDate deadline = Optional.ofNullable(getConsoleInput(reader))
                .map(LocalDate::parse)
                .orElseGet(oldTask::getDeadline);

        System.out.println("Enter category:");
        Category category = Optional.ofNullable(getConsoleInput(reader))
                .map(Category::getCategory)
                .orElseGet(oldTask::getCategory);

        List<SubTask> subTasks = createSubTasksFromConsole(reader);

        return Task.builder()
                .name(name)
                .creationDate(creationDate)
                .deadline(deadline)
                .description(description)
                .subTasks(subTasks)
                .category(category)
                .build();
    }

    public static @NotNull List<SubTask> createSubTasksFromConsole(BufferedReader reader) {
        System.out.println("Enter number of sub-tasks:");
        int numSubTasks = Optional.ofNullable(getConsoleInput(reader))
                .map(Integer::parseInt)
                .orElse(0);
        List<SubTask> subTasks = new ArrayList<>();

        for (int i = 0; i < numSubTasks; i++) {
            System.out.println("Enter sub-task " + (i + 1) + " name:");
            String subTaskName = getConsoleInput(reader);

            System.out.println("Enter sub-task " + (i + 1) + " description:");
            String subTaskDescription = getConsoleInput(reader);

            subTasks.add(new SubTask(subTaskName, subTaskDescription));
        }
        return subTasks;
    }

    public static SubTask createSubTaskFromConsole(BufferedReader reader) {

        System.out.println("Enter sub-task name:");
        String subTaskName = getConsoleInput(reader);

        System.out.println("Enter sub-task description:");
        String subTaskDescription = getConsoleInput(reader);

        return new SubTask(subTaskName, subTaskDescription);
    }

    public static int getOrderNumberFromConsole(BufferedReader reader) {
        return Optional.ofNullable(getConsoleInput(reader))
                .map(Integer::parseInt)
                .orElse(-1);
    }
}
