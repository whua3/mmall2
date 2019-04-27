package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: whua
 * @create: 2019/04/27 17:44
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //当读取缓存中的内容时，如果没有对应的key，则调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getValueByKey(String key) {
        String value;
        try {
            value = localCache.get(key);
            if (StringUtils.isBlank(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("localCache get error", e);
            e.printStackTrace();
        }
        return null;
    }
}
