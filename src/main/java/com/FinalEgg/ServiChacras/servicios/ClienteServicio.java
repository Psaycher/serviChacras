package com.FinalEgg.ServiChacras.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.FinalEgg.ServiChacras.entidades.Cliente;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;

@Service
public class ClienteServicio {
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Transactional
    public void crearCliente(Usuario usuario) throws MiExcepcion {
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setPromPuntuacion(0.0);
        clienteRepositorio.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() { return clienteRepositorio.findAll(); }

    @Transactional
    public void actualizar(String id, String nombre, String apellido, String email, String password, String password2, String barrioChacras, String rolString, String direccion, String telefono) throws MiExcepcion {
        Optional<Cliente> optionalCliente = clienteRepositorio.findById(id);

        optionalCliente.ifPresent(cliente -> {
            Usuario usuario = cliente.getUsuario();
            UsuarioServicio usuarioServicio = new UsuarioServicio();

            try { usuarioServicio.actualizar(usuario.getId(), nombre, apellido, email, password, password2, barrioChacras, rolString, direccion, telefono); }
            catch (MiExcepcion e) { e.printStackTrace(); }

            cliente.setUsuario(usuario);
            clienteRepositorio.save(cliente);
        });
    }

    @Transactional
    public void eliminarCliente(String id) { clienteRepositorio.deleteById(id); }

    @Transactional
    public void eliminarPorIdUsuario(String IdUsuario) {
        String id = clienteRepositorio.idUsuario(IdUsuario);
        if (id != null) { clienteRepositorio.deleteById(id); }
    }

    @Transactional(readOnly = true)
    public String idUsuario(String id) { return clienteRepositorio.idUsuario(id); }

    @Transactional
    public void ClienteMensaje(String id) { clienteRepositorio.deleteById(id); }

    @Transactional(readOnly = true)
    public Cliente getOne(String id) { return clienteRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public Cliente getPorEmail(String email) { return clienteRepositorio.getPorEmail(email); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorBarrio(String barrio) { return clienteRepositorio.getPorBarrio(barrio); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorBarrioCompartido(String barrio, String idProveedor) { return clienteRepositorio.getPorBarrioCompartido(barrio, idProveedor); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorNombreCompleto(String nombreUsuario) { return clienteRepositorio.getPorNombreCompleto(nombreUsuario); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorNombreCompartido(String nombreUsuario, String idProveedor) { return clienteRepositorio.getPorNombreCompartido(nombreUsuario, idProveedor); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorDireccion(String barrio, String direccion) { return clienteRepositorio.getPorDireccion(barrio, direccion); }

    @Transactional(readOnly = true)
    public Cliente getPorPedido(String id) { return clienteRepositorio.getPorPedido(id); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorPedidoYNombre(String nombreUsuario, String idPedido) { return clienteRepositorio.getPorPedidoYNombre(nombreUsuario, idPedido); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorPedidoCompartido(String idProveedor) { return clienteRepositorio.getPorPedidoCompartido(idProveedor); }

    @Transactional(readOnly = true)
    public List<Object> getComentarios(String id) { return clienteRepositorio.getComentarios(id); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorBarrioYNombre(String nombreUsuario, String barrio) { return clienteRepositorio.getPorBarrioYNombre(nombreUsuario, barrio); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorBarrioYNombreCompartido( String nombreUsuario, String barrio, String idProveedor) { return clienteRepositorio.getPorBarrioYNombreCompartido(nombreUsuario, barrio, idProveedor); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorPedidoYBarrio(String idPedido, String barrio) { return clienteRepositorio.getPorPedidoYBarrio(idPedido, barrio); }

    @Transactional(readOnly = true)
    public List<Cliente> getPorPedidoBarrioYNombre(String nombreUsuario, String idPedido, String barrio) { return clienteRepositorio.getPorPedidoBarrioYNombre(nombreUsuario, idPedido, barrio); }
}