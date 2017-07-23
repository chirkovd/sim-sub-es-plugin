package org.elasticsearch.plugin.services;

import org.elasticsearch.plugin.utils.TokenizerType;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimSubServiceImpl implements SimSubService {

    private static final int FIRST_CODE = 0;
    private static final int LAST_CODE = 255;

    @Override
    public String highlight(String target, String query) {
        if (target.contains(query)) {
            return target.replace(query, "<hi>".concat(query).concat("</hi>"));
        } else {
            return target;
        }
    }

    @Override
    public String fingerprints(String target, TokenizerType type) {
        Set<Integer> charsSet = target.chars().mapToObj(Integer::valueOf).collect(Collectors.toSet());
        StringBuilder fingerprint = new StringBuilder();
        for (int i = FIRST_CODE; i <= LAST_CODE; i++) {
            fingerprint.append(charsSet.contains(i) ? "1" : "0");
        }
        return fingerprint.toString();
    }

    @Override
    public Object loadQuery(String queryValue) {
        return queryValue;
    }

    @Override
    public Object countSimilarity(Object targetValue, Object query, String simType) {
        Set<Character> targetSet = targetValue.toString().chars().mapToObj(e -> (char) e).collect(Collectors.toSet());
        Set<Character> querySet = query.toString().chars().mapToObj(e -> (char) e).collect(Collectors.toSet());

        int sum = targetSet.size() + querySet.size();
        querySet.retainAll(targetSet);

        //similarity by tanimoto
        return ((float) querySet.size() / (sum - querySet.size())) * 100F;
    }

    @Override
    public boolean isSubstructure(Object targetValue, Object query) {
        return !(targetValue == null || query == null) && targetValue.toString().contains(query.toString());
    }
}
