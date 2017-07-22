package org.elasticsearch.plugin.index.highlighters;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.plugin.services.SimSubServiceFactory;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.fetch.subphase.highlight.Highlighter;
import org.elasticsearch.search.fetch.subphase.highlight.HighlighterContext;
import org.elasticsearch.search.fetch.subphase.highlight.SearchContextHighlight;

import java.util.Map;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimSubHighlighter implements Highlighter {

    public static final String NAME = "sim-sub-highlighter";
    public static final String QUERY_OPTION = "query";

    private final SimSubService simSubService;

    public SimSubHighlighter() {
        this.simSubService = SimSubServiceFactory.getInstance();
    }

    @Override
    public HighlightField highlight(HighlighterContext highlighterContext) {
        SearchContextHighlight.FieldOptions fieldOptions = highlighterContext.field.fieldOptions();
        Map<String, Object> options = fieldOptions.options();
        if (options != null && options.containsKey(QUERY_OPTION)) {
            String query = (String) options.get(QUERY_OPTION);

            Map<String, Object> sourceAsMap = highlighterContext.hitContext.hit().getSourceAsMap();
            String target = (String) sourceAsMap.get(highlighterContext.fieldName);
            String highlightedValue = simSubService.highlight(target, query);
            if (highlightedValue != null) {
                Text[] texts = {new Text(highlightedValue)};
                return new HighlightField(highlighterContext.fieldName, texts);
            }
        }
        return null;
    }

    @Override
    public boolean canHighlight(FieldMapper fieldMapper) {
        return true;
    }
}
