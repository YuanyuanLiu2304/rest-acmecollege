

package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.STUDENT_CLUB_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
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

import acmecollege.entity.AcademicStudentClub;
import acmecollege.entity.StudentClub;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestStudentClub {
	
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
	public void test01_testAdminGetStudentClubById() {
		String scId = "2";
		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).path(scId).register(adminAuth)
				.request(MediaType.APPLICATION_JSON).get();
		assertThat("The response should be 200 (ok)", response.getStatus(), is((200)));

	}

	@Test
	@Order(2)
	public void test02_testUserGetStudentClubById() {
		String scId = "2";
		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).path(scId).register(userAuth)
				.request(MediaType.APPLICATION_JSON).get();
		assertThat("The response should be 200 (ok)", response.getStatus(), is((200)));

	}

	@Test
	@Order(3)
	public void test03_testAdminPostNewStudentClub() throws JsonMappingException, JsonProcessingException {
		final String newClubName = "ProgrammingA " ;
		StudentClub newStudentClub = new AcademicStudentClub(newClubName);

		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).register(adminAuth).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(newStudentClub));

		StudentClub studentClub = response.readEntity(new GenericType<StudentClub>() {
		});

		assertThat("The response should be 200 (ok)", response.getStatus(), is((200)));
		assertThat(String.format("The student club's name should be %s", newClubName), studentClub.getName(),
				is((newClubName)));
	}

	@Test
	@Order(4)
	public void test04_testUserPostNewStudentClub() throws JsonMappingException, JsonProcessingException {
		final String newClubName = " ProgrammingAA " ;
		StudentClub newStudentClub = new AcademicStudentClub(newClubName);

		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).register(userAuth).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(newStudentClub));



		assertThat(response.getStatus(), is(403));
	}

	@Test
	@Order(5)
	public void test05_testAdminPutStudentClub() throws JsonMappingException, JsonProcessingException {
		String scId = "2";
		final String newClubName = "AdminPutClub ";
		StudentClub updatingStudentClub = new AcademicStudentClub(newClubName);

		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).path(scId).register(adminAuth)
				.request(MediaType.APPLICATION_JSON).put(Entity.json(updatingStudentClub));


		assertThat("The response should be 200 (ok)", response.getStatus(), is(200));

	}

	@Test
	@Order(6)
	public void test06_testUserPutStudentClub() throws JsonMappingException, JsonProcessingException {
		String scId = "2";
		final String newClubName = "UserPutClub ";
		StudentClub updatingStudentClub = new AcademicStudentClub(newClubName);

		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).path(scId).register(adminAuth)
				.request(MediaType.APPLICATION_JSON).put(Entity.json(updatingStudentClub));


		assertThat("The response should be 200 (ok)", response.getStatus(), is(200));
	}
	
	@Test
	@Order(7)
	public void test07_testadminDeleteExistingStudentClub() throws JsonMappingException, JsonProcessingException {
		
		int latestCreatedId=3;
		String scId = String.valueOf(latestCreatedId);

		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).path(scId).register(adminAuth)
				.request(MediaType.APPLICATION_JSON).delete();

		assertThat("The response should be 200 (ok)", response.getStatus(), is(200));
		
	}
	
	@Test
	@Order(8)
	public void test08_testUserDeleteStudentClub() throws JsonMappingException, JsonProcessingException {
		String scId = "2";

		Response response = webTarget.path(STUDENT_CLUB_RESOURCE_NAME).path(scId).register(userAuth)
				.request(MediaType.APPLICATION_JSON).delete();


		assertThat("The response should be 403 (forbidden)", response.getStatus(), is(403));
	}


}
