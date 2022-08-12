package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Optional<Recipe> findRecipeById(long id) {
        return recipeRepository.findById(id);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void delete(long id) {
        recipeRepository.deleteById(id);
    }

    public void update(long id, Recipe newRecipe) {
        Optional<Recipe> old = findRecipeById(id);
        if (old.isPresent()) {
            Recipe oldRecipe = old.get();
            oldRecipe.category = newRecipe.category;
            oldRecipe.name = newRecipe.name;
            oldRecipe.description = newRecipe.description;
            oldRecipe.directions = newRecipe.directions;
            oldRecipe.ingredients = newRecipe.ingredients;
            recipeRepository.save(oldRecipe);
        }
    }

    public List<Recipe> getAllByCategory(String category) {
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> getAllByName(String name) {
        return recipeRepository.findAllByNameIgnoreCaseContainsOrderByDateDesc(name);
    }
}
