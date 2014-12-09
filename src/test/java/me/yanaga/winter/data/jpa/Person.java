package me.yanaga.winter.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			Person other = (Person) obj;
			return Objects.equals(this.name, other.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
