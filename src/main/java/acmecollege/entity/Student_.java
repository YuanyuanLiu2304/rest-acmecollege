package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Student.class)
public abstract class Student_ extends acmecollege.entity.PojoBase_ {

	public static volatile SingularAttribute<Student, String> firstName;
	public static volatile SingularAttribute<Student, String> lastName;
	public static volatile SetAttribute<Student, MembershipCard> membershipCards;
	public static volatile SetAttribute<Student, CourseRegistration> courseRegistrations;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String MEMBERSHIP_CARDS = "membershipCards";
	public static final String COURSE_REGISTRATIONS = "courseRegistrations";

}

