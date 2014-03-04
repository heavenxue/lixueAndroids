package com.lixueandroid.imgloader;

import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * {@link LinkedBlockingDeque} using 后进先出算法
 * 
 * @author liuxe
 * @since 1.6.3
 */
public class LIFOLinkedBlockingDeque<T> extends LinkedBlockingDeque<T> {

	private static final long serialVersionUID = -4114786347960826192L;

	
	/**
	 * 指定的元素插入此双端队列的前面，如果有可能立即这样做不违反
	 *容量的限制，返回<TT>真</ TT>成功后和<tt>的假</ P>如果没有空间，目前
	 *可用。当使用有容量限制的双端队列，这种方法通常优于{@＃addFirst
	 * addFirst}方法，它可能无法插入元素，而只是抛出一个异常。
	 * 
	 * @param e
	 *            the element to add
	 * @throws ClassCastException
	 *             {@inheritDoc}
	 * @throws NullPointerException
	 *             if the specified element is null
	 * @throws IllegalArgumentException
	 *             {@inheritDoc}
	 */
	@Override
	public boolean offer(T e) {
		return super.offerFirst(e);
	}

	/**
	 * Retrieves and removes the first element of this deque. This method differs from {@link #pollFirst pollFirst} only
	 * in that it throws an exception if this deque is empty.
	 * 
	 * @return the head of this deque
	 * @throws NoSuchElementException
	 *             if this deque is empty
	 */
	@Override
	public T remove() {
		return super.removeFirst();
	}
}