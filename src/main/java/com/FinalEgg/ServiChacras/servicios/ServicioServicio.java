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
    public void crearServicio(String nombre, String categoria, String detalle) throws MiExcepcion {
        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);
        servicio.setCategoria(categoria);
        servicio.setDetalle(detalle);
        servicioRepositorio.save(servicio);
    }

    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() { return servicioRepositorio.findAll(); }

    @Transactional
    public void actualizar(String id, String nombre, String categoria, String nueva, String detalle) throws MiExcepcion {
        Optional<Servicio> respuesta = servicioRepositorio.findById(id);
        System.out.println("dentro de actualizar en servicioServicio");

        if (respuesta.isPresent()) {
            Servicio servicio = respuesta.get();
            
            if (nombre == null || nombre.trim().isEmpty()) { nombre = servicio.getNombre(); }

            if (categoria == null || categoria.trim().isEmpty()) { 
                if (nueva == null || nueva.trim().isEmpty()) { categoria = servicio.getCategoria(); }
                else { categoria = nueva; }
            }

            if (detalle == null || detalle.trim().isEmpty()) { detalle = servicio.getDetalle(); }  

            servicio.setNombre(nombre);
            servicio.setCategoria(categoria);
            servicio.setDetalle(detalle);
            servicioRepositorio.save(servicio);
        };
    }

    @Transactional
    public void eliminarServicio(String id) { servicioRepositorio.deleteById(id); }

    @Transactional(readOnly = true)
    public Servicio getOne(String id) { return servicioRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public List<String> listarCategoria() { return servicioRepositorio.listarCategorias(); }

    @Transactional(readOnly = true)
    public List<Object> getServicioPorProveedores(String id) { return servicioRepositorio.getServicioPorProveedores(id); }

    @Transactional(readOnly = true)
    public List<Object> getServicioPorPedido(String id) { return servicioRepositorio.getServicioPorPedido(id); }

    @Transactional(readOnly = true)
    public List<Servicio> listarPorCategoria(String categoria) { return servicioRepositorio.listarPorCategoria(categoria); }
}