package com.FinalEgg.ServiChacras.controladores;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.FinalEgg.ServiChacras.entidades.Pedido;
import com.FinalEgg.ServiChacras.entidades.Mensaje;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.entidades.Servicio;
import com.FinalEgg.ServiChacras.entidades.Notificacion;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.servicios.PedidoServicio;
import com.FinalEgg.ServiChacras.servicios.UsuarioServicio;
import com.FinalEgg.ServiChacras.servicios.ServicioServicio;
import com.FinalEgg.ServiChacras.servicios.ProveedorServicio;
import com.FinalEgg.ServiChacras.repositorios.UsuarioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.MensajeRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ServicioRepositorio;
import com.FinalEgg.ServiChacras.repositorios.NotificacionRepositorio;

@Controller
@RequestMapping("/")
public class PortalControlador {
   @Autowired
   private PedidoServicio pedidoServicio;
   @Autowired
   private UsuarioServicio usuarioServicio;
   @Autowired
   private ServicioServicio servicioServicio;
   @Autowired
   private ProveedorServicio proveedorServicio;
   @Autowired
   private MensajeRepositorio mensajeRepositorio;
   @Autowired
   private UsuarioRepositorio usuarioRepositorio;
   @Autowired
   private ServicioRepositorio servicioRepositorio;
   @Autowired
   private NotificacionRepositorio notificacionRepositorio;


   @GetMapping("/")
   public String index(HttpSession session) {
      Usuario logueado = (Usuario) session.getAttribute("usuariosession");
      if (logueado != null) { return "redirect:/inicio"; }
      
      return "index.html";
   }


   @GetMapping("/registrar")
   public String registrar(ModelMap modelo) {
      List<Servicio> limpieza = servicioServicio.listarPorCategoria("Servicios de limpieza");
      List<Servicio> mantenimiento = servicioServicio.listarPorCategoria("Servicios de mantenimiento y reparaciones");
      List<Servicio> seguridad = servicioServicio.listarPorCategoria("Servicios de seguridad");
      List<Servicio> tecnologia = servicioServicio.listarPorCategoria("Servicios de tecnologia y conectividad");
      List<Servicio> cuidado = servicioServicio.listarPorCategoria("Servicios de cuidado personal y bienestar");
      List<Servicio> logistica = servicioServicio.listarPorCategoria("Servicios de entrega y logistica");

      modelo.addAttribute("limpieza", limpieza);
      modelo.addAttribute("mantenimiento", mantenimiento);
      modelo.addAttribute("seguridad", seguridad);
      modelo.addAttribute("tecnologia", tecnologia);
      modelo.addAttribute("cuidado", cuidado);
      modelo.addAttribute("logistica", logistica);

      return "registro.html";
   }


   @GetMapping("/login")
   public String login(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
      Usuario logueado = (Usuario) session.getAttribute("usuariosession");

      if (logueado != null) { return "redirect:/inicio"; }

      if (error != null) {modelo.put("error", "Usuario o Contraseña inválidos!");}

      return "login.html";
   }


