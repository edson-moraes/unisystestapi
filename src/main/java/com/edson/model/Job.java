package com.edson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@JsonPropertyOrder(value = {"id", "name", "active", "parentJob", "tasks"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job implements Serializable {

    @Id
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "job_id")
    private Set<Task> tasks;

    @OneToOne(fetch = FetchType.LAZY)
    private Job parentJob;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Job getParentJob() {
        return parentJob;
    }

    public void setParentJob(Job parentJob) {
        this.parentJob = parentJob;
    }
}
