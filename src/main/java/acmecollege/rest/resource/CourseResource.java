
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
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
import acmecollege.entity.Course;
import acmecollege.entity.CourseRegistration;

@Path(COURSE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
	
	
	private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
   
    
    @GET
    public Response getCourses() {
        LOG.debug("Retrieving all courses...");
        List<Course> courses = service.getAll(Course.class,Course.ALL_COURSES_QUERY);
        LOG.debug("Courses found = {}", courses);
        
        return Response.ok(courses).build();
    }
    
    @GET
    @Path("/{courseId}")
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getCourseById(@PathParam("courseId") int courseId) {
        LOG.debug("Retrieving course with id = {}", courseId);
        Course course = service.getById(Course.class, Course.COURSE_BY_ID_QUERY, courseId);
        
        return Response.ok(course).build();
    }
    
  
   
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourse(Course newCourse) {
    	
        LOG.debug("Adding a new course = {}", newCourse);
       
        service.persistCourse(newCourse);
        
        return Response.ok(newCourse).build();
       
    }

   
    @PUT
    @Path("/{courseId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response updateCourse(@PathParam("courseId") int courseId, Course updatingCourse) {
        LOG.debug("Updating a specific course with id = {}", courseId);
       
        Course updatedCourse = service.updateCourseById(courseId, updatingCourse);
       
        return Response.ok(updatedCourse).build();
    }
    
    
    @DELETE
    @Path("/{courseId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteCourse(@PathParam("courseId") int courseId) {
        LOG.debug("Deleting course with id = {}", courseId);
       
        service.deleteCourseById(courseId);
        return Response.ok("Course id " + courseId + " delete successfully").build();
    }
    
   

    
    @POST
    @Path("/{courseId}/studentId")
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourseRegistrationToCourse(@PathParam("courseId") int courseId, CourseRegistration courseRegistration) {
        LOG.debug( "Adding a new CourseRegistration to a course with id = {}", courseId);
        
        Course course = service.getById(Course.class, Course.COURSE_BY_ID_QUERY, courseId);
        courseRegistration.setCourse(course);
        course.getCourseRegistrations().add(courseRegistration);
        service.updateCourseById(courseId, course);
                
        return Response.ok(sc).build();
    }

    
    

}
