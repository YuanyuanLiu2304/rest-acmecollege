package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SecurityRole.class)
public abstract class SecurityRole_ {

	public static volatile SingularAttribute<SecurityRole, String> roleName;
	public static volatile SingularAttribute<SecurityRole, Integer> id;
	public static volatile SetAttribute<SecurityRole, SecurityUser> users;

	public static final String ROLE_NAME = "roleName";
	public static final String ID = "id";
	public static final String USERS = "users";

}

