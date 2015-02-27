package me.yanaga.winter.data.jpa.cdi;

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

import com.google.common.collect.ImmutableSet;
import me.yanaga.winter.data.jpa.Repository;
import me.yanaga.winter.data.jpa.SimpleRepository;
import me.yanaga.winter.data.jpa.metadata.RepositoryMetadata;
import me.yanaga.winter.data.jpa.proxy.ProxyFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleRepositoryBean<T extends Repository> implements Bean<T> {

	private final Set<Annotation> qualifiers;

	private final Class<T> repositoryType;

	private final BeanManager beanManager;

	private final Bean<EntityManager> entityManagerBean;

	public SimpleRepositoryBean(Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager, Bean<EntityManager> entityManagerBean) {
		checkNotNull(qualifiers);
		checkNotNull(repositoryType);
		checkNotNull(beanManager);
		checkArgument(repositoryType.isInterface());
		checkNotNull(entityManagerBean);

		this.qualifiers = qualifiers;
		this.repositoryType = repositoryType;
		this.beanManager = beanManager;
		this.entityManagerBean = entityManagerBean;
	}

	public Set<Type> getTypes() {
		ImmutableSet.Builder<Type> builder = ImmutableSet.builder();
		builder.add(repositoryType);
		builder.addAll(Arrays.asList(repositoryType.getInterfaces()));
		return builder.build();
	}

	protected <S> S getDependencyInstance(Bean<S> bean, Class<S> type) {
		CreationalContext<S> creationalContext = beanManager.createCreationalContext(bean);
		return (S) beanManager.getReference(bean, type, creationalContext);
	}

	public final T create(CreationalContext<T> creationalContext) {
		RepositoryMetadata metadata = RepositoryMetadata.of(repositoryType);
		EntityManager entityManager = getDependencyInstance(entityManagerBean, EntityManager.class);
		return ProxyFactory.newProxy(new SimpleRepository<>(metadata.getEntityClass(), entityManager), repositoryType);
	}

	public void destroy(T instance, CreationalContext<T> creationalContext) {
		creationalContext.release();
	}

	public Set<Annotation> getQualifiers() {
		return qualifiers;
	}

	public String getName() {
		return repositoryType.getName();
	}

	public Set<Class<? extends Annotation>> getStereotypes() {
		return Stream.of(repositoryType.getAnnotations())
				.map(a -> a.annotationType())
				.filter(a -> a.isAnnotationPresent(Stereotype.class))
				.collect(Collectors.toSet());
	}

	public Class<?> getBeanClass() {
		return repositoryType;
	}

	public boolean isAlternative() {
		return repositoryType.isAnnotationPresent(Alternative.class);
	}

	public boolean isNullable() {
		return false;
	}

	public Set<InjectionPoint> getInjectionPoints() {
		return Collections.emptySet();
	}

	public Class<? extends Annotation> getScope() {
		return ApplicationScoped.class;
	}

}
