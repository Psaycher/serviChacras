package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

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
                    else if (nombreUsuario == null || nombreUsuario.isEmpty()) { proveedores = proveedorServicio.listarProveedores();}

                } else {
                    if (nombreUsuario != null) { proveedores = proveedorServicio.getPorServicioYNombre(nombreUsuario, idServicio); }
                    else if (nombreUsuario == null || nombreUsuario.isEmpty()) { proveedores = proveedorServicio.getPorServicio(idServicio); }
                }

            } else {

                if (idServicio == null) {
                    if (nombreUsuario != null) { proveedores = proveedorServicio.getPorBarrioYNombre(nombreUsuario, barrio); }
                    else if (nombreUsuario == null || nombreUsuario.isEmpty()) { proveedores = proveedorServicio.getPorBarrio(barrio); }

                } else {
                    if (nombreUsuario != null) { proveedores = proveedorServicio.getPorServicioBarrioYNombre(idServicio, nombreUsuario, barrio); }
                    else if (nombreUsuario == null || nombreUsuario.isEmpty()) { proveedores = proveedorServicio.getPorServicioYBarrio(idServicio, barrio); }
                }
            }

            proveedores = proveedores.stream().filter(Objects::nonNull).collect(Collectors.toList());

            modelo.addAttribute("proveedores", proveedores);
        }

        if (rolSession != null && rolSession.equals("PROVEEDOR")) {
            List<Cliente> clientes = new ArrayList<>();
            System.out.println("entro en condicional del session:" + rolSession);
            String idProveedor = proveedorServicio.idUsuario(logueado.getId());
            boolean pedidoHay = false;
            boolean vacio = false;            

            if (barrio == null) {
                if (idPedido == null) {
                    if (nombreUsuario != null) { clientes = clienteServicio.getPorNombreCompartido(nombreUsuario, idProveedor); }
                    else { clientes = clienteServicio.getPorPedidoCompartido(idProveedor); }
                    
                } else { 
                    pedidoHay = true; 

                    if (nombreUsuario != null) { clientes = clienteServicio.getPorPedidoYNombre(nombreUsuario, idPedido); }
                    else { clientes.add(clienteServicio.getPorPedido(idPedido)); }
                }

            } else {

                if (idPedido == null) {
                    if (nombreUsuario != null) { clientes = clienteServicio.getPorBarrioYNombreCompartido(nombreUsuario, barrio, idProveedor); }
                    else { clientes = clienteServicio.getPorBarrioCompartido(barrio, idProveedor); }

                } else { 
                    pedidoHay = true;

                    if (nombreUsuario != null) { clientes = clienteServicio.getPorPedidoBarrioYNombre(nombreUsuario, idPedido, barrio); }
                    else { clientes = clienteServicio.getPorPedidoYBarrio(idPedido, barrio); }
                }
            }

            if (clientes.isEmpty()) { vacio = true; }
            else {
                clientes = clientes.stream().filter(Objects::nonNull).collect(Collectors.toList());
                modelo.addAttribute("clientes", clientes);
            }

            System.out.println("clientes está vacio: "+ vacio);
            modelo.addAttribute("pedidoHay", pedidoHay);            
            modelo.addAttribute("vacio", vacio);
        }

        modelo.addAttribute("rolSession", rolSession);

        return "expositor.html";
    }
}