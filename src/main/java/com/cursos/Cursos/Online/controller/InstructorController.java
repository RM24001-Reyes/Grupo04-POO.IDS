package com.cursos.Cursos.Online.controller;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.model.Instructor;
import com.cursos.Cursos.Online.model.Estudiante;
import com.cursos.Cursos.Online.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructores")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    // Crear curso
    @PostMapping("/curso")
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        return ResponseEntity.ok(instructorService.crearCurso(curso));
    }

    //  Editar curso
    @PutMapping("/curso/{idCurso}")
    public ResponseEntity<Curso> editarCurso(@PathVariable Long idCurso,
                                             @RequestParam String titulo,
                                             @RequestParam String descripcion) {
        return ResponseEntity.ok(instructorService.editarCurso(idCurso, titulo, descripcion));
    }

    //  Eliminar curso
    @DeleteMapping("/curso/{idCurso}")
    public ResponseEntity<String> eliminarCurso(@PathVariable Long idCurso) {
        instructorService.eliminarCurso(idCurso);
        return ResponseEntity.ok("Curso eliminado correctamente.");
    }

    //  Listar estudiantes de un curso
    @GetMapping("/curso/{idCurso}/estudiantes")
    public ResponseEntity<List<Estudiante>> listarEstudiantesDeCurso(@PathVariable Long idCurso) {
        return ResponseEntity.ok(instructorService.listarEstudiantesInscritos(idCurso));
    }
}
