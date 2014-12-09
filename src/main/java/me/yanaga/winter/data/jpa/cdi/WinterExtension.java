package me.yanaga.winter.data.jpa.cdi;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import me.yanaga.winter.data.jpa.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WinterExtension implements Extension {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private final Map<Class<? extends Repository>, Set<Annotation>> repositoryTypes = Maps.newHashMap();

	private final Map<Set<Annotation>, Bean<EntityManager>> entityManagers = Maps.newHashMap();

	<X extends Repository> void processAnnotatedType(@Observes ProcessAnnotatedType<X> processAnnotatedType) {
		logger.debug("Searching for Repository interfaces.");
		AnnotatedType<X> annotatedType = processAnnotatedType.getAnnotatedType();
		Class<X> repositoryType = annotatedType.getJavaClass();
		if (isRepository(repositoryType)) {
			logger.info(String.format("Found Repository interface: '%s'", repositoryType));
			repositoryTypes.put(repositoryType, getQualifiers(repositoryType));
		}
	}

	<X> void processBean(@Observes ProcessBean<X> processBean) {
		System.err.println("ProcessBean " + processBean.getBean().getTypes());
		logger.debug("Searching for EntityManagers.");
		Bean<X> bean = processBean.getBean();
		bean.getTypes().stream()
				.filter(t -> t instanceof Class<?> && EntityManager.class.isAssignableFrom((Class<?>) t))
				.map(t -> ImmutableSet.copyOf(bean.getQualifiers()))
				.filter(q -> bean.isAlternative() || !entityManagers.containsKey(q))
				.forEach(q -> entityManagers.put(q, (Bean<EntityManager>) bean));
	}


	private boolean isRepository(Class<?> klazz) {
		return klazz.isInterface() && Repository.class.isAssignableFrom(klazz);
	}

	private Set<Annotation> getQualifiers(final Class<?> type) {
		Set<Annotation> qualifiers = Stream.of(type.getAnnotations())
				.filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
				.collect(Collectors.toSet());
		if (qualifiers.isEmpty()) {
			qualifiers.add(DefaultAnnotationLiteral.INSTANCE);
		}
		qualifiers.add(AnyAnnotationLiteral.INSTANCE);
		return ImmutableSet.copyOf(qualifiers);
	}

	void afterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) {
		logger.debug("Registering the Repository implementations.");
		repositoryTypes.forEach((k, a) -> afterBeanDiscovery.addBean(createRepositoryBean(k, a, beanManager)));
	}

	private <T extends Repository> Bean<T> createRepositoryBean(Class<T> repositoryType, Set<Annotation> qualifiers, BeanManager beanManager) {
		logger.debug(String.format("Creating a Repository Bean for interface '%s'.", repositoryType));
		Bean<EntityManager> entityManagerBean = entityManagers.get(qualifiers);
		if (entityManagerBean == null) {
			throw new UnsatisfiedResolutionException(String.format("Unable to resolve a bean for '%s' with qualifiers %s.",
					EntityManager.class.getName(), qualifiers));
		}
		return new SimpleRepositoryBean<>(qualifiers, repositoryType, beanManager, entityManagerBean);
	}


	static class DefaultAnnotationLiteral extends AnnotationLiteral<Default> implements Default {
		private static final DefaultAnnotationLiteral INSTANCE = new DefaultAnnotationLiteral();
	}

	static class AnyAnnotationLiteral extends AnnotationLiteral<Any> implements Any {
		private static final AnyAnnotationLiteral INSTANCE = new AnyAnnotationLiteral();
	}
}
