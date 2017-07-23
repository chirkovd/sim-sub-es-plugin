package org.elasticsearch.plugin.index.tokenizers.similarity;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.plugin.services.SimSubService;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimilarityTokenizerFactory extends AbstractTokenizerFactory {

    private final SimSubService simSubService;

    public SimilarityTokenizerFactory(IndexSettings indexSettings, SimSubService simSubService,
                                      String name, Settings settings) {
        super(indexSettings, name, settings);
        this.simSubService = simSubService;
    }

    @Override
    public Tokenizer create() {
        return new SimilarityTokenizer(simSubService);
    }
}