   @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_MIXTO')")
   @GetMapping("/inicio")
   public String inicio( ModelMap modelo, HttpSession session, @RequestParam(required = false) String idUsuario ) {
      Usuario logueado = (Usuario) session.getAttribute("usuariosession");

      //Conteo e indicaciones de Notificaciones en Navegador----------------------->
      Integer mensajeNoVisto = mensajeRepositorio.contarPorUsuarioNoVisto(idUsuario);
      List<Mensaje> mensajes = mensajeRepositorio.getPorUsuarioNoVisto(idUsuario);

      //Conteo e indicaciones de Notificaciones en Navegador----------------------->
      Integer notificacionNoVisto = notificacionRepositorio.contarPorUsuarioNoVisto(idUsuario);
      List<Notificacion> notificaciones = notificacionRepositorio.getPorUsuarioNoVisto(idUsuario);
      List<String> notas = new ArrayList<>();
      String nota = "";

      for (Notificacion notificacion : notificaciones) {
         nota = notificacion.getRemitente() + " - " + notificacion.getAsunto();
         notas.add(nota);
      }

      if ( logueado.getRol().toString().equalsIgnoreCase("ADMIN") ) { 
         String rolSession = "ADMIN";
         modelo.addAttribute("rolSession", rolSession);

         return "inicio-varios.html"; 
      }

      if ( logueado.getRol().toString().equalsIgnoreCase("CLIENTE") ) {

         //Opciones del Filtro avanzado en Navegador---------------------------------->
         String rolSession = "CLIENTE";

         List<Servicio> limpieza = servicioServicio.listarPorCategoria("Servicios de limpieza");
         List<Servicio> mantenimiento = servicioServicio.listarPorCategoria("Servicios de mantenimiento y reparaciones");
         List<Servicio> seguridad = servicioServicio.listarPorCategoria("Servicios de seguridad");
         List<Servicio> tecnologia = servicioServicio.listarPorCategoria("Servicios de tecnologia y conectividad");
         List<Servicio> cuidado = servicioServicio.listarPorCategoria("Servicios de cuidado personal y bienestar");
         List<Servicio> logistica = servicioServicio.listarPorCategoria("Servicios de entrega y logistica");
   
         modelo.addAttribute("rolSession", rolSession);
         modelo.addAttribute("limpieza", limpieza);
         modelo.addAttribute("mantenimiento", mantenimiento);
         modelo.addAttribute("seguridad", seguridad);
         modelo.addAttribute("tecnologia", tecnologia);
         modelo.addAttribute("cuidado", cuidado);
         modelo.addAttribute("logistica", logistica);
         //--------------------------------------------------------------------------//
                  
         //Modelos para Notificaciones y Mensajes en Navegador----------------------->
         modelo.put("notificacionNoVisto", notificacionNoVisto);
         modelo.put("notificaciones", notificaciones);

         modelo.put("notas", notas);
         modelo.put("mensajeNoVisto", mensajeNoVisto);
         modelo.put("mensajes", mensajes);
         //--------------------------------------------------------------------------//

         return "inicio-cliente.html";
      }

      if ( logueado.getRol().toString().equalsIgnoreCase("PROVEEDOR") ) { 
         //Opciones del Filtro avanzado en Navegador---------------------------------->
         String rolSession = "PROVEEDOR";
         System.out.println(rolSession);

         String idProveedor = proveedorServicio.idUsuario(logueado.getId());
         List<Pedido> pedidos = pedidoServicio.getPedidoPorProveedores(idProveedor);

         modelo.addAttribute("rolSession", rolSession);
         modelo.addAttribute("pedidos", pedidos);
         //--------------------------------------------------------------------------//
                  
         //Modelos para Notificaciones y Mensajes en Navegador----------------------->
         modelo.put("notificacionNoVisto", notificacionNoVisto);
         modelo.put("notificaciones", notificaciones);
         
         modelo.put("notas", notas);
         modelo.put("mensajeNoVisto", mensajeNoVisto);
         modelo.put("mensajes", mensajes);
         //--------------------------------------------------------------------------//
         //--------------------------------------------------------------------------//

         return "inicio-proveedor.html"; 
      }

      if ( logueado.getRol().toString().equalsIgnoreCase("MIXTO") ) { 
         String rolSession = "MIXTO";
         modelo.addAttribute("rolSession", rolSession);

         return "inicio-varios.html"; 
      }
      return "inicio.html";
   }


   @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MIXTO')")
   @GetMapping("/inivarios")
   public String inivarios( ModelMap modelo, HttpSession session, @RequestParam(required = false, defaultValue = "") String rolSession,
                           @RequestParam(required = false, defaultValue = "") String accionAdmin) {
      Usuario logueado = (Usuario) session.getAttribute("usuariosession");

      if ("ADMIN".equalsIgnoreCase(String.valueOf(logueado.getRol()))) {

         System.out.println("Esta es la accion elegida por el admin: " + accionAdmin);

         switch (accionAdmin) {
            case "USUARIO": return "usuario-gestor.html";
            case "SERVICIO": return "servicio-gestor.html";
            case "PEDIDO": return "pedido-gestor.html";
         }
      }

      if (rolSession.equals("CLIENTE")) {
         modelo.addAttribute("rolSession", rolSession);
         return "inicio-cliente.html";
      }

      if (rolSession.equals("PROVEEDOR")) {
         modelo.addAttribute("rolSession", rolSession);
         return "inicio-proveedor.html";
      }

      return "redirect:/inicio";
   }


