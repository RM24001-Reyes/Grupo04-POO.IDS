package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.repository.CursoRepository;
import com.cursos.Cursos.Online.repository.EstudianteRepository;
import com.cursos.Cursos.Online.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    private CursoRepository cursoRepository;
    private EstudianteRepository estudianteRepository;
    private InstructorRepository instructorRepository;
    private InstructorService instructorService;

    private final Long id = 1L;  // ID general del usuario

    @BeforeEach
    void setup() {
        cursoRepository = mock(CursoRepository.class);
        estudianteRepository = mock(EstudianteRepository.class);
        instructorRepository = mock(InstructorRepository.class);

        instructorService = new InstructorService(
                cursoRepository,
                estudianteRepository,
                instructorRepository
        );
    }

    // ============================================
    // TEST: Crear curso (éxito)
    // ============================================
    @Test
    void testCrearCurso_Exito() {
        Curso curso = new Curso();
        curso.setTitulo("Java");
        curso.setDescripcion("Curso básico");

        Instructor instructor = new Instructor();
        instructor.setId(id);

        when(instructorRepository.findById(id)).thenReturn(Optional.of(instructor));
        when(cursoRepository.save(curso)).thenReturn(curso);

        Curso resultado = instructorService.crearCurso(id, curso);

        assertNotNull(resultado);
        assertEquals("Java", resultado.getTitulo());
        assertEquals(instructor, resultado.getInstructor());

        verify(cursoRepository).save(curso);
    }

    // ============================================
    // TEST: Crear curso (falta título/descripción)
    // ============================================
    @Test
    void testCrearCurso_FaltanCampos() {
        Curso curso = new Curso();
        curso.setTitulo(null); // Faltante
        curso.setDescripcion("Desc");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> instructorService.crearCurso(id, curso));

        assertEquals("ESTOS SON CAMPOS OBLIGATORIOS", ex.getMessage());
    }

    // ============================================
    // TEST: Crear curso → instructor NO existe
    // ============================================
    @Test
    void testCrearCurso_InstructorNoExiste() {
        Curso curso = new Curso();
        curso.setTitulo("Java");
        curso.setDescripcion("Curso básico");

        when(instructorRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> instructorService.crearCurso(id, curso));

        assertEquals("El instructor no existe", ex.getMessage());
    }

    // ============================================
    // TEST: Editar curso (éxito)
    // ============================================
    @Test
    void testEditarCurso_Exito() {
        Curso cursoExistente = new Curso();
        cursoExistente.setId_curso(1L);
        cursoExistente.setTitulo("Viejo");
        cursoExistente.setDescripcion("Viejo desc");

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoExistente));
        when(cursoRepository.save(any(Curso.class))).thenAnswer(i -> i.getArgument(0));

        Curso actualizado = instructorService.editarCurso(1L, "Nuevo", "Nuevo desc");

        assertEquals("Nuevo", actualizado.getTitulo());
        assertEquals("Nuevo desc", actualizado.getDescripcion());
    }

    // ============================================
    // TEST: Editar curso (no existe)
    // ============================================
    @Test
    void testEditarCurso_NoExiste() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> instructorService.editarCurso(1L, "Nuevo", "Nuevo"));

        assertEquals("El curso no existe", ex.getMessage());
    }

    // ============================================
    // TEST: Eliminar curso (éxito)
    // ============================================
    @Test
    void testEliminarCurso_Exito() {
        Curso curso = new Curso();
        curso.setId_curso(1L);

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        instructorService.eliminarCurso(1L);

        verify(cursoRepository).delete(curso);
    }

    // ============================================
    // TEST: Eliminar curso (no existe)
    // ============================================
    @Test
    void testEliminarCurso_NoExiste() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> instructorService.eliminarCurso(1L));

        assertEquals("El curso no existe", ex.getMessage());
    }

    // ============================================
    // TEST: Listar estudiantes inscritos
    // ============================================
    @Test
    void testListarEstudiantesInscritos_Exito() {
        Curso curso = new Curso();
        curso.setId_curso(1L);

        Estudiante e1 = new Estudiante();
        Estudiante e2 = new Estudiante();

        curso.setEstudiantesInscritos(List.of(e1, e2));

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        List<Estudiante> resultado = instructorService.listarEstudiantesInscritos(1L);

        assertEquals(2, resultado.size());
    }

    // ============================================
    // TEST: Listar estudiantes → curso NO existe
    // ============================================
    @Test
    void testListarEstudiantesInscritos_NoExisteCurso() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> instructorService.listarEstudiantesInscritos(1L));

        assertEquals("Curso no encontrado", ex.getMessage());
    }
}
