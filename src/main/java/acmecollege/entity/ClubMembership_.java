package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClubMembership.class)
public abstract class ClubMembership_ extends acmecollege.entity.PojoBase_ {

	public static volatile SingularAttribute<ClubMembership, StudentClub> club;
	public static volatile SingularAttribute<ClubMembership, MembershipCard> card;
	public static volatile SingularAttribute<ClubMembership, DurationAndStatus> durationAndStatus;

	public static final String CLUB = "club";
	public static final String CARD = "card";
	public static final String DURATION_AND_STATUS = "durationAndStatus";

}

