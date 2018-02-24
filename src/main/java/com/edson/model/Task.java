package com.edson.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;


@Entity
@JsonPropertyOrder(value = {"id", "name", "weight", "completed", "createdAt"})
public class Task implements Serializable {

    @Id
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double weight;

    private boolean completed;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Calendar createdAt;

    @JsonIgnore
    @Column(updatable = false)
    private Long job_id;

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Calendar getCreatedAt() {
        if (createdAt == null) {
            return new GregorianCalendar();
        } else {
            return createdAt;
        }
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public Long getJob_id() {
        return job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }

}
