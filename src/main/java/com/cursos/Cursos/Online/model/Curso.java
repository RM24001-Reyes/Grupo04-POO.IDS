
package com.cursos.Cursos.Online.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    @JsonBackReference(value = "instructor-curso")
    private Instructor instructor;

    @Enumerated(EnumType.STRING)
    private EstadoCurso estado;

    @ManyToMany
    @JoinTable(
            name = "inscripciones",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    @JsonIgnore
    private List<Estudiante> estudiantesInscritos;
}
