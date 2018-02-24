package com.edson.dao;

import com.edson.model.Job;

public class JobDao extends GenericDao<Job, Long> {
    public JobDao() {
        super(Job.class);
    }
}
