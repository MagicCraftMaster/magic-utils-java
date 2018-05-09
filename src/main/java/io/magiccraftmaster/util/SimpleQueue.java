package io.magiccraftmaster.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A simple implementation of {@link Queue}
 */
@SuppressWarnings(value = {"unused", "WeakerAccess"})
public final class SimpleQueue<t> extends ArrayList<t> implements Queue<t> {
	private int cap;

	/**
	 * Creates the default queue without a cap
	 */
	public SimpleQueue() {}

	/**
	 * Creates the default queue with a cap
	 * @param cap the maximum number of elements able to be enqueued
	 */
	@SuppressWarnings("unused")
	public SimpleQueue(int cap) {
		this.cap = cap;
	}

	/**
	 * Creates the default queue with an initial capacity and cap
	 * @param initialCapacity the initial capacity of the queue
	 * @param cap the maximum number of elements able to be enqueued
	 */
	@SuppressWarnings("unused")
	public SimpleQueue(int initialCapacity, int cap) {
		super(initialCapacity);
		this.cap = cap;
	}

	/**
	 * Creates the default queue from a collection with a cap
	 * @param c the collection to inherit from
	 * @param cap the maximum number of elements able to be enqueued
	 */
	@SuppressWarnings("unused")
	public SimpleQueue(Collection<? extends t> c, int cap) {
		super(c);
		this.cap = cap;
	}

	/**
	 * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param t the element to add
     * @return {@code true} if the element was added to this queue, else
     *         {@code false}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
	 */
	@Override
	public boolean offer(t t) {
		return (cap == 0 || size() < cap) && add(t);
	}

	/**
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link #poll poll} only in that it throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
	@Override
	public t remove() {
		if (isEmpty()) throw new NoSuchElementException();
		t out = get(0);
		remove(out);
		return out;
	}

	/**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
	@Override
	public t poll() {
		if (isEmpty()) return null;
		t out = get(0);
		remove(out);
		return out;
	}

	/**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from {@link #peek peek} only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
	@Override
	public t element() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(0);
	}

	/**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
	@Override
	public t peek() {
		if (isEmpty()) return null;
		return get(0);
	}
}
