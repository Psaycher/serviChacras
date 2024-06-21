package com.FinalEgg.ServiChacras.servicios;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.FinalEgg.ServiChacras.enumeraciones.Rol;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.entidades.Puntuacion;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.PuntuacionRepositorio;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;

@Service
public class PuntuacionServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private PuntuacionRepositorio puntuacionRepositorio;

    @Transactional
    public void PuntuarUsuario(String idPuntuador, String idPuntuado, String rolString, Integer puntos) throws MiExcepcion {
        Optional<Usuario> optPuntuador = usuarioRepositorio.findById(idPuntuador);
        Optional<Usuario> optPuntuado = usuarioRepositorio.findById(idPuntuador);
        Rol rol = Rol.valueOf(rolString.toUpperCase());
        Puntuacion puntuacion = new Puntuacion();

        if (optPuntuador.isPresent()) { 
            Usuario puntuador = optPuntuador.get(); 

            if (optPuntuado.isPresent()) { 
                Usuario puntuado = optPuntuado.get();
                
                puntuacion.setPuntuador(puntuador);
                puntuacion.setPuntuado(puntuado);
                puntuacion.setRol(rol);
                puntuacion.setPuntos(puntos);

                puntuacionRepositorio.save(puntuacion);
        
            } else { throw new MiExcepcion("El usuario que recibe la puntuación no existe."); }

        } else { throw new MiExcepcion("El usuario que otorga la puntuación no existe."); }
    }
}