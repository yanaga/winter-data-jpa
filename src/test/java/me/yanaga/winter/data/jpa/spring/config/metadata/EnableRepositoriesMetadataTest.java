package me.yanaga.winter.data.jpa.spring.config.metadata;

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

import me.yanaga.winter.data.jpa.Person;
import me.yanaga.winter.data.jpa.spring.config.EnableRepositories;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;


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
		assertThat(metadata.getPackagesToScan()).contains("me.yanaga.winter.data.jpa.spring.config.metadata");
	}

	@Test
	public void testMetadataBasePackageClasses() {
		StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(BasePackageClassesConfiguration.class);
		EnableRepositoriesMetadata metadata = EnableRepositoriesMetadata.of(annotationMetadata);
		assertThat(metadata.getPackagesToScan()).contains("me.yanaga.winter.data.jpa");
	}

	@Test
	public void testMetadataBasePackagesAndClasses() {
		StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(BasePackagesAndClassesConfiguration.class);
		EnableRepositoriesMetadata metadata = EnableRepositoriesMetadata.of(annotationMetadata);
		assertThat(metadata.getPackagesToScan()).contains("me.yanaga", "me.yanaga.winter.data.jpa");
	}

	@Test
	public void testMetadataValueAndBasePackagesAndClasses() {
		StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(ValueAndBasePackagesAndClassesConfiguration.class);
		EnableRepositoriesMetadata metadata = EnableRepositoriesMetadata.of(annotationMetadata);
		assertThat(metadata.getPackagesToScan()).contains("me", "me.yanaga", "me.yanaga.winter.data.jpa");
	}

}
