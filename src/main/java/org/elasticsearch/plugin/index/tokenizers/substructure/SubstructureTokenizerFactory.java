package org.elasticsearch.plugin.index.tokenizers.substructure;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.plugin.services.SimSubServiceFactory;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SubstructureTokenizerFactory extends AbstractTokenizerFactory {

    private final SimSubService simSubService;

    public SubstructureTokenizerFactory(IndexSettings indexSettings, Environment environment,
                                        String name, Settings settings) {
        super(indexSettings, name, settings);
        this.simSubService = SimSubServiceFactory.getInstance();
    }

    @Override
    public Tokenizer create() {
        return new SubstructureTokenizer(simSubService);
    }
}