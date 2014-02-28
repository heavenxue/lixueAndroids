package com.lixueandroid.imgloader;

import java.util.Collection;

/**
 *缓存接口
 * 
 * @author lixue
 * @since 1.0.0
 */
public interface MemoryCacheAware<K, V> {
	/**
	 * 以键的方式给缓存放入值
	 * 
	 * @return <b>true：</b> -如果值放缓存成功 <b>false：</b> - 如果值未放成功
	 */
	boolean put(K key, V value);

	/** 返回主键的值. 如果没有键值，那么将返回null。 */
	V get(K key);

	/** 移除键 */
	void remove(K key);

	/**返回缓存的所有键 */
	Collection<K> keys();

	/** 移除缓存中所有键 */
	void clear();
}