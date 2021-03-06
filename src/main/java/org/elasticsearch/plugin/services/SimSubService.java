package org.elasticsearch.plugin.services;

import org.elasticsearch.plugin.utils.TokenizerType;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public interface SimSubService {

    String highlight(String target, String query, String startTag, String endTag);

    String fingerprints(String target, TokenizerType type);

    Object loadQuery(String queryValue);

    Object countSimilarity(Object targetValue, Object query, String simType);

    boolean isSubstructure(Object targetValue, Object query);
}
