
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.MEMBERSHIP_CARD_RESOURCE_NAME;
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

import com.fasterxml.jackson.core.JsonProcessingException;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.Student;

@Path(MEMBERSHIP_CARD_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembershipCardResource {

	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected ACMECollegeService service;

	@Inject
	protected SecurityContext sc;

	@GET
	@RolesAllowed({ ADMIN_ROLE })
	public Response getMembershipCards() throws JsonProcessingException {
		LOG.debug("Retrieving all membership cards...");
		List<MembershipCard> membershipCards = service.getAll(MembershipCard.class,
				MembershipCard.ALL_CARDS_QUERY_NAME);
		Response response = Response.ok(membershipCards).build();
		LOG.debug("Membership cards found = {}", membershipCards);
		return response;
	}


	@GET
	@RolesAllowed({ ADMIN_ROLE, USER_ROLE })
	@Path("/{membershipCardId}")
	public Response getMembershipCardById(@PathParam("membershipCardId") int membershipCardId) {
		LOG.debug("Retrieving membership card with id = {}", membershipCardId);

		Response response = Response.noContent().build();
		MembershipCard membershipCard = service.getById(MembershipCard.class, MembershipCard.FIND_BY_ID, membershipCardId );

		if (membershipCard != null) {
			response = Response.ok(membershipCard).build();
		}

		return response;
	}

	
	
	@POST
	@RolesAllowed({ ADMIN_ROLE })
	@Path("/studentId/{studentId}/membershipId/{membershipId}/signed/{signed}")
	public Response addMembershipCard(@PathParam("studentId") int studentId, @PathParam("membershipId") int membershipId, @PathParam("signed") byte signed) {
		LOG.debug("Adding a new membership card");
		LOG.debug(studentId);
		LOG.debug(membershipId);
		LOG.debug(signed);
		ClubMembership clubMembership = service.getClubMembershipById(membershipId);
		Student owner = service.getStudentById(studentId);
		MembershipCard card = new MembershipCard(clubMembership, owner, signed);
		MembershipCard membershipCard = service.persistMembershipCard(card);

		return Response.ok(membershipCard).build();
	}
	
	@POST
	@RolesAllowed({ ADMIN_ROLE })
	public Response addMembershipCardToStudent(Student owner) {
		LOG.debug("Adding a new membership card to student", owner);

		Response response = Response.noContent().build();
		LOG.debug(owner);
		if (owner != null && owner.getId() > 0) {
			MembershipCard card = new MembershipCard();
			Student s = service.getStudentById(owner.getId());
			LOG.debug(s);
			if (s != null) {
				card.setOwner(s);
			}
			card.setSigned(true);
			service.persistMembershipCard(card);

			response = Response.ok(card).build();
		}

		return response;
	}

	
	   
    @PUT
    @Path("/{membershipCardId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response updateMemberhipcard(@PathParam("membershipCardId") int membershipCardId, MembershipCard updatingMembershipCard) {
        LOG.debug("Updating a specific membershipCard with id = {}", membershipCardId);        
    	MembershipCard updatedMembershipCard = service.updateMembershipCard(membershipCardId, updatingMembershipCard);
		return Response.ok(updatedMembershipCard).build();

    }

    @DELETE
    @Path("/{membershipCardId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteMembershipCard(@PathParam("membershipCardId") int membershipCardId) {
        LOG.debug("Deleting professor with id = {}", membershipCardId);
       
        service.deleteMembershipCard(membershipCardId);
        return Response.ok("Membership card id " + membershipCardId + " delete successfully").build();
    }
    



}