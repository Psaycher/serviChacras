package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.ui.ModelMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.FinalEgg.ServiChacras.entidades.Cliente;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.entidades.Proveedor;
import com.FinalEgg.ServiChacras.servicios.ClienteServicio;
import com.FinalEgg.ServiChacras.servicios.ProveedorServicio;
import com.FinalEgg.ServiChacras.servicios.NotificacionServicio;
import com.FinalEgg.ServiChacras.repositorios.PedidoRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;

@Controller
@RequestMapping("/pedido")
public class PedidoControlador {
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
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private NotificacionServicio notificacionServicio;

    //MÉTODO PARA SOLICITAR PEDIDO A PROVEEDOR
    @PostMapping("/notificarPedido/{id}")
    public String notificarPedido(@PathVariable String id, @RequestParam String tipoNota, @RequestParam String rolSession, @RequestParam(required = false) String stringDia,
                                  @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String detalleText, 
                                  @RequestParam(required = false) String pedidoId, HttpSession session, ModelMap modelo) throws MiExcepcion {

        Usuario remitente = (Usuario) session.getAttribute("usuariosession");
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);

        String idRemitente = remitente.getId();        
        String idProveedor = "";        
        String idDenuncia = "";
        String notaString = "";
        String idPedido = "";
        String idCliente= "";
        String idPago = "";
        String exito = "";
        Integer valor = 0;

        if (optionalUsuario.isPresent()) {
            Usuario destinatario = optionalUsuario.get();
            String idDestinatario = destinatario.getId();  

            switch (tipoNota) {
                case "PEDIDOSolicitud" -> { 
                    notaString = tipoNota;
                    exito = "Se ha notificado la solicitud del Pedido"; 
                }
                case "PEDIDOAceptado" -> {
                    notaString = tipoNota;
                    idProveedor = proveedorRepositorio.idUsuario(id);
                    idCliente = clienteRepositorio.idUsuario(idRemitente);
                    exito = "Se ha notificado la aceptación del Pedido";
                }
                case "PEDIDORechazado" -> { 
                    notaString = tipoNota; 
                    exito = "Se ha notificado el rechazo del Pedido";
                }
                case "PEDIDOCancelado" -> { 
                    notaString = tipoNota;
                    idPedido = pedidoId;
                    exito = "Se ha notificado la cancelación del Pedido"; 
                }
                case "PEDIDOFinalizado" -> { 
                    notaString = tipoNota; 
                    idPedido = pedidoId;
                    exito = "Se ha notificado la finalización del Pedido";
                }
            }
            notificacionServicio.crearNotificacion(notaString, idRemitente, idDestinatario, idCliente, 
                                                   idProveedor, idPago, idPedido, idDenuncia, valor);

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha encontrado Usuario con el id: " + id);
            return "error.html";        
        }

        modelo.addAttribute("rolSession", rolSession);
        modelo.addAttribute("exito", exito);

        if (rolSession != null && rolSession.equals("CLIENTE")) {
            List<Proveedor> proveedores = proveedorServicio.listarProveedores();
            modelo.addAttribute("proveedores", proveedores);
        }
        if (rolSession != null && rolSession.equals("PROVEEDOR")) {
            List<Cliente> clientes = clienteServicio.getPorPedidoCompartido(proveedorRepositorio.idUsuario(remitente.getId()));
            modelo.addAttribute("clientes", clientes);
        }

        return "expositor.html";
    }
}
