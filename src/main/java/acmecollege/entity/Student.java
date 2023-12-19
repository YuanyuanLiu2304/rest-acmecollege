
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the student database table.
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "student")
@NamedQuery(name = Student.ALL_STUDENTS_QUERY_NAME, query = "SELECT s FROM Student s LEFT JOIN FETCH s.membershipCards LEFT JOIN FETCH s.courseRegistrations")
@NamedQuery(name = Student.QUERY_STUDENT_BY_ID, query = "SELECT s FROM Student s LEFT JOIN FETCH s.membershipCards LEFT JOIN FETCH s.courseRegistrations WHERE s.id = :param1")
public class Student extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

    public static final String ALL_STUDENTS_QUERY_NAME = "Student.findAll";
    public static final String QUERY_STUDENT_BY_ID = "Student.findAllByID";

    public Student() {
    	super();
    }
    
	@Basic(optional = false)
    @Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Basic(optional = false)
    @Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@OneToMany(cascade= {CascadeType.MERGE}, mappedBy = "owner")
	private Set<MembershipCard> membershipCards = new HashSet<>();

	@OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY, mappedBy = "student")
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	public String getFirstName() {
		return firstName;
		
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    // Simplify JSON body, skip MembershipCards
    @JsonIgnore
    public Set<MembershipCard> getMembershipCards() {
		return membershipCards;
	}

	public void setMembershipCards(Set<MembershipCard> membershipCards) {
		this.membershipCards = membershipCards;
	}

    // Simplify JSON body, skip CourseRegistrations
    @JsonIgnore
    public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	

}