package com.cursos.Cursos.Online.controller;

import com.cursos.Cursos.Online.model.Usuario;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Crear un usuario genérico (opcional)
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));
    }

    //  Crear un instructor
    @PostMapping("/instructores")
    public ResponseEntity<Instructor> crearInstructor(@RequestBody Instructor instructor) {
        return ResponseEntity.ok(usuarioService.crearInstructor(instructor));
    }

    // ✅ Crear un estudiante
    @PostMapping("/estudiantes")
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        return ResponseEntity.ok(usuarioService.crearEstudiante(estudiante));
    }

    //  Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    //  Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.of(usuarioService.obtenerPorId(id));
    }

    //  Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datos) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, datos));
    }

    //  Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }
}
