package com.FinalEgg.ServiChacras.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.FinalEgg.ServiChacras.entidades.Pedido;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.entidades.Imagen;
import com.FinalEgg.ServiChacras.entidades.Mensaje;
import com.FinalEgg.ServiChacras.enumeraciones.Estado;
import com.FinalEgg.ServiChacras.enumeraciones.Rol;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.MensajeRepositorio;

@Service
public class MensajeServicio {
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Transactional
    public Mensaje crearMensaje(String idRemitente, String asunto, Pedido pedido, Usuario usuario, String rolString, String contenido, MultipartFile archivo) throws MiExcepcion {
        String remitente = mensajeRepositorio.getRemitente(idRemitente);
        Rol rol = Rol.valueOf(rolString.toUpperCase());
        Mensaje mensaje = new Mensaje();

        mensaje.setAsunto(asunto);
        mensaje.setRemitente(remitente);
        mensaje.setPedido(pedido);
        mensaje.setUsuario(usuario);
        mensaje.setRol(rol);
        mensaje.setVisto(Estado.PENDIENTE);
        mensaje.setFechaMensaje(new Date());
        mensaje.setContenido(contenido);

        Imagen imagen = imagenServicio.guardar(archivo);
        mensaje.setImagen(imagen);
        
        mensajeRepositorio.save(mensaje);
        return mensaje;   
    }

    @Transactional
    public void mensajeVisto(String id) throws MiExcepcion {
        Optional<Mensaje> opcionalMensaje = mensajeRepositorio.findById(id);

        if(opcionalMensaje.isPresent()) {
            Mensaje mensaje = opcionalMensaje.get();
            mensaje.setVisto(Estado.EFECTUADO);
            mensajeRepositorio.save(mensaje);
        }
    }

    @Transactional
    public void deletearMensaje(String id) { mensajeRepositorio.deleteById(id); }

    @Transactional(readOnly = true)
    public Mensaje getOne(String id) { return mensajeRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public List<Mensaje> listarMensajes() { return mensajeRepositorio.findAll(); }

    @Transactional(readOnly = true)
    public List<Mensaje> bandejaDeSalida(String idUsuario) { return mensajeRepositorio.bandejaDeSalida(idUsuario); }

    @Transactional(readOnly = true)
    public String getRemitente(String idRemitente) { return mensajeRepositorio.getRemitente(idRemitente); }

    @Transactional(readOnly = true)
    public List<Mensaje> bandejaDeEntrada(String idUsuario) { return mensajeRepositorio.bandejaDeEntrada(idUsuario); }

    @Transactional(readOnly = true)
    public List<Mensaje> getPorUsuarioNoVisto(String idUsuario) { return mensajeRepositorio.getPorUsuarioNoVisto(idUsuario); }

    @Transactional(readOnly = true)
    public Integer contarPorUsuarioNoVisto(String idUsuario) { return mensajeRepositorio.contarPorUsuarioNoVisto(idUsuario); }

    @Transactional(readOnly = true)
    public List<Mensaje> conversacionPorPedido(String idPedido) { return mensajeRepositorio.conversacionPorPedido(idPedido); }

    @Transactional(readOnly = true)
    public List<Mensaje> msjsRecibidosPorPedido(String idPedido) { return mensajeRepositorio.msjsRecibidosPorPedido(idPedido); }

    @Transactional(readOnly = true)   
    public List<Mensaje> msjsCliente(String idUsuario) { return mensajeRepositorio.msjsCliente(idUsuario); }

    @Transactional(readOnly = true)
    public List<Mensaje> msjsProveedor(String idUsuario) { return mensajeRepositorio.msjsProveedor(idUsuario); }

    @Transactional(readOnly = true)
    public List<Mensaje> porPedidoYFecha(String idPedido, Date fecha) { return mensajeRepositorio.porPedidoYFecha(idPedido, fecha); }

    @Transactional(readOnly = true)
    public List<Mensaje> porPedidoYEntreFechas(String idPedido, Date fechaInicio, Date fechaFin) { return mensajeRepositorio.porPedidoYEntreFechas(idPedido,  fechaInicio, fechaFin); }
}
