package abnamro.recipes.service;

import abnamro.recipes.repository.RecipeRepository;
import abnamro.recipes.repository.search.RecipeSearchRepository;
import abnamro.recipes.domain.Recipe;
import abnamro.recipes.service.dto.RecipeDTO;
import abnamro.recipes.service.mapper.RecipeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Recipe}.
 */
@Service
@Transactional
public class RecipeService {

    private final Logger log = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    private final RecipeSearchRepository recipeSearchRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, RecipeSearchRepository recipeSearchRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.recipeSearchRepository = recipeSearchRepository;
    }

    /**
     * Save a recipe.
     *
     * @param recipeDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipeDTO save(RecipeDTO recipeDTO) {
        log.debug("Request to save Recipe : {}", recipeDTO);
        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe = recipeRepository.save(recipe);
        RecipeDTO result = recipeMapper.toDto(recipe);
        recipeSearchRepository.save(recipe);
        return result;
    }

    /**
     * Partially update a recipe.
     *
     * @param recipeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecipeDTO> partialUpdate(RecipeDTO recipeDTO) {
        log.debug("Request to partially update Recipe : {}", recipeDTO);

        return recipeRepository
            .findById(recipeDTO.getId())
            .map(existingRecipe -> {
                recipeMapper.partialUpdate(existingRecipe, recipeDTO);

                return existingRecipe;
            })
            .map(recipeRepository::save)
            .map(savedRecipe -> {
                recipeSearchRepository.save(savedRecipe);

                return savedRecipe;
            })
            .map(recipeMapper::toDto);
    }

    /**
     * Get all the recipes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeDTO> findAll() {
        log.debug("Request to get all Recipes");
        return recipeRepository.findAll().stream().map(recipeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one recipe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecipeDTO> findOne(Long id) {
        log.debug("Request to get Recipe : {}", id);
        return recipeRepository.findById(id).map(recipeMapper::toDto);
    }

    /**
     * Delete the recipe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Recipe : {}", id);
        recipeRepository.deleteById(id);
        recipeSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipe corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeDTO> search(String query) {
        log.debug("Request to search Recipes for query {}", query);
        return StreamSupport
            .stream(recipeSearchRepository.search(query).spliterator(), false)
            .map(recipeMapper::toDto)
            .collect(Collectors.toList());
    }
}
