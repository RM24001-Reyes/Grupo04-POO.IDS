package com.cursos.Cursos.Online.repository;

import com.cursos.Cursos.Online.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long>{
}
