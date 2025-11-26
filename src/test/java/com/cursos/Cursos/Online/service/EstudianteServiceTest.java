package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.EstadoCurso;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.repository.CursoRepository;
import com.cursos.Cursos.Online.repository.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstudianteServiceTest {

    private EstudianteRepository estudianteRepository;
    private CursoRepository cursoRepository;
    private EstudianteService estudianteService;

    @BeforeEach
    void setup() {
        estudianteRepository = mock(EstudianteRepository.class);
        cursoRepository = mock(CursoRepository.class);
        estudianteService = new EstudianteService(estudianteRepository, cursoRepository);
    }

    // ===============================================
    // 1. INSCRIBIR ESTUDIANTE EN CURSO (CASO ÉXITO)
    // ===============================================
    @Test
    void testInscribirCurso_Exito() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setCursosInscritos(new ArrayList<>());

        Curso curso = new Curso();
        curso.setId_curso(10L);
        curso.setEstado(EstadoCurso.ACTIVO);
        curso.setEstudiantesInscritos(new ArrayList<>());

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(cursoRepository.findById(10L)).thenReturn(Optional.of(curso));

        String resultado = estudianteService.inscribirCurso(1L, 10L);

        assertEquals("✅ Inscripción completada exitosamente.", resultado);
        assertTrue(curso.getEstudiantesInscritos().contains(estudiante));
        assertTrue(estudiante.getCursosInscritos().contains(curso));

        verify(cursoRepository).save(curso);
        verify(estudianteRepository).save(estudiante);
    }

    // ===============================================
    // 2. CURSO NO ACTIVO
    // ===============================================
    @Test
    void testInscribirCurso_CursoNoActivo() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);

        Curso curso = new Curso();
        curso.setEstado(EstadoCurso.INACTIVO);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(cursoRepository.findById(10L)).thenReturn(Optional.of(curso));

        String resultado = estudianteService.inscribirCurso(1L, 10L);

        assertEquals("El curso no está activo.", resultado);
    }

    // ===============================================
    // 3. ESTUDIANTE YA INSCRITO
    // ===============================================
    @Test
    void testInscribirCurso_YaInscrito() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);

        Curso curso = new Curso();
        curso.setEstado(EstadoCurso.ACTIVO);
        curso.setEstudiantesInscritos(new ArrayList<>(List.of(estudiante)));

        estudiante.setCursosInscritos(new ArrayList<>(List.of(curso)));

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(cursoRepository.findById(10L)).thenReturn(Optional.of(curso));

        String resultado = estudianteService.inscribirCurso(1L, 10L);

        assertEquals("El estudiante ya está inscrito en este curso.", resultado);
    }

    // ===============================================
    // 4. ESTUDIANTE NO EXISTE
    // ===============================================
    @Test
    void testInscribirCurso_EstudianteNoExiste() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> estudianteService.inscribirCurso(1L, 10L)
        );

        assertEquals("El ID del estudiante no existe.", ex.getMessage());
    }

    // ===============================================
    // 5. CURSO NO EXISTE
    // ===============================================
    @Test
    void testInscribirCurso_CursoNoExiste() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(cursoRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> estudianteService.inscribirCurso(1L, 10L)
        );

        assertEquals("El curso no existe, ingrese otro ID.", ex.getMessage());
    }

    // ===============================================
    // 6. SALIR DE UN CURSO (CASO ÉXITO)
    // ===============================================
    @Test
    void testSalirCurso_Exito() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setCursosInscritos(new ArrayList<>());

        Curso curso = new Curso();
        curso.setId_curso(10L);
        curso.setEstudiantesInscritos(new ArrayList<>());
        curso.getEstudiantesInscritos().add(estudiante);
        estudiante.getCursosInscritos().add(curso);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(cursoRepository.findById(10L)).thenReturn(Optional.of(curso));

        String resultado = estudianteService.salirCurso(1L, 10L);

        assertEquals("✅ El estudiante ha salido del curso exitosamente.", resultado);
        assertFalse(curso.getEstudiantesInscritos().contains(estudiante));
        assertFalse(estudiante.getCursosInscritos().contains(curso));

        verify(cursoRepository).save(curso);
        verify(estudianteRepository).save(estudiante);
    }

    // ===============================================
    // 7. SALIR DE CURSO SIN ESTAR INSCRITO
    // ===============================================
    @Test
    void testSalirCurso_NoInscrito() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setCursosInscritos(new ArrayList<>());

        Curso curso = new Curso();
        curso.setEstudiantesInscritos(new ArrayList<>());

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(cursoRepository.findById(10L)).thenReturn(Optional.of(curso));

        String resultado = estudianteService.salirCurso(1L, 10L);

        assertEquals("El estudiante no está inscrito en este curso.", resultado);
    }

    // ===============================================
    // 8. LISTAR CURSOS DEL ESTUDIANTE
    // ===============================================
    @Test
    void testListarCursosDeEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1L);

        Curso c1 = new Curso();
        Curso c2 = new Curso();

        estudiante.setCursosInscritos(List.of(c1, c2));

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        List<Curso> resultado = estudianteService.listarCursosDeEstudiante(1L);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(c1));
        assertTrue(resultado.contains(c2));
    }

    // ===============================================
    // 9. LISTAR CURSOS → ESTUDIANTE NO EXISTE
    // ===============================================
    @Test
    void testListarCursos_EstudianteNoExiste() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> estudianteService.listarCursosDeEstudiante(1L)
        );

        assertEquals("Estudiante no encontrado.", ex.getMessage());
    }
}
