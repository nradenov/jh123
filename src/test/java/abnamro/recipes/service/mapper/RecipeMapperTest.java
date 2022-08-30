package abnamro.recipes.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class RecipeMapperTest {

    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setUp() {
        recipeMapper = new RecipeMapperImpl();
    }
}
