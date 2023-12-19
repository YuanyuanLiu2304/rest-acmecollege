
package acmecollege.entity;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the course_registration database table.
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "course_registration")
@Access(AccessType.FIELD)
@NamedQuery(name = CourseRegistration.ALL_COURSEREGISTRATION_QUERY, query = "SELECT cr FROM CourseRegistration cr ")
@NamedQuery(name = CourseRegistration.COURSEREGISTRATION_BY_ID_QUERY, query = "SELECT cr FROM CourseRegistration cr WHERE cr.id = :param1")
public class CourseRegistration extends PojoBaseCompositeKey<CourseRegistrationPK> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ALL_COURSEREGISTRATION_QUERY = "CourseRegistration.findAll";
	public static final String COURSEREGISTRATION_BY_ID_QUERY = "CourseRegistration.findByID";

	
	@EmbeddedId
	private CourseRegistrationPK id;

	// @MapsId is used to map a part of composite key to an entity.
	@MapsId("studentId")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
	private Student student;

	@MapsId("courseId")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
	private Course course;


	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "professor_id", referencedColumnName = "professor_id", nullable = true)
	private Professor professor;

	@Column(name = "numeric_grade")
	private int numericGrade;

	@Column(length = 3, name = "letter_grade")
	private String letterGrade;


	public CourseRegistration() {
		id = new CourseRegistrationPK();
	}

	@Override
	public CourseRegistrationPK getId() {
		return id;
	}

	@Override
	public void setId(CourseRegistrationPK id) {
		this.id = id;
	}

	@JsonIgnore
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		id.setStudentId(student.id);
		this.student = student;
	}

	@JsonIgnore
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		id.setCourseId(course.id);
		this.course = course;
	}

	@JsonIgnore
	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public int getNumericGrade() {
		return numericGrade;
	}
	
	public void setNumericGrade(int numericGrade) {
		this.numericGrade = numericGrade;
	}

	public String getLetterGrade() {
		return letterGrade;
	}

	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}

	

}