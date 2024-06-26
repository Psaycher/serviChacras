package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.FinalEgg.ServiChacras.entidades.*;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.servicios.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    @Autowired
    private PedidoServicio pedidoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ClienteServicio clienteServicio;    
    @Autowired
    private ServicioServicio servicioServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @GetMapping("/dashboard")
    public  String panelAdministrativo(ModelMap modelo){
        String rolSession = "ADMIN";

        modelo.addAttribute("rolSession", rolSession);
        return "inicio-varios.html";  
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(ModelMap modelo){
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        String listado = "USUARIO";
        modelo.addAttribute("usuarios", usuarios);

        modelo.addAttribute("listado", listado);
        return "usuario-gestor.html"; 
    }

    @GetMapping("/servicios")
    public String listarServicios(ModelMap modelo){
        List<Servicio> servicios = servicioServicio.listarServicios();
        modelo.addAttribute("servicios", servicios);
        return "servicio-gestor.html"; 
    }

    @GetMapping("/pedidos")
    public  String listarPedidos(ModelMap modelo){
        List<Pedido> pedidos = pedidoServicio.listarPedidos();
        modelo.addAttribute("pedidos", pedidos);
        return "pedido-gestor.html";
    }

    // @GetMapping("/modificarRol/{id}")
    // public  String cambiarRol(@PathVariable String id) throws MiExcepcion {
    //     usuarioServicio.cambiarRol(id);
    //     return "redirect:/admin/usuarios";
    // }

    @GetMapping("/modificarUsuario/{id}")
    public  String modificarUsuario(@PathVariable String id, ModelMap modelo) throws MiExcepcion {
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            modelo.addAttribute("usuario", usuario);

        } else { 
            modelo.addAttribute("codigo", 500);
            modelo.addAttribute("mensaje", "No se ha encontrado Usuario con el id: " + id);
            return "error.html";
        }

        return "actualizar-usuario.html";
    }

    @GetMapping("/altaBaja/{id}")
    public  String altaBaja(@PathVariable String id, ModelMap modelo) throws MiExcepcion {
        Usuario usuario = usuarioServicio.getOne(id);
        usuarioServicio.altaBaja(id);
        return "actualizar-usuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(@PathVariable String id, ModelMap modelo) {
        System.out.println("Entro a eliminarUsuario con id: " + id);

        try {
            usuarioServicio.eliminarUsuario(id);
            System.out.println("salio de eliminarUsuario y volvio al controlador");
            modelo.addAttribute("exito", "El usuario fue eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("se metio al catch del controlador");
            modelo.addAttribute("codigo", 500);
            modelo.addAttribute("mensaje", "Error al eliminar usuario con id: " + id);
            return "error.html";
        }
        
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        String listado = "USUARIO";

        modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("listado", listado);
        return "usuario-gestor.html";
    }
}
