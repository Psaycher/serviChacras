package com.FinalEgg.ServiChacras.enumeraciones;

public enum Barrio {
    BARRIO_1("Los Robles"),
    BARRIO_2("Las Acacias"),
    BARRIO_3("Los Tilos"),
    FORANEO("Foraneo");
    
    private final String nombre;

    Barrio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

