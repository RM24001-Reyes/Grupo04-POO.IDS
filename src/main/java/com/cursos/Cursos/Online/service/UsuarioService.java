package com.cursos.Cursos.Online.service;

import com.cursos.Cursos.Online.exception.ConflictException;
import com.cursos.Cursos.Online.exception.ResourceNotFoundException;
import com.cursos.Cursos.Online.model.Usuario;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.repository.UsuarioRepository;
import com.cursos.Cursos.Online.repository.InstructorRepository;
import com.cursos.Cursos.Online.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final InstructorRepository instructorRepository;
    private final EstudianteRepository estudianteRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          InstructorRepository instructorRepository,
                          EstudianteRepository estudianteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.instructorRepository = instructorRepository;
        this.estudianteRepository = estudianteRepository;
    }

    // ============================================================
    // CREAR USUARIO GENÉRICO
    // ============================================================
    public Usuario crearUsuario(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ConflictException("El correo '" + usuario.getEmail() + "' ya está registrado.");
        }

        return usuarioRepository.save(usuario);
    }

    // ============================================================
    // CREAR INSTRUCTOR
    // ============================================================
    public Instructor crearInstructor(Instructor instructor) {

        if (usuarioRepository.existsByEmail(instructor.getEmail())) {
            throw new ConflictException("El correo '" + instructor.getEmail() + "' ya está registrado.");
        }

        return instructorRepository.save(instructor);
    }

    // ============================================================
    // CREAR ESTUDIANTE
    // ============================================================
    public Estudiante crearEstudiante(Estudiante estudiante) {

        if (usuarioRepository.existsByEmail(estudiante.getEmail())) {
            throw new ConflictException("El correo '" + estudiante.getEmail() + "' ya está registrado.");
        }

        return estudianteRepository.save(estudiante);
    }

    // ============================================================
    // LISTAR
    // ============================================================
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }


    // ============================================================
    // ACTUALIZAR USUARIO
    // ============================================================
    public Usuario actualizarUsuario(Long id, Usuario datos) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Si cambia el email, validar repetido
        if (!usuario.getEmail().equals(datos.getEmail())
                && usuarioRepository.existsByEmail(datos.getEmail())) {
            throw new ConflictException("El correo '" + datos.getEmail() + "' ya está registrado.");
        }

        usuario.setNombre(datos.getNombre());
        usuario.setEmail(datos.getEmail());
        usuario.setRol(datos.getRol());

        return usuarioRepository.save(usuario);
    }

    // ============================================================
    // ELIMINAR
    // ============================================================
    public void eliminarUsuario(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }
}
