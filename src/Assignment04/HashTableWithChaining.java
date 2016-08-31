package Assignment04;

public class HashTableWithChaining<E> {
	private Node<E>[] objects;
	private int count;
	
	@SuppressWarnings("unchecked")
	public HashTableWithChaining(int size) {
		objects = (Node<E>[]) new Node[size];
	}
	
	public void add(E object) {
		if(add(objects, object)) {
			count++;
		}
		if(count > (size()*0.75f)) {
			expand();
		}
	}
	
	private boolean add(Node<E>[] objects, E object) {
		int hashCode = object.hashCode()%objects.length;
		Node<E> node = new Node<E>(object);
		if(objects[hashCode] == null) {
			objects[hashCode] = node;
			return true;
		} else {
			Node<E> current = objects[hashCode];
			while(current.next != null) {
				current = current.next;
			}
			current.next = node;
			return false;
		}
	}
	@SuppressWarnings("unchecked")
	private void expand() {
		Node<E>[] newHashTable = (Node<E>[]) new Node[size()*2];
		for(Node<E> i : objects) {
			if(i != null) {
				Node<E> current = i;
				while(current != null) {
					add(newHashTable, current.data);
					current = current.next;
				}
			}
		}
		objects = newHashTable;
	}
	public boolean remove(E object) {
		int hashCode = object.hashCode()%size();
		if(contains(object)) {
			Node<E> current = objects[hashCode];
			Node<E> previous = null;
			boolean loop = true;
			while(current != null && loop) {
				if(current.data == object) {
					if(previous == null) { // 1st in linked list
						objects[hashCode] = objects[hashCode].next;
					} else {
						previous.next = current.next;
						loop = false;
					}
				} else 
					previous = current;
					current = current.next;
			}
			if(objects[hashCode] == null) {
				count--;
			}
			return true;
		}
		return false;
	}
	
	public boolean contains(E object) {
		int hashCode = object.hashCode()%size();
		if(objects[hashCode] != null) {
			Node<E> current = objects[hashCode];
			while(current != null) {
				if(current.data == object) {
					return true;
				} else 
					current = current.next;
			}
		}
		return false;
	}
	
	public int size() {
		return objects.length;
	}
	
	public String toString() {
		String output = "";
		for(Node<E> i : objects) {
			if(i != null) {
				Node<E> current = i;
				while(current != null) {
					output += ("[" + current + "] ");
					current = current.next;
				}
			} else {
				output += "[null] ";
			}
			output += "\n";
		}
		return output;
	}
	
	public static void main(String[] args) {
		System.out.println("Creating a Hash Table that can hold 10 elements..");
		HashTableWithChaining<Person> hashTable = new HashTableWithChaining<Person>(10);
		System.out.println("1) Test the add method. Add 'Shane' Person");
		Person shane = new Person("Shane");
		hashTable.add(shane);
		Person rob = new Person("Robert");
		System.out.println("Result: " + hashTable);
		System.out.println("2) Test the remove method, remove Robert: " + hashTable.remove(rob));
		System.out.println("Now add Robert..");
		hashTable.add(rob);
		System.out.println("Result: " + hashTable);
		System.out.println("Remove Shane : " + hashTable.remove(shane));
		System.out.println("Result: " + hashTable);	
		System.out.println("3) Test contains method: Contains Robert? " + hashTable.contains(rob) + ", Contains Shane? " + hashTable.contains(shane));
		System.out.println("4) Test Chaining: Add boRert (same as Robert), add 3 different Shane's");
		hashTable.add(new Person("boRert"));
		hashTable.add(shane);
		hashTable.add(new Person("Shane"));
		Person s = new Person("Shane");
		hashTable.add(s);
		System.out.println("Result: " + hashTable);
		System.out.println("Remove the last Shane that was added (have reference to it) AND Remove Robert");
		hashTable.remove(s);
		hashTable.remove(rob);
		System.out.println("Result: " + hashTable);
		System.out.println("5) Test load factor... Add 5 more people");
		hashTable.add(new Person("Jack"));
		hashTable.add(new Person("Craig"));
		hashTable.add(new Person("Xavier"));
		hashTable.add(new Person("Harry"));
		hashTable.add(new Person("Damion"));
		System.out.println("Result: " + hashTable);
		System.out.println("Add one more to force capacity expand..");
		hashTable.add(new Person("Pablo"));
		System.out.println("Result: " + hashTable);
	}
	@SuppressWarnings("hiding")
	public class Node<E> {	// Node inner-class
		private Node<E> next;
		private E data;
		public Node(E data) {
			this.data = data;
		}
		public String toString() { return data.toString(); }
	}
}
