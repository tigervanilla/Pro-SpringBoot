package todoinmemory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import todoinmemory.domain.ToDo;
import todoinmemory.repository.ToDoRepository;

@RestController
public class ToDoController {
    private ToDoRepository repository;

    private ToDoController(ToDoRepository repository){
        this.repository=repository;
    }

//    We are doing async and non-blocking calls.

    @GetMapping("/todo/{id}")
    public Mono<ToDo> getToDo(@PathVariable String id){
        return this.repository.findById(id);
    }

    @GetMapping("/todos")
    public Flux<ToDo> getToDos(){
        return this.repository.findAll();
    }
}
