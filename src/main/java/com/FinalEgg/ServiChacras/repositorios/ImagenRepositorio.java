package com.FinalEgg.ServiChacras.repositorios;

import org.springframework.stereotype.Repository;
import com.FinalEgg.ServiChacras.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String> {}