package org.elasticsearch.plugin.index.scripts.substructure;

import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.script.AbstractSearchScript;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SubstructureScript extends AbstractSearchScript {

    public static final String NAME = "substructure_script";

    private final SimSubService simSubService;
    private final String field;

    private Object query;

    public SubstructureScript(SimSubService simSubService, String queryValue, String field) {
        this.simSubService = simSubService;
        this.field = field;
        this.query = simSubService.loadQuery(queryValue);
    }

    @Override
    public Object run() {
        Object targetValue = source().get(field);
        return targetValue != null && simSubService.isSubstructure(targetValue, query);
    }
}
