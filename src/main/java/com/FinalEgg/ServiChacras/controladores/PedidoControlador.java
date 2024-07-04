package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.ui.ModelMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.FinalEgg.ServiChacras.entidades.Pedido;
import com.FinalEgg.ServiChacras.entidades.Mensaje;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.entidades.Notificacion;
import com.FinalEgg.ServiChacras.servicios.PagoServicio;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.servicios.ClienteServicio;
import com.FinalEgg.ServiChacras.servicios.ProveedorServicio;
import com.FinalEgg.ServiChacras.servicios.NotificacionServicio;
import com.FinalEgg.ServiChacras.repositorios.PedidoRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;
import com.FinalEgg.ServiChacras.repositorios.MensajeRepositorio;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;
import com.FinalEgg.ServiChacras.repositorios.NotificacionRepositorio;

@Controller
@RequestMapping("/pedido")
public class PedidoControlador {
    @Autowired
    private PagoServicio pagoServicio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private MensajeRepositorio mensajeRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    @Autowired
    private NotificacionRepositorio notificacionRepositorio;

    //MÉTODO PARA SOLICITAR PEDIDO AL PROVEEDOR
    @PostMapping("/solicitudPedido/{id}")
    public String solicitudPedido(@PathVariable String id, @RequestParam String tipoNota, @RequestParam String rolSession, @RequestParam(required = false) String stringDia,
                                  @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime, @RequestParam String asunto, 
                                  @RequestParam(required = false) String detalleText, HttpSession session, ModelMap modelo) throws MiExcepcion {
        
        Usuario remitente = (Usuario) session.getAttribute("usuariosession");
        String idRemitente = remitente.getId();
        String idDestinatario = "";
        String idProveedor = "";               
        String idDenuncia = "";
        String idPedido = "";
        String idCliente= "";
        String idPago = "";
        Integer valor = 0;

        if (stringDia == null || stringDia.isEmpty()) { stringDia = "no especificado."; }        

        if (startTime == null || startTime.isEmpty()) { startTime = "no especificado."; } 
        else { if (endTime != null && !endTime.isEmpty()) { startTime = startTime + " hasta las " + endTime; } }

        if (asunto == null || asunto.isEmpty()) { asunto = "no especificado."; }

        if (detalleText == null || detalleText.isEmpty()) { detalleText = "no especificado."; }
        

        String detalle = "Disponibilidad los días: " + stringDia + "\n" +
                         "horarios disponibles de: " + startTime + ".\n" +
                         "Se requiere: " + detalleText;

        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);
        
        if (optionalUsuario.isPresent()) {
            Usuario destinatario = optionalUsuario.get();
            idDestinatario = destinatario.getId();

            idProveedor = proveedorServicio.idUsuario(idDestinatario);

            notificacionServicio.crearNotificacion(tipoNota, idRemitente, idDestinatario, idCliente, 
            idProveedor, idPago, idPedido, idDenuncia, asunto, detalle, valor);

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha encontrado Usuario con el id: " + id);
            return "error.html";        
        }

