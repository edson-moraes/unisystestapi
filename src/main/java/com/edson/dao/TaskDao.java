package com.edson.dao;

import com.edson.model.Task;

public class TaskDao extends GenericDao<Task, Long> {
    public TaskDao() {
        super(Task.class);
    }
}
