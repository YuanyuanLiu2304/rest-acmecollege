package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MembershipCard.class)
public abstract class MembershipCard_ extends acmecollege.entity.PojoBase_ {

	public static volatile SingularAttribute<MembershipCard, Student> owner;
	public static volatile SingularAttribute<MembershipCard, ClubMembership> clubMembership;
	public static volatile SingularAttribute<MembershipCard, Byte> signed;

	public static final String OWNER = "owner";
	public static final String CLUB_MEMBERSHIP = "clubMembership";
	public static final String SIGNED = "signed";

}

