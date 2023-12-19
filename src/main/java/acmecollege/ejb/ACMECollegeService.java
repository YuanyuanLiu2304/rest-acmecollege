
package acmecollege.ejb;

import static acmecollege.entity.StudentClub.IS_DUPLICATE_QUERY_NAME;
import static acmecollege.entity.StudentClub.SPECIFIC_STUDENT_CLUB_QUERY_NAME;
import static acmecollege.utility.MyConstants.DEFAULT_KEY_SIZE;
import static acmecollege.utility.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static acmecollege.utility.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static acmecollege.utility.MyConstants.DEFAULT_SALT_SIZE;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PREFIX;
import static acmecollege.utility.MyConstants.PARAM1;
import static acmecollege.utility.MyConstants.PROPERTY_ALGORITHM;
import static acmecollege.utility.MyConstants.PROPERTY_ITERATIONS;
import static acmecollege.utility.MyConstants.PROPERTY_KEY_SIZE;
import static acmecollege.utility.MyConstants.PROPERTY_SALT_SIZE;
import static acmecollege.utility.MyConstants.PU_NAME;
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import acmecollege.entity.CourseRegistration;
import acmecollege.entity.CourseRegistrationPK;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.Professor;
import acmecollege.entity.SecurityRole;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;
import acmecollege.entity.StudentClub;



/**
 * Stateless Singleton EJB Bean - ACMECollegeService
 */
