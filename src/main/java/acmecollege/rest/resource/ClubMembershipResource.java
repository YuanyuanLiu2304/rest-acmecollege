
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.CLUB_MEMBERSHIP_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.ClubMembership;


@Path(CLUB_MEMBERSHIP_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClubMembershipResource {
	

	private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
    
    @GET
    public Response getClubMembership() {
        LOG.debug("Retrieving all clubMembership...");
        List<ClubMembership> cm = service.getAll(ClubMembership.class, ClubMembership.FIND_ALL);
   
        LOG.debug("Courses found = {}", cm);
        
        return Response.ok(cm).build();
    }
    
    @GET
    @Path("/{clubmembershipId}")
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getClubmembershipById(@PathParam("clubmembershipId") int clubmembershipId) {
        LOG.debug("Retrieving clubmembership with id = {}", clubmembershipId);
        ClubMembership cm = service.getClubMembershipById(clubmembershipId);
 
        return Response.ok(cm).build();
    }
    
    
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addClubMembership(ClubMembership newcm) {
      	
        LOG.debug("Adding a new clubmembership = {}", newcm);
        service.persistClubMembership(newcm);
        
        return Response.ok(newcm).build();
    }
    
    
    
    @PUT
    @Path("/{clubmembershipId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response updateClubMembership(@PathParam("clubmembershipId") int clubmembershipId, ClubMembership updatingcm) {
        LOG.debug("Updating a specific clubmembership with id = {}", clubmembershipId);
       
        ClubMembership updatedCM = service.updateClubMembership(clubmembershipId, updatingcm);
       
       
        return Response.ok(updatedCM).build();
    }
    
    @DELETE
    @Path("/{lubmembershipId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteClubmembership(@PathParam("lubmembershipId") int clubmembershipId) {
        LOG.debug("Deleting clubmembership with id = {}", clubmembershipId);
       
        service.deleteClubMembershipById(clubmembershipId);
        return Response.ok("ClubMembership id " + clubmembershipId + " delete successfully").build();
    }
    
    
   
}
