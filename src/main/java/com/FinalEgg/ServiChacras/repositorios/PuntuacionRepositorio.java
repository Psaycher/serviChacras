package com.FinalEgg.ServiChacras.repositorios;

import org.springframework.stereotype.Repository;
import com.FinalEgg.ServiChacras.entidades.Puntuacion;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PuntuacionRepositorio extends JpaRepository<Puntuacion, String> {}