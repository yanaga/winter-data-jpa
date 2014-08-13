package me.yanaga.specrepo.querydsl;

import com.mysema.query.types.EntityPath;
import me.yanaga.specrepo.Person;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertNotNull;

public class EntityPathResolverTest {

	@Test
	public void testResolveEntityPath() throws Exception {
		EntityPathResolver entityPathResolver = new EntityPathResolver();
		EntityPath<Person> entityPath = entityPathResolver.resolveEntityPath(Person.class);
		assertNotNull(entityPath);
		assertThat(entityPath.getType(), equalTo(Person.class));
	}

}
