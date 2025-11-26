package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.exception.ConflictException;
import com.cursos.Cursos.Online.exception.ResourceNotFoundException;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.model.Rol;
import com.cursos.Cursos.Online.model.Usuario;
import com.cursos.Cursos.Online.repository.EstudianteRepository;
import com.cursos.Cursos.Online.repository.InstructorRepository;
import com.cursos.Cursos.Online.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private InstructorRepository instructorRepository;
    private EstudianteRepository estudianteRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        instructorRepository = Mockito.mock(InstructorRepository.class);
        estudianteRepository = Mockito.mock(EstudianteRepository.class);

        usuarioService = new UsuarioService(
                usuarioRepository,
                instructorRepository,
                estudianteRepository
        );
    }

    // ============================================================
    // CREAR USUARIO
    // ============================================================
    @Test
    void testCrearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@mail.com");

        Mockito.when(usuarioRepository.existsByEmail("carlos@mail.com")).thenReturn(false);
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.crearUsuario(usuario);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Carlos", resultado.getNombre());
        Mockito.verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCrearUsuario_CorreoDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setEmail("existe@mail.com");

        Mockito.when(usuarioRepository.existsByEmail("existe@mail.com")).thenReturn(true);

        Assertions.assertThrows(ConflictException.class,
                () -> usuarioService.crearUsuario(usuario));
    }

    // ============================================================
    // CREAR INSTRUCTOR
    // ============================================================
    @Test
    void testCrearInstructor() {
        Instructor instructor = new Instructor();
        instructor.setNombre("Profe Juan");
        instructor.setEmail("juan@mail.com");

        Mockito.when(usuarioRepository.existsByEmail("juan@mail.com")).thenReturn(false);
        Mockito.when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor resultado = usuarioService.crearInstructor(instructor);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Profe Juan", resultado.getNombre());
        Mockito.verify(instructorRepository).save(instructor);
    }

    // ============================================================
    // CREAR ESTUDIANTE
    // ============================================================
    @Test
    void testCrearEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Ana");
        estudiante.setEmail("ana@mail.com");

        Mockito.when(usuarioRepository.existsByEmail("ana@mail.com")).thenReturn(false);
        Mockito.when(estudianteRepository.save(estudiante)).thenReturn(estudiante);

        Estudiante resultado = usuarioService.crearEstudiante(estudiante);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Ana", resultado.getNombre());
        Mockito.verify(estudianteRepository).save(estudiante);
    }

    // ============================================================
    // LISTAR
    // ============================================================
    @Test
    void testListarUsuarios() {
        List<Usuario> usuarios = Arrays.asList(new Usuario(), new Usuario());

        Mockito.when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarUsuarios();

        Assertions.assertEquals(2, resultado.size());
        Mockito.verify(usuarioRepository).findAll();
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================
    @Test
    void testObtenerPorId() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.obtenerPorId(1L);

        Assertions.assertTrue(resultado.isPresent());
        Assertions.assertEquals(1L, resultado.get().getId());
        Mockito.verify(usuarioRepository).findById(1L);
    }

    // ============================================================
    // ACTUALIZAR USUARIO
    // ============================================================
    @Test
    void testActualizarUsuario_Exito() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombre("Viejo");
        usuarioExistente.setEmail("viejo@mail.com");
        usuarioExistente.setRol(Rol.ESTUDIANTE);

        Usuario datos = new Usuario();
        datos.setNombre("Nuevo");
        datos.setEmail("nuevo@mail.com");
        datos.setRol(Rol.INSTRUCTOR);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        Mockito.when(usuarioRepository.existsByEmail("nuevo@mail.com")).thenReturn(false);
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario actualizado = usuarioService.actualizarUsuario(1L, datos);

        Assertions.assertEquals("Nuevo", actualizado.getNombre());
        Assertions.assertEquals("nuevo@mail.com", actualizado.getEmail());
        Assertions.assertEquals(Rol.INSTRUCTOR, actualizado.getRol());
    }

    @Test
    void testActualizarUsuario_NoExiste() {
        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Usuario datos = new Usuario();

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.actualizarUsuario(1L, datos));

        Assertions.assertEquals("Usuario no encontrado", ex.getMessage());
    }

    // ============================================================
    // ELIMINAR USUARIO
    // ============================================================
    @Test
    void testEliminarUsuario() {
        Mockito.when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.eliminarUsuario(1L);

        Mockito.verify(usuarioRepository).existsById(1L);
        Mockito.verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void testEliminarUsuario_NoExiste() {
        Mockito.when(usuarioRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.eliminarUsuario(1L)
        );

        Assertions.assertEquals("Usuario no encontrado", ex.getMessage());
    }
}