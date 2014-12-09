package me.yanaga.winter.data.jpa.spring.config.metadata;

import com.google.common.collect.ImmutableList;
import me.yanaga.winter.data.jpa.spring.config.EnableRepositories;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class EnableRepositoriesMetadata {

	private static final String ANNOTATION_NAME = EnableRepositories.class.getName();

	private static final String VALUE = "value";

	private static final String BASE_PACKAGES = "basePackages";

	private static final String BASE_PACKAGE_CLASSES = "basePackageClasses";

	private List<String> packagesToScan;

	private EnableRepositoriesMetadata(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public static EnableRepositoriesMetadata of(AnnotationMetadata annotationMetadata) {
		List<String> packageAttributes = getPackageAttributes(annotationMetadata);
		if (packageAttributes.stream().allMatch(l -> l.isEmpty())) {
			return new EnableRepositoriesMetadata(ImmutableList.of(obtainPackageName(annotationMetadata.getClassName())));
		}
		return new EnableRepositoriesMetadata(packageAttributes);
	}

	private static String obtainPackageName(String className) {
		int lastIndexOf = className.lastIndexOf('.');
		return lastIndexOf != -1 ? className.substring(0, lastIndexOf) : "";
	}

	private static List<String> getPackageAttributes(AnnotationMetadata annotationMetadata) {
		return ImmutableList.<String>builder()
				.addAll(getValueAttributes(annotationMetadata))
				.addAll(getBasePackagesAttributes(annotationMetadata))
				.addAll(getBasePackageClassesAttributes(annotationMetadata))
				.build();
	}

	private static List<String> getValueAttributes(AnnotationMetadata annotationMetadata) {
		return ImmutableList.copyOf((String[]) annotationMetadata.getAnnotationAttributes(ANNOTATION_NAME).get(VALUE));
	}

	private static List<String> getBasePackagesAttributes(AnnotationMetadata annotationMetadata) {
		return ImmutableList.copyOf((String[]) annotationMetadata.getAnnotationAttributes(ANNOTATION_NAME).get(BASE_PACKAGES));
	}

	private static List<String> getBasePackageClassesAttributes(AnnotationMetadata annotationMetadata) {
		Class<?>[] basePackageClasses = (Class<?>[]) annotationMetadata.getAnnotationAttributes(ANNOTATION_NAME).get(BASE_PACKAGE_CLASSES);
		return Arrays.stream(basePackageClasses).map(k -> k.getPackage().getName()).collect(collectingAndThen(toList(), ImmutableList::copyOf));
	}

	public List<String> getPackagesToScan() {
		return packagesToScan;
	}

}
