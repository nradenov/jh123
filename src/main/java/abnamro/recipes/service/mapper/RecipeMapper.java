package abnamro.recipes.service.mapper;

import abnamro.recipes.domain.Recipe;

import abnamro.recipes.service.api.dto.RecipeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Recipe} and its DTO {@link RecipeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {}
