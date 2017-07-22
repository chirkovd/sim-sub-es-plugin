package org.elasticsearch.plugin.index.tokenizers.substructure;

import org.elasticsearch.plugin.index.tokenizers.AbstractSimSubTokenizer;
import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.plugin.utils.TokenizerType;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public final class SubstructureTokenizer extends AbstractSimSubTokenizer {

    public static final String NAME = "sim-sub-substructure";

    protected SubstructureTokenizer(SimSubService simSubService) {
        super(TokenizerType.SUBSTRUCTURE, simSubService);
    }
}
