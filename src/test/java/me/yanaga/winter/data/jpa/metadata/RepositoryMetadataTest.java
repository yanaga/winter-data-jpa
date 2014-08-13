package me.yanaga.winter.data.jpa.metadata;

import me.yanaga.winter.data.jpa.Person;
import me.yanaga.winter.data.jpa.PersonRepository;
import me.yanaga.winter.data.jpa.Repository;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RepositoryMetadataTest {

	@Test
	public void testOf() {
		RepositoryMetadata metadata = RepositoryMetadata.of(PersonRepository.class);
		assertEquals(metadata.getEntityClass(), Person.class);
		assertEquals(metadata.getIdClass(), Long.class);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testWrongInterface() {
		RepositoryMetadata.of(WrongRepository.class);
	}

	private static interface WrongRepository extends Repository {
	}

}
