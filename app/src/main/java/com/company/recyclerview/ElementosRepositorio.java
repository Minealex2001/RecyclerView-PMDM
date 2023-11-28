package com.company.recyclerview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementosRepositorio {

    List<Pokemon> pokemons;

    interface Callback {
        void cuandoFinalice(List<Pokemon> pokemons);
    }

    ElementosRepositorio() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode root = objectMapper.readTree(new URL("https://pokeapi.co/api/v2/pokemon-species/"));
        pokemons = Collections.singletonList(objectMapper.readValue(root.get("count").get("Results").traverse(), new TypeReference<Pokemon>() {
        }));
    }

    List<Pokemon> obtener() {
        return pokemons;
    }

    void insertar(Pokemon pokemon, Callback callback){
        pokemons.add(pokemon);
        callback.cuandoFinalice(pokemons);
    }

    void eliminar(Pokemon pokemon, Callback callback) {
        pokemons.remove(pokemon);
        callback.cuandoFinalice(pokemons);
    }

    void actualizar(Pokemon pokemon, float valoracion, Callback callback) {
        pokemon.valoracion = valoracion;
        callback.cuandoFinalice(pokemons);
    }
}
