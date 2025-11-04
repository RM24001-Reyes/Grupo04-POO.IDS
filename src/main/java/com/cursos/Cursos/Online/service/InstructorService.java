package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.EstadoCurso;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.repository.CursoRepository;
import com.cursos.Cursos.Online.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class InstructorService {

    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;

    public InstructorService(CursoRepository cursoRepository,
                             EstudianteRepository estudianteRepository) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
    }

    public Curso crearCurso(Curso curso) {
        if (curso.getTitulo() == null || curso.getDescripcion() == null) ;
        {
            throw new RuntimeException("ESTOS SON CAMPOS OBLIGATORIOS");
        }

    }

    public Curso editarCurso(Long idCurso, String nuevoTitulo, String nuevaDescripcion) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("El curso no existe"));
        curso.setTitulo(nuevoTitulo);
        curso.setDescripcion(nuevaDescripcion);
        return cursoRepository.save(curso);
    }
    public void eliminarCurso(Long id){
        Curso curso =cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El curso no existe"));
         cursoRepository.delete(curso);

    }
    public List<Estudiante> listarEstudiantesInscritos(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado."));
        return curso.getEstudiantesInscritos();
    }

}

