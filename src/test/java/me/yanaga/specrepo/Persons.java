package me.yanaga.specrepo;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import java.util.function.Consumer;

public class Persons {

	public static BooleanExpression withNameContaining(String value) {
		return QPerson.person.name.containsIgnoreCase(value);
	}

	public static BooleanExpression withIdGreaterThatn(Long value) {
		return QPerson.person.id.gt(value);
	}

	public static Consumer<JPQLQuery> orderByNameDesc() {
		return q -> q.orderBy(QPerson.person.name.desc());
	}

	public static Consumer<JPQLQuery> orderByIdDesc() {
		return q -> q.orderBy(QPerson.person.id.desc());
	}

}
