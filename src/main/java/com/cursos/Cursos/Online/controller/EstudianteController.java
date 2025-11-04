package com.cursos.Cursos.Online.controller;

import com.cursos.Cursos.Online.model.Curso;
import com.cursos.Cursos.Online.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    //  Inscribirse en un curso
    @PostMapping("/inscribir/{idUsuario}/{idCurso}")
    public ResponseEntity<String> inscribirCurso(@PathVariable Long idUsuario, @PathVariable Long idCurso) {
        return ResponseEntity.ok(estudianteService.inscribirCurso(idUsuario, idCurso));
    }

    //  Salir de un curso
    @DeleteMapping("/salir/{idUsuario}/{idCurso}")
    public ResponseEntity<String> salirCurso(@PathVariable Long idUsuario, @PathVariable Long idCurso) {
        return ResponseEntity.ok(estudianteService.salirCurso(idUsuario, idCurso));
    }

    //  Listar cursos del estudiante
    @GetMapping("/cursos/{idUsuario}")
    public ResponseEntity<List<Curso>> listarCursosDeEstudiante(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(estudianteService.listarCursosDeEstudiante(idUsuario));
    }
}
