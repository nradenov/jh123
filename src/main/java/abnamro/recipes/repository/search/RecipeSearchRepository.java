package abnamro.recipes.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import abnamro.recipes.domain.Recipe;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Recipe} entity.
 */
public interface RecipeSearchRepository extends ElasticsearchRepository<Recipe, Long>, RecipeSearchRepositoryInternal {}

interface RecipeSearchRepositoryInternal {
    Stream<Recipe> search(String query);
}

class RecipeSearchRepositoryInternalImpl implements RecipeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    RecipeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Recipe> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Recipe.class).map(SearchHit::getContent).stream();
    }
}
