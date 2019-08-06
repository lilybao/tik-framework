package com.jeerigger.common.shiro.cache;

import com.jeerigger.frame.exception.FrameException;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ShiroCacheManager implements CacheManager {
    @Autowired
    private org.springframework.cache.CacheManager springCacheManager;

    private final ConcurrentMap<String, ShiroCache> CACHES = new ConcurrentHashMap<>();

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        ShiroCache<K, V> shiroCache = this.CACHES.get(cacheName);
        if (shiroCache != null) {
            return shiroCache;
        } else {
            synchronized (this.CACHES) {
                shiroCache = this.CACHES.get(cacheName);
                if (shiroCache == null) {
                    org.springframework.cache.Cache springCache = this.springCacheManager.getCache(cacheName);
                    if (springCache == null) {
                        if (this.springCacheManager instanceof EhCacheCacheManager) {
                            EhCacheCacheManager ehCacheCacheManager = (EhCacheCacheManager) this.springCacheManager;
                            ehCacheCacheManager.getCacheManager().addCache(cacheName);
                            springCache = this.springCacheManager.getCache(cacheName);
                        }
                    }
                    if (springCache == null) {
                        throw new FrameException(" not found cacheName " + cacheName);
                    }
                    shiroCache = new ShiroCache(cacheName, springCache);

                    this.CACHES.put(cacheName, shiroCache);
                }
                return shiroCache;
            }
        }
    }

    /**
     * spring Cache包装
     */
    @RequiredArgsConstructor
    public static class ShiroCache<K, V> implements Cache<K, V> {
        private final String cacheName;
        private final org.springframework.cache.Cache springCache;

        @Override
        public void clear() throws CacheException {
            this.springCache.clear();
        }

        @Override
        public V get(K key) throws CacheException {
            org.springframework.cache.Cache.ValueWrapper wrapper = this.springCache.get(key);
            return wrapper == null ? null : (V) wrapper.get();
        }

        @Override
        public V put(K key, V value) throws CacheException {
            this.springCache.put(key, value);
            return value;
        }

        @Override
        public V remove(K key) throws CacheException {
            V v = this.get(key);
            this.springCache.evict(key);
            return v;
        }

        @Override
        public Set<K> keys() {
            throw new UnsupportedOperationException(" not supported ");
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException(" not supported ");
        }

        @Override
        public Collection<V> values() {
            throw new UnsupportedOperationException(" not supported ");
        }

        @Override
        public String toString() {
            return "cacheName:" + this.cacheName;
        }
    }
}
