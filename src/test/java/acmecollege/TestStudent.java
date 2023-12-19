

package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.STUDENT_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.invoke.MethodHandles;
import java.net.URI;

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

import acmecollege.entity.Student;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestStudent {
	
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
    public void test01_get_students_with_userrole() throws JsonMappingException, JsonProcessingException {
    	 Response response = webTarget
    	            .register(userAuth)
    	            .path("/"+ STUDENT_RESOURCE_NAME)
    	            .request()
    	            .get();
    	        
    	        assertThat(response.getStatus(), is(403));
    	        
   	           
    }
    
    
    @Test
    @Order(2)
    public void test02_get_student_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	 Response response = webTarget
    	            .register(userAuth)
    	            .path("/"+ STUDENT_RESOURCE_NAME + "/1")
    	            .request()
    	            .get();
    	        
    	        assertThat(response.getStatus(), is(200));
    	        
   	           Student student = response.readEntity(new GenericType<Student>(){});
    	        
    	        assertThat(student.getFirstName(), is("John"));
    	        assertThat(student.getLastName(),is("Smith"));

    	        
    }
    
    
   
	@Test
    @Order(3)
    public void test03_create_student_adminrole() throws JsonMappingException, JsonProcessingException {
        
      
        // create a student
        Student student = new Student();
        student.setFirstName("Rachel");
        student.setLastName("Smith");
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(student));
        
        assertThat(response.getStatus(), is(200));
        
        int generatedId = response.readEntity(new GenericType<Student>(){}).getId();
        
        // verify the new inserted student properties
        Response res = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/" + generatedId)
                .request()
                .get();
        
        Student newStudent = res.readEntity(new GenericType<Student>(){});
        assertThat(res.getStatus(), is(200));
        assertThat(newStudent.getFirstName(),is("Rachel"));
        assertThat(newStudent.getLastName(),is("Smith"));
        
    }
	
	@Test
    @Order(4)
    public void test04_create_student_userrole() throws JsonMappingException, JsonProcessingException {
        
      
        // create a student
        Student student = new Student();
        student.setFirstName("User");
        student.setLastName("Role");
        Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(student));
        
        assertThat(response.getStatus(), is(403));
              
    }
	
	
	@Test
    @Order(5)
    public void test05_update_student_By_Id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	
    	Student s = new Student();
    	s.setFirstName("Update First Name");
    	s.setLastName("Updated Last Name");
    		
    	Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/1")
                .request()
                .put(Entity.json(s));
    	
            assertThat(response.getStatus(), is(200));
            
        
            
            Response res = webTarget
                    .register(userAuth)
                    .path(STUDENT_RESOURCE_NAME + "/1")
                    .request()
                    .get();
            
                assertThat(res.getStatus(), is(200));
                Student updatedStudent = res.readEntity(new GenericType<Student>(){});
                // verify the updated course registration properties
                assertThat(updatedStudent.getFirstName(),is("Update First Name"));
                assertThat(updatedStudent.getLastName(),is("Updated Last Name"));
          
    }
    
	
	@Test
    @Order(6)
    public void test06_update_student_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	
    	Student s = new Student();
    	s.setFirstName("Update First Name");
    	s.setLastName("Updated Last Name");
    		
    	Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME + "/1")
                .request()
                .put(Entity.json(s));
    	
            assertThat(response.getStatus(), is(403));
         
    }
    
    
    @Test
    @Order(7)
    public void test07__delete_student_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
    	
    	Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME + "/1")
                .request()
                .delete();
    	
            assertThat(response.getStatus(), is(403));
           
    }
    
    
    @Test
    @Order(8)
    public void test08_delete_student_By_Id_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	String sid = "2";
    	Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .path(sid)
                .request()
                .delete();
    	
            assertThat(response.getStatus(), is(200));
            
          
    }

}
