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
import com.FinalEgg.ServiChacras.entidades.Proveedor;
import com.FinalEgg.ServiChacras.servicios.ClienteServicio;
import com.FinalEgg.ServiChacras.servicios.ProveedorServicio;
import com.FinalEgg.ServiChacras.repositorios.PedidoRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
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

    //MÉTODO QUE LISTA PROVEEDORES O CLIENTES SEGÚN USUARIO Y DERIVA A expositor.html
    @GetMapping("/expositor")
    public String expositor(ModelMap modelo, HttpSession session, @RequestParam(value = "rolSession", required = false) String rolSession, @RequestParam(required = false) String barrio,
                            @RequestParam(required = false) String idServicio, @RequestParam(required = false) String idPedido, @RequestParam(required = false) String nombreUsuario) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (rolSession != null && rolSession.equals("CLIENTE")) {
            List<Proveedor> proveedores = new ArrayList<>();

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

    //MÉTODO PARA IMAGEN DE PROVEEDOR
    @GetMapping("/imagen/{id}")
    public ResponseEntity<byte[]> ImagenProveedor(@PathVariable String id) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        byte[] imagen = proveedor.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    //MÉTODO PARA VISUALIZAR LA INFO DE UN USUARIO
    @GetMapping("/unicard/{id}")
    public String unicard(@PathVariable String id, @RequestParam String rolSession, ModelMap modelo) {
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);
        System.out.println("entro a unicard con rolSession: " + rolSession);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            System.out.println("encontro al usuario "+ usuario.getNombre() + " " + usuario.getApellido());

            if (rolSession.equals("CLIENTE")) {
                Optional<Proveedor> opcionalProveedor = proveedorRepositorio.findById(proveedorRepositorio.idUsuario(id));
                System.out.println("Entro al condicional de rolSession = CLIENTE");

                if (opcionalProveedor.isPresent()) {
                    Proveedor proveedor = opcionalProveedor.get();
                    System.out.println("encontro al proveedor: " + usuario.getNombre() + " " + usuario.getApellido() + ", " + proveedor.getServicio().getNombre());
                    Integer cantidadPedidos = pedidoRepositorio.contarPedidosPorProveedor(proveedor.getId());
                    System.out.println("La cantidad de pedidos es: " + cantidadPedidos);

                    modelo.addAttribute("cantidadPedidos", cantidadPedidos);
                    modelo.addAttribute("proveedor", proveedor);
                    modelo.addAttribute("usuario", usuario);
                    modelo.addAttribute("rolSession", rolSession);
                    return "carta-usuario.html";

                } else {
                    modelo.addAttribute("codigo", 404);
                    modelo.addAttribute("mensaje", "No se ha encontrado al proveedor con id de usuario: " + id);
                    return "error.html";
                }
            }

            if (rolSession.equals("PROVEEDOR")) {
                Optional<Cliente> opcionalCliente = clienteRepositorio.findById(clienteRepositorio.idUsuario(id));

                if (opcionalCliente.isPresent()) {
                    Cliente cliente = opcionalCliente.get();
                    Integer cantidadPedidos = pedidoRepositorio.contarPedidosPorCliente(cliente.getId());
                    
                    modelo.addAttribute("cantidadPedidos", cantidadPedidos); 
                    modelo.addAttribute("cliente", cliente); 

                } else {
                    modelo.addAttribute("codigo", 404);
                    modelo.addAttribute("mensaje", "No se ha encontrado al cliente con id de usuario: " + id);
                    return "error.html";
                }
            }

            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("rolSession", rolSession);
            return "carta-usuario.html";

        } else { 
            modelo.addAttribute("codigo", 404);
            modelo.addAttribute("mensaje", "No se ha encontrado Usuario con el id: " + id);
            return "error.html";
        }
    }
}