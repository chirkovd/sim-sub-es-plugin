package org.elasticsearch.plugin;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.plugin.index.highlighters.SimSubHighlighter;
import org.elasticsearch.plugin.index.scripts.similarity.SimilarityScript;
import org.elasticsearch.plugin.index.scripts.similarity.SimilarityScriptFactory;
import org.elasticsearch.plugin.index.scripts.substructure.SubstructureScript;
import org.elasticsearch.plugin.index.scripts.substructure.SubstructureScriptFactory;
import org.elasticsearch.plugin.model.SimSubItem;
import org.elasticsearch.plugin.services.SimSubSearchPluginStub;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.*;
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
public class SimSubSearchTest extends ESIntegTestCase {

    public static final String SS_TEST = "ss_test";
    public static final String SS_STRING = "ss_string";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static String mapping;
    private static String settings;

    @BeforeClass
    public static void setup() {
        ClassLoader classLoader = SimSubSearchTest.class.getClassLoader();
        try {
            mapping = IOUtils.toString(classLoader.getResourceAsStream("es.mapping.json"), "UTF-8");
            settings = IOUtils.toString(classLoader.getResourceAsStream("es.settings.json"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singleton(SimSubSearchPluginStub.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        if (!indexExists(SS_TEST)) {
            CreateIndexRequestBuilder builder = prepareCreate(SS_TEST);
            builder.setSettings(settings, XContentType.JSON);
            builder.addMapping(SS_STRING, mapping, XContentType.JSON);
            CreateIndexResponse indexResponse = builder.execute().actionGet();
            Assert.assertNotNull(indexResponse);
        }
    }

    @Test
    public void testSimilaritySearch() throws Exception {
        SimSubItem item1 = new SimSubItem();
        item1.setId(1L);
        item1.setItem("first item");

        SimSubItem item2 = new SimSubItem();
        item2.setId(2L);
        item2.setItem("second item");

        List<SimSubItem> items = Arrays.asList(item1, item2);

        for (SimSubItem item : items) {
            IndexResponse response = index(SS_TEST, SS_STRING, String.valueOf(item.getId()), getSource(item));
            Assert.assertNotNull(response);
        }

        for (SimSubItem item : items) {
            GetResponse response = get(SS_TEST, SS_STRING, String.valueOf(item.getId()));
            Assert.assertNotNull(response);
        }

        String queryValue = "irst item";
        QueryBuilder query = QueryBuilders.matchQuery("item.sim", queryValue);

        Map<String, Object> params = new HashMap<>();
        params.put(SimilarityScriptFactory.FIELD, "item");
        params.put(SimilarityScriptFactory.TYPE, "tanimoto");
        params.put(SimilarityScriptFactory.QUERY, queryValue);

        Script script = new Script(ScriptType.INLINE, "native", SimilarityScript.NAME, params);

        FunctionScoreQueryBuilder scoreQueryBuilder = QueryBuilders
                .functionScoreQuery(query, ScoreFunctionBuilders.scriptFunction(script));
        scoreQueryBuilder.boostMode(CombineFunction.REPLACE);
        scoreQueryBuilder.setMinScore(80);

        SearchRequestBuilder requestBuilder = client().prepareSearch(SS_TEST).setTypes(SS_STRING);
        requestBuilder.setQuery(scoreQueryBuilder);

        SearchResponse searchResponse = requestBuilder.get();
        Assert.assertNotNull(searchResponse);

        SearchHits hits = searchResponse.getHits();
        Assert.assertTrue(hits.getTotalHits() == 1);
        Assert.assertTrue(hits.getMaxScore() == 87.5F);
        Assert.assertEquals("first item", hits.getAt(0).getSource().get("item"));
    }

    @Test
    public void testSubstructureSearch() throws Exception {
        SimSubItem item1 = new SimSubItem();
        item1.setId(3L);
        item1.setItem("hello world");

        SimSubItem item2 = new SimSubItem();
        item2.setId(4L);
        item2.setItem("hello application");

        List<SimSubItem> items = Arrays.asList(item1, item2);

        for (SimSubItem item : items) {
            IndexResponse response = index(SS_TEST, SS_STRING, String.valueOf(item.getId()), getSource(item));
            Assert.assertNotNull(response);
        }

        for (SimSubItem item : items) {
            GetResponse response = get(SS_TEST, SS_STRING, String.valueOf(item.getId()));
            Assert.assertNotNull(response);
        }
        String queryValue = "world";

        /* Init highlighter */
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("item");
        highlightBuilder.highlighterType(SimSubHighlighter.NAME);
        highlightBuilder.options(Collections.singletonMap(SimSubHighlighter.QUERY_OPTION, queryValue));

        /* Substructure search by tokenizer */
        QueryBuilder query = QueryBuilders.matchQuery("item.sub", queryValue).operator(Operator.AND);
        SearchRequestBuilder requestBuilder = client().prepareSearch(SS_TEST).setTypes(SS_STRING);
        requestBuilder.setQuery(query);
        requestBuilder.highlighter(highlightBuilder);

        SearchResponse searchResponse = requestBuilder.get();
        Assert.assertNotNull(searchResponse);

        SearchHits hits = searchResponse.getHits();
        Assert.assertTrue(hits.getTotalHits() == 1);

        Map<String, HighlightField> fields = hits.getAt(0).getHighlightFields();
        Assert.assertNotNull(fields);

        Text[] fragments = fields.get("item").getFragments();
        String highlighted = Arrays.asList(fragments).stream().map(Text::string).collect(Collectors.joining(""));
        Assert.assertEquals("hello <hi>world</hi>", highlighted);

        /* Substructure search by script */
        Map<String, Object> params = new HashMap<>();
        params.put(SubstructureScriptFactory.FIELD, "item");
        params.put(SubstructureScriptFactory.QUERY, queryValue);

        Script script = new Script(ScriptType.INLINE, "native", SubstructureScript.NAME, params);
        ScriptQueryBuilder scriptQuery = QueryBuilders.scriptQuery(script);
        query = QueryBuilders.boolQuery().must(scriptQuery);

        SearchRequestBuilder scriptRequestBuilder = client().prepareSearch(SS_TEST).setTypes(SS_STRING);
        scriptRequestBuilder.setQuery(query);
        scriptRequestBuilder.highlighter(highlightBuilder);

        searchResponse = scriptRequestBuilder.get();
        Assert.assertNotNull(searchResponse);

        hits = searchResponse.getHits();
        Assert.assertTrue(hits.getTotalHits() == 1);

        fields = hits.getAt(0).getHighlightFields();
        Assert.assertNotNull(fields);

        fragments = fields.get("item").getFragments();
        highlighted = Arrays.asList(fragments).stream().map(Text::string).collect(Collectors.joining(""));
        Assert.assertEquals("hello <hi>world</hi>", highlighted);
    }


    private String getSource(SimSubItem object) {
        try {
            return OBJECT_MAPPER.writerFor(object.getClass()).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot build source");
        }
    }
}