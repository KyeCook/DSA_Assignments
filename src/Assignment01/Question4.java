package Assignment01;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
/**
 * @author Shane Birdsall
 * Student ID: 14870204
 * 
*	Asymptotic complexity of each method:
*		1. getRandom() - O(1), ArrayList uses an Array so random access is very fast. 
*			Accessing a random element using a random index is O(1), or constant due to knowing where in the list that specific index is.
*			
*			If instead of ArrayList I used LinkedList then the asymptotic complexity of accessing/getting a random index would be O(n). 
*			This is due to having to iterate through each element in the list to get to the n'th element.
*
*		2. removeRandom() - Before removing a element from a random index, that index must first be found. As discussed above accessing an index is O(1). 
*			After finding the index and removing the element, all elements to the right must be shift to the left.
*			(Index -= 1 for each element with an index higher than the randomly generated index). 
*			This can result in O(n) if the first element is the one to be removed, due to the entire list having to be shifted to the left.
*			Therefore removeRandom is O(n).
*
*			If instead of ArrayList I used LinkedList then it would seem the asymptotic complexity of removing an element at a random index is O(1) 
*			because it does not need to decrement index values like the ArrayList. However an element can not be removed until it is found, as previously 
*			discussed access time for a LinkedList is O(n). So in fact the Asymptotic complexity is still O(n).
*		
*		3. insertRandom() - Similar to removing, when inserting at a random index that index must first be found. Once again this is O(1).
*			In order to add the element and retain the list, all elements to the right of the randomly chosen element must be shift to the right. 
*			(Index += 1 for each element with an index higher than the randomly generated index). 
*			Similarly to removing this can result in O(n) if the first element (index 0) is the index for the element to be inserted, due to the 
*			entire list having to be shifted to the right. If the size of the Array is not large enough, it will be copied into a new array containing 
*			the original data set and the new element, an operation which is still O(n).
*
*			If instead of ArrayList I used LinkedList then it would seem the asymptotic complexity of adding an element at a random index is O(1) 
*			because it does not need to increment index values like the ArrayList or worry about the list not being big enough. However an element 
*			can not be added to a index until that index is found, as previously discussed access time for a LinkedList is O(n). 
*			So in fact the Asymptotic complexity is still O(n).
*
*	Overall: If the random index generated is a high number then using an ArrayList will be more efficient. This is due to finding the index being a O(1) operation.
*			 Having a high number decreases the amount of 'shifts' needed in the array, therefore decreasing runtime. A high index paired with a LinkedList however
*			 will be very slow, this is because we need to iterate from 0 to the generated index, having a high number means a lot of iterations. 
*			 Which leads to when LinkedList would be preferred, this is of course the opposite. If the randomly generated index is close to 0, or a low number.
*			 This is because finding the index will be O(1) if the generated number is 0, removing, or adding then also being O(1), making the whole operation O(1).
*
*			 The above can be represented as follows: ArrayList efficiency:  O(n - r)		Note: Only applies for adding and removing, finding is always O(1).
*													  LinkedList efficiency: O(1 + r)		Note: Applies for finding which effects adding and removing.
*			 Where r = Randomly generated number.
*			 
* @author Shane Birdsall
*/
public class Question4<E> extends ArrayList<E> implements RandomObtainable<E> {
	private static final long serialVersionUID = 1L;
	Random rand = new Random();

	public E getRandom() {
		if(this.isEmpty()) {
			throw new NoSuchElementException();
		} else 
			return super.get(rand.nextInt(this.size()));
	}
	public boolean removeRandom() {
		if(!this.isEmpty()) {
			this.remove(rand.nextInt(this.size()));
			return true;
		} else 
			return false; // No elements to remove
	}
	public boolean insertRandom(E element) {
		// If the ArrayList is empty it is not logical to insert an element randomly because there is only one possible index to add to.
		// Instead the add(E element) method should be called for the first element. This method will return false if it is used on an empty List.
		if(this.size() > 0) { 
			this.add(rand.nextInt(this.size()), element);
			return true;
		} else
			return false; 
	}
	
	public static void main(String[] args) {
		Question4<String> stringList = new Question4<>();

		stringList.add("1st String");
		stringList.add("2nd String");
		stringList.add("3rd String");
		stringList.add("4th String");
		stringList.add("5th String");
		System.out.println("Five ordered Strings: " + stringList);
		
		System.out.println("Adding one string randomly...");
		stringList.insertRandom("Random String");
		System.out.println("New List: " + stringList);
		
		System.out.println("Return one string randomly..." + stringList.getRandom());
		stringList.removeRandom();
		System.out.println("Removing one string randomly... \nNew List: " + stringList);
		System.out.println("Return three random strings: 1. " + stringList.getRandom() + " 2. " + stringList.getRandom() + " 3. " + stringList.getRandom());
		
		// Larger List to test (10 million)
		System.out.println("Creating list of 10 million numbers...");
		Question4<Integer> integerList = new Question4<>();
		for(int i = 0; i < 10000000; i++) {
			integerList.add(i);
		}
		
		long startTime = System.currentTimeMillis();	
		System.out.println("Getting random number from list of 50 million...");
		System.out.println(integerList.getRandom()); // Very fast O(1)
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
		System.out.println("Adding number 8 to list at a random position...");
		startTime = System.currentTimeMillis();
		integerList.insertRandom(8);				// O(n)
		endTime = System.currentTimeMillis();
		System.out.println("New List size: " + integerList.size());
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
		System.out.println("Removing a number randomly...");
		startTime = System.currentTimeMillis();
		integerList.removeRandom();			// O(n) 
		endTime = System.currentTimeMillis();
		System.out.println("Number removed, new List size: " + integerList.size());
		System.out.println("New List size: " + integerList.size());
		System.out.println("That took " + (endTime - startTime) + " milliseconds");	
	}		
}
