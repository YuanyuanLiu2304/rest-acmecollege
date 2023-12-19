package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Professor.class)
public abstract class Professor_ extends acmecollege.entity.PojoBase_ {

	public static volatile SingularAttribute<Professor, String> firstName;
	public static volatile SingularAttribute<Professor, String> lastName;
	public static volatile SetAttribute<Professor, CourseRegistration> courseRegistrations;
	public static volatile SingularAttribute<Professor, String> department;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String COURSE_REGISTRATIONS = "courseRegistrations";
	public static final String DEPARTMENT = "department";

}

