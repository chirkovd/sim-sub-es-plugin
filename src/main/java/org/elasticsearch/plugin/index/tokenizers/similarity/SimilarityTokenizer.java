package org.elasticsearch.plugin.index.tokenizers.similarity;

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
public final class SimilarityTokenizer extends AbstractSimSubTokenizer {

    public static final String NAME = "sim-sub-similarity";

    protected SimilarityTokenizer(SimSubService simSubService) {
        super(TokenizerType.SIMILARITY, simSubService);
    }
}
