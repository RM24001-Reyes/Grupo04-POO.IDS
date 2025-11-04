
package com.cursos.Cursos.Online.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Data
@Entity
public class Estudiante extends Usuario {

    @ManyToMany(mappedBy = "estudiantesInscritos")
    @JsonIgnore
    private List<Curso> cursosInscritos;
}
