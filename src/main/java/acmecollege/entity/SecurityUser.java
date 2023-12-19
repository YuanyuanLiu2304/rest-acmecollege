
package acmecollege.entity;


import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import acmecollege.rest.serializer.SecurityRoleSerializer;


/**
 * User class used for (JSR-375) Java EE Security authorization/authentication
 */

@SuppressWarnings("unused")
@Entity
@Table(name = "security_user")
@NamedQuery( name = SecurityUser.SECURITY_USER_BY_ID_QUERY,query = "SELECT su FROM SecurityUser su left join fetch su.student WHERE su.student.id = :param1")
@NamedQuery( name = SecurityUser.SECURITY_USER_BY_NAME_QUERY,query = "SELECT su FROM SecurityUser su left join fetch su.student WHERE su.username = :param1")
public class SecurityUser implements Serializable, Principal {
    /** Explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    public static final String SECURITY_USER_BY_NAME_QUERY = "SecurityUser.userByName";
    public static final String SECURITY_USER_BY_ID_QUERY = "SecurityUser.userById";
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    protected int id;
    
    @Basic(optional = false)
    protected String username;
    
    @Basic(optional = false)
    @Column(name = "password_hash", nullable = false)
    protected String pwHash;
    
    
    @OneToOne(cascade = CascadeType.MERGE, optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", referencedColumnName = "id", nullable = true)
    protected Student student;
    
    
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "user_has_role",
        joinColumns = @JoinColumn(referencedColumnName = "user_id", name = "user_id"), // this entity, which is SecurityUser
        inverseJoinColumns = @JoinColumn(referencedColumnName = "role_id", name = "role_id")) // the other entity, which is SecurityRole
    protected Set<SecurityRole> roles = new HashSet<SecurityRole>();

    public SecurityUser() {
        super();
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwHash() {
        return pwHash;
    }
    
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

 
    @JsonSerialize(contentUsing = SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    
    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // Only include member variables that really contribute to an object's identity
        // i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
        // they shouldn't be part of the hashCode calculation
        return prime * result + Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof SecurityUser otherSecurityUser) {
        
            return Objects.equals(this.getId(), otherSecurityUser.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityUser [id = ").append(id).append(", username = ").append(username).append("]");
        return builder.toString();
    }
    
}
