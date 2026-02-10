package com.example.hello.util;

import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Utility class demonstrating usage of Apache Commons Collections 4.x (updated from vulnerable 3.2.1).
 * CVE-2015-6420 fixed in version 4.x by removing dangerous deserialization capabilities.
 */
public class CollectionUtils {

    private static final Logger logger = LogManager.getLogger(CollectionUtils.class);

    /**
     * Creates a lazy map that auto-generates default values.
     * Uses Commons Collections 4.x Transformer API (safer than 3.x).
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> createDefaultMap(final String defaultValue) {
        logger.debug("Creating lazy map with default value: " + defaultValue);

        Transformer<String, String> transformer = new ConstantTransformer<>(defaultValue);
        Map<String, String> lazyMap = LazyMap.lazyMap(new HashMap<String, String>(), transformer);
        return lazyMap;
    }

    /**
     * Transforms values using chained transformers.
     */
    @SuppressWarnings("unchecked")
    public static Transformer<String, String> createChainedTransformer(Transformer<String, String>... transformers) {
        return new ChainedTransformer<>(transformers);
    }
}
