package me.yanaga.winter.data.jpa.metadata;

import com.google.common.reflect.TypeToken;
import me.yanaga.winter.data.jpa.Repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RepositoryMetadata {

	private final Class<?> entityClass;

	private final Class<? extends Serializable> idClass;

	private RepositoryMetadata(Class<?> entityClass, Class<? extends Serializable> idClass) {
		this.entityClass = entityClass;
		this.idClass = idClass;
	}

	public static RepositoryMetadata of(Class<? extends Repository> repositoryClass) {
		checkNotNull(repositoryClass);
		checkArgument(Repository.class.isAssignableFrom(repositoryClass));
		TypeToken<? extends Repository> typeToken = TypeToken.of(repositoryClass);
		Type[] genericInterfaces = typeToken.getRawType().getGenericInterfaces();
		if (genericInterfaces[0] instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments();
			Class<?> entityClass = (Class<?>) actualTypeArguments[0];
			Class<? extends Serializable> idClass = (Class<? extends Serializable>) actualTypeArguments[1];
			return new RepositoryMetadata(entityClass, idClass);
		}
		throw new IllegalArgumentException("Unable to determinate entityClass or idClass.");
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public Class<? extends Serializable> getIdClass() {
		return idClass;
	}

}
