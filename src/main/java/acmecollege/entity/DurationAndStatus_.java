package acmecollege.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DurationAndStatus.class)
public abstract class DurationAndStatus_ {

	public static volatile SingularAttribute<DurationAndStatus, LocalDateTime> endDate;
	public static volatile SingularAttribute<DurationAndStatus, Byte> active;
	public static volatile SingularAttribute<DurationAndStatus, LocalDateTime> startDate;

	public static final String END_DATE = "endDate";
	public static final String ACTIVE = "active";
	public static final String START_DATE = "startDate";

}

