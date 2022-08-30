package abnamro.recipes.service.mapper;

import abnamro.recipes.service.dto.RecipeDTO;
import abnamro.recipes.domain.Recipe;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recipe} and its DTO {@link RecipeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {}
