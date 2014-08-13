package me.yanaga.specrepo.metadata;

import me.yanaga.specrepo.Person;
import me.yanaga.specrepo.PersonRepository;
import me.yanaga.specrepo.Repository;
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
