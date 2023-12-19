package acmecollege.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PojoBase.class)
public abstract class PojoBase_ {

	public static volatile SingularAttribute<PojoBase, LocalDateTime> created;
	public static volatile SingularAttribute<PojoBase, Integer> id;
	public static volatile SingularAttribute<PojoBase, Integer> version;
	public static volatile SingularAttribute<PojoBase, LocalDateTime> updated;

	public static final String CREATED = "created";
	public static final String ID = "id";
	public static final String VERSION = "version";
	public static final String UPDATED = "updated";

}

