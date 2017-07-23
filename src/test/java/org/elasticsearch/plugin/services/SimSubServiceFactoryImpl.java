package org.elasticsearch.plugin.services;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public final class SimSubServiceFactoryImpl implements SimSubServiceFactory {

    private static volatile SimSubService instance;

    public SimSubService getInstance() {
        SimSubService result = instance;
        if (result == null) {
            synchronized (SimSubService.class) {
                result = instance;
                if (result == null) {
                    instance = result = new SimSubServiceImpl();
                }
            }
        }
        return result;
    }

}
