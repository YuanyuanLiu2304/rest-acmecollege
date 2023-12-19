
package acmecollege.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
public class AcademicStudentClub extends StudentClub implements Serializable {
	private static final long serialVersionUID = 1L;

	public AcademicStudentClub() {
		super(true);
	}
	
	public AcademicStudentClub(String clubName) {
		this();
		setName(clubName);
	}
	
}
