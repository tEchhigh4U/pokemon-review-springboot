package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
// This is a good in memory database
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon(){

        // Arrange
        Pokemon pokemon = Pokemon.builder()
                                .name("Pokemon")
                                .type("electric")
                                .build();

        // Act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        // Assert
        Assertions.assertNotNull(savedPokemon);
        Assertions.assertTrue(savedPokemon.getId() > 0);
    }

    @Test
    public void PokemonRepository_GetAll_ReturnMoreThanOnePokemon(){
        Pokemon pokemon = Pokemon.builder()
                .name("Pokemon")
                .type("electric")
                .build();

        Pokemon pokemon2 = Pokemon.builder()
                .name("Pokemon")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);
        pokemonRepository.save(pokemon2);

        List<Pokemon> pokemonList = pokemonRepository.findAll();

        Assertions.assertEquals(2, pokemonList.size());
        Assertions.assertNotNull(pokemonList);
    }

    @Test
    public void PokemonRepository_FindById_ReturnPokemon(){
        Pokemon pokemon = Pokemon.builder()
                .name("Pokemon")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonList = pokemonRepository.findById(pokemon.getId()).get();

        Assertions.assertNotNull(pokemonList);
    }

    @Test
    public void PokemonRepository_FindByType_ReturnPokemonNotNull(){
        Pokemon pokemon = Pokemon.builder()
                .name("Pokemon")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonList = pokemonRepository.findByType(pokemon.getType()).get();

        Assertions.assertNotNull(pokemonList);
    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnPokemonNotNull(){
        Pokemon pokemon = Pokemon.builder()
                .name("Pokemon")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        Pokemon SavedPokemon = pokemonRepository.findById(pokemon.getId()).get();
        SavedPokemon.setName("New Pokemon");
        SavedPokemon.setType("Thunder");

        Pokemon updatedPokemon = pokemonRepository.save(SavedPokemon);

        Assertions.assertNotNull(updatedPokemon.getName());
        Assertions.assertNotNull(updatedPokemon.getType());
    }

    @Test
    public void PokemonRepository_DetelePokemon_ReturnPokemonIsEmpty() {
        Pokemon pokemon = Pokemon.builder()
                .name("Pokemon")
                .type("electric")
                .build();

        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());

        Optional<Pokemon> pokemonResult = pokemonRepository.findById(pokemon.getId());

        Assertions.assertFalse(pokemonResult.isPresent());
    }
}