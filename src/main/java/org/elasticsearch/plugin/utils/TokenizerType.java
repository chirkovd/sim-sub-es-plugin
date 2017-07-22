package org.elasticsearch.plugin.utils;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public enum TokenizerType {
    SIMILARITY("sim"),
    SUBSTRUCTURE("sub");

    private String type;

    TokenizerType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
