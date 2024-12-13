package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

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
    public void ReviewService_CreateReview_ReturnsReviewDto(){
        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.createReview(pokemon.getId(), reviewDto);

        Assertions.assertNotNull(savedReview);
    }

    @Test
    public void ReviewService_GetReviewByPokemonId_ReturnsReviewDto(){
        int reviewId = 1;
        when(reviewRepository.findByPokemonId(reviewId)).thenReturn(Arrays.asList(review));

        List<ReviewDto> pokenmonReturn = reviewService.getReviewsByPokemonId(reviewId);

        Assertions.assertNotNull(pokenmonReturn);
    }

    @Test
    public void ReviewService_GetReviewById_ReturnsReviewDto() {
        int reviewId = 1;
        int pokemonId = 1;

        review.setPokemon(pokemon);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        ReviewDto reviewReturn = reviewService.getReviewById(reviewId, pokemonId);

        Assertions.assertNotNull(reviewReturn);
    }

    @Test
    public void ReviewService_UpdateReview_ReturnsUpdatedReviewDto(){
        int reviewId = 1;
        int pokemonId = 1;

        //Cannot invoke "com.pokemonreview.api.models.Review.getId()" because "review" is null
        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDto updatedReview = reviewService.updateReview(reviewId, pokemonId, reviewDto);

        Assertions.assertNotNull(updatedReview);
    }

    @Test
    public void ReviewService_DeleteReview_ReturnsDeletedReviewDto(){
        int reviewId = 1;
        int pokemonId = 1;

        review.setPokemon(pokemon);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        assertAll(() -> reviewService.deleteReview(reviewId, pokemonId));
    }
}
