package me.yanaga.winter.data.jpa.spring;

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

import me.yanaga.winter.data.jpa.Repository;
import me.yanaga.winter.data.jpa.SimpleRepository;
import me.yanaga.winter.data.jpa.metadata.RepositoryMetadata;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SimpleRepositoryFactoryBean implements FactoryBean<Repository> {

	private Class<? extends Repository> repositoryInterface;

	private EntityManager entityManager;

	@Override
	public Repository getObject() throws Exception {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(new Class[] { repositoryInterface });
		RepositoryMetadata metadata = RepositoryMetadata.of(repositoryInterface);
		proxyFactory.setTarget(new SimpleRepository<>(metadata.getEntityClass(), entityManager));
		return (Repository) proxyFactory.getProxy();
	}

	@Override
	public Class<?> getObjectType() {
		return repositoryInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setRepositoryInterface(Class<? extends Repository> repositoryInterface) {
		this.repositoryInterface = repositoryInterface;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
