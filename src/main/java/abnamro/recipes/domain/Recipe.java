package abnamro.recipes.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "recipe")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "ingredients", nullable = false)
    private String ingredients;

    @NotNull
    @Column(name = "servings", nullable = false)
    private Integer servings;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @NotNull
    @Column(name = "instructions", nullable = false)
    private String instructions;

    public Long getId() {
        return this.id;
    }

    public Recipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Recipe title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public Recipe ingredients(String ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getServings() {
        return this.servings;
    }

    public Recipe servings(Integer servings) {
        this.setServings(servings);
        return this;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return this.category;
    }

    public Recipe category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public Recipe instructions(String instructions) {
        this.setInstructions(instructions);
        return this;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return id != null && id.equals(((Recipe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", ingredients='" + getIngredients() + "'" +
            ", servings=" + getServings() +
            ", category='" + getCategory() + "'" +
            ", instructions='" + getInstructions() + "'" +
            "}";
    }
}
