package org.elasticsearch.plugin;

import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugin.index.highlighters.SimSubHighlighter;
import org.elasticsearch.plugin.index.scripts.similarity.SimilarityScriptFactory;
import org.elasticsearch.plugin.index.scripts.substructure.SubstructureScriptFactory;
import org.elasticsearch.plugin.index.tokenizers.similarity.SimilarityTokenizer;
import org.elasticsearch.plugin.index.tokenizers.similarity.SimilarityTokenizerFactory;
import org.elasticsearch.plugin.index.tokenizers.substructure.SubstructureTokenizer;
import org.elasticsearch.plugin.index.tokenizers.substructure.SubstructureTokenizerFactory;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.search.fetch.subphase.highlight.Highlighter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimSubSearchPlugin extends Plugin implements AnalysisPlugin, ScriptPlugin, SearchPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> tokenizers = new HashMap<>();
        tokenizers.put(SubstructureTokenizer.NAME, SubstructureTokenizerFactory::new);
        tokenizers.put(SimilarityTokenizer.NAME, SimilarityTokenizerFactory::new);
        return tokenizers;
    }

    @Override
    public List<NativeScriptFactory> getNativeScripts() {
        List<NativeScriptFactory> scriptFactories = new ArrayList<>();
        scriptFactories.add(new SimilarityScriptFactory());
        scriptFactories.add(new SubstructureScriptFactory());
        return scriptFactories;
    }

    @Override
    public Map<String, Highlighter> getHighlighters() {
        Map<String, Highlighter> highlighters = new HashMap<>();
        highlighters.put(SimSubHighlighter.NAME, new SimSubHighlighter());
        return highlighters;
    }
}