@Singleton
@SuppressWarnings("unused")
public class ACMECollegeService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LogManager.getLogger();
    
    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;
    
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    
    @Transactional
    public void buildUserForNewStudent(Student newStudent) {
        SecurityUser userForNewStudent = new SecurityUser();
        userForNewStudent.setUsername(
            DEFAULT_USER_PREFIX + "_" + newStudent.getFirstName() + "." + newStudent.getLastName());
        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(PROPERTY_SALT_SIZE, DEFAULT_SALT_SIZE);
        pbAndjProperties.put(PROPERTY_KEY_SIZE, DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());
        userForNewStudent.setPwHash(pwHash);
        userForNewStudent.setStudent(newStudent);
        SecurityRole userRole = em.createNamedQuery(SecurityRole.FIND_BY_ROLE_NAME, SecurityRole.class)
                .setParameter(PARAM1, USER_ROLE)
                .getSingleResult();
        userForNewStudent.getRoles().add(userRole);
        userRole.getUsers().add(userForNewStudent);
        em.persist(userForNewStudent);
    }

    
    public List<Student> getAllStudents() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        cq.select(cq.from(Student.class));
        return em.createQuery(cq).getResultList();
    }

    public Student getStudentById(int id) {
        return em.find(Student.class, id);
    }

    @Transactional
    public Student persistStudent(Student newStudent) {
    	newStudent.setMembershipCards(null);
        em.persist(newStudent);
        return newStudent;
    }

    /**
     * To update a student
     * 
     * @param id - id of entity to update
     * @param studentWithUpdates - entity with updated information
     * @return Entity with updated information
     */
    @Transactional
    public Student updateStudentById(int id, Student studentWithUpdates) {
        Student studentToBeUpdated = getStudentById(id);
        if (studentToBeUpdated != null) {
            em.refresh(studentToBeUpdated);
            studentToBeUpdated.setFullName(studentWithUpdates.getFirstName(), studentWithUpdates.getLastName());
            em.merge(studentToBeUpdated);
            em.flush();
        }
        return studentToBeUpdated;
    }

    /**
     * To delete a student by id
     * 
     * @param id - student id to delete
     */
    @Transactional
    public void deleteStudentById(int id) {
        Student student = getStudentById(id);
        
       if (student != null) {        
          
            TypedQuery<SecurityUser> findUser = 
            		em.createNamedQuery(SecurityUser.SECURITY_USER_BY_ID_QUERY, SecurityUser.class)
                    .setParameter(PARAM1,id);
               
            SecurityUser sUser = findUser.getSingleResult();
            em.remove(sUser);
            em.remove(student);
        }
    }
    
    @Transactional
    public Professor setProfessorForStudentCourse(int studentId, int courseId, Professor newProfessor) {
        Student studentToBeUpdated = em.find(Student.class, studentId);
        if (studentToBeUpdated != null) { // Student exists
            Set<CourseRegistration> courseRegistrations = studentToBeUpdated.getCourseRegistrations();
            courseRegistrations.forEach(c -> {
                if (c.getCourse().getId() == courseId) {
                    if (c.getProfessor() != null) { // Professor exists
                        Professor prof = em.find(Professor.class, c.getProfessor().getId());
                        prof.setProfessor(newProfessor.getFirstName(),
                        				  newProfessor.getLastName(),
                        				  newProfessor.getDepartment());
                        em.merge(prof);
                    }
                    else { // Professor does not exist
                        c.setProfessor(newProfessor);
                        em.merge(studentToBeUpdated);
                    }
                }
            });
            return newProfessor;
        }
        else return null;  // Student doesn't exists
    }

    
    public List<StudentClub> getAllStudentClubs() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StudentClub> cq = cb.createQuery(StudentClub.class);
        cq.select(cq.from(StudentClub.class));
        return em.createQuery(cq).getResultList();
    }

   
    public StudentClub getStudentClubById(int id) {
        TypedQuery<StudentClub> specificStudentClubQuery = em.createNamedQuery(SPECIFIC_STUDENT_CLUB_QUERY_NAME, StudentClub.class);
        specificStudentClubQuery.setParameter(PARAM1, id);
        return specificStudentClubQuery.getSingleResult();
    }
    
    // These methods are more generic.

    public <T> List<T> getAll(Class<T> entity, String namedQuery) {
        TypedQuery<T> allQuery = em.createNamedQuery(namedQuery, entity);
        return allQuery.getResultList();
    }
    
    public <T> T getById(Class<T> entity, String namedQuery, int id) {
        TypedQuery<T> allQuery = em.createNamedQuery(namedQuery, entity);
        allQuery.setParameter(PARAM1, id);
        return allQuery.getSingleResult();
    }

    @Transactional
    public StudentClub deleteStudentClub(int id) {
       
    	StudentClub sc = getById(StudentClub.class, StudentClub.SPECIFIC_STUDENT_CLUB_QUERY_NAME, id);
        if (sc != null) {
            Set<ClubMembership> memberships = sc.getClubMemberships();
            List<ClubMembership> list = new LinkedList<>();
            memberships.forEach(list::add);
            list.forEach(m -> {
                if (m.getCard() != null) {
                    MembershipCard mc = getById(MembershipCard.class, MembershipCard.FIND_BY_ID, m.getCard().getId());
                    mc.setClubMembership(null);
                }
                m.setCard(null);
                em.merge(m);
            });
            em.remove(sc);
            return sc;
        }
        return null;
    }
    
   
    public boolean isDuplicated(StudentClub newStudentClub) {
        TypedQuery<Long> allStudentClubsQuery = em.createNamedQuery(IS_DUPLICATE_QUERY_NAME, Long.class);
        allStudentClubsQuery.setParameter(PARAM1, newStudentClub.getName());
        return (allStudentClubsQuery.getSingleResult() >= 1);
    }

    @Transactional
    public StudentClub persistStudentClub(StudentClub newStudentClub) {
        em.persist(newStudentClub);
        return newStudentClub;
    }

    @Transactional
    public StudentClub updateStudentClub(int id, StudentClub updatingStudentClub) {
    	StudentClub studentClubToBeUpdated = getStudentClubById(id);
        if (studentClubToBeUpdated != null) {
            em.refresh(studentClubToBeUpdated);
            studentClubToBeUpdated.setName(updatingStudentClub.getName());
            em.merge(studentClubToBeUpdated);
            em.flush();
        }
        return studentClubToBeUpdated;
    }
    
    @Transactional
    public ClubMembership persistClubMembership(ClubMembership newClubMembership) {
        em.persist(newClubMembership);
        return newClubMembership;
    }

    public ClubMembership getClubMembershipById(int cmId) {
        TypedQuery<ClubMembership> allClubMembershipQuery = em.createNamedQuery(ClubMembership.FIND_BY_ID, ClubMembership.class);
        allClubMembershipQuery.setParameter(PARAM1, cmId);
        return allClubMembershipQuery.getSingleResult();
    }

    @Transactional
    public ClubMembership updateClubMembership(int id, ClubMembership clubMembershipWithUpdates) {
    	ClubMembership clubMembershipToBeUpdated = getClubMembershipById(id);
        if (clubMembershipToBeUpdated != null) {
            em.refresh(clubMembershipToBeUpdated);
            em.merge(clubMembershipWithUpdates);
            em.flush();
        }
        return clubMembershipToBeUpdated;
    }
    
    
    // below methods for Course CRUD
    @Transactional
    public Course persistCourse(Course newCourse) {
        em.persist(newCourse);       
        return newCourse;
       
    }

    @Transactional
    public Course updateCourseById(int courseId, Course updatingCourse) {
      
        Course updatedCourse = em.find(Course.class, courseId);
        
        updatedCourse.setCourseCode(updatingCourse.getCourseCode());
        updatedCourse.setCourseTitle(updatingCourse.getCourseTitle());
        updatedCourse.setYear(updatingCourse.getYear());
        updatedCourse.setSemester(updatingCourse.getSemester());
        updatedCourse.setCreditUnits(updatingCourse.getCreditUnits());
        updatedCourse.setOnline(updatingCourse.getOnline());
      
        
        em.merge(updatedCourse);
       
        return updatedCourse;
    }
    
    
    @Transactional
    public void deleteCourseById(int courseId) {
       
        Course deletingCourse = em.find(Course.class, courseId);
        em.remove(deletingCourse);
        
    }
    
    
    // below methods for Professor CRUD
    @Transactional
    public Professor persistProfessor(Professor newProfessor) {
        em.persist(newProfessor);       
        return newProfessor;
       
    }

    @Transactional
    public Professor updateProfessorById(int professorId, Professor professor) {
      
    	Professor updatedProfessor = em.find(Professor.class, professorId);
    	updatedProfessor.setFirstName(professor.getFirstName());
    	updatedProfessor.setLastName(professor.getLastName());
    	updatedProfessor.setDepartment(professor.getDepartment());
        		
        em.merge(updatedProfessor);
       
        return updatedProfessor;
    }
    
    
    @Transactional
    public void deleteProfessorById(int professorId) {
       
    	Professor deletingProfessor = em.find(Professor.class, professorId);
    	
    	for(CourseRegistration cr: deletingProfessor.getCourseRegistrations()) {
    		cr.setProfessor(null);
    	}
    	
        em.remove(deletingProfessor);
        
    }
    
    // below CRUD method for CourseRegistration class 
    public CourseRegistration getCourseRegistrationById(CourseRegistrationPK id) {
        return em.find(CourseRegistration.class, id);
    }

    @Transactional
    public CourseRegistration persistCourseRegistration(CourseRegistration newCourseRegistration) {
        // Set the associated Course
        Course course = em.find(Course.class, newCourseRegistration.getId().getCourseId());
        newCourseRegistration.setCourse(course);

        // Set the associated Student
        Student student = em.find(Student.class, newCourseRegistration.getId().getStudentId());
        newCourseRegistration.setStudent(student);

        // Persist the provided CourseRegistration entity
        em.persist(newCourseRegistration);

        return newCourseRegistration;
    }


    
    @Transactional
    public CourseRegistration updateCourseRegistrationById(CourseRegistrationPK id,CourseRegistration updatedRegistration) {
        
        CourseRegistration crToBeUpdate = em.find(CourseRegistration.class,id);

      
        // Ensure the existingRegistration is not null
        if (crToBeUpdate != null) {
          
        	crToBeUpdate.setNumericGrade(updatedRegistration.getNumericGrade());
        	crToBeUpdate.setLetterGrade(updatedRegistration.getLetterGrade());
        	
            return em.merge(crToBeUpdate);
        }

       
        return null;
    }


    @Transactional
    public void deleteCourseRegistrationById(CourseRegistrationPK id) {
    	
        CourseRegistration courseRegistration = em.find(CourseRegistration.class, id);
        em.remove(courseRegistration);

    }
    
    //Below for MemberhispCard CRUD operations.
    @Transactional
	public MembershipCard persistMembershipCard(MembershipCard newcard) {
	        em.persist(newcard);       
	        return newcard;
	       
	}

    
    @Transactional
	public void deleteMembershipCard(int mcId) {
        MembershipCard deletingMembershipCard = em.find(MembershipCard.class, mcId);
        em.remove(deletingMembershipCard);

	}

    @Transactional
	public MembershipCard updateMembershipCard(int membershipCardId, MembershipCard updatingMembershipCard) {
    	MembershipCard updatedMC = em.find(MembershipCard.class, membershipCardId);
    	updatedMC.setClubMembership(updatingMembershipCard.getClubMembership()); 			
        em.merge(updatedMC);
        return updatedMC;
	}
    
    //for ClubMembership Delete Method
    @Transactional
	public void deleteClubMembershipById(int clubmembershipId) {
        ClubMembership deletingClubMembership = em.find(ClubMembership.class, clubmembershipId);
        em.remove(deletingClubMembership);
	}

   

    
}