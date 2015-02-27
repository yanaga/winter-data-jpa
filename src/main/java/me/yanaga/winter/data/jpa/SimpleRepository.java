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
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;
import me.yanaga.winter.data.jpa.querydsl.EntityPathResolver;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleRepository<T, ID extends Serializable> implements Repository<T, ID> {

	private Class<T> type;

	private EntityPath<T> entityPath;

	private EntityManager entityManager;

	public SimpleRepository(Class<T> type, EntityManager entityManager) {
		this.type = type;
		this.entityPath = new EntityPathResolver().resolveEntityPath(type);
		this.entityManager = entityManager;
	}

	@Override
	public T save(T entity) {
		Object identifier = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
		if (identifier == null) {
			entityManager.persist(entity);
			return entity;
		}
		return entityManager.merge(entity);
	}

	@Override
	public T save(Supplier<T> supplier) {
		return save(supplier.get());
	}

	@Override
	public T save(EntityBuilder<T> builder) {
		return save(builder.build());
	}

	@Override
	public T delete(T entity) {
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		}
		else {
			entityManager.remove(entityManager.merge(entity));
		}
		return entity;
	}

	@Override
	public Optional<T> findOne(ID id) {
		return Optional.ofNullable(entityManager.find(type, id));
	}

	@Override
	public Optional<T> findOne(Predicate predicate) {
		return findOne(q -> q.where(predicate));
	}

	@Override
	public Optional<T> findOne(Predicate predicate, Consumer<JPQLQuery> consumer) {
		return findOne(q -> consumer.accept(q.where(predicate)));
	}

	@Override
	public Optional<T> findOne(Consumer<JPQLQuery> consumer) {
		JPAQuery query = new JPAQuery(entityManager).from(entityPath);
		consumer.accept(query);
		return Optional.ofNullable(query.uniqueResult(entityPath));
	}

	@Override
	public List<T> findAll() {
		return findAll(q -> {
		});
	}

	@Override
	public List<T> findAll(Predicate predicate) {
		return findAll(q -> q.where(predicate));
	}

	@Override
	public List<T> findAll(Predicate predicate, Consumer<JPQLQuery> consumer) {
		return findAll(q -> consumer.accept(q.where(predicate)));
	}

	@Override
	public List<T> findAll(Consumer<JPQLQuery> consumer) {
		return find(q -> {
			consumer.accept(q.from(entityPath));
			return q.list(entityPath);
		});
	}

	@Override
	public <N> N find(Function<JPQLQuery, N> function) {
		JPAQuery query = new JPAQuery(entityManager);
		return function.apply(query);
	}

}
