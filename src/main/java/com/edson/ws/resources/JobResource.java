package com.edson.ws.resources;

import com.edson.dao.JobDao;
import com.edson.model.Job;
import com.edson.model.Task;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/jobs")
@Produces("application/json")
public class JobResource {


    private static JobDao jobDaoInstance;

    static {
        jobDaoInstance = new JobDao();
    }

    private void sortJobsByWeight(List<Job> jobs) {
        jobs.sort((o1, o2) -> {
            double v1 = 0.0, v2 = 0.0;
            for (Task t1 : o1.getTasks()) {
                v1 += t1.getWeight();
            }
            for (Task t2 : o2.getTasks()) {
                v2 += t2.getWeight();
            }
            return (int) (v2 - v1);
        });
    }

    private boolean jobReferenceItself(Job job, Long initialId) {
        if (job.getParentJob() == null) {
            return false;
        }

        if (job.getId() == job.getParentJob().getId()) {
            return true;
        } else if (job.getId() == initialId) {
            return true;
        } else if (job.getParentJob().getId() == initialId) {
            return true;
        } else {
            Job auxJob = jobDaoInstance.find(job.getParentJob().getId());
            if (initialId == null) {
                return jobReferenceItself(auxJob, job.getId());
            } else {
                return jobReferenceItself(auxJob, initialId);
            }
        }
    }

    @GET
    public Response listJobs(@QueryParam("sortByWeight") Boolean sortByWeight) {
        List<Job> jobs = jobDaoInstance.findAll();
        if (sortByWeight != null && sortByWeight.booleanValue()) {
            sortJobsByWeight(jobs);
        }

        if (jobs.isEmpty()) {
            return Response.noContent().entity(jobs).build();
        } else {
            return Response.ok().entity(jobs).build();
        }

    }

    @POST
    public Response createJob(@Valid Job job) {
        if (job.getId() == null) {
            return Response.status(400).entity(job).build();
        }
        if (jobReferenceItself(job, null)) {
            return Response.status(400, "Job's cannot have self references").build();
        }
        jobDaoInstance.save(job);
        return Response.ok().entity(job).build();
    }


    @Path("/{id}")
    @GET
    public Response getJob(@PathParam("id") Long id) throws NotFoundException {
        Job job = jobDaoInstance.find(id);
        if (job == null) {
            return Response.noContent().entity(job).build();
        } else {
            return Response.ok().entity(job).build();
        }
    }


    @Path("/{id}")
    @DELETE
    public Response deleteJob(@PathParam("id") Long id) {
        try {
            jobDaoInstance.delete(id);
            return Response.ok().build();
        } catch (NotFoundException ex) {
            return Response.noContent().build();
        }
    }


    @Path("/{id}")
    @PUT
    public Response updateJob(@PathParam("id") Long id, @Valid Job job) throws NotFoundException {
        if (jobReferenceItself(job, null)) {
            return Response.status(400, "Job's cannot have self references").build();
        }
        return Response.ok().entity(jobDaoInstance.update(job)).build();
    }

}
