package abnamro.recipes.service.dto;

import abnamro.recipes.domain.Recipe;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Recipe} entity.
 */
public class RecipeDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String ingredients;

    @NotNull
    private Integer servings;

    @NotNull
    private String category;

    @NotNull
    private String instructions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecipeDTO)) {
            return false;
        }

        RecipeDTO recipeDTO = (RecipeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recipeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecipeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", ingredients='" + getIngredients() + "'" +
            ", servings=" + getServings() +
            ", category='" + getCategory() + "'" +
            ", instructions='" + getInstructions() + "'" +
            "}";
    }
}
