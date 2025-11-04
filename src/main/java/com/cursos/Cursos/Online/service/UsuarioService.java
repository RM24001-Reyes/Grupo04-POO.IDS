package com.cursos.Cursos.Online.service;

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

    // ✅ Usuario genérico (si lo usas)
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // ✅ Crear instructor
    public Instructor crearInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    // ✅ Crear estudiante
    public Estudiante crearEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario actualizarUsuario(Long id, Usuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(datos.getNombre());
        usuario.setEmail(datos.getEmail());
        usuario.setRol(datos.getRol());
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
