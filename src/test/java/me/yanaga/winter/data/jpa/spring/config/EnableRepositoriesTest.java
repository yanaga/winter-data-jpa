package me.yanaga.winter.data.jpa.spring.config;

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

import me.yanaga.winter.data.jpa.PersonRepository;
import me.yanaga.winter.data.jpa.Repository;
import me.yanaga.winter.data.jpa.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertNotNull;


@ContextConfiguration(classes = TestConfig.class)
public class EnableRepositoriesTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testBeansOfTypeRepository() {
		assertNotNull(applicationContext);
		Map<String, Repository> beansOfType = applicationContext.getBeansOfType(Repository.class);
		assertThat(beansOfType.keySet()).hasSize(1);
	}

	@Test
	public void testBeansOfTypePersonRepository() {
		assertNotNull(applicationContext);
		Map<String, PersonRepository> beansOfType = applicationContext.getBeansOfType(PersonRepository.class);
		assertThat(beansOfType).containsKey("personRepository");
		assertThat(beansOfType.get("personRepository")).isInstanceOf(PersonRepository.class);
	}

}
