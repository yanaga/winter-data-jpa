package me.yanaga.winter.data.jpa.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RepositoriesRegistrar.class)
@Documented
public @interface EnableRepositories {

	public String[] value() default {};

	public String[] basePackages() default {};

	public Class<?>[] basePackageClasses() default {};

}
