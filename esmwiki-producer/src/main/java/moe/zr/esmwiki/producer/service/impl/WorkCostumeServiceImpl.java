package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.WorkCostume;
import moe.zr.esmwiki.producer.repository.WorkCostumeRepository;
import moe.zr.service.WorkCostumeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@DubboService
@Path("/api/work/costume")
public class WorkCostumeServiceImpl implements WorkCostumeService {
    final
    WorkCostumeRepository repository;

    public WorkCostumeServiceImpl(WorkCostumeRepository workCostumeRepository) {
        this.repository = workCostumeRepository;
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public WorkCostume save(WorkCostume workCostume) {
        return repository.save(workCostume);
    }

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public WorkCostume query(WorkCostume workCostume) {
        String id = workCostume.getId();
        if (id == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
        Optional<WorkCostume> byId = repository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
