package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.PokemonController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false) // Disables automatic filter registration for focused and isolated testing of web layers
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @Autowired
    private ObjectMapper objectMapper; // Injects the Spring-managed ObjectMapper to ensure consistent configuration for JSON processing throughout the application

    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;
    private PokemonDto pokemonDto;

    @BeforeEach
    public void init(){
        pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        pokemonDto = PokemonDto.builder().name("pikachu").type("electric").build();
        review = Review.builder().title("title").content("content").stars(5).build();
        reviewDto = ReviewDto.builder().title("title").content("content").build();
    }

    @Test
    public void PokemonControllerTest_CreatePokemon_ReturnsCreated() throws Exception {
        given(pokemonService.createPokemon(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/pokemon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(pokemonDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(pokemonDto.getType()));
    }

    @Test
    public void PokemonControllerTest_GetAllPokemon_ReturnsResponseDto() throws Exception {
        PokemonResponse responseDto = PokemonResponse.builder().pageSize(10).last(true).pageNo(1).content(Arrays.asList(pokemonDto)).build();
        when(pokemonService.getAllPokemon(1,10)).thenReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo","1")
                .param("pageSize","10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value(responseDto.getContent().size()));
    }
}
