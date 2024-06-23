package com.FinalEgg.ServiChacras.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.FinalEgg.ServiChacras.entidades.*;
import com.FinalEgg.ServiChacras.enumeraciones.Estado;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.PedidoRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ServicioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;

@Service
public class PedidoServicio {
    @Autowired
    private MensajeServicio mensajeServicio;
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ServicioRepositorio servicioRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Transactional
    public Pedido crearPedido(String idCliente, String idServicio, String idProveedor) throws MiExcepcion {
        Pedido pedido = new Pedido();

        Optional<Cliente> opcionalCliente = clienteRepositorio.findById(idCliente);
        Cliente cliente = new Cliente();

        if (opcionalCliente.isPresent()) { cliente = opcionalCliente.get(); }
        pedido.setCliente(cliente);

        Optional<Servicio> opcionalServicio = servicioRepositorio.findById(idServicio);
        Servicio servicio = new Servicio();

        if (opcionalServicio.isPresent()) { servicio = opcionalServicio.get(); }
        pedido.setServicio(servicio);

        Optional<Proveedor> opcionalProveedor = proveedorRepositorio.findById(idProveedor);
        Proveedor proveedor = new Proveedor();

        if (opcionalProveedor.isPresent()) { proveedor = opcionalProveedor.get(); }
        pedido.setProveedor(proveedor);
        pedido.setFechaPedido(new Date());
        pedido.setEstado(Estado.PENDIENTE);
        pedido.setAlta(true);

        pedidoRepositorio.save(pedido);
        return pedido;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() { return pedidoRepositorio.findAll(); }

    @Transactional
    public void actualizar(String id, String estadoString, String comentario, Integer puntuacion) throws MiExcepcion {
        Optional<Pedido> optionalPedido = pedidoRepositorio.findById(id);

        optionalPedido.ifPresent(pedido -> {
            Estado estado = Estado.valueOf(estadoString.toUpperCase());
            pedido.setEstado(estado);
            pedido.setComentario(comentario);
            pedidoRepositorio.save(pedido);
        });
    }

    @Transactional
    public void agregarMensajear(String id, String idUsuario, String idRemitente, String asunto, String rolString, String contenido, MultipartFile archivo) throws MiExcepcion {
        Optional<Pedido> optionalPedido = pedidoRepositorio.findById(id);
        
        if (optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(idUsuario);

            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                List<Mensaje> listMensajes = pedido.getMensajes();

                try {
                    Mensaje mensaje = mensajeServicio.crearMensaje(idRemitente, asunto, pedido, usuario, rolString, contenido, archivo);
            
                    if (listMensajes == null) { listMensajes = new ArrayList<>(); }
            
                    listMensajes.add(mensaje); 
                    pedido.setMensajes(listMensajes); 
                    pedidoRepositorio.save(pedido);

                } catch (MiExcepcion e) { throw new MiExcepcion("Error al crear el mensaje: " + e.getMessage()); }

            } else { throw new MiExcepcion("El usuario seleccionado no existe."); }

        } else { throw new MiExcepcion("El pedido seleccionado no existe."); } 
    }

    @Transactional
    public void eliminarMensaje(String idPedido, String idMensaje) throws MiExcepcion {
        Optional<Pedido> optionalPedido = pedidoRepositorio.findById(idPedido);
        
        if (optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            List<Mensaje> mensajes = pedido.getMensajes();
            
            boolean removed = mensajes.removeIf(mensaje -> mensaje.getId().equals(idMensaje));
            
            if (removed) { pedidoRepositorio.save(pedido); } 
            else { throw new MiExcepcion("El mensaje con id " + idMensaje + " no existe en el pedido con id " + idPedido); }

        } else { throw new MiExcepcion("El pedido con id " + idPedido + " no existe."); }
    }

    @Transactional
    public void cancelarPedido(String id) { 
        Optional<Pedido> respuesta = pedidoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Pedido pedido = respuesta.get();
            pedido.setAlta(false); 
        }
    }

    @Transactional
    public void deletearPedido(String id) { pedidoRepositorio.deleteById(id); }

    @Transactional(readOnly = true)
    public Pedido getOne(String id) { return pedidoRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public String getIdPedidoPorClientes(String id) { return pedidoRepositorio.getIdPedidoPorClientes(id); }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidoPorClientes(String id) { return pedidoRepositorio.getPedidoPorClientes(id); }

    @Transactional(readOnly = true)
    public String getIdPedidoPorProveedores(String id) { return pedidoRepositorio.getIdPedidoPorProveedores(id); }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidoPorProveedores(String id) { return pedidoRepositorio.getPedidoPorProveedores(id); }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidosPendiente() { return pedidoRepositorio.getPedidosPendiente(); }
}