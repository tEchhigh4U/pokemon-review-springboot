package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTests {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTests(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void ReviewRepository_SaveAll_ReturnSavedReview() {
        Review review = Review.builder()
                .title("Title")
                .content("Testing")
                .stars(5)
                .build();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertNotNull(savedReview);
        Assertions.assertTrue(savedReview.getId() > 0);
    }

    @Test
    public void ReviewRepository_GetAll_ReturnsMoreThanOneReview() {
        Review review = Review.builder()
                .title("Title")
                .content("Testing")
                .stars(5)
                .build();

        Review review2 = Review.builder()
                .title("Title")
                .content("Testing")
                .stars(5)
                .build();

        reviewRepository.save(review);
        reviewRepository.save(review2);

        List<Review> reviews = reviewRepository.findAll();

        Assertions.assertEquals(2, reviews.size());
        Assertions.assertTrue(reviews.contains(review));
        Assertions.assertTrue(reviews.contains(review2));
        Assertions.assertFalse(reviews.contains(null));
    }

    @Test
    public void ReviewRepository_FindById_ReturnsMoreThanOneReview() {
        Review review = Review.builder()
                .title("Title")
                .content("Testing")
                .stars(5)
                .build();

        reviewRepository.save(review);

        Review reviewResult = reviewRepository.findById(review.getId()).get();

        Assertions.assertNotNull(reviewResult);
        Assertions.assertTrue(reviewResult.getId() > 0);
    }

    @Test
    public void ReviewRepository_UpdateReview_ReturnsUpdatedReview() {
        Review review = Review.builder()
                .title("Title")
                .content("Testing")
                .stars(5)
                .build();

        reviewRepository.save(review);

        Review reviewResult = reviewRepository.findById(review.getId()).get();
        reviewResult.setTitle("Updated Title");
        reviewResult.setContent("Updated Content");
        reviewRepository.save(reviewResult);

        Review updatedReviewResult = reviewRepository.findById(review.getId()).get();

        Assertions.assertNotNull(updatedReviewResult);
        Assertions.assertTrue(updatedReviewResult.getId() > 0);
        Assertions.assertEquals("Updated Title", updatedReviewResult.getTitle());
        Assertions.assertEquals("Updated Content", updatedReviewResult.getContent());

    }

    @Test
    public void ReviewRepository_DeleteReview_ReturnsReviewIsDelete() {
        Review review = Review.builder()
                .title("Title")
                .content("Testing")
                .stars(5)
                .build();

        reviewRepository.save(review);

        reviewRepository.deleteById(review.getId());
        Optional<Review> deletedReview = reviewRepository.findById(review.getId());

        Assertions.assertFalse(deletedReview.isPresent());
    }
}
