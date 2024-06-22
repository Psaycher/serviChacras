package com.FinalEgg.ServiChacras.entidades;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import com.FinalEgg.ServiChacras.enumeraciones.Rol;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Puntuacion {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name ="id_puntuador")
    private Usuario puntuador;
    
    @ManyToOne
    @JoinColumn(name ="id_puntuado")
    private Usuario puntuado;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Integer puntos;
}