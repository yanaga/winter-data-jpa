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

import me.yanaga.winter.data.jpa.Repository;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.IOException;

class RepositoryComponentProvider extends ClassPathScanningCandidateComponentProvider {

	RepositoryComponentProvider() {
		super(false);
		super.addIncludeFilter(new InterfaceTypeFilter(Repository.class));
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		boolean isNonRepositoryInterface = !Repository.class.getName().equals(beanDefinition.getBeanClassName());
		boolean isTopLevelType = !beanDefinition.getMetadata().hasEnclosingClass();
		return isNonRepositoryInterface && isTopLevelType;
	}

	static class InterfaceTypeFilter extends AssignableTypeFilter {

		public InterfaceTypeFilter(Class<?> targetType) {
			super(targetType);
		}

		@Override
		public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
			return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
		}

	}

}
