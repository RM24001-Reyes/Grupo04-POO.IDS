package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.EstadoCurso;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.repository.CursoRepository;
import com.cursos.Cursos.Online.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstructorRepository instructorRepository;

    public CursoService(CursoRepository cursoRepository, InstructorRepository instructorRepository) {
        this.cursoRepository = cursoRepository;
        this.instructorRepository = instructorRepository;
    }

    // Listar todos los cursos
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    // Obtener curso por ID
    public Optional<Curso> obtenerCurso(Long idCurso) {
        return cursoRepository.findById(idCurso);
    }

    // Crear curso
    public Curso crearCurso(Curso curso) {
        if (curso.getTitulo() == null || curso.getDescripcion() == null) {
            throw new RuntimeException("El título y la descripción son obligatorios.");
        }

        // Validar instructor existente
        if (curso.getInstructor() == null || curso.getInstructor().getId() == null) {
            throw new RuntimeException("Debe asignar un instructor existente al curso.");
        }

        Instructor instructor = instructorRepository.findById(curso.getInstructor().getId())
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado."));

        curso.setInstructor(instructor);
        curso.setEstado(EstadoCurso.ACTIVO);

        return cursoRepository.save(curso);
    }

    // Actualizar curso
    public Curso actualizarCurso(Long id, Curso datos) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado."));
        curso.setTitulo(datos.getTitulo());
        curso.setDescripcion(datos.getDescripcion());
        curso.setEstado(datos.getEstado());
        return cursoRepository.save(curso);
    }

    // Eliminar curso
    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("El curso con ID " + id + " no existe.");
        }
        cursoRepository.deleteById(id);
    }
}
