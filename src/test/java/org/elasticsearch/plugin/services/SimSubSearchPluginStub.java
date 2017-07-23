package org.elasticsearch.plugin.services;

import org.elasticsearch.plugin.SimSubSearchPlugin;

/**
 * Copyright (c) 2017, DIPE Systems. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * <p>
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/23/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimSubSearchPluginStub extends SimSubSearchPlugin {

    public SimSubSearchPluginStub() {
        super(new SimSubServiceFactoryImpl());
    }
}
