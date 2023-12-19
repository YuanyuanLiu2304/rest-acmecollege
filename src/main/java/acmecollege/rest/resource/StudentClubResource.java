
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.STUDENT_CLUB_RESOURCE_NAME;
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
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.StudentClub;

@Path(STUDENT_CLUB_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentClubResource {
    
    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
    
    @GET
    public Response getStudentClubs() {
        LOG.debug("Retrieving all student clubs...");
        List<StudentClub> studentClubs = service.getAllStudentClubs();
        LOG.debug("Student clubs found = {}", studentClubs);
        Response response = Response.ok(studentClubs).build();
        return response;
    }
    
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path("/{studentClubId}")
    public Response getStudentClubById(@PathParam("studentClubId") int studentClubId) {
        LOG.debug("Retrieving student club with id = {}", studentClubId);
        StudentClub studentClub = service.getStudentClubById(studentClubId);
        Response response = Response.ok(studentClub).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{studentClubId}")
    public Response deleteStudentClub(@PathParam("studentClubId") int scId) {
        LOG.debug("Deleting student club with id = {}", scId);
        service.deleteStudentClub(scId);
        Response response = Response.ok("StudentClub id " + scId + " delete successfully").build();
        return response;
    }
    

    
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addStudentClub(StudentClub newStudentClub) {
        LOG.debug("Adding a new student club = {}", newStudentClub);
        if (service.isDuplicated(newStudentClub)) {
            HttpErrorResponse err = new HttpErrorResponse(Status.CONFLICT.getStatusCode(), "Entity already exists");
            return Response.status(Status.CONFLICT).entity(err).build();
        }
        else {
            StudentClub tempStudentClub = service.persistStudentClub(newStudentClub);
            return Response.ok(tempStudentClub).build();
        }
    }

    
    @PUT
    @Path("/{studentClubId}")
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response updateStudentClub(@PathParam("studentClubId") int scId, StudentClub updatingStudentClub) {
        LOG.debug("Updating a specific student club with id = {}", scId);
        Response response = null;
        StudentClub updatedStudentClub = service.updateStudentClub(scId, updatingStudentClub);
        response = Response.ok(updatedStudentClub).build();
        return response;
    }
    
    
    @POST
    @Path("/{studentClubId}/clubmembership")
    @RolesAllowed({ADMIN_ROLE})
    public Response addClubMembershipToStudentClub(@PathParam("studentClubId") int scId, ClubMembership newClubMembership) {
        LOG.debug( "Adding a new ClubMembership to student club with id = {}", scId);
        
        StudentClub sc = service.getStudentClubById(scId);
        newClubMembership.setStudentClub(sc);
        sc.getClubMemberships().add(newClubMembership);
        service.updateStudentClub(scId, sc);
        
        return Response.ok(sc).build();
    }

    
    
}
