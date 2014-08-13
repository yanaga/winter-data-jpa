package me.yanaga.specrepo.querydsl;

import com.mysema.query.types.EntityPath;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;

public class EntityPathResolver {

	public <T> EntityPath<T> resolveEntityPath(Class<T> type) {
		String className = String.format("%s.Q%s", type.getPackage().getName(), type.getSimpleName());
		try {
			Class<?> klazz = Class.forName(className);
			return (EntityPath<T>) Stream.of(klazz.getDeclaredFields())
					.filter(f -> Modifier.isStatic(f.getModifiers()) && klazz.equals(f.getType()))
					.findAny().get().get(null);
		}
		catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.format("Unable to locate Q class: '%s'", className));
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.format("Unable to access Q property of class: '%s'", className));
		}
	}

}
