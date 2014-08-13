package me.yanaga.winter.data.jpa.metadata;

import me.yanaga.winter.data.jpa.Person;
import me.yanaga.winter.data.jpa.config.EnableRepositories;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


public class EnableRepositoriesMetadataTest {

	@EnableRepositories
	private static class EmptyConfiguration {
	}

	@EnableRepositories(basePackageClasses = Person.class)
	private static class BasePackageClassesConfiguration {
	}

	@EnableRepositories(basePackages = "me.yanaga", basePackageClasses = Person.class)
	private static class BasePackagesAndClassesConfiguration {
	}

	@EnableRepositories(value = "me", basePackages = "me.yanaga", basePackageClasses = Person.class)
	private static class ValueAndBasePackagesAndClassesConfiguration {
	}

	@Test
	public void testEmpty() {
		StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(EmptyConfiguration.class);
		EnableRepositoriesMetadata metadata = EnableRepositoriesMetadata.of(annotationMetadata);
		assertThat(metadata.getPackagesToScan(), contains("me.yanaga.winter.data.jpa.metadata"));
	}

	@Test
	public void testMetadataBasePackageClasses() {
		StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(BasePackageClassesConfiguration.class);
		EnableRepositoriesMetadata metadata = EnableRepositoriesMetadata.of(annotationMetadata);
		assertThat(metadata.getPackagesToScan(), contains("me.yanaga.winter.data.jpa"));
	}

	@Test
	public void testMetadataBasePackagesAndClasses() {
		StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(BasePackagesAndClassesConfiguration.class);
		EnableRepositoriesMetadata metadata = EnableRepositoriesMetadata.of(annotationMetadata);
		assertThat(metadata.getPackagesToScan(), contains("me.yanaga", "me.yanaga.winter.data.jpa"));
	}

	@Test
	public void testMetadataValueAndBasePackagesAndClasses() {
		StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(ValueAndBasePackagesAndClassesConfiguration.class);
		EnableRepositoriesMetadata metadata = EnableRepositoriesMetadata.of(annotationMetadata);
		assertThat(metadata.getPackagesToScan(), contains("me", "me.yanaga", "me.yanaga.winter.data.jpa"));
	}

}
