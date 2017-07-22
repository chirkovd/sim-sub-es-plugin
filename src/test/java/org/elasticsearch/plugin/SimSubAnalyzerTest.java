package org.elasticsearch.plugin;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.plugin.index.tokenizers.similarity.SimilarityTokenizer;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.test.ESIntegTestCase.Scope.TEST;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/23/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
@RunWith(RandomizedRunner.class)
@ESIntegTestCase.ClusterScope(scope = TEST, numDataNodes = 1)
public class SimSubAnalyzerTest extends ESIntegTestCase {

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singleton(SimSubSearchPlugin.class);
    }

    @Test
    public void testAnalyseTokenizer() throws Exception {
        AnalyzeRequest analyzeRequest = new AnalyzeRequest();
        analyzeRequest.tokenizer(SimilarityTokenizer.NAME);
        Map<String, String> tokenFilter = new HashMap<>();
        tokenFilter.put("type", "length");
        tokenFilter.put("min", "1");
        analyzeRequest.addTokenFilter(tokenFilter);

        String text = "hello world";
        analyzeRequest.text(text);

        AnalyzeResponse response = client().admin().indices().analyze(analyzeRequest).get();
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getTokens().size()
                == text.chars().mapToObj(e -> (char) e).collect(Collectors.toSet()).size());
    }
}
