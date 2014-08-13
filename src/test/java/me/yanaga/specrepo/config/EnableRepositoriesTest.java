package me.yanaga.specrepo.config;

import me.yanaga.specrepo.PersonRepository;
import me.yanaga.specrepo.Repository;
import me.yanaga.specrepo.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


@ContextConfiguration(classes = TestConfiguration.class)
public class EnableRepositoriesTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testBeansOfTypeRepository() {
		assertNotNull(applicationContext);
		Map<String, Repository> beansOfType = applicationContext.getBeansOfType(Repository.class);
		assertEquals(1, beansOfType.keySet().size());
	}

	@Test
	public void testBeansOfTypePersonRepository() {
		assertNotNull(applicationContext);
		Map<String, PersonRepository> beansOfType = applicationContext.getBeansOfType(PersonRepository.class);
		assertEquals(1, beansOfType.keySet().size());
		beansOfType.values().iterator().next().findOne(1L);
	}

}
