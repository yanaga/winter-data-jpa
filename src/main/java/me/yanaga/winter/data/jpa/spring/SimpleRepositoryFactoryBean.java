package me.yanaga.winter.data.jpa.spring;

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
