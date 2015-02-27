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
import com.mysema.query.types.Predicate;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@org.springframework.stereotype.Repository
public interface Repository<T, ID extends Serializable> {

	public T save(T entity);

	public T save(Supplier<T> supplier);

	public T save(EntityBuilder<T> builder);

	public T delete(T entity);

	public Optional<T> findOne(ID id);

	public Optional<T> findOne(Predicate predicate);

	public Optional<T> findOne(Predicate predicate, Consumer<JPQLQuery> consumer);

	public Optional<T> findOne(Consumer<JPQLQuery> consumer);

	public List<T> findAll();

	public List<T> findAll(Predicate predicate);

	public List<T> findAll(Predicate predicate, Consumer<JPQLQuery> consumer);

	public List<T> findAll(Consumer<JPQLQuery> consumer);

	public <N> N find(Function<JPQLQuery, N> function);

}
