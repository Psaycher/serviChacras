package com.FinalEgg.ServiChacras.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.FinalEgg.ServiChacras.entidades.Servicio;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.ServicioRepositorio;

@Service
public class ServicioServicio {
    @Autowired
    private ServicioRepositorio servicioRepositorio;

    @Transactional
    public void crearServicio(String nombre, String detalle) throws MiExcepcion {
        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);
        servicio.setDetalle(detalle);
        servicioRepositorio.save(servicio);
    }

    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() { return servicioRepositorio.findAll(); }

    @Transactional
    public void actualizar(String id, String nombre, String detalle) throws MiExcepcion {
        Optional<Servicio> optionalServicio = servicioRepositorio.findById(id);

        optionalServicio.ifPresent(servicio -> {
            servicio.setNombre(nombre);
            servicio.setDetalle(detalle);
            servicioRepositorio.save(servicio);
        });
    }

    @Transactional
    public void deletearServicio(String id) { servicioRepositorio.deleteById(id); }

    @Transactional(readOnly = true)
    public Servicio getOne(String id) { return servicioRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public List<Object> getServicioPorProveedores(String id) { return servicioRepositorio.getServicioPorProveedores(id); }

    @Transactional(readOnly = true)
    public List<Object> getServicioPorPedido(String id) { return servicioRepositorio.getServicioPorPedido(id); }

    @Transactional(readOnly = true)
    public List<Servicio> listarPorCategoria(String categoria) { return servicioRepositorio.listarPorCategoria(categoria); }
}