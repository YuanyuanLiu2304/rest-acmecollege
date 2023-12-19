
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCourse {
	
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
    public void test01_all_courses_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Course> courses = response.readEntity(new GenericType<List<Course>>(){});
        assertThat(courses, is(not(empty())));
       
    }
    
    
    @Test
    @Order(2)
    public void test02_create_course_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	
    	Course newCourse = new Course("CST2355","Database",2023,"Winter",56,(byte)0);
    	Response response = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(newCourse));
    	
            assertThat(response.getStatus(), is(200));
           
            int generatedId = response.readEntity(new GenericType<Course>(){}).getId();
            
            // use GET to check if the course created
            Response res = webTarget
                    .register(userAuth)
                    .path(COURSE_RESOURCE_NAME + "/" + generatedId)
                    .request()
                    .get();
            
                assertThat(res.getStatus(), is(200));
                Course course = res.readEntity(new GenericType<Course>(){});
                
             assertThat(course.getCourseCode(), is("CST2355"));
             assertThat(course.getCourseTitle(), is("Database"));
            
    }
    
    
    @Test
    @Order(3)
    public void test03_get_course_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(userAuth)
                .path(COURSE_RESOURCE_NAME + "/1")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            Course course = response.readEntity(new GenericType<Course>(){});
            
         assertThat(course.getCourseCode(), is("CST8277"));
         assertThat(course.getCourseTitle(), is("Enterprise Application Programming"));
    }
    
    
    @Test
    @Order(4)
    public void test04_update_course_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	
    	Course updatedCourse = new Course("CST8288","CP",2023,"fall",72,(byte)0);
    		
    	Response response = webTarget
                .register(userAuth)
                .path(COURSE_RESOURCE_NAME + "/1")
                .request()
                .put(Entity.json(updatedCourse));
    	
            assertThat(response.getStatus(), is(403));
            
    }
    
    
    @Test
    @Order(5)
    public void test05_delete_course_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(userAuth)
                .path(COURSE_RESOURCE_NAME + "/1")
                .request()
                .delete();
    	
            assertThat(response.getStatus(), is(403));
           
    }
    
    
    @Test
    @Order(6)
    public void test06__delete_course_By_Id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME + "/2")
                .request()
                .delete();
    	
            assertThat(response.getStatus(), is(200));
            
           
            
    }
    
    

}
