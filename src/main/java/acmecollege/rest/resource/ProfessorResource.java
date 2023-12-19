
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.PROFESSOR_SUBRESOURCE_NAME;
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
import acmecollege.entity.CourseRegistration;
import acmecollege.entity.Professor;


@Path(PROFESSOR_SUBRESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfessorResource {
	
	private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
   
    
    @GET
    public Response getProfessors() {
        LOG.debug("Retrieving all professors...");
        List<Professor> professors = service.getAll(Professor.class,Professor.ALL_PROFESSOR_QUERY);
        LOG.debug("professors found = {}", professors);
        
        return Response.ok(professors).build();
    }
    
    @GET
    @Path("/{professorId}")
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getProfessorById(@PathParam("professorId") int professorId) {
        LOG.debug("Retrieving professor with id = {}", professorId);
        Professor professor= service.getById(Professor.class,Professor.PROFESSOR_BY_ID_QUERY, professorId);
        
        return Response.ok(professor).build();
    }
    
   
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addProfessor(Professor newProfessor) {
    	
        LOG.debug("Adding a new professo = {}", newProfessor);
       
        service.persistProfessor(newProfessor);
        
        return Response.ok(newProfessor).build();
       
    }

   
    @PUT
    @Path("/{professorId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response updateProfessor(@PathParam("professorId") int professorId, Professor updatingProfessor) {
        LOG.debug("Updating a specific professor with id = {}", professorId);
       
        Professor updatedProfessor = service.updateProfessorById(professorId, updatingProfessor);
       
       
        return Response.ok(updatedProfessor).build();
    }
    
    
    @DELETE
    @Path("/{professorId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteProfessor(@PathParam("professorId") int professorId) {
        LOG.debug("Deleting professor with id = {}", professorId);
       
        service.deleteProfessorById(professorId);
        return Response.ok("Professor id " + professorId + " delete successfully").build();
    }
    
   

    
    @POST
    @Path("/{professorId}/courseId")
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourseRegistrationToProfessor(@PathParam("professorId") int professorId, CourseRegistration courseRegistration) {
        LOG.debug( "Adding a new CourseRegistration to a professor with id = {}", professorId);
        
        Professor professor = service.getById(Professor.class,Professor.PROFESSOR_BY_ID_QUERY, professorId);
        courseRegistration.setProfessor(professor);
        professor.getCourseRegistrations().add(courseRegistration);
        service.updateProfessorById(professorId, professor);
                
        return Response.ok(sc).build();
    }


}
