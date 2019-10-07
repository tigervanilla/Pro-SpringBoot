package todoinmemory.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

// @Data generates a default constructor (if you don’t have one)
// and all the setters, getters, and overrides, such as the toString
@Data
public class ToDo {
    @NotNull
    private String id;

    @NotNull
    @NotBlank
    private String description;

    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean completed;

    public ToDo() {
        LocalDateTime date=LocalDateTime.now();
        this.id= UUID.randomUUID().toString();
        this.created=date;
        this.modified=date;
    }

    public ToDo(String description) {
        this();
        this.description=description;
    }
}
