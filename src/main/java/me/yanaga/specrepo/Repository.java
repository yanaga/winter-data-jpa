package me.yanaga.specrepo;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.expr.BooleanExpression;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Repository<T, ID extends Serializable> {

	public T save(T entity);

	public T delete(T entity);

	public T findOne(ID id);

	public T findOne(BooleanExpression predicate);

	public T findOne(BooleanExpression predicate, Consumer<JPQLQuery> consumer);

	public T findOne(Consumer<JPQLQuery> consumer);

	public List<T> findAll(BooleanExpression predicate);

	public List<T> findAll(BooleanExpression predicate, Consumer<JPQLQuery> consumer);

	public List<T> findAll(Consumer<JPQLQuery> consumer);

	public <N> N find(Function<JPQLQuery, N> function);

}
