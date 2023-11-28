package com.company.recyclerview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class ElementosViewModel extends AndroidViewModel {

    ElementosRepositorio elementosRepositorio;

    MutableLiveData<List<Pokemon>> listElementosMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Pokemon> elementoSeleccionado = new MutableLiveData<>();

    public ElementosViewModel(@NonNull Application application) throws IOException {
        super(application);

        elementosRepositorio = new ElementosRepositorio();

        listElementosMutableLiveData.setValue(elementosRepositorio.obtener());
    }

    MutableLiveData<List<Pokemon>> obtener(){
        return listElementosMutableLiveData;
    }

    void insertar(Pokemon pokemon){
        elementosRepositorio.insertar(pokemon, new ElementosRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Pokemon> pokemons) {
                listElementosMutableLiveData.setValue(pokemons);
            }
        });
    }

    void eliminar(Pokemon pokemon){
        elementosRepositorio.eliminar(pokemon, new ElementosRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Pokemon> pokemons) {
                listElementosMutableLiveData.setValue(pokemons);
            }
        });
    }

    void actualizar(Pokemon pokemon, float valoracion){
        elementosRepositorio.actualizar(pokemon, valoracion, new ElementosRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Pokemon> pokemons) {
                listElementosMutableLiveData.setValue(pokemons);
            }
        });
    }
    void seleccionar(Pokemon pokemon){
        elementoSeleccionado.setValue(pokemon);
    }

    MutableLiveData<Pokemon> seleccionado(){
        return elementoSeleccionado;
    }
}
