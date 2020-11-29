package me.kyunghwan.review.movie;

import lombok.RequiredArgsConstructor;
import me.kyunghwan.review.exception.MovieNotFoundException;
import me.kyunghwan.review.movie.dto.MovieResponseDto;
import me.kyunghwan.review.util.JsonUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RequestMapping("/api/movies")
@RestController
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getMovies() {
        return ResponseEntity.ok(movieService.findMovieList());
    }

    @GetMapping("/{idx}")
    public ResponseEntity<?> getMovie(@PathVariable Long idx) throws MovieNotFoundException {
        Movie movie = movieService.findMovie(idx);

        WebMvcLinkBuilder self = linkTo(MovieController.class).slash(movie.getIdx());
        EntityModel<MovieResponseDto> entityModel = EntityModel.of(new MovieResponseDto(movie)).add(self.withSelfRel());
        return ResponseEntity.ok(entityModel);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<?> exceptionHandler(MovieNotFoundException exception) {
        return badRequest(exception.getMessage());
    }

    private ResponseEntity<?> badRequest(String message) {
        return ResponseEntity.badRequest().body(JsonUtils.toJson("message", message));
    }

}
