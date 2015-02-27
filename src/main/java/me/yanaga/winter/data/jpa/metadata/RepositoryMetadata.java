package me.yanaga.winter.data.jpa.metadata;

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
