package me.yanaga.winter.data.jpa.spring.config;

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

import me.yanaga.winter.data.jpa.spring.SimpleRepositoryFactoryBean;
import me.yanaga.winter.data.jpa.spring.config.metadata.EnableRepositoriesMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

public class RepositoriesRegistrar implements ImportBeanDefinitionRegistrar {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		EnableRepositoriesMetadata enableRepositoriesMetadata = EnableRepositoriesMetadata.of(importingClassMetadata);
		RepositoryComponentProvider provider = new RepositoryComponentProvider();
		enableRepositoriesMetadata.getPackagesToScan().stream().forEach(p -> {
			provider.findCandidateComponents(p).stream().forEach(b -> {
				BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SimpleRepositoryFactoryBean.class);
				builder.addPropertyValue("repositoryInterface", b.getBeanClassName());
				AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
				String beanName = defineRepositoryBeanName(b.getBeanClassName());

				logger.debug(String.format("Registering beanDefinition of Repository '%s' with name '%s'", b.getBeanClassName(), beanName));

				registry.registerBeanDefinition(beanName, beanDefinition);
			});
		});
	}

	private String defineRepositoryBeanName(String repositoryInterface) {
		String shortClassName = ClassUtils.getShortName(repositoryInterface);
		return Introspector.decapitalize(shortClassName);
	}

}
