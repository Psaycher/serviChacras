package com.FinalEgg.ServiChacras.entidades;

import java.util.List;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Proveedor {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy = "uuid2")
    private String id;

    @OneToOne
    @JoinColumn(name ="id_usuario")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name ="id_imagen")
    private Imagen imagen;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name ="id_servicio")
    private Servicio servicio;

    @OneToMany(mappedBy = "proveedor")
    private List<Pedido> pedidos;

    @Column(name = "cantidad_pedidos")
    private Integer cantPedido;

    @Column(name = "promedio_puntuacion")
    private Integer promPuntuacion;
}
