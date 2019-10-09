package todoinmemory.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * @Data generates a default constructor (if you don’t have one)
 * and all the setters, getters, and overrides, such as the toString
 *
 * @Entity specifies that the class is an entity and is persisted into the database engine selected.
 *
 * @NoArgsContructor belongs to Lombok library.
 * It creates a class constructor with no arguments.
 * It is required that JPA have a constructor with no arguments.
*/
@Entity
@Data
@NoArgsConstructor
public class ToDo {

    /*
     * @Id specifies the primary key of an entity. The annotated filed should
     * be any Java primitive type or primitive wrapper type.
     *
     * @GeneratedValue provides the generation strategies for the values of primary keys(simple keys only).
     * There are different strategies like IDENTITY, AUTO, SEQUENCE, TABLE and a key generator
     *
     * system-uuid is a unique 36-character ID
    */
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @NotNull
    @NotBlank
    private String description;

    @Column(insertable = true, updatable = false)
    private LocalDateTime created;

    private LocalDateTime modified;
    private boolean completed;

/*
    This is No Longer needed because of @NoArgsConstructor
    public ToDo() {
        LocalDateTime date=LocalDateTime.now();
        this.id= UUID.randomUUID().toString();
        this.created=date;
        this.modified=date;
    }
*/

    public ToDo(String description) {
        this();
        this.description=description;
    }

    /*
     * @PrePersist is a callback that is triggered before any persistent action is taken.
     * It sets the new timestamp for the created and modified fields before the record is inserted into the database.
     */
    @PrePersist
    void onCreate(){
        this.setCreated(LocalDateTime.now());
    }

    /*
     * @PreUpdate is a callback that is triggered before any update action is taken.
     * It sets the new timestamp for the modified field before it is updated into the database.
     */
    @PreUpdate
    void onUpdate(){
        this.setModified(LocalDateTime.now());
    }
}
