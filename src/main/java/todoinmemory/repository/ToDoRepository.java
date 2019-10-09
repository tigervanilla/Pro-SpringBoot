package todoinmemory.repository;

import org.springframework.data.repository.CrudRepository;
import todoinmemory.domain.ToDo;

public interface ToDoRepository extends CrudRepository<ToDo, String> {}

/*
* It is not necessary to create a concrete class or implement anything;
* Spring Data JPA does the implementation for us.
* All the CRUD actions handle anything that we need to persist data.
* That's it-there's nothing else that we need to do to use ToDoRepository.
*/