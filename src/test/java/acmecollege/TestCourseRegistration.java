
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.COURSE_REGISTRATION_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.STUDENT_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmecollege.entity.Course;
import acmecollege.entity.CourseRegistration;
import acmecollege.entity.CourseRegistrationPK;
import acmecollege.entity.Student;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCourseRegistration {
	
	
	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

   
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    
    protected WebTarget webTarget;
   

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
    }

    
    
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
       
        
    }

    
 
    @Test
    @Order(1)
    public void test01_all_course_registrations_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME)
            .request()
            .get();
        
        assertThat(response.getStatus(), is(200));
        
        List<CourseRegistration> cr = response.readEntity(new GenericType<List<CourseRegistration>>(){});
        
        assertThat(cr, is(not(empty())));
        assertThat(cr, hasSize(2));
    }
    
    @Test
    @Order(2)
    public void test02_get_course_registration_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	 Response response = webTarget
    	            .register(userAuth)
    	            .path("/"+ COURSE_REGISTRATION_RESOURCE_NAME + "/1/1")
    	            .request()
    	            .get();
    	        
    	        assertThat(response.getStatus(), is(200));
    	        
    	        CourseRegistration cr = response.readEntity(new GenericType<CourseRegistration>(){});
    	        
    	        assertThat(cr.getNumericGrade(),is(100));
    	        assertThat(cr.getLetterGrade(),is("A+"));
    }
    
    @Test
    @Order(3)
    public void test03_create_course_registration_with_adminrole() throws JsonMappingException, JsonProcessingException {
        
        // create a new course first before test course registration as the course registration is associated with course and student
        Course course = new Course("CST8109","Network Programming",2023,"Spring",56,(byte)0);
        Response r1 = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(course));
        
        int courseId = r1.readEntity(new GenericType<Course>(){}).getId();
        
       
        // create a student
        Student student = new Student();
        student.setFirstName("Yuanyuan");
        student.setLastName("Liu");
        Response r2 = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(student));
        
        int studentId = r2.readEntity(new GenericType<Student>(){}).getId();
       
        
        CourseRegistration cr = new CourseRegistration();
        cr.setId(new CourseRegistrationPK(studentId, courseId));
        cr.setNumericGrade(90);
        
        Response r3 = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .post(Entity.json(cr));
        
        assertThat(r3.getStatus(), is(200));
        
    }

    

    
    @Test
    @Order(4)
    public void test04_update_course_registration_By_Id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	
    	Response r1 = webTarget
 	            .register(adminAuth)
 	            .path("/"+ COURSE_REGISTRATION_RESOURCE_NAME + "/1/1")
 	            .request()
 	            .get();
 	        
 	        assertThat(r1.getStatus(), is(200));
 	     
 	      // GET the courseRegistration first
 	     CourseRegistration cr = r1.readEntity(new GenericType<CourseRegistration>(){});
    	 cr.setNumericGrade(80);
    	 cr.setLetterGrade("B+");
    		
    	Response r2 = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/1/1")
                .request()
                .put(Entity.json(cr));
    	
            assertThat(r2.getStatus(), is(200));
            
        
            
            Response r3 = webTarget
                    .register(userAuth)
                    .path(COURSE_REGISTRATION_RESOURCE_NAME + "/1/1")
                    .request()
                    .get();
            
                assertThat(r3.getStatus(), is(200));
                CourseRegistration updatedCr = r3.readEntity(new GenericType<CourseRegistration>(){});
                // verify the updated course registration properties
                assertThat(updatedCr.getLetterGrade(),is("B+"));
                assertThat(updatedCr.getNumericGrade(),is(80));
          
    }
    
    
    @Test
    @Order(5)
    public void test05__delete_course_registration_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	
    	Response response = webTarget
                .register(userAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/1/1")
                .request()
                .delete();
    	
            assertThat(response.getStatus(), is(403));
           
    }
    
    
    @Test
    @Order(6)
    public void test06_delete_course_registration_By_Id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	
    	Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/1/2")
                .request()
                .delete();
    	
            assertThat(response.getStatus(), is(200));
            
          
    }


    

}
