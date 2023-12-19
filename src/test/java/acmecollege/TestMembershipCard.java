
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.MEMBERSHIP_CARD_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.equalTo;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
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

import acmecollege.entity.MembershipCard;
import acmecollege.entity.Student;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestMembershipCard {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

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

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

	@Test
	@Order(1)
	public void test01_AdminGetMembershipCardById() {

		Response response = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).register(adminAuth)
				.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));

		List<MembershipCard> membershipCard = response.readEntity(new GenericType<List<MembershipCard>>() {
		});
		
		assertThat("The response should be 200 (ok)", response.getStatus(), is(equalTo(200)));
		assertThat("The professor list should not be empty.", membershipCard, is(not(empty())));

	}
	

	@Test
	@Order(2)
	public void test02_UserGetMembershipCardById() {
		String mcId = "2";
		Response response = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).path(mcId).register(userAuth)
				.request(MediaType.APPLICATION_JSON).get();

		MembershipCard membershipCard = response.readEntity(new GenericType<MembershipCard>() {
		});

		assertThat("The response should be 200 (ok)", response.getStatus(), is(equalTo(200)));
	}
	
	@Test
	@Order(3)
	public void test03_AdminPostNewMembershipCardToStudent() throws JsonMappingException, JsonProcessingException {
		final int newStudentId = 1;
		Student newStudent = new Student();
		newStudent.setId(newStudentId);

		Response response = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).register(adminAuth).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(newStudent));

		MembershipCard membershipCard = response.readEntity(new GenericType<MembershipCard>() {
		});
		int latestCreatedId = newStudent.getId();

		assertThat("The response should be 200 (ok)", response.getStatus(), is(equalTo(200)));
		assertThat("The membership card's owner should be 1", newStudentId, is((equalTo(latestCreatedId))));
	}

	@Test
	@Order(4)
	public void test04_UserPostNewMembershipCardToStudent() throws JsonMappingException, JsonProcessingException {
		final int newStudentId = 1;
		Student newStudent = new Student();
		newStudent.setId(newStudentId);

		Response response = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).register(userAuth).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(newStudent));

    	
            assertThat(response.getStatus(), is(403));


	}
	
	   @Test
	    @Order(5)
	    public void test05_update_membership_By_Id_with_userrole() throws JsonMappingException, JsonProcessingException {
	    	
		   MembershipCard updatedMC = new MembershipCard();
	    		
	    	Response response = webTarget
	                .register(userAuth)
	                .path(MEMBERSHIP_CARD_RESOURCE_NAME)
	                .request()
	                .put(Entity.json(updatedMC));
	    	
	            assertThat(response.getStatus(), is(405));
	            
	    }
	
	@Test
	@Order(6)
	public void test06_adminDeleteExistingMembershipCard() throws JsonMappingException, JsonProcessingException {
		String mcId = "3";

		Response response = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).path(mcId).register(adminAuth)
				.request(MediaType.APPLICATION_JSON).delete();
		

		assertThat("The response should be 200 (ok)", response.getStatus(), is(equalTo(200)));
	}
	
	@Test
	@Order(7)
	public void test07_adminDeleteNonExistingMembershipCard() throws JsonMappingException, JsonProcessingException {
		String mcId = "100";
		
		Response response = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).path(mcId).register(adminAuth)
				.request(MediaType.APPLICATION_JSON).delete();
		
		StatusType statusInfo = response.getStatusInfo();
		assertThat("The response should be 500", statusInfo.getStatusCode(), is(equalTo(500)));
		assertThat("There should be no content.", statusInfo.getReasonPhrase(), is((equalTo("Internal Server Error"))));
	}
	
	@Test
	@Order(8)
	public void test08_UserDeleteMembershipCard() throws JsonMappingException, JsonProcessingException {
		String mcId = "3";

		Response response = webTarget.path(MEMBERSHIP_CARD_RESOURCE_NAME).path(mcId).register(userAuth)
				.request(MediaType.APPLICATION_JSON).delete();

		StatusType statusInfo = response.getStatusInfo();
		assertThat("The response should be 500", statusInfo.getStatusCode(), is(equalTo(403)));

	}
	

}