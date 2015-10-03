package com.hewei.es;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hewei.constants.ESConstants.ES_CLUSTER_NAME;
import static com.hewei.constants.ESConstants.ES_IP;
import static com.hewei.constants.ESConstants.ES_PORT;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/2  11:24
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class ES {

    public static final Logger LOG = LoggerFactory.getLogger(ES.class);

    private static class HEWEI {

        private static final Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", ES_CLUSTER_NAME).put("client.transport.sniff", true).build();

        private static final Client CLIENT = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(ES_IP, ES_PORT));
    }

    public static Client client() {
        return HEWEI.CLIENT;
    }

    public static boolean existIndex(String indexName) {
        return client().admin().indices().prepareExists(indexName).get().isExists();
    }
}