   @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_MIXTO')")
   @GetMapping("/perfil")
   public String perfil( ModelMap modelo, HttpSession session ) {
      Usuario usuario = (Usuario) session.getAttribute("usuariosession");

      List<Servicio> limpieza = servicioServicio.listarPorCategoria("Servicios de limpieza");
      List<Servicio> mantenimiento = servicioServicio.listarPorCategoria("Servicios de mantenimiento y reparaciones");
      List<Servicio> seguridad = servicioServicio.listarPorCategoria("Servicios de seguridad");
      List<Servicio> tecnologia = servicioServicio.listarPorCategoria("Servicios de tecnologia y conectividad");
      List<Servicio> cuidado = servicioServicio.listarPorCategoria("Servicios de cuidado personal y bienestar");
      List<Servicio> logistica = servicioServicio.listarPorCategoria("Servicios de entrega y logistica");

      modelo.addAttribute("limpieza", limpieza);
      modelo.addAttribute("mantenimiento", mantenimiento);
      modelo.addAttribute("seguridad", seguridad);
      modelo.addAttribute("tecnologia", tecnologia);
      modelo.addAttribute("cuidado", cuidado);
      modelo.addAttribute("logistica", logistica);
      modelo.put("usuario", usuario);

      return "actualizar-usuario.html";
   }


