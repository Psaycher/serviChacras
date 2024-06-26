package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import com.FinalEgg.ServiChacras.entidades.*;
import com.FinalEgg.ServiChacras.servicios.*;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;

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
    public  String modificarUsuario(@PathVariable String id, ModelMap modelo, HttpSession session) throws MiExcepcion {
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);
        Usuario admin = (Usuario) session.getAttribute("usuariosession");
        String idAdmin = admin.getId();

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            List<Servicio> limpieza = servicioServicio.listarPorCategoria("Servicios de limpieza");
            List<Servicio> mantenimiento = servicioServicio.listarPorCategoria("Servicios de mantenimiento y reparaciones");
            List<Servicio> seguridad = servicioServicio.listarPorCategoria("Servicios de seguridad");
            List<Servicio> tecnologia = servicioServicio.listarPorCategoria("Servicios de tecnologia y conectividad");
            List<Servicio> cuidado = servicioServicio.listarPorCategoria("Servicios de cuidado personal y bienestar");
            List<Servicio> logistica = servicioServicio.listarPorCategoria("Servicios de entrega y logistica");

            modelo.addAttribute("barrios", usuarioServicio.obtenerListaDeBarrios());

            modelo.addAttribute("limpieza", limpieza);
            modelo.addAttribute("mantenimiento", mantenimiento);
            modelo.addAttribute("seguridad", seguridad);
            modelo.addAttribute("tecnologia", tecnologia);
            modelo.addAttribute("cuidado", cuidado);
            modelo.addAttribute("logistica", logistica);
            modelo.addAttribute("idAdmin", idAdmin);
            modelo.put("usuario", usuario);
           
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
            modelo.addAttribute("exito", "El usuario fue eliminado correctamente.");

        } catch (Exception e) {
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
