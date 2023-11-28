package com.company.recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.company.recyclerview.databinding.FragmentRecyclerElementosBinding;
import com.company.recyclerview.databinding.ViewholderElementoBinding;

import java.util.List;


public class RecyclerElementosFragment extends Fragment {

    private FragmentRecyclerElementosBinding binding;
    private ElementosViewModel elementosViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRecyclerElementosBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elementosViewModel = new ViewModelProvider(requireActivity()).get(ElementosViewModel.class);
        navController = Navigation.findNavController(view);

        // navegar a NuevoElemento cuando se hace click en el FloatingActionButton
        binding.irANuevoElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_recyclerElementosFragment_to_nuevoElementoFragment);
            }
        });

        // crear el Adaptador
        ElementosAdapter elementosAdapter = new ElementosAdapter();

        // asociar el Adaptador con el RecyclerView
        binding.recyclerView.setAdapter(elementosAdapter);

        // obtener el array de Elementos, y pasarselo al Adaptador
        elementosViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> pokemons) {
                elementosAdapter.establecerLista(pokemons);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT  | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                Pokemon pokemon = elementosAdapter.obtenerElemento(posicion);
                elementosViewModel.eliminar(pokemon);

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    class ElementosAdapter extends RecyclerView.Adapter<ElementoViewHolder> {

        // referencia al Array que obtenemos del ViewModel
        List<Pokemon> pokemons;

        // crear un nuevo ViewHolder
        @NonNull
        @Override
        public ElementoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ElementoViewHolder(ViewholderElementoBinding.inflate(getLayoutInflater(), parent, false));
        }

        // rellenar un ViewHolder en una posición del Recycler con los datos del elemento que
        // esté en esa misma posición en el Array
        @Override
        public void onBindViewHolder(@NonNull ElementoViewHolder holder, int position) {

            Pokemon pokemon = pokemons.get(position);

            holder.binding.nombre.setText(pokemon.nombre);
            holder.binding.valoracion.setRating(pokemon.valoracion);

            holder.binding.valoracion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                if(fromUser) {
                    elementosViewModel.actualizar(pokemon, rating);
                }
            });

            holder.itemView.setOnClickListener(v -> {
                elementosViewModel.seleccionar(pokemon);
                navController.navigate(R.id.action_recyclerElementosFragment_to_mostrarElementoFragment);
            });
        }
        public Pokemon obtenerElemento(int posicion){
            return pokemons.get(posicion);
        }

        // informar al Recycler de cuántos elementos habrá en la lista
        @Override
        public int getItemCount() {
            return pokemons != null ? pokemons.size() : 0;
        }

        // establecer la referencia a la lista, y notificar al Recycler para que se regenere
        public void establecerLista(List<Pokemon> pokemons){
            this.pokemons = pokemons;
            notifyDataSetChanged();
        }
    }

    // Clase para inicializar el ViewBinding en los ViewHolder
    class ElementoViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderElementoBinding binding;

        public ElementoViewHolder(ViewholderElementoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}