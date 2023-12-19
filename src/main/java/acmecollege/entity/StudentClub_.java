package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StudentClub.class)
public abstract class StudentClub_ extends acmecollege.entity.PojoBase_ {

	public static volatile SingularAttribute<StudentClub, String> name;
	public static volatile SetAttribute<StudentClub, ClubMembership> clubMemberships;

	public static final String NAME = "name";
	public static final String CLUB_MEMBERSHIPS = "clubMemberships";

}

