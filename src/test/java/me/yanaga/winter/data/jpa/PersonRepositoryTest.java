package me.yanaga.winter.data.jpa;

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


import com.mysema.query.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

@ContextConfiguration(classes = TestConfig.class)
public class PersonRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void testSaveAndFindOne() {
		Person person = new Person();
		person.setName("Yanaga");
		Person saved = personRepository.save(person);
		assertNotNull(saved.getId());
		Optional<Person> found = personRepository.findOne(saved.getId());
		assertNotNull(found);
		assertThat(saved.getId()).isEqualTo(found.map(Person::getId).get());
	}

	@Test
	public void testSaveSupplierAndFindOne() {
		Person.Supplier supplier = Person.supplier("Yanaga");
		Person saved = personRepository.save(supplier);
		assertNotNull(saved.getId());
		Optional<Person> found = personRepository.findOne(saved.getId());
		assertNotNull(found);
		assertThat(saved.getId()).isEqualTo(found.map(Person::getId).get());
	}

	@Test
	public void testSaveEntityBuilderAndFindOne() {
		Person.Builder builder = Person.builder("Yanaga");
		Person saved = personRepository.save(builder);
		assertNotNull(saved.getId());
		Optional<Person> found = personRepository.findOne(saved.getId());
		assertNotNull(found);
		assertThat(saved.getId()).isEqualTo(found.map(Person::getId).get());
	}

	@Test
	public void testFindOnePredicate() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		Optional<Person> found = personRepository.findOne(Persons.withNameContaining("a"));
		assertThat(found.get()).isEqualTo(first);
	}

	@Test
	public void testFindOnePredicateEmptyOptional() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		Optional<Person> found = personRepository.findOne(Persons.withNameContaining("z"));
		assertThat(found.isPresent()).isFalse();
	}

	@Test
	public void testFindOnePredicateAndConsumer() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		Optional<Person> found = personRepository.findOne(Persons.withNameContaining("a"), q -> q.limit(1));
		assertThat(found.get()).isEqualTo(first);
	}

	@Test
	public void testFindOneConsumer() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		Optional<Person> found = personRepository.findOne(q -> q.limit(1));
		assertThat(found.get()).isEqualTo(first);
	}

	@Test
	public void testFindAll() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		assertThat(personRepository.findAll()).contains(first);
		Person second = new Person();
		second.setName("Yanaga");
		personRepository.save(second);
		assertThat(personRepository.findAll()).contains(first, second);
	}

	@Test
	public void testFindAllPredicate() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		List<Person> persons = personRepository.findAll(Persons.withNameContaining("a"));
		assertThat(persons).contains(first);
	}

	@Test
	public void testFindAllPredicateAndConsumer() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		List<Person> persons = personRepository.findAll(Persons.withNameContaining("a"), q -> q.limit(1));
		assertThat(persons).contains(first);
	}

	@Test
	public void testFindAllConsumer() {
		Person first = new Person();
		first.setName("Yanaga");
		personRepository.save(first);
		List<Person> persons = personRepository.findAll(q -> q.limit(1));
		assertThat(persons).contains(first);
	}

	@Test
	public void testFindAllPredicateWithMultipleSpecs() {
		Person first = new Person();
		first.setName("Edson");
		personRepository.save(first);
		Person second = new Person();
		second.setName("Yanaga");
		personRepository.save(second);
		List<Person> persons = personRepository.findAll(Persons.withNameContaining("a").and(Persons.withIdGreaterThan(0L)));
		assertThat(persons).contains(second);
	}

	@Test
	public void testFindAllPredicateWithBooleanBuilder() {
		Person first = new Person();
		first.setName("Edson");
		personRepository.save(first);
		Person second = new Person();
		second.setName("Yanaga");
		personRepository.save(second);
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(Persons.withNameContaining("a"));
		builder.and(Persons.withIdGreaterThan(0L));
		List<Person> persons = personRepository.findAll(builder);
		assertThat(persons).contains(second);
	}

	@Test
	public void testFindAllPredicateWithEmptyBooleanBuilder() {
		Person first = new Person();
		first.setName("Edson");
		personRepository.save(first);
		Person second = new Person();
		second.setName("Yanaga");
		personRepository.save(second);
		BooleanBuilder builder = new BooleanBuilder();
		List<Person> persons = personRepository.findAll(builder);
		assertThat(persons).contains(first, second);
	}

	@Test
	public void testFind() {
		Person first = new Person();
		first.setName("Edson");
		personRepository.save(first);
		Person second = new Person();
		second.setName("Yanaga");
		personRepository.save(second);
		List<Person> persons = personRepository.find(
				q -> q.from(QPerson.person).list(QPerson.person));
		assertEquals(persons.size(), 2);
		assertThat(persons).contains(first, second);
		Person found = personRepository.find(q -> q.from(QPerson.person)
				.where(QPerson.person.name.eq("Yanaga"))
				.uniqueResult(QPerson.person));
		assertThat(found).isEqualTo(second);
		Long sum = personRepository
				.find(q -> q.from(QPerson.person).where(Persons.withNameContaining("a")).uniqueResult(QPerson.person.id.sum()));
		assertThat(sum).isGreaterThan(1L);
	}

}
