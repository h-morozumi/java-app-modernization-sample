package com.example.hello.util;

import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Utility class demonstrating usage of vulnerable Apache Commons Collections 3.2.1.
 * CVE-2015-6420: Unsafe deserialization via InvokerTransformer.
 */
public class CollectionUtils {

    private static final Logger logger = Logger.getLogger(CollectionUtils.class);

    /**
     * Creates a lazy map that auto-generates default values.
     * Uses Commons Collections 3.x Transformer API (vulnerable to deserialization attacks).
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> createDefaultMap(final String defaultValue) {
        logger.debug("Creating lazy map with default value: " + defaultValue);

        Transformer transformer = new ConstantTransformer(defaultValue);
        Map<String, String> lazyMap = LazyMap.decorate(new HashMap<String, String>(), transformer);
        return lazyMap;
    }

    /**
     * Transforms values using chained transformers.
     */
    @SuppressWarnings("unchecked")
    public static Transformer createChainedTransformer(Transformer... transformers) {
        return new ChainedTransformer(transformers);
    }
}
