package Assignment02;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author sehall
 * @param <E>
 */
public interface DequeADT<E>  {
	// adds one element to the rear of this queue
	void enqueueRear(E element);
	// removes and returns the front element of the queue
	E dequeueFront() throws NoSuchElementException;
	// returns without removing the front element of this queue
	E first() throws NoSuchElementException;
	// adds one element to the front of this queue
	void enqueueFront(E element);
	// removes and returns the rear element of the queue
	E dequeueRear() throws NoSuchElementException;
	// returns without removing the rear element of this queue
	E last() throws NoSuchElementException;
	// returns true if this queue contains no elements
	boolean isEmpty();
	// returns the number of elements in this queue
	int size();
	//returns an Iterator which iterates from front or back
	Iterator<E> iterator(boolean fromFront);
	//clears the deque completely
	void clear();
}
