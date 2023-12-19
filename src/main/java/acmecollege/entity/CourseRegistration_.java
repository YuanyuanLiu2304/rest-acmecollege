package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CourseRegistration.class)
public abstract class CourseRegistration_ extends acmecollege.entity.PojoBaseCompositeKey_ {

	public static volatile SingularAttribute<CourseRegistration, Professor> professor;
	public static volatile SingularAttribute<CourseRegistration, Student> student;
	public static volatile SingularAttribute<CourseRegistration, Course> course;
	public static volatile SingularAttribute<CourseRegistration, String> letterGrade;
	public static volatile SingularAttribute<CourseRegistration, CourseRegistrationPK> id;
	public static volatile SingularAttribute<CourseRegistration, Integer> numericGrade;

	public static final String PROFESSOR = "professor";
	public static final String STUDENT = "student";
	public static final String COURSE = "course";
	public static final String LETTER_GRADE = "letterGrade";
	public static final String ID = "id";
	public static final String NUMERIC_GRADE = "numericGrade";

}

