package com.FinalEgg.ServiChacras.entidades;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import org.hibernate.annotations.GenericGenerator;
import com.FinalEgg.ServiChacras.enumeraciones.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String email;
    private String password;

    private String barrio;
    private String direccion;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;    

    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones;

    private boolean alta;
}