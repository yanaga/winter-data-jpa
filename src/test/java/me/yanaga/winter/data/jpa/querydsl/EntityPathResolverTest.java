package me.yanaga.winter.data.jpa.querydsl;

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

import com.mysema.query.types.EntityPath;
import me.yanaga.winter.data.jpa.Person;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertNotNull;

public class EntityPathResolverTest {

	@Test
	public void testResolveEntityPath() throws Exception {
		EntityPathResolver entityPathResolver = new EntityPathResolver();
		EntityPath<Person> entityPath = entityPathResolver.resolveEntityPath(Person.class);
		assertNotNull(entityPath);
		assertThat(entityPath.getType()).isEqualTo(Person.class);
	}

}
