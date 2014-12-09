package me.yanaga.winter.data.jpa.spring.config;

import me.yanaga.winter.data.jpa.spring.SimpleRepositoryFactoryBean;
import me.yanaga.winter.data.jpa.spring.config.metadata.EnableRepositoriesMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class RepositoriesRegistrar implements ImportBeanDefinitionRegistrar {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		EnableRepositoriesMetadata enableRepositoriesMetadata = EnableRepositoriesMetadata.of(importingClassMetadata);
		RepositoryComponentProvider provider = new RepositoryComponentProvider();
		enableRepositoriesMetadata.getPackagesToScan().stream().forEach(p -> {
			provider.findCandidateComponents(p).stream().forEach(b -> {
				BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SimpleRepositoryFactoryBean.class);
				builder.addPropertyValue("repositoryInterface", b.getBeanClassName());
				AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
				String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);

				logger.debug(String.format("Registering beanDefinition of Repository '%s'", b.getBeanClassName()));

				registry.registerBeanDefinition(beanName, beanDefinition);
			});
		});
	}

}
