package org.elasticsearch.plugin.services;

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
public class SimSubServiceImpl implements SimSubService {

    @Override
    public String highlight(String target, String query) {
        return null;
    }

    @Override
    public String fingerprints(String target, String type) {
        return null;
    }

    @Override
    public Object loadQuery(String queryValue) {
        return null;
    }

    @Override
    public Object countSimilarity(Object targetValue, Object query, String simType) {
        return null;
    }

    @Override
    public boolean isSubstructure(Object targetValue, Object query) {
        return false;
    }
}
