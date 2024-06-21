package com.FinalEgg.ServiChacras.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.FinalEgg.ServiChacras.entidades.*;
import com.FinalEgg.ServiChacras.enumeraciones.*;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;
import com.FinalEgg.ServiChacras.repositorios.NotificacionRepositorio;

public class NotificacionServicio {
    @Autowired
    private PagoServicio pagoServicio;
    @Autowired
    private PedidoServicio pedidoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;    
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private NotificacionRepositorio notificacionRepositorio;
    

    @Transactional
    public void crearNotificacion(String notaString, String idRemitente, String idDestinatario, String idCliente, String idProveedor, String idPago, String idPedido, String idDenuncia, Integer valor) throws MiExcepcion {
        TipoDeNota nota = TipoDeNota.valueOf(notaString.toUpperCase());
        Notificacion notificacion = new Notificacion();
        String asunto = "";

        Optional<Usuario> optionalRemitente = usuarioRepositorio.findById(idRemitente);
        
        if (optionalRemitente.isPresent()) {
            Usuario usuarioRemitente = optionalRemitente.get();
            String remitente = usuarioRemitente.getNombre() + " " + usuarioRemitente.getApellido();
            notificacion.setRemitente(remitente);

            Optional<Usuario> optionalDestinatario = usuarioRepositorio.findById(idDestinatario);

            if (optionalDestinatario.isPresent()) {
                Usuario destinatario = optionalDestinatario.get();
                notificacion.setDestinatario(destinatario);
                notificacion.setVisto(Estado.PENDIENTE);
                notificacion.setDenuncia(idDenuncia);
                notificacion.setPedido(idPedido);
                notificacion.setPago(idPago);
                notificacion.setNota(nota); 
                
                switch (notaString) {
                    case "PEDIDOSolicitud" -> { 
                        asunto = "Solicitud de Pedido";
                        String idServicio = proveedorRepositorio.getIdServicio(idProveedor);
                        Pedido pedido = pedidoServicio.crearPedido(idCliente, idServicio, idProveedor);
                        notificacion.setPedido(pedido.getId());  
                    }
                    case "PEDIDOAceptado"-> { asunto = "Se ha aceptado el Pedido"; }
                    case "PEDIDORechazado" -> { asunto = "Se ha rechazado el Pedido"; }
                    case "PEDIDOCancelado" -> { asunto = "Se ha cancelado el Pedido"; }
                    case "PEDIDOFinalizado" -> { 
                        asunto = "Se ha concluido el Pedido"; 
                        List<Notificacion> notificaciones = notificacionRepositorio.notificacionPorPedido(idPedido);
                        notificaciones.removeIf(noti -> noti.getPedido().equals(idPedido));
                        notificacion.setPedido(idPedido);
                    }
                    case "PAGOPendiente"-> { 
                        asunto = "Pago Pendiente"; 
                        Pago pago = pagoServicio.crearPago(idCliente, idProveedor, valor);
                        notificacion.setPago(pago.getId());  
                    }
                    case "PAGODemandado" -> { asunto = "Pago Exigido"; }
                    case "PAGOEfectuado" -> { 
                        asunto = "Ha recibido el Pago"; 
                        List<Notificacion> notificaciones = notificacionRepositorio.notificacionPorPago(idPago);
                        notificaciones.removeIf(noti -> noti.getPago().equals(idPago));
                        notificacion.setPago(idPago);
                    }
                    case "DENUNCIA"-> { asunto = "Tiene una Denuncia"; }
                    case "DENUNCIARechazada" -> { 
                        asunto = "Denuncia desestimada";
                        List<Notificacion> notificaciones = notificacionRepositorio.notificacionPorDenuncia(idDenuncia);
                        notificaciones.removeIf(noti -> noti.getDenuncia().equals(idDenuncia));
                        notificacion.setDenuncia(idDenuncia);
                    }
                    case "PUNTUACION" -> { asunto = "Fué puntuado"; }
                }
        
                notificacion.setAsunto(asunto);

                notificacionRepositorio.save(notificacion);
                usuarioServicio.notificarUsuario(destinatario, notificacion);

            } else { throw new MiExcepcion("El usuario destinatario seleccionado no existe."); }

        } else { throw new MiExcepcion("El usuario remitente seleccionado no existe."); }
    }

    @Transactional
    public void notificacionVisto(String id) throws MiExcepcion {
        Optional<Notificacion> opcionalNotificacion = notificacionRepositorio.findById(id);

        if(opcionalNotificacion.isPresent()) {
            Notificacion notificacion = opcionalNotificacion.get();
            notificacion.setVisto(Estado.EFECTUADO);
            notificacionRepositorio.save(notificacion);
        }
    }
}
