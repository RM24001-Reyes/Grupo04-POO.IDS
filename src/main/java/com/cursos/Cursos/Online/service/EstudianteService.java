package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.EstadoCurso;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.repository.CursoRepository;
import com.cursos.Cursos.Online.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    public EstudianteService(EstudianteRepository estudianteRepository, CursoRepository cursoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    //  1. Inscribirse en un curso
    public String inscribirCurso(Long id, Long idCurso) {
        // Buscar estudiante e curso
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El ID del estudiante no existe."));
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("El curso no existe, ingrese otro ID."));

        // Validar si el curso está activo
        if (curso.getEstado() != EstadoCurso.ACTIVO) {
            return "El curso no está activo.";
        }

        // Validar si ya está inscrito
        if (curso.getEstudiantesInscritos().contains(estudiante)) {
            return "El estudiante ya está inscrito en este curso.";
        }

        // Agregar inscripción en ambos lados
        curso.getEstudiantesInscritos().add(estudiante);
        estudiante.getCursosInscritos().add(curso);

        // Guardar en BD
        cursoRepository.save(curso);
        estudianteRepository.save(estudiante);

        return "✅ Inscripción completada exitosamente.";
    }

    // 2. Salir de un curso
    public String salirCurso(Long idUsuario, Long idCurso) {
        Estudiante estudiante = estudianteRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("El ID del estudiante no se ha encontrado."));
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("El curso no se ha encontrado, ingrese otro ID."));

        // Validar si realmente está inscrito
        if (!curso.getEstudiantesInscritos().contains(estudiante)) {
            return "El estudiante no está inscrito en este curso.";
        }

        // Eliminar relación en ambos lados
        curso.getEstudiantesInscritos().remove(estudiante);
        estudiante.getCursosInscritos().remove(curso);

        // Guardar cambios
        cursoRepository.save(curso);
        estudianteRepository.save(estudiante);

        return "✅ El estudiante ha salido del curso exitosamente.";
    }

    //  3. Listar cursos de un estudiante
    public List<Curso> listarCursosDeEstudiante(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado."));
        return estudiante.getCursosInscritos();
    }
}
