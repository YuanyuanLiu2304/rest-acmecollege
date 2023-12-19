
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.COURSE_REGISTRATION_RESOURCE_NAME;
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
import acmecollege.entity.CourseRegistrationPK;


@Path(COURSE_REGISTRATION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseRegistrationResource {
	
	
	private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
   
    
    @GET
    public Response getCourseRegistrations() {
        LOG.debug("Retrieving all Course Registrations...");
        List<CourseRegistration> courseRegistrations = service.getAll(CourseRegistration.class,CourseRegistration.ALL_COURSEREGISTRATION_QUERY);
        LOG.debug("Courses found = {}",courseRegistrations);
        
        return Response.ok(courseRegistrations).build();
    }
    
    @GET
    @Path("/{studentId}/{courseId}")
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getCourseRegistrationById(@PathParam("studentId") int studentId,@PathParam("courseId") int courseId) {
        LOG.debug("Retrieving course registration with studentId = {} courseId = {}", studentId,courseId);
        CourseRegistrationPK id = new CourseRegistrationPK(studentId,courseId);
        CourseRegistration cr = service.getCourseRegistrationById(id);
        return Response.ok(cr).build();
    }
    
  
   
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourseRegistration(CourseRegistration courseRegistration) {
    	
        LOG.debug("Adding a new CourseRegistration = {}", courseRegistration);
       
        service.persistCourseRegistration(courseRegistration);
        
        return Response.ok(courseRegistration).build();
       
    }

   
    @PUT
    @Path("/{studentId}/{courseId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response updateCourseRegistration(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId, CourseRegistration cr) {
        LOG.debug("Updating a specific CourseRegistration with studentId = {} courseId = {}", studentId,courseId);
        CourseRegistrationPK id = new CourseRegistrationPK(studentId,courseId);
        CourseRegistration updatedCourseRegistration = service.updateCourseRegistrationById(id, cr);
       
        return Response.ok(updatedCourseRegistration).build();
    }
    
    
    @DELETE
    @Path("/{studentId}/{courseId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteCourseRegistration(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId) {
     
        LOG.debug("Deleting a specific CourseRegistration with studentId = {} courseId = {}", studentId,courseId);
        CourseRegistrationPK id = new CourseRegistrationPK(studentId,courseId);
        service.deleteCourseRegistrationById(id);
        return Response.ok("StudentId " + studentId + ", CourseId " + courseId + " delete successfully").build();
    }
    
   

    
   

}
