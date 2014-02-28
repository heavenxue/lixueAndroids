package com.lixueandroid.imgloader;

import java.util.Collection;
import java.util.Comparator;


/**
 * 缓存提供了特殊的功能：一些不同的键被视为等同于（using {@link Comparator comparator}）。而当你尝试把一些值缓存键，以便条目“等于”键将之前从缓存中删除。<br/>
 * <b>注意:</b> 用于内部需求。通常情况下，你不需要使用这个类.
 * 
 * @author lixue
 * @since 1.0.0
 */
public class FuzzyKeyMemoryCache<K, V> implements MemoryCacheAware<K, V> {

	private final MemoryCacheAware<K, V> cache;
	private final Comparator<K> keyComparator;

	public FuzzyKeyMemoryCache(MemoryCacheAware<K, V> cache, Comparator<K> keyComparator) {
		this.cache = cache;
		this.keyComparator = keyComparator;
	}

	@Override
	public boolean put(K key, V value) {
		// Search equal key and remove this entry
		synchronized (cache) {
			K keyToRemove = null;
			for (K cacheKey : cache.keys()) {
				if (keyComparator.compare(key, cacheKey) == 0) {
					keyToRemove = cacheKey;
					break;
				}
			}
			if (keyToRemove != null) {
				cache.remove(keyToRemove);
			}
		}
		return cache.put(key, value);
	}

	@Override
	public V get(K key) {
		return cache.get(key);
	}

	@Override
	public void remove(K key) {
		cache.remove(key);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Collection<K> keys() {
		return cache.keys();
	}
}
