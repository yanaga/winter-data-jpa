package me.yanaga.winter.data.jpa.metadata;

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
