package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.FinalEgg.ServiChacras.entidades.Pedido;
import com.FinalEgg.ServiChacras.entidades.Mensaje;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.entidades.Notificacion;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.servicios.PedidoServicio;
import com.FinalEgg.ServiChacras.servicios.NotificacionServicio;
import com.FinalEgg.ServiChacras.repositorios.MensajeRepositorio;
import com.FinalEgg.ServiChacras.repositorios.NotificacionRepositorio;

@Controller
@RequestMapping("/notificacion")
public class NotificacionControlador {
    @Autowired
    private PedidoServicio pedidoServicio;
    @Autowired
    private MensajeRepositorio mensajeRepositorio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    @Autowired
    private NotificacionRepositorio notificacionRepositorio;

    //MÉTODO PARA VER DETALLE DE UNA NOTIFICACION
    @GetMapping("/verNotificacion/{id}")
    public String verNotificacion(@PathVariable String id, HttpSession session, ModelMap modelo) throws MiExcepcion {
        Optional<Notificacion> opcionalNotificacion = notificacionRepositorio.findById(id);
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        String hayDenuncia = "";
        String hayPedido = "";
        String hayPago = "";
        String nota = "";

        if (opcionalNotificacion.isPresent()) {
            Notificacion notificacion = opcionalNotificacion.get();
            notificacionServicio.notificacionVisto(notificacion);
            nota = String.valueOf(notificacion.getNota()); 
            
            if (String.valueOf(notificacion.getNota()).equals("PEDIDOAceptado")) { hayPedido = "HAY"; }
            if (String.valueOf(notificacion.getNota()).equals("PEDIDOCancelado")) { hayPedido = "HAY"; }
            if (String.valueOf(notificacion.getNota()).equals("PEDIDOFinalizado")) { hayPedido = "HAY"; } 
            if (String.valueOf(notificacion.getNota()).equals("CONFIRMARFinalizacion")) { hayPedido = "HAY"; }

            if (String.valueOf(notificacion.getNota()).equals("PAGODemandado")) { hayPago = "HAY"; }
            if (String.valueOf(notificacion.getNota()).equals("PAGOEfectuado")) { hayPago = "HAY"; }

            if (String.valueOf(notificacion.getNota()).equals("DENUNCIA")) { hayDenuncia = "HAY"; }
            if (String.valueOf(notificacion.getNota()).equals("DENUNCIARechazada")) { hayDenuncia = "HAY"; }

            if (hayPedido.equals("HAY")) {
                Pedido pedido = pedidoServicio.getOne(notificacion.getPedido());
                Integer valor = pedido.getPago().getValor();
                modelo.addAttribute("valor", valor);
            }

            modelo.addAttribute("notificacion", notificacion);
            modelo.addAttribute("hayDenuncia", hayDenuncia);
            modelo.addAttribute("hayPedido", hayPedido);
            modelo.addAttribute("hayPago", hayPago);
            modelo.addAttribute("logueado", logueado);
            modelo.addAttribute("nota", nota);

        } else {
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "Error en la carga de la notificacion.");
            return "error.html";
        }
        return "carta-notificacion.html";
    }

    //MÉTODO PARA IR A LA BANDEJA DE NOTIFICACIONES
    @GetMapping("/bandejaNotificaciones/{rolSession}")
    public String bandejaNotificaciones(@PathVariable String rolSession, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Optional<List<Notificacion>> opcionalListNotificacion = Optional.ofNullable(notificacionRepositorio.getPorUsuario(logueado.getId()));
  
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

        if (opcionalListNotificacion.isPresent()) {
            List<Notificacion> listNotificaciones = opcionalListNotificacion.get();
            modelo.addAttribute("listNotificaciones", listNotificaciones);
            modelo.addAttribute("rolSession", rolSession);
            modelo.addAttribute("logueado", logueado);
            modelo.addAttribute("navBlock", navBlock);            

        } else {
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "Error al encontrar las notificaciones del usuario con id: "+ logueado.getId());
            return "error.html";
        }
        return "bandeja-notificaciones.html";
    }    
}
