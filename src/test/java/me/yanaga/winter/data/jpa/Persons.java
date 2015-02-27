package me.yanaga.winter.data.jpa;

/*
 * #%L
 * winter-data-jpa
 * %%
 * Copyright (C) 2015 Edson Yanaga
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.expr.BooleanExpression;

import java.util.function.Consumer;

public class Persons {

	public static BooleanExpression withNameContaining(String value) {
		return QPerson.person.name.containsIgnoreCase(value);
	}

	public static BooleanExpression withIdGreaterThan(Long value) {
		return QPerson.person.id.gt(value);
	}

	public static Consumer<JPQLQuery> orderByNameDesc() {
		return q -> q.orderBy(QPerson.person.name.desc());
	}

	public static Consumer<JPQLQuery> orderByIdDesc() {
		return q -> q.orderBy(QPerson.person.id.desc());
	}

}
