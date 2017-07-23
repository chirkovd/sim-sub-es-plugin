package org.elasticsearch.plugin.model;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/23/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SimSubItem {

    private long id;
    private String item;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
