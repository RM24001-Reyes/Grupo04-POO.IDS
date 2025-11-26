package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.repository.CursoRepository;
import com.cursos.Cursos.Online.repository.EstudianteRepository;
import com.cursos.Cursos.Online.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorService {

    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;
    private final InstructorRepository instructorRepository;

    public InstructorService(CursoRepository cursoRepository,
                             EstudianteRepository estudianteRepository,
                             InstructorRepository instructorRepository) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
        this.instructorRepository = instructorRepository;
    }

    public Curso crearCurso(Long id, Curso curso) {

        if (curso.getTitulo() == null || curso.getDescripcion() == null) {
            throw new RuntimeException("ESTOS SON CAMPOS OBLIGATORIOS");
        }

        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El instructor no existe"));

        curso.setInstructor(instructor);

        return cursoRepository.save(curso);
    }

    public Curso editarCurso(Long idCurso, String nuevoTitulo, String nuevaDescripcion) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("El curso no existe"));

        curso.setTitulo(nuevoTitulo);
        curso.setDescripcion(nuevaDescripcion);

        return cursoRepository.save(curso);
    }

    public void eliminarCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El curso no existe"));

        cursoRepository.delete(curso);
    }

    public List<Estudiante> listarEstudiantesInscritos(Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        return curso.getEstudiantesInscritos();
    }
}
