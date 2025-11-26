package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.EstadoCurso;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.repository.CursoRepository;
import com.cursos.Cursos.Online.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceTest {

    private CursoRepository cursoRepository;
    private InstructorRepository instructorRepository;
    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        cursoRepository = mock(CursoRepository.class);
        instructorRepository = mock(InstructorRepository.class);
        cursoService = new CursoService(cursoRepository, instructorRepository);
    }

    @Test
    void testListarCursos() {
        List<Curso> cursos = Arrays.asList(new Curso(), new Curso());
        when(cursoRepository.findAll()).thenReturn(cursos);

        List<Curso> resultado = cursoService.listarCursos();

        assertEquals(2, resultado.size());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerCurso() {
        Curso curso = new Curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        Optional<Curso> resultado = cursoService.obtenerCurso(1L);

        assertTrue(resultado.isPresent());
        verify(cursoRepository).findById(1L);
    }

    @Test
    void testCrearCurso_Exitoso() {
        Instructor instructor = new Instructor();
        instructor.setId(10L);

        Curso curso = new Curso();
        curso.setTitulo("Java");
        curso.setDescripcion("Curso básico");
        curso.setInstructor(instructor);

        when(instructorRepository.findById(10L)).thenReturn(Optional.of(instructor));
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Curso resultado = cursoService.crearCurso(curso);

        assertNotNull(resultado);
        assertEquals(EstadoCurso.ACTIVO, resultado.getEstado());
        verify(instructorRepository).findById(10L);
        verify(cursoRepository).save(curso);
    }

    @Test
    void testCrearCurso_SinTituloODescripcion() {
        Curso curso = new Curso();
        curso.setInstructor(new Instructor());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cursoService.crearCurso(curso)
        );

        assertEquals("El título y la descripción son obligatorios.", ex.getMessage());
    }

    @Test
    void testCrearCurso_InstructorNoExiste() {
        Instructor instructor = new Instructor();
        instructor.setId(999L);

        Curso curso = new Curso();
        curso.setTitulo("Test");
        curso.setDescripcion("Desc");
        curso.setInstructor(instructor);

        when(instructorRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cursoService.crearCurso(curso)
        );

        assertEquals("Instructor no encontrado.", ex.getMessage());
    }

    @Test
    void testActualizarCurso() {
        Curso cursoExistente = new Curso();
        cursoExistente.setId(1L);

        Curso nuevosDatos = new Curso();
        nuevosDatos.setTitulo("Nuevo título");
        nuevosDatos.setDescripcion("Nueva desc");
        nuevosDatos.setEstado(EstadoCurso.INACTIVO);

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoExistente));
        when(cursoRepository.save(any(Curso.class))).thenAnswer(inv -> inv.getArgument(0));

        Curso actualizado = cursoService.actualizarCurso(1L, nuevosDatos);

        assertEquals("Nuevo título", actualizado.getTitulo());
        assertEquals("Nueva desc", actualizado.getDescripcion());
        assertEquals(EstadoCurso.INACTIVO, actualizado.getEstado());
    }

    @Test
    void testEliminarCurso_Exitoso() {
        when(cursoRepository.existsById(1L)).thenReturn(true);

        cursoService.eliminarCurso(1L);

        verify(cursoRepository).deleteById(1L);
    }

    @Test
    void testEliminarCurso_NoExiste() {
        when(cursoRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cursoService.eliminarCurso(1L)
        );

        assertEquals("El curso con ID 1 no existe.", ex.getMessage());
    }
}
