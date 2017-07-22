package org.elasticsearch.plugin.index.tokenizers.similarity;

import org.elasticsearch.plugin.index.tokenizers.AbstractSimSubTokenizer;
import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.plugin.utils.TokenizerType;

/**
 * Copyright (c) 2017, DIPE Systems. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * <p>
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimilarityTokenizer extends AbstractSimSubTokenizer {

    public static final String NAME = "sim-sub-similarity";

    protected SimilarityTokenizer(SimSubService simSubService) {
        super(TokenizerType.SIMILARITY, simSubService);
    }
}
