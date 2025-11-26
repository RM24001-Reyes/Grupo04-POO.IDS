package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.exception.ConflictException;
import com.cursos.Cursos.Online.exception.ResourceNotFoundException;
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
            throw new ConflictException("El título y la descripción son obligatorios.");
        }

        // Validación: curso con el mismo nombre
        cursoRepository.findByTitulo(curso.getTitulo())
                .ifPresent(c -> {
                    throw new ConflictException("Ya existe un curso con el nombre: " + curso.getTitulo());
                });

        // Validar instructor existente
        if (curso.getInstructor() == null || curso.getInstructor().getId() == null) {
            throw new ConflictException("Debe asignar un instructor existente al curso.");
        }

        Instructor instructor = instructorRepository.findById(curso.getInstructor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado."));

        curso.setInstructor(instructor);
        curso.setEstado(EstadoCurso.ACTIVO);

        return cursoRepository.save(curso);
    }

    // Actualizar curso
    public Curso actualizarCurso(Long id, Curso datos) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado."));

        // Validar si el nuevo nombre ya existe en otro curso
        cursoRepository.findByTitulo(datos.getTitulo())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new ConflictException("Ya existe otro curso con el nombre: " + datos.getTitulo());
                    }
                });

        curso.setTitulo(datos.getTitulo());
        curso.setDescripcion(datos.getDescripcion());
        curso.setEstado(datos.getEstado());

        return cursoRepository.save(curso);
    }

    // Eliminar curso
    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("El curso con ID " + id + " no existe.");
        }
        cursoRepository.deleteById(id);
    }
}
