package com.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum UserCommand {

    DISPLAY_ALL("display all"),
    DISPLAY_OVERDUE("display overdue"),
    DISPLAY_ALL_TASK_BY_CATEGORY_PARAM("display all tasks by category"),
    DISPLAY_ALL_SUB_TASK_BY_CATEGORY_PARAM("display all sub tasks by category"),
    INSERT_TASK("insert task"),
    UPDATE_TASK_BY_ID_PARAM("update task by id"),
    GET_TASK_BY_ID_PARAM("get task by id"),
    DELETE_TASK_BY_ID_PARAM("delete task by id"),
    INSERT_SUBTASK_BY_TASK_ID_PARAM("insert subtask by task id"),
    UPDATE_SUBTASK_BY_TASK_ID("update subtask by task id"),
    DELETE_SUBTASK_BY_TASK_ID("delete subtask by task id"),
    SEARCH_TASKS_BY_DESCRIPTION_PARAM("search tasks by description"),
    SEARCH_SUB_TASKS_BY_DESCRIPTION_PARAM("search sub tasks by description"),
    ERROR("error"),
    HELP("help"),
    EXIT("exit");

    private String consoleCommand;

    public static UserCommand fromString(String textCommand) {
        for (UserCommand command : UserCommand.values()) {
            if (command.getConsoleCommand().equalsIgnoreCase(textCommand)) {
                return command;
            }
        }
        return ERROR;
    }

    public static List<String> getCommands() {
        return Arrays.stream(UserCommand.values()).
                map(command -> {
                    if (command.name().contains("PARAM")) {
                        return command.getConsoleCommand() + " -p <param>";
                    } else {
                        return command.getConsoleCommand();
                    }
                })
                .toList();
    }

}
