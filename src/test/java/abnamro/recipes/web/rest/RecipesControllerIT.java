package abnamro.recipes.web.rest;

import abnamro.recipes.RecipeApp;
import abnamro.recipes.domain.Recipe;
import abnamro.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Integration tests for the {@link abnamro.recipes.web.api.RecipesApiController} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = RecipeApp.class)
@AutoConfigureMockMvc
class RecipesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeRepository mockRepository;

    @BeforeEach
    public void init() {
        Recipe recipe =
            new Recipe().id(1L).category("fish").ingredients("salmon oil garlic").instructions("marinade, enjoy").title("Salmon in oil").servings(4);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(recipe));
    }
    @Test
    public void findRecipeById_ok() throws Exception {

        Recipe recipe = new Recipe().id(1L).category("fish").ingredients("salmon oil garlic").instructions("marinade, enjoy").title("Salmon in oil").servings(4);

        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        mockMvc.perform(get("/api/recipes/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.category", is("fish")))
            .andExpect(jsonPath("$.ingredients", is("salmon oil garlic")))
            .andExpect(jsonPath("$.instructions", is("marinade, enjoy")))
            .andExpect(jsonPath("$.servings", is(4)))
            .andExpect(jsonPath("$.title", is("Salmon in oil")));

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_allRecipe_OK() throws Exception {

        List<Recipe> recipes = Arrays.asList(
            new Recipe().id(1L).category("fish").ingredients("salmon oil garlic").instructions("marinade, enjoy").title("Salmon in oil").servings(4),
            new Recipe().id(2L).category("meat").ingredients("beef oil garlic").instructions("marinade, grill, enjoy").title("Steak on grill").servings(2));

        when(mockRepository.findAll()).thenReturn(recipes);

        mockMvc.perform(get("/api/recipes").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].category", is("fish")))
            .andExpect(jsonPath("$[0].ingredients", is("salmon oil garlic")))
            .andExpect(jsonPath("$[0].instructions", is("marinade, enjoy")))
            .andExpect(jsonPath("$[0].title", is("Salmon in oil")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].category", is("meat")))
            .andExpect(jsonPath("$[1].ingredients", is("beef oil garlic")))
            .andExpect(jsonPath("$[1].instructions", is("marinade, grill, enjoy")))
            .andExpect(jsonPath("$[1].title", is("Steak on grill")));

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_bookIdNotFound_404() throws Exception {
        mockMvc.perform(get("/recipes/5")).andExpect(status().isNotFound());
    }

}
