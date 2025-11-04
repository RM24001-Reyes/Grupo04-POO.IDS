package com.cursos.Cursos.Online.controller;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    //  Listar todos los cursos
    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    // Obtener curso por ID
    @GetMapping("/{idCurso}")
    public ResponseEntity<Curso> obtenerCurso(@PathVariable Long idCurso) {
        return ResponseEntity.of(cursoService.obtenerCurso(idCurso));
    }

    //  Crear curso (versión general)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.crearCurso(curso));
    }

    // Actualizar curso
    @PutMapping("/{idCurso}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long idCurso, @RequestBody Curso datos) {
        return ResponseEntity.ok(cursoService.actualizarCurso((long) idCurso, datos));
    }

    // Eliminar curso
    @DeleteMapping("/{idCurso}")
    public ResponseEntity<String> eliminarCurso(@PathVariable Long idCurso) {
        cursoService.eliminarCurso((long) Math.toIntExact(idCurso));
        return ResponseEntity.ok("Curso eliminado correctamente.");
    }
}
