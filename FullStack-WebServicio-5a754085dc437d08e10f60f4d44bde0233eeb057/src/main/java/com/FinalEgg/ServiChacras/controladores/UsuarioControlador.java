package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.ArrayList;

import org.springframework.ui.ModelMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.FinalEgg.ServiChacras.entidades.Cliente;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.entidades.Proveedor;
import com.FinalEgg.ServiChacras.servicios.ClienteServicio;
import com.FinalEgg.ServiChacras.servicios.ProveedorServicio;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;

    @GetMapping("/expositor")
    public String expositor(ModelMap modelo, HttpSession session, @RequestParam(value = "rolSession", required = false) String rolSession, @RequestParam(required = false) String barrio,
                            @RequestParam(required = false) String idServicio, @RequestParam(required = false) String idPedido, @RequestParam(required = false) String nombreUsuario) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        System.out.println("entro el controlador y este es el session:" + rolSession);

        if (rolSession != null && rolSession.equals("CLIENTE")) {
            List<Proveedor> proveedores = new ArrayList<>();
            System.out.println("entro en condicional del session:" + rolSession);

            if (barrio == null) {
                if (idServicio == null) {
                    if (nombreUsuario != null) { proveedores = proveedorServicio.getPorNombreCompleto(nombreUsuario); }
                    else { proveedores = proveedorServicio.listarProveedores();}

                } else {
                    if (nombreUsuario != null) { proveedores = proveedorServicio.getPorServicioYNombre(nombreUsuario, idServicio); }
                    else { proveedores = proveedorServicio.getPorServicio(idServicio); }
                }

            } else {

                if (idServicio == null) {
                    if (nombreUsuario != null) { proveedores = proveedorServicio.getPorBarrioYNombre(nombreUsuario, barrio); }
                    else { proveedores = proveedorServicio.getPorBarrio(barrio); }

                } else {
                    if (nombreUsuario != null) { proveedores = proveedorServicio.getPorServicioBarrioYNombre(idServicio, nombreUsuario, barrio); }
                    else { proveedores = proveedorServicio.getPorServicioYBarrio(idServicio, barrio); }
                }
            }

            modelo.addAttribute("proveedores", proveedores);
        }

        if (rolSession != null && rolSession.equals("Proveedor")) {
            List<Cliente> clientes = new ArrayList<>();
            System.out.println("entro en condicional del session:" + rolSession);
            boolean vacio = false;

            if (barrio == null) {
                if (idPedido == null) {
                    if (nombreUsuario != null) { clientes = clienteServicio.getPorNombreCompleto(nombreUsuario); }

                } else { clientes.add(clienteServicio.getPorPedido(idPedido)); }

            } else {

                if (idPedido == null) {
                    if (nombreUsuario != null) { clientes = clienteServicio.getPorBarrioYNombre(nombreUsuario, barrio); }
                    else { clientes = clienteServicio.getPorBarrio(barrio); }

                } else { clientes.add(clienteServicio.getPorPedido(idPedido)); }
            }

            if (clientes.isEmpty()) { vacio = true; }

            modelo.addAttribute("clientes", clientes);
            modelo.addAttribute("vacio", vacio);
        }

        modelo.addAttribute("rolSession", rolSession);

        return "expositor.html";
    }
}