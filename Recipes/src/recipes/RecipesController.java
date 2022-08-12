package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RecipesController {

    private final RecipeService recipeService;

    @Autowired
    public RecipesController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        Optional<Recipe> tmp = recipeService.findRecipeById(id);
        return tmp.map(recipe -> new ResponseEntity<>(recipe, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> postRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails details) {
        recipe.setAuthor(details.getUsername());
        return new ResponseEntity<>(Map.of("id", recipeService.save(recipe).id), HttpStatus.OK);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable long id, @AuthenticationPrincipal UserDetails details) {
        if (recipeService.findRecipeById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (!details.getUsername().equals(recipeService.findRecipeById(id).get().getAuthor())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            recipeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> putRecipe(@PathVariable long id, @Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails details) {
        Optional<Recipe> old = recipeService.findRecipeById(id);
        if (old.isPresent()) {
            if (!details.getUsername().equals(old.get().getAuthor())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            recipeService.update(id, recipe);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> searchRecipe(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        if (name == null && category == null || name != null && category != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (name == null) {
            List<Recipe> result = recipeService.getAllByCategory(category);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            List<Recipe> result = recipeService.getAllByName(name);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}
