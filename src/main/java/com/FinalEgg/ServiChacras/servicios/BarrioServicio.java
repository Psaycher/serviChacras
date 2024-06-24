package com.FinalEgg.ServiChacras.servicios;

import org.springframework.stereotype.Service;

import com.FinalEgg.ServiChacras.enumeraciones.Barrio;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BarrioServicio {

    public List<String> obtenerListaDeBarrios() {
        return Arrays.stream(Barrio.values())
                     .map(Barrio::getNombre)
                     .collect(Collectors.toList());
    }
}