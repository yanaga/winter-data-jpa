package me.yanaga.specrepo.config;

import me.yanaga.specrepo.Repository;
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
