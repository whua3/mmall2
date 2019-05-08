package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: whua
 * @create: 2019/04/27 17:44
 */
@Slf4j
public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000).maximumSize(10000).expireAfterAccess(30, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                //当读取缓存中的内容时，如果没有对应的key，则调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getValueByKey(String key) {
        try {
            String value = localCache.get(key);
            if (StringUtils.equals(value, "null")) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("localCache get error", e);
            e.printStackTrace();
        }
        return null;
    }
}
