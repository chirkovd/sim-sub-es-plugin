package org.elasticsearch.plugin.index.scripts.similarity;

import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.script.AbstractSearchScript;

public class SimilarityScript extends AbstractSearchScript {

    public static final String NAME = "similarity_script";

    private final SimSubService simSubService;
    private final String field;
    private final String simType;
    private final Object query;

    public SimilarityScript(SimSubService simSubService, String simType,
                            String queryValue, String field) {
        this.simSubService = simSubService;
        this.simType = simType;
        this.field = field;
        this.query = simSubService.loadQuery(queryValue);
    }

    @Override
    public Object run() {
        Object targetValue = source().get(field);
        if (targetValue != null) {
            return simSubService.countSimilarity(targetValue, query, simType);
        } else {
            return -1.0f;
        }
    }
}
