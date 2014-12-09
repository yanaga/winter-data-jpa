package me.yanaga.winter.data.jpa;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Predicate;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Repository<T, ID extends Serializable> {

	public T save(T entity);

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