        modelo.addAttribute("rolSession", rolSession);
        modelo.addAttribute("exito", "Se ha notificado la solicitud de un Pedido");
        return "expositor.html";
    }

    //MÉTODO PARA ACEPTAR PEDIDO AL CLIENTE
    @PostMapping("/aceptarPedido")
    public String aceptarPedido(@RequestParam("id") String id, @RequestParam("valor") Integer valor, HttpSession session, ModelMap modelo) throws MiExcepcion {
        Usuario remitente = (Usuario) session.getAttribute("usuariosession");
        String idRemitente = remitente.getId();
        String rolSession = "PROVEEDOR";
        String idDestinatario = "";
        String idCliente = "";
        String detalle = "";
        String asunto= "";

        String notaString = "PEDIDOAceptado";

        Optional<Notificacion> optionalNotificacion = notificacionRepositorio.findById(id);

        if (optionalNotificacion.isPresent()) {
            Notificacion notificacion = optionalNotificacion.get();
            idDestinatario = notificacion.getRemitente().getId();
            idCliente = clienteServicio.idUsuario(idDestinatario);
            detalle = notificacion.getDetalle();
            asunto = notificacion.getAsunto();

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha la notificacion del Usuario con el id: " + idDestinatario);
            return "error.html";        
        }

        String idProveedor = proveedorServicio.idUsuario(idRemitente);               
        String idDenuncia = "";
        String idPedido = "";   
        String idPago = "";

        notificacionServicio.crearNotificacion(notaString, idRemitente, idDestinatario, idCliente, 
                                                idProveedor, idPago, idPedido, idDenuncia, asunto, detalle, valor);

        modelo.addAttribute("rolSession", rolSession);
        modelo.addAttribute("exito", "Se ha notificado que ha aceptado del Pedido");
        return "bandeja-pedidos.html";
    }

    //MÉTODO PARA RECHAZAR PEDIDO AL CLIENTE
    @GetMapping("/rechazarPedido/{id}")
    public String rechazarPedido(@PathVariable String id, HttpSession session, ModelMap modelo) throws MiExcepcion {
        Usuario remitente = (Usuario) session.getAttribute("usuariosession");
        String idRemitente = remitente.getId();
        String rolSession = "PROVEEDOR";
        String idDestinatario = "";
        String detalle = "";
        String asunto= "";

        String notaString = "PEDIDORechazado";

        Optional<Notificacion> optionalNotificacion = notificacionRepositorio.findById(id);

        if (optionalNotificacion.isPresent()) {
            Notificacion notificacion = optionalNotificacion.get();
            idDestinatario = notificacion.getRemitente().getId();
            detalle = notificacion.getDetalle();
            asunto = notificacion.getAsunto();

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha la notificacion del Usuario con el id: " + idDestinatario);
            return "error.html";        
        }

        String idProveedor = "";               
        String idDenuncia = "";
        String idPedido = "";
        String idCliente= "";
        String idPago = "";
        Integer valor = 0;

        notificacionServicio.crearNotificacion(notaString, idRemitente, idDestinatario, idCliente, 
                                                idProveedor, idPago, idPedido, idDenuncia, asunto, detalle, valor);

        modelo.addAttribute("rolSession", rolSession);
        modelo.addAttribute("exito", "Se ha rechazado la solicitud del Pedido");
        return "expositor.html";
    }

    //MÉTODO PARA CANCELAR PEDIDO AL PROVEEDOR
    @GetMapping("/cancelarPedido/{id}")
    public String cancelarPedido(@PathVariable String id, HttpSession session, ModelMap modelo) throws MiExcepcion {
        Usuario remitente = (Usuario) session.getAttribute("usuariosession");
        String idRemitente = remitente.getId();
        String rolSession = "CLIENTE";
        String idDestinatario = "";
        String idPedido = "";
        String detalle = "";
        String asunto= "";

        String notaString = "PEDIDOCancelado";

        Optional<Notificacion> optionalNotificacion = notificacionRepositorio.findById(id);

        if (optionalNotificacion.isPresent()) {
            Notificacion notificacion = optionalNotificacion.get();
            idDestinatario = notificacion.getRemitente().getId();
            idPedido = notificacion.getPedido();
            detalle = notificacion.getDetalle();
            asunto = notificacion.getAsunto();

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha la notificacion del Usuario con el id: " + idDestinatario);
            return "error.html";        
        }

        String idProveedor = "";               
        String idDenuncia = "";
        String idCliente= "";
        String idPago = "";
        Integer valor = 0;

        notificacionServicio.crearNotificacion(notaString, idRemitente, idDestinatario, idCliente, 
                                                idProveedor, idPago, idPedido, idDenuncia, asunto, detalle, valor);

        modelo.addAttribute("rolSession", rolSession);
        modelo.addAttribute("exito", "Se ha cancelado la solicitud del Pedido");
        return "expositor.html";
    }

    //MÉTODO PARA INFORMAR LA FINALIZACION PEDIDO AL CLIENTE
    @GetMapping("/finalizarPedido/{id}")
    public String finalizarPedido(@PathVariable String id, HttpSession session, ModelMap modelo) throws MiExcepcion {
        Usuario remitente = (Usuario) session.getAttribute("usuariosession");
        String idRemitente = remitente.getId();
        String rolSession = "PROVEEDOR";
        String idDestinatario = "";
        String idPedido = "";
        String detalle = "";
        String asunto= "";

        String notaString = "PEDIDOFinalizado";

        Optional<Notificacion> optionalNotificacion = notificacionRepositorio.findById(id);

        if (optionalNotificacion.isPresent()) {
            Notificacion notificacion = optionalNotificacion.get();
            idDestinatario = notificacion.getRemitente().getId();
            idPedido = notificacion.getPedido();
            detalle = notificacion.getDetalle();
            asunto = notificacion.getAsunto();

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha la notificacion del Usuario con el id: " + idDestinatario);
            return "error.html";        
        }

        String idProveedor = "";               
        String idDenuncia = "";
        String idCliente= "";
        String idPago = "";
        Integer valor = 0;

        notificacionServicio.crearNotificacion(notaString, idRemitente, idDestinatario, idCliente, 
                                                idProveedor, idPago, idPedido, idDenuncia, asunto, detalle, valor);

        modelo.addAttribute("rolSession", rolSession);
        modelo.addAttribute("exito", "Se ha notificado que considera el pedido concluido");
        return "expositor.html";
    }

    //MÉTODO PARA CONFIRMAR LA CONFORMIDAD DE LA FINALIZACION AL PROVEEDOR
    @GetMapping("/confirmarFinPedido/{id}")
    public String confirmarFinPedido(@PathVariable String id, HttpSession session, ModelMap modelo) throws MiExcepcion {
        Usuario remitente = (Usuario) session.getAttribute("usuariosession");
        String idRemitente = remitente.getId();
        String rolSession = "CLIENTE";
        String idDestinatario = "";
        String idPedido = "";
        String detalle = "";
        String asunto= "";

        String notaString = "CONFIRMARFinalizacion";

        Optional<Notificacion> optionalNotificacion = notificacionRepositorio.findById(id);

        if (optionalNotificacion.isPresent()) {
            Notificacion notificacion = optionalNotificacion.get();
            idDestinatario = notificacion.getRemitente().getId();
            idPedido = notificacion.getPedido();
            detalle = notificacion.getDetalle();
            asunto = notificacion.getAsunto();

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha la notificacion del Usuario con el id: " + idDestinatario);
            return "error.html";        
        }

        String idProveedor = "";               
        String idDenuncia = "";
        String idCliente= "";
        String idPago = "";
        Integer valor = 0;

        notificacionServicio.crearNotificacion(notaString, idRemitente, idDestinatario, idCliente, 
                                                idProveedor, idPago, idPedido, idDenuncia, asunto, detalle, valor);

        modelo.addAttribute("rolSession", rolSession);
        modelo.addAttribute("exito", "Se ha informado que confirma finalizado el pedido");
        return "expositor.html";
    }

    //MÉTODO PARA IR A LA BANDEJA DE NOTIFICACIONES
    @GetMapping("/bandejaPedidos/{rolSession}")
    public String bandejaPedidos(@PathVariable String rolSession, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");     
  
        //Conteo e indicaciones de Notificaciones en Navegador----------------------->
        Integer mensajeNoVisto = mensajeRepositorio.contarPorUsuarioNoVisto(logueado.getId());
        List<Mensaje> mensajes = mensajeRepositorio.getPorUsuarioNoVisto(logueado.getId());

        //Conteo e indicaciones de Notificaciones en Navegador----------------------->
        Integer notificacionNoVisto = notificacionRepositorio.contarPorUsuarioNoVisto(logueado.getId());
        List<Notificacion> notificaciones = notificacionRepositorio.getPorUsuarioNoVisto(logueado.getId());
        List<String> notas = new ArrayList<>();
        String navBlock = "buscadores";
        String nota = "";

        for (Notificacion notificacion : notificaciones) {
            nota = notificacion.getAsunto() + ": " + notificacion.getRemitente();
            notas.add(nota);
        }

        //Modelos para Notificaciones y Mensajes en Navegador----------------------->
        modelo.put("notificacionNoVisto", notificacionNoVisto);
        modelo.put("notificaciones", notificaciones);

        modelo.put("notas", notas);
        modelo.put("mensajeNoVisto", mensajeNoVisto);
        modelo.put("mensajes", mensajes);
        //--------------------------------------------------------------------------/
        System.out.println("bandeja de pedidos");

        if (rolSession != null && rolSession.equals("CLIENTE")) {
            Optional<List<Pedido>> opcionalListPedido = Optional.ofNullable(pedidoRepositorio.getPedidosPorClientes(clienteServicio.idUsuario(logueado.getId())));
            System.out.println("entro en rolSesion "+ rolSession);

            if (opcionalListPedido.isPresent()) {
                List<Pedido> pedidos = opcionalListPedido.get();
                modelo.addAttribute("pedidos", pedidos);
                modelo.addAttribute("logueado", logueado);
                System.out.println("se encontro la lista de pedidos");
    
            } else {
                modelo.addAttribute("codigo", 404);
                modelo.addAttribute("mensaje", "Error al encontrar los pedidos del usuario con id: "+ logueado.getId());
                return "error.html";
            }
        }
        if (rolSession != null && rolSession.equals("PROVEEDOR")) {
            Optional<List<Pedido>> opcionalListPedido = Optional.ofNullable(pedidoRepositorio.getPedidosPorProveedores(proveedorServicio.idUsuario(logueado.getId())));
            String avancePedido = "";
            System.out.println("entro en rolSesion "+ rolSession);

            if (opcionalListPedido.isPresent()) {
                List<Pedido> pedidos = opcionalListPedido.get();
                System.out.println("se encontro la lista de pedidos");
                
                for (Pedido pedido : pedidos) {
                    List<Notificacion> listNotificaciones = notificacionServicio.listarPorPedido(pedido.getId());

                    for (Notificacion notificacion : listNotificaciones) {                        
                        if (String.valueOf(notificacion.getNota()).equals("PEDIDOFinalizado")) { avancePedido = "FINALIZADO"; } 
                        else if (String.valueOf(notificacion.getNota()).equals("PEDIDOAceptado")) { avancePedido = "ACEPTADO"; }
                    }
                }
                modelo.addAttribute("avancePedido", avancePedido);
                modelo.addAttribute("logueado", logueado);
                modelo.addAttribute("pedidos", pedidos);
                
            } else {
                modelo.addAttribute("codigo", 404);
                modelo.addAttribute("mensaje", "Error al encontrar los pedidos del usuario con id: "+ logueado.getId());
                return "error.html";
            }
        }    
        modelo.addAttribute("navBlock", navBlock);
        System.out.println("antes de salir de bandejaPedidos");   
        return "bandeja-pedidos.html";
    }
}
