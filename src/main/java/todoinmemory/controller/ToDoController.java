package todoinmemory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoinmemory.domain.ToDo;
import todoinmemory.domain.ToDoBuilder;
import todoinmemory.repository.CommonRepository;
import todoinmemory.validation.ToDoValidationError;
import todoinmemory.validation.ToDoValidationErrorBuilder;

import javax.validation.Valid;
import java.net.URI;

// @RestController annotation is a convenience annotation that is itself annotated with @Controller and @ResponseBody
@RestController
@RequestMapping("/api")
public class ToDoController {

    private CommonRepository<ToDo> repository;
/*
 The constructor is annotated with @Autowired, meaning
 that it injects the CommonRepository<ToDo> implementation
 This annotation can be omitted; Spring automatically injects any declared dependency since version 4.3.
*/
    @Autowired
    public ToDoController(CommonRepository<ToDo> repository) {
        this.repository=repository;
    }

/*
    ResponseEntity<T>. This class returns a full response,
    including HTTP headers, and the body is converted through
    HttpMessageConverters and written to the HTTP response. The
    ResponseEntity<T> class supports a fluent API, so it is easy to create
    the response.
*/

    @GetMapping("/todo")
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable String id) {
        return ResponseEntity.ok(repository.findById(id));
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<ToDo> setCompleted(@PathVariable  String id) {
        ToDo result=repository.findById(id);
        result.setCompleted(true);
        repository.save(result);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location",location.toString()).build();
    }

/*  @RequestBody annotation sends a request with a body.
 *  Normally, when you submit a form or a particular content, this
 *  class receives a JSON format ToDo, then the HttpMessageConverter
 *  deserializes the JSON into a ToDo instance; this is done automatically
 *  thanks to Spring Boot and its auto-configuration because it registers
 *  the MappingJackson2HttpMessageConverter by default.
*/

/*
@Valid. This annotation validates incoming data and is used as
a method’s parameters. To trigger a validator, it is necessary to
annotate the data you want to validate with @NotNull, @NotBlank,
and other annotations. In this case, the ToDo class uses those
annotations in the ID and description fields. If the validator finds
errors, they are collected in the Errors class (in this case, a hibernate
validator that came with the spring-webmvc jars is registered and
used as a global validator; you can create your own custom validation
and override Spring Boot’s defaults).
*/
    @RequestMapping(value = "/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createTodo(@Valid @RequestBody ToDo toDo, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        ToDo result=repository.save(toDo);
//        Location exposes the ID of the todo you have just created
        URI location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable String id) {
        repository.delete(ToDoBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<ToDo> deleteToDo(@RequestBody ToDo toDo) {
        repository.delete(toDo);
        return ResponseEntity.noContent().build();
    }

/*
 * @ResponseStatus. Normally, this annotation is used when a method
 * has a void return type (or null return value). This annotation sends
 * back the HTTP status code specified in the response.
*/
@ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        return new ToDoValidationError(exception.getMessage());
    }
}
