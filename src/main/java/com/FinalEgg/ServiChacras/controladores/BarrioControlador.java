package com.FinalEgg.ServiChacras.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.FinalEgg.ServiChacras.enumeraciones.Barrio;

@Controller
public class BarrioControlador {

    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        // Obtener los nombres de los barrios de la enumeración Barrios
        Barrio[] barrios = Barrio.values();
        String[] nombresBarrios = new String[barrios.length];
        for (int i = 0; i < barrios.length; i++) {
            nombresBarrios[i] = barrios[i].getNombre();
        }

        // Agregar los nombres de los barrios al modelo
        model.addAttribute("barrios", nombresBarrios);

        // Retornar el nombre de la vista del formulario
        return "registro"; // Este nombre debe coincidir con el nombre del archivo HTML
    }
}
