package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SecurityUser.class)
public abstract class SecurityUser_ {

	public static volatile SingularAttribute<SecurityUser, Student> student;
	public static volatile SetAttribute<SecurityUser, SecurityRole> roles;
	public static volatile SingularAttribute<SecurityUser, String> pwHash;
	public static volatile SingularAttribute<SecurityUser, Integer> id;
	public static volatile SingularAttribute<SecurityUser, String> username;

	public static final String STUDENT = "student";
	public static final String ROLES = "roles";
	public static final String PW_HASH = "pwHash";
	public static final String ID = "id";
	public static final String USERNAME = "username";

}