   @PostMapping("/registro")
   public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, @RequestParam String password,
                          @RequestParam String password2, @RequestParam(required = false) Integer barrio, @RequestParam(required = false) String rol,
                          @RequestParam String direccion, @RequestParam String telefono, @RequestParam(required = false) MultipartFile archivo,
                          @RequestParam(required = false) String descripcion, @RequestParam(required = false) String idServicio, ModelMap modelo, HttpSession session ) {

      Usuario logueado = (Usuario) session.getAttribute("usuariosession");
      if (logueado != null) { return "redirect:/inicio"; }

      if (barrio == null) { barrio = 0;}
      if (direccion.trim().isEmpty()) { direccion = "No especificada";}
      if (telefono.trim().isEmpty()) { telefono = "No especificada";}
      if (rol == null || rol.trim().isEmpty()) { rol = "USER"; }

      Usuario posibleUsuario = usuarioRepositorio.buscarPorEmail(email);

      String detalle = "";
       
      if (posibleUsuario != null) {
         modelo.put("error", "El usuario no pudo ser registrado");

         detalle = "El correo ya se encuentra registrado";

         modelo.put("detalle", detalle);
        
         List<Servicio> servicios = servicioServicio.listarServicios();
         modelo.addAttribute("servicios", servicios);
         return "registro.html";
      }

      try {
         usuarioServicio.registrar(nombre, apellido, email, password, password2,  barrio, rol, direccion, telefono);

         if (rol.equalsIgnoreCase("proveedor") || rol.equalsIgnoreCase("mixto")) {
            Usuario usuario = usuarioServicio.getPorEmail(email);

            if ((descripcion.equals("")) || (descripcion == null) || (idServicio == null)) {
               usuario = usuarioServicio.getPorEmail(email);
               usuarioRepositorio.deleteById(usuario.getId());

               String concat = ".";
               String servicioNombre = "servicio";

               if (idServicio != null) {
                  Optional<Servicio> optionalServicio = servicioRepositorio.findById(idServicio);
                  if (optionalServicio.isPresent()) {
                     servicioNombre = "";
                     concat = "";
                  }
               }

               String descrip = "";
               if ((descripcion.equals("")) || (descripcion == null)) {
                  descrip = "descripción.";
                  if(!servicioNombre.equals("")) { concat = ", "; }
               }

               List<Servicio> limpieza = servicioServicio.listarPorCategoria("Servicios de limpieza");
               List<Servicio> mantenimiento = servicioServicio.listarPorCategoria("Servicios de mantenimiento y reparaciones");
               List<Servicio> seguridad = servicioServicio.listarPorCategoria("Servicios de seguridad");
               List<Servicio> tecnologia = servicioServicio.listarPorCategoria("Servicios de tecnologia y conectividad");
               List<Servicio> cuidado = servicioServicio.listarPorCategoria("Servicios de cuidado personal y bienestar");
               List<Servicio> logistica = servicioServicio.listarPorCategoria("Servicios de entrega y logistica");

               modelo.addAttribute("limpieza", limpieza);
               modelo.addAttribute("mantenimiento", mantenimiento);
               modelo.addAttribute("seguridad", seguridad);
               modelo.addAttribute("tecnologia", tecnologia);
               modelo.addAttribute("cuidado", cuidado);
               modelo.addAttribute("logistica", logistica);

               modelo.put("error", "Los siguientes datos no pueden estar nulos: "+servicioNombre+concat+descrip);
               return "registro.html";
            }
            usuarioServicio.definirMixto(usuario, archivo, descripcion, idServicio, 0);
         }
      
      } catch (MiExcepcion ex) {
         modelo.put("error", "El usuario no pudo ser registrado");
         
         detalle = nombre + " " + apellido + "\n" + email + ": " + email;
         modelo.put("detalle", detalle);

         List<Servicio> servicios = servicioServicio.listarServicios();
         modelo.addAttribute("servicios", servicios);
         return "registro.html";
      }
      return "index.html";
   }


   @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_MIXTO')")
   @PostMapping("/perfil/{id}")
   public String actualizar(@PathVariable String id, @RequestParam(required = false) String nombre, @RequestParam(required = false) String apellido, @RequestParam(required = false) String email,
                            @RequestParam(required = false) String password, @RequestParam(required = false) String password2, @RequestParam(required = false) Integer barrio,
                            @RequestParam(required = false) String rol, @RequestParam(required = false) String direccion, @RequestParam(required = false) String telefono, 
                            @RequestParam(required = false) MultipartFile archivo, @RequestParam(required = false) String descripcion, @RequestParam(required = false) String idServicio, ModelMap modelo ) {

      String detalle = "";

      try {
         usuarioServicio.actualizar(id, nombre, apellido, email, password, password2, barrio, rol, direccion, telefono);
         
         if (rol.equalsIgnoreCase("proveedor") || rol.equalsIgnoreCase("mixto")) {
            Usuario usuario = usuarioServicio.getOne(id);
            usuarioServicio.definirMixto(usuario, archivo, descripcion, idServicio, 1);

            if ((descripcion.equals("")) || (descripcion == null) || (idServicio == null)) {
               usuario = usuarioServicio.getPorEmail(email);
               usuarioRepositorio.deleteById(usuario.getId());

               String concat = ".";
               String servicioNombre = "servicio";

               if (idServicio != null) {
                  Optional<Servicio> optionalServicio = servicioRepositorio.findById(idServicio);
                  if (optionalServicio.isPresent()) {
                     servicioNombre = "";
                     concat = "";
                  }
               }

               String descrip = "";
               if ((descripcion.equals("")) || (descripcion == null)) {
                  descrip = "descripción.";
                  if(!servicioNombre.equals("")) { concat = ", "; }
               }

               List<Servicio> limpieza = servicioServicio.listarPorCategoria("Servicios de limpieza");
               List<Servicio> mantenimiento = servicioServicio.listarPorCategoria("Servicios de mantenimiento y reparaciones");
               List<Servicio> seguridad = servicioServicio.listarPorCategoria("Servicios de seguridad");
               List<Servicio> tecnologia = servicioServicio.listarPorCategoria("Servicios de tecnologia y conectividad");
               List<Servicio> cuidado = servicioServicio.listarPorCategoria("Servicios de cuidado personal y bienestar");
               List<Servicio> logistica = servicioServicio.listarPorCategoria("Servicios de entrega y logistica");

               modelo.addAttribute("limpieza", limpieza);
               modelo.addAttribute("mantenimiento", mantenimiento);
               modelo.addAttribute("seguridad", seguridad);
               modelo.addAttribute("tecnologia", tecnologia);
               modelo.addAttribute("cuidado", cuidado);
               modelo.addAttribute("logistica", logistica);

               modelo.put("error", "Los siguientes datos no pueden estar nulos: "+servicioNombre+concat+descrip);
               return "registro.html";
            }
            usuarioServicio.definirMixto(usuario, archivo, descripcion, idServicio, 0);
         }
         modelo.put("exito", "Usuario actualizado correctamente, ¿desea modificar algo más?");

      } catch (MiExcepcion ex) {
         detalle = nombre + " " + apellido + "\n" + email + ": " + email;
         modelo.put("detalle", detalle);

         return "actualizar-usuario.html";
      }
      return "redirect:/inicio";
   }


   
  }