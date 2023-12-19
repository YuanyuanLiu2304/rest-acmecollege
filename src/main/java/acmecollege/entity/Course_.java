package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Course.class)
public abstract class Course_ extends acmecollege.entity.PojoBase_ {

	public static volatile SingularAttribute<Course, String> courseTitle;
	public static volatile SingularAttribute<Course, Integer> year;
	public static volatile SingularAttribute<Course, String> courseCode;
	public static volatile SingularAttribute<Course, Byte> online;
	public static volatile SingularAttribute<Course, String> semester;
	public static volatile SingularAttribute<Course, Integer> creditUnits;
	public static volatile SetAttribute<Course, CourseRegistration> courseRegistrations;

	public static final String COURSE_TITLE = "courseTitle";
	public static final String YEAR = "year";
	public static final String COURSE_CODE = "courseCode";
	public static final String ONLINE = "online";
	public static final String SEMESTER = "semester";
	public static final String CREDIT_UNITS = "creditUnits";
	public static final String COURSE_REGISTRATIONS = "courseRegistrations";

}

