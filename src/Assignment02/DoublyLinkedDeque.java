package Assignment02;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
/**
 * @author Shane Birdsall
 * @param <E>
 */
public class DoublyLinkedDeque<E extends Comparable<E>> implements DequeADT<E> {

	private Node<E> first,last;
	private int listSize;

	public DoublyLinkedDeque() {
		this(null, null);
	}
	public DoublyLinkedDeque(Node<E> first, Node<E> last) {
		this.first = first;
		this.last = last;
		this.listSize = 0;
	}

	public void enqueueFront(E element) {
		if(first == null) { // Case when queue is empty
			first = new Node<>(element);
			last = first; // Only one node - it is both the first and last in this scenario
		} else {	// adds one element to the front of this queue
			Node<E> newNode = new Node<>(element);
			newNode.next = first;
			first.previous = newNode;
			first = newNode;
		}
		listSize++;
	}
	public void enqueueRear(E element) {
		if(last == null) { // Case when queue is empty
			enqueueFront(element); // Empty queue scenario, rear being no different than front.
		} else { // adds one element to the rear of this queue
			Node<E> newNode = new Node<>(element);
			last.next = newNode;
			newNode.previous = last;
			last = newNode;
		}
		listSize++;
	}
	public E dequeueFront() throws NoSuchElementException { 
		if(first == null) {
			throw new NoSuchElementException(); // Throw exception when there is no first node
		}
		E  data = first.data; // removes and returns the front element of the queue
		if(first.next != null) {
			first.next.previous = null; // Dereference from queue
		}
		first = first.next; // Change what is now the first Node
		listSize--;
		return data; 
	}
	public E dequeueRear() throws NoSuchElementException {
		if(last == null) {
			throw new NoSuchElementException(); // Throw exception when there is no last node
		}  // removes and returns the rear element of the queue
		E  data = last.data;
		if(last.previous != null) {
			last.previous.next = null; // Dereference from queue
		}
		last = last.previous; // Change what is now the last Node
		listSize--;
		return data;
	}
	public E first() throws NoSuchElementException {
		if(first == null) {
			throw new NoSuchElementException(); // Throw exception when there is no first node
		} else 
			return first.data; // returns without removing the front element of this queue
	}
	public E last() throws NoSuchElementException {
		if(last == null) {
			throw new NoSuchElementException(); // Throw exception when there is no last node
		} else 
			return last.data; // returns without removing the rear element of this queue
	}
	public boolean isEmpty() {
		if(first == null) {
			return true;  // returns true if this queue contains no elements
		} else return false;
	}
	public int size() {
		return this.listSize; // returns the number of elements in this queue
	}
	public Iterator<E> iterator(boolean fromFront) {
		return new DoublyLinkedDequeIterator(fromFront); //returns an Iterator which iterates from front or back
	}
	public void clear() { //clears the deque completely
		this.listSize = 0;
		first = null;
		last = null;	// No reference to the list, let java garbage collection clean it up.
	}
	public void quickSort() {
		quickSort(this.first, this.last); // Initial quickSort call
	}
	private void quickSort(Node<E> pivot, Node<E> end) {
		Node<E> current = pivot, last = end, first = pivot; // Set current to the pivot, later to be iterated. Initially last is end and first is pivot.
		Node<E> tempNext, tempPrevious; // temp nodes for use with switching node links.
		if(current != end) { // base case -> Don't want to continue if the 'partition of the deque' is only one node -> It is sorted already!
			current = current.next; // Iterate
			while(current != null) { // Stop looping when end of list reached	
				tempNext = current.next; // Store next and previous nodes in temp Nodes. Just helps make sure no links are lost. 
				tempPrevious = current.previous; // Could be done with only one temp but ehh.
				if(current.data.compareTo(pivot.data) < 0) { // Only move the node if it is smaller than the pivot, otherwise leave it in place.
					if(current == last) { // case when the last node is shifted
						last = last.previous; // Keep track of last node for current 'partition of the deque'
					}
					if(first.previous != null) { // If it's null then don't do this --> Would cause a null pointer exception (i.e null.next)
						first.previous.next = current;	// Link what is before first node before the current node (because it won't always be null)
					}
					current.previous = first.previous;
					first.previous = current; // Link old first and new first/current.
					current.next = first;

					if(tempPrevious != null) {
						tempPrevious.next = tempNext; // Adjust links to remove current from its position (previous now links to next)
					}
					if(tempNext != null) {
						tempNext.previous = tempPrevious; // Adjust links to remove current from its position (next now links to previous)
					}
					first = current; // current node is now the first in the list
				}	
				current = tempNext; // Essentially iterating through
			}
			this.first = first; // Make loop shorter
			while(this.first.previous != null) {	// Adjust the first node (this.first, not first for current function)
				this.first = this.first.previous; // Iterate
			}
			this.last = last; // Make loop shorter
			while(this.last.next != null) { // Adjust the last node (this.last, not last for current function)
				this.last = this.last.next; // Iterate
			}
			//Recursion 
			if(pivot.previous != null && first != pivot) {	
				// stop null pointer exceptions && stop case of the first element being the pivot (in which case sort right instead)
				quickSort(first, pivot.previous); // Sort left of pivot
			}
			if(pivot.next != null) { // stop null pointer exceptions, no other special cases for sorting to the right.
				quickSort(pivot.next, last); // Sort right of pivot
			}
		}
	}
	public String toString() {
		String deque = "";
		Iterator<E> myIterator = iterator(true);
		while(myIterator.hasNext()) {
			deque += ("["+myIterator.next()+"]");
		}
		return deque;
	}
	@SuppressWarnings("hiding")
	public class Node<E> {	// Node inner-class
		public Node(E data) {
			this.data = data;
		}
		private Node<E> next, previous;
		private E data;
		public String toString() { return this.data.toString(); }
	}

