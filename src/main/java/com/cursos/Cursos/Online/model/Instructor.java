package com.cursos.Cursos.Online.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Instructor extends Usuario {

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "instructor-curso")
    private List<Curso> cursosCreados;
}
