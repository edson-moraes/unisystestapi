package com.edson.ws.resources;


import com.edson.dao.TaskDao;
import com.edson.model.Task;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Path("/tasks")
@Produces("application/json")
public class TaskResource {

    private static TaskDao taskDaoInstance;

    static {
        taskDaoInstance = new TaskDao();
    }

    private Calendar parseDateString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            return null;
        }
        return cal;
    }

    @GET
    public Response listTasks(@QueryParam("createdAt") String createdAt) {

        List<Task> tasks = new ArrayList<>();
        if (createdAt == null || createdAt.isEmpty()) {
            tasks = taskDaoInstance.findAll();
        } else {

            Calendar cal = parseDateString(createdAt);
            if (cal == null) {
                return Response.status(400).entity(tasks).build();
            }
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("createdAt", cal);
            tasks = taskDaoInstance.find(parameters);
        }

        if (tasks.isEmpty()) {
            return Response.noContent().entity(tasks).build();
        } else {
            return Response.ok().entity(tasks).build();
        }
    }

    @POST
    public Response createTask(@Valid Task task) {
        if (task.getId() == null) {
            return Response.status(400).entity(task).build();
        }
        taskDaoInstance.save(task);
        return Response.ok().entity(task).build();
    }

    @Path("/{id}")
    @GET
    public Response getTask(@PathParam("id") Long id) {
        Task task = taskDaoInstance.find(id);
        if (task == null) {
            return Response.noContent().entity(task).build();
        } else {
            return Response.ok().entity(task).build();
        }
    }

    @Path("/{id}")
    @DELETE
    public Response deleteTask(@PathParam("id") Long id) {
        try {
            taskDaoInstance.delete(id);
            return Response.ok().build();
        } catch (NotFoundException ex) {
            return Response.noContent().build();
        }
    }

    @Path("/{id}")
    @PUT
    public Response updateTask(@PathParam("id") Integer id, @Valid Task task) {
        return Response.ok().entity(taskDaoInstance.update(task)).build();
    }

}
