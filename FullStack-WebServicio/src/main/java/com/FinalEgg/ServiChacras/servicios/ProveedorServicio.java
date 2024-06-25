package com.FinalEgg.ServiChacras.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.FinalEgg.ServiChacras.entidades.*;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.ServicioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;

@Service
public class ProveedorServicio {
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private ServicioRepositorio servicioRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Transactional
    public void crearProveedor(Usuario usuario, MultipartFile archivo, String descripcion, String idServicio) throws MiExcepcion {
        Proveedor proveedor = new Proveedor();

        proveedor.setUsuario(usuario);

        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setImagen(imagen);

        proveedor.setDescripcion(descripcion);

        Optional<Servicio> opcionalServicio = servicioRepositorio.findById(idServicio);
        Servicio servicio = new Servicio();

        if (opcionalServicio.isPresent()) { servicio = opcionalServicio.get(); }
        proveedor.setServicio(servicio);

        proveedorRepositorio.save(proveedor);
    }

    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedores() { return proveedorRepositorio.findAll(); }

    @Transactional
    public void actualizar(String id, Usuario usuario, MultipartFile archivo, String descripcion, String idServicio) throws MiExcepcion {
        Optional<Proveedor> optionalProveedor = proveedorRepositorio.findById(id);

        optionalProveedor.ifPresent(proveedor -> {
            proveedor.setUsuario(usuario);

            Imagen imagen = null;
            try { imagen = imagenServicio.guardar(archivo); }
            catch (MiExcepcion e) { throw new RuntimeException(e); }
            proveedor.setImagen(imagen);

            proveedor.setDescripcion(descripcion);

            Optional<Servicio> opcionalServicio = servicioRepositorio.findById(idServicio);
            Servicio servicio = new Servicio();

            if (opcionalServicio.isPresent()) { servicio = opcionalServicio.get(); }
            proveedor.setServicio(servicio);

            proveedorRepositorio.save(proveedor);
        });
    }

    @Transactional
    public void eliminarProveedor(String id) { proveedorRepositorio.deleteById(id); }

    @Transactional
    public void eliminarPorIdUsuario(String IdUsuario) {
        String id = proveedorRepositorio.idUsuario(IdUsuario);
        if (id != null) { proveedorRepositorio.deleteById(id); }
    }
    
    @Transactional(readOnly = true)
    public Proveedor getOne(String id) { return proveedorRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public String idUsuario(String idUsuario) { return proveedorRepositorio.idUsuario(idUsuario); }

    @Transactional(readOnly = true)
    public Proveedor getPorEmail(String email) { return proveedorRepositorio.getPorEmail(email); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorNombreCompleto(String nombreUsuario) { return proveedorRepositorio.getPorNombreCompleto(nombreUsuario); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorBarrio(String barrio) { return proveedorRepositorio.getPorBarrio(barrio); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorDireccion(String barrio, String direccion) { return proveedorRepositorio.getPorDireccion(barrio, direccion); }

    @Transactional(readOnly = true)
    public Proveedor getPorPedido(String id) { return proveedorRepositorio.getPorPedido(id); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorServicio(String id) { return proveedorRepositorio.getPorServicio(id); }

    @Transactional(readOnly = true)
    public List<Object> getComentarios(String id) { return proveedorRepositorio.getComentarios(id); }

    @Transactional(readOnly = true)
    public Integer getPuntuaciones(String id) { return proveedorRepositorio.getPuntuaciones(id); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorServicioYNombre(String nombreUsuario, String idServicio) { return proveedorRepositorio.getPorServicioYNombre(nombreUsuario, idServicio); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorServicioYBarrio(String idServicio, String barrio) { return proveedorRepositorio.getPorServicioYBarrio(idServicio, barrio); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorBarrioYNombre(String nombreUsuario, String barrio) { return proveedorRepositorio.getPorBarrioYNombre(nombreUsuario, barrio); }

    @Transactional(readOnly = true)
    public List<Proveedor> getPorServicioBarrioYNombre(String idServicio, String nombreUsuario, String barrio) { return proveedorRepositorio.getPorServicioBarrioYNombre(idServicio, nombreUsuario, barrio); }
}