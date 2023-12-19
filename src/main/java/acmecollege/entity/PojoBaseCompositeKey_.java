package acmecollege.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PojoBaseCompositeKey.class)
public abstract class PojoBaseCompositeKey_ {

	public static volatile SingularAttribute<PojoBaseCompositeKey, LocalDateTime> created;
	public static volatile SingularAttribute<PojoBaseCompositeKey, Integer> version;
	public static volatile SingularAttribute<PojoBaseCompositeKey, LocalDateTime> updated;

	public static final String CREATED = "created";
	public static final String VERSION = "version";
	public static final String UPDATED = "updated";

}

