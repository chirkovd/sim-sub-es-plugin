package org.elasticsearch.plugin.index.scripts.substructure;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.plugin.services.SimSubServiceFactory;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

import java.util.Map;

public class SubstructureScriptFactory implements NativeScriptFactory {

    public static final String QUERY = "query";
    public static final String FIELD = "field";

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        String queryStr = (String) params.getOrDefault(QUERY, "");
        String strField = (String) params.getOrDefault(FIELD, "field");
        return new SubstructureScript(SimSubServiceFactory.getInstance(), queryStr, strField);
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
