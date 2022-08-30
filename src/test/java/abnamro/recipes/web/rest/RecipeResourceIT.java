package abnamro.recipes.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import abnamro.recipes.repository.search.RecipeSearchRepositoryMockConfiguration;
import abnamro.recipes.domain.Recipe;
import abnamro.recipes.repository.RecipeRepository;
import abnamro.recipes.repository.search.RecipeSearchRepository;
import abnamro.recipes.service.dto.RecipeDTO;
import abnamro.recipes.service.mapper.RecipeMapper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecipeResource} REST controller.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecipeResourceIT {

    private static final String DEFAULT_TITLE = "default title";
    private static final String UPDATED_TITLE = "updated title";

    private static final String DEFAULT_INGREDIENTS = "aaa bbb ccc";
    private static final String UPDATED_INGREDIENTS = "bbb cc ddd";

    private static final Integer DEFAULT_SERVINGS = 1;
    private static final Integer UPDATED_SERVINGS = 2;

    private static final String DEFAULT_CATEGORY = "default category";
    private static final String UPDATED_CATEGORY = "updated category";

    private static final String DEFAULT_INSTRUCTIONS = "default instructions";
    private static final String UPDATED_INSTRUCTIONS = "updated instructions";

    private static final String ENTITY_API_URL = "/api/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/recipes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see RecipeSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeSearchRepository mockRecipeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeMockMvc;

    private Recipe recipe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createEntity(EntityManager em) {
        Recipe recipe = new Recipe()
            .title(DEFAULT_TITLE)
            .ingredients(DEFAULT_INGREDIENTS)
            .servings(DEFAULT_SERVINGS)
            .category(DEFAULT_CATEGORY)
            .instructions(DEFAULT_INSTRUCTIONS);
        return recipe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createUpdatedEntity(EntityManager em) {
        Recipe recipe = new Recipe()
            .title(UPDATED_TITLE)
            .ingredients(UPDATED_INGREDIENTS)
            .servings(UPDATED_SERVINGS)
            .category(UPDATED_CATEGORY)
            .instructions(UPDATED_INSTRUCTIONS);
        return recipe;
    }

    @BeforeEach
    public void initTest() {
        recipe = createEntity(em);
    }

    @Test
    @Transactional
    void createRecipe() throws Exception {
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();
        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);
        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isCreated());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate + 1);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRecipe.getIngredients()).isEqualTo(DEFAULT_INGREDIENTS);
        assertThat(testRecipe.getServings()).isEqualTo(DEFAULT_SERVINGS);
        assertThat(testRecipe.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testRecipe.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(1)).save(testRecipe);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setTitle(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIngredientsIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setIngredients(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServingsIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setServings(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setCategory(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInstructionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setInstructions(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecipes() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS)))
            .andExpect(jsonPath("$.[*].servings").value(hasItem(DEFAULT_SERVINGS)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)));
    }

    @Test
    @Transactional
    void getRecipe() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL_ID, recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipe.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.ingredients").value(DEFAULT_INGREDIENTS))
            .andExpect(jsonPath("$.servings").value(DEFAULT_SERVINGS))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.instructions").value(DEFAULT_INSTRUCTIONS));
    }

    @Test
    @Transactional
    void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecipe() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe
        Recipe updatedRecipe = recipeRepository.findById(recipe.getId()).get();
        // Disconnect from session so that the updates on updatedRecipe are not directly saved in db
        em.detach(updatedRecipe);
        updatedRecipe
            .title(UPDATED_TITLE)
            .ingredients(UPDATED_INGREDIENTS)
            .servings(UPDATED_SERVINGS)
            .category(UPDATED_CATEGORY)
            .instructions(UPDATED_INSTRUCTIONS);
        RecipeDTO recipeDTO = recipeMapper.toDto(updatedRecipe);

        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRecipe.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testRecipe.getServings()).isEqualTo(UPDATED_SERVINGS);
        assertThat(testRecipe.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testRecipe.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository).save(testRecipe);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(count.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(0)).save(recipe);
    }

    @Test
    @Transactional
    void partialUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe.title(UPDATED_TITLE).ingredients(UPDATED_INGREDIENTS);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRecipe.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testRecipe.getServings()).isEqualTo(DEFAULT_SERVINGS);
        assertThat(testRecipe.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testRecipe.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void fullUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe
            .title(UPDATED_TITLE)
            .ingredients(UPDATED_INGREDIENTS)
            .servings(UPDATED_SERVINGS)
            .category(UPDATED_CATEGORY)
            .instructions(UPDATED_INSTRUCTIONS);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRecipe.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testRecipe.getServings()).isEqualTo(UPDATED_SERVINGS);
        assertThat(testRecipe.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testRecipe.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(count.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(0)).save(recipe);
    }

    @Test
    @Transactional
    void deleteRecipe() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        int databaseSizeBeforeDelete = recipeRepository.findAll().size();

        // Delete the recipe
        restRecipeMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(1)).deleteById(recipe.getId());
    }

    @Test
    @Transactional
    void searchRecipe() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);
        when(mockRecipeSearchRepository.search("id:" + recipe.getId())).thenReturn(Stream.of(recipe));

        // Search the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS)))
            .andExpect(jsonPath("$.[*].servings").value(hasItem(DEFAULT_SERVINGS)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)));
    }
}