	public class DoublyLinkedDequeIterator implements Iterator<E> { // Iterator inner-class
		private Node<E> currentNode;
		private boolean fromFront;

		public DoublyLinkedDequeIterator(boolean fromFront) {
			if(fromFront)
				currentNode = first;
			else 
				currentNode = last;
			this.fromFront = fromFront;
		}
		public boolean hasNext() {
			if(fromFront) {
				if(currentNode != null)
					return true;
			} else {
				if(currentNode != null)
					return true;
			}
			return false;
		}
		public E next() {
			E data = currentNode.data;
			if(fromFront)
				currentNode = currentNode.next;
			else
				currentNode = currentNode.previous; 
			return data;
		}
	}

	public static void main(String[] args) {
		DoublyLinkedDeque<String> stringDeque = new DoublyLinkedDeque<>();
		System.out.println("Shanes Testing:");
		System.out.println("Enqueue A, then B, then C from the front");
		stringDeque.enqueueFront("A");
		stringDeque.enqueueFront("B");
		stringDeque.enqueueFront("C");
		System.out.println("Result: " +stringDeque);

		System.out.println("Now dequeue once from the front");
		stringDeque.dequeueFront();
		System.out.println("Result: " +stringDeque);

		System.out.println("Now enqueue D, then E, then F from the rear");
		stringDeque.enqueueRear("D");
		stringDeque.enqueueRear("E");
		stringDeque.enqueueRear("F");
		System.out.println("Result: " +stringDeque);

		System.out.println("Now dequeue once from the rear");
		stringDeque.dequeueRear();
		System.out.println("Result: " +stringDeque);

		System.out.println("Now Iterate from front to back");
		Iterator<String> frontToBack = stringDeque.iterator(true);
		while(frontToBack.hasNext()) {
			System.out.print("[" +frontToBack.next() +"]");
		} System.out.println();

		System.out.println("Now Iterate from back to front");
		Iterator<String> backToFront = stringDeque.iterator(false);
		while(backToFront.hasNext()) {
			System.out.print("[" +backToFront.next() +"]");
		} System.out.println();

		System.out.println("What is the first element? Result: " +stringDeque.first());
		System.out.println("What is the last element? Result: " +stringDeque.last());

		System.out.println("Is the list empty? Result: " +stringDeque.isEmpty());
		System.out.println("What is the size of the deque? Result: " +stringDeque.size());

		System.out.println("Now clear the deque");
		stringDeque.clear();
		System.out.println("Result: "+stringDeque +"\nIs the list empty? " +stringDeque.isEmpty() +"\nSize is now: " +stringDeque.size());

		System.out.println("\nNow lets test the quicksort...");
		System.out.println("First lets quicksort some letters...");
		stringDeque.enqueueFront("A");
		stringDeque.enqueueFront("D");
		stringDeque.enqueueFront("F");
		stringDeque.enqueueFront("B");
		stringDeque.enqueueFront("X");
		stringDeque.enqueueFront("T");
		stringDeque.enqueueFront("Z");
		System.out.println("Here is the deque of letters: " +stringDeque);
		stringDeque.quickSort();
		System.out.println("Result after quicksorting: "+stringDeque);

		System.out.println("Now lets extend this test by creating three random deques and quick sorting them...");
		DoublyLinkedDeque<Integer> b = new DoublyLinkedDeque<>();
		Random rand = new Random();
		for(int i = 0; i < 3; i++) {
			System.out.println("----Random Deque #"+(i+1)+"----");
			for(int j = 0; j < 20; j++) {
				b.enqueueFront(rand.nextInt(100));
			}
			System.out.println("Random Deque: " +b);
			System.out.println("Front: " +b.first.data +" , Last: " +b.last.data);
			b.quickSort();
			System.out.println("Random Deque after Quicksort: " +b);
			System.out.println("Front: " +b.first.data +" , Last: " +b.last.data);
			b.quickSort();
		}
		System.out.println("Final quicksort test: Quicksort an already sorted deque");
		System.out.println("Quicksorted Deque: " +b);
		b.quickSort();
		System.out.println("Quicksorted Deque after another quicksort: " +b);
	}
}


