package com.FinalEgg.ServiChacras.servicios;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.FinalEgg.ServiChacras.repositorios.*;
import com.FinalEgg.ServiChacras.entidades.Usuario;
import com.FinalEgg.ServiChacras.entidades.Cliente;
import com.FinalEgg.ServiChacras.entidades.Mensaje;
import com.FinalEgg.ServiChacras.entidades.Notificacion;
import com.FinalEgg.ServiChacras.entidades.Proveedor;
import com.FinalEgg.ServiChacras.enumeraciones.Rol;
import com.FinalEgg.ServiChacras.enumeraciones.Barrio;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;

@Service
public class UsuarioServicio implements UserDetailsService {
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Transactional
    public void registrar(String nombre, String apellido, String email, String password, String password2, Integer barrio, String rolString, String direccion, String telefono) throws MiExcepcion {
        validar(nombre, apellido, email, password, password2);
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

        switch (barrio) {
            case 1 -> { usuario.setBarrio(Barrio.BARRIO1Ruta2km69); }
            case 2 -> { usuario.setBarrio(Barrio.BARRIO2Ruta2km75); }
            case 3 -> { usuario.setBarrio(Barrio.BARRIO3Ruta13km86); }
            default -> { usuario.setBarrio(Barrio.FORANEO); }
        }

        usuario.setDireccion(direccion);
        usuario.setTelefono(telefono);

        Rol rol = Rol.valueOf(rolString.toUpperCase());
        usuario.setRol(rol);
        usuario.setAlta(true);
        
        usuarioRepositorio.save(usuario);
        if(rolString.toUpperCase().equals("CLIENTE")){ definirCliente(usuario, 0); }
    }

    @Transactional
    public void definirCliente(Usuario usuario, Integer num) throws MiExcepcion {
        if (num == 0) { clienteServicio.crearCliente(usuario); }
        else {
            Optional<Cliente> opcionalCliente = clienteRepositorio.findById(clienteRepositorio.idUsuario(usuario.getId()));
            Optional<Proveedor> opcionalProveedor = proveedorRepositorio.findById(proveedorRepositorio.idUsuario(usuario.getId()));

            if (!opcionalCliente.isPresent()) { clienteServicio.crearCliente(usuario); }
            opcionalProveedor.ifPresent(proveedor -> { proveedorRepositorio.deleteById(proveedor.getId()); });
        }
    }
            
    @Transactional
    public void definirMixto(Usuario usuario, MultipartFile archivo, String descripcion, String idServicio, Integer num) throws MiExcepcion {
        String rol = String.valueOf(usuario.getRol());

        if (num == 0) {
            if(rol.equals("MIXTO")) { clienteServicio.crearCliente(usuario); }
            proveedorServicio.crearProveedor(usuario, archivo, descripcion, idServicio);
            System.out.println("salio de crearProveedor en proveedorServicio");

        } else {
            Optional<Cliente> opcionalCliente = clienteRepositorio.findById(clienteRepositorio.idUsuario(usuario.getId()));
            Optional<Proveedor> opcionalProveedor = proveedorRepositorio.findById(proveedorRepositorio.idUsuario(usuario.getId()));

            switch (rol) {                
                case "PROVEEDOR" -> {
                    opcionalCliente.ifPresent(cliente -> { clienteRepositorio.deleteById(cliente.getId()); });
                    if (!opcionalProveedor.isPresent()) { proveedorServicio.crearProveedor(usuario, archivo, descripcion, idServicio); }
                }
                case "MIXTO" -> {
                    if (!opcionalCliente.isPresent()) { clienteServicio.crearCliente(usuario); }
                    if (!opcionalProveedor.isPresent()) { proveedorServicio.crearProveedor(usuario, archivo, descripcion, idServicio); }
                }
            }
        }
    }         
    
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() { return usuarioRepositorio.findAll();}

    @Transactional
    public void actualizar(String id, String nombre, String apellido, String email, String password, String password2, Integer barrio, String rolString, String direccion, String telefono) throws MiExcepcion {

        validar(email, nombre, apellido, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            if (barrio != null) {
                switch (barrio) {
                    case 1 -> { usuario.setBarrio(Barrio.BARRIO1Ruta2km69); }
                    case 2 -> { usuario.setBarrio(Barrio.BARRIO2Ruta2km75); }
                    case 3 -> { usuario.setBarrio(Barrio.BARRIO3Ruta13km86); }
                    default -> { usuario.setBarrio(Barrio.FORANEO); }
                }
            } else { usuario.setBarrio(Barrio.FORANEO); }

            usuario.setDireccion(direccion);
            usuario.setTelefono(telefono);

            Rol rol = Rol.valueOf(rolString.toUpperCase());
            usuario.setRol(rol);      

            usuarioRepositorio.save(usuario);
            definirCliente(usuario, 1);
        }
    }

    @Transactional
    public void cambiarRol(String id) throws MiExcepcion {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if(!usuario.getRol().equals(Rol.ADMIN)) { usuario.setRol(Rol.ADMIN); }

            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional
    public void notificarUsuario(Usuario usuario, Notificacion notificacion) throws MiExcepcion {
        List<Notificacion> listNotificaciones = usuario.getNotificaciones();

        if (listNotificaciones == null) { listNotificaciones = new ArrayList<>(); }

        listNotificaciones.add(notificacion); 
        usuario.setNotificaciones(listNotificaciones);          
        usuarioRepositorio.save(usuario);
    }

    private void validar(String email, String nombre, String apellido, String password, String password2) throws MiExcepcion {

        if (email.isEmpty() || email == null) { throw new MiExcepcion("el email no puede ser nulo o estar vacío"); }
        if (nombre.isEmpty() || nombre == null) { throw new MiExcepcion("el nombre del Usuario no puede ser nulo o estar vacío"); }
        if (apellido.isEmpty() || apellido == null) { throw new MiExcepcion("el apellido no puede ser nulo o estar vacío"); }
        if (password.isEmpty() || password == null || password.length() <= 4) { throw new MiExcepcion("el password del Usuario no puede estar vacía, y debe tener más de 4 dígitos"); }
        if (!password.equals(password2)) { throw new MiExcepcion("Las contraseñas ingresadas deben ser iguales"); }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol().toString());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(),permisos);

        }else{ return null; }
    }

    @Transactional
    public void eliminarUsuario(String id) { 
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setAlta(false); 
        }
    }

    @Transactional(readOnly = true)
    public Usuario getOne(String id) { return usuarioRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public Usuario getPorEmail(String email) { return usuarioRepositorio.buscarPorEmail(email); }

    @Transactional(readOnly = true)
    public String getIdPorEmail(String email) { return usuarioRepositorio.getIdPorEmail(email); }

    @Transactional(readOnly = true)
    public List<Object> getClientes() { return usuarioRepositorio.getClientes(); }

    @Transactional(readOnly = true)
    public List<Object> getProveedores() { return usuarioRepositorio.getProveedores(); }
}