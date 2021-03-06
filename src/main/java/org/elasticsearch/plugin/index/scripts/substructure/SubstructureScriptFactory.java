package org.elasticsearch.plugin.index.scripts.substructure;

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
public class SubstructureScriptFactory implements NativeScriptFactory {

    public static final String QUERY = "query";
    public static final String FIELD = "field";

    private final SimSubService simSubService;

    public SubstructureScriptFactory(SimSubService simSubService) {
        this.simSubService = simSubService;
    }

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        String queryStr = (String) params.getOrDefault(QUERY, "");
        String strField = (String) params.getOrDefault(FIELD, "field");
        return new SubstructureScript(simSubService, queryStr, strField);
    }

    @Override
    public boolean needsScores() {
        return false;
    }

    @Override
    public String getName() {
        return SubstructureScript.NAME;
    }
}
