package com.in28minutes.springboot.jpa;

import java.util.List;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//using PaginAndSortingRepository this time(provides more features)
@RepositoryRestResource(path="users",collectionResourceRel ="users" )
public interface UserRestRepository extends PagingAndSortingRepository<User, Long> {

	List<User> findByRole(String role);
	
}
