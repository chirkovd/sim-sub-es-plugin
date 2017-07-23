package org.elasticsearch.plugin.index.scripts.similarity;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

import java.util.Map;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimilarityScriptFactory implements NativeScriptFactory {

    public static final String TYPE = "type";
    public static final String QUERY = "query";
    public static final String FIELD = "field";

    private final SimSubService simSubService;

    public SimilarityScriptFactory(SimSubService simSubService) {
        this.simSubService = simSubService;
    }

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        String simType = (String) params.getOrDefault(TYPE, "tanimoto");
        String queryStr = (String) params.getOrDefault(QUERY, "");
        String strField = (String) params.getOrDefault(FIELD, "field");
        return new SimilarityScript(simSubService, simType, queryStr, strField);
    }

    @Override
    public boolean needsScores() {
        return false;
    }

    @Override
    public String getName() {
        return SimilarityScript.NAME;
    }
}
