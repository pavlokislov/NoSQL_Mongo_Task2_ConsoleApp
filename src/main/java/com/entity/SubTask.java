package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubTask {

    @BsonProperty("name")
    private String name;
    @BsonProperty("description")
    private String description;

    @Override
    public String toString() {
        return "\n - SubTask - "
                + "Name: " + name
                + "Description: " + description + "\n";
    }
}