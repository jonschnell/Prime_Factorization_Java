package edu.iastate.cs228.hw3;

/**
 *  
 * @author Jonathon Schnell
 * @version 1.0
 * @since 4-1-2020
 * COM S 228
 * homework3
 *
 */

import java.util.ListIterator;

public class PrimeFactorization implements Iterable<PrimeFactor> {
	private static final long OVERFLOW = -1;
	private long value; // the factored integer
						// it is set to OVERFLOW when the number is greater than 2^63-1, the
						// largest number representable by the type long.

	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;

	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;

	private int size; // number of distinct prime factors

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor constructs an empty list to represent the number 1.
	 * 
	 * Combined with the add() method, it can be used to create a prime
	 * factorization.
	 */
	public PrimeFactorization() {
		// make a head and tail
		this.head = new Node(null);
		this.tail = new Node(null);
		// link them
		head.next = tail;
		tail.previous = head;
		// set size and value
		size = 0;
		value = 1;
	}

	/**
	 * Obtains the prime factorization of n and creates a doubly linked list to
	 * store the result. Follows the direct search factorization algorithm in
	 * Section 1.2 of the project description.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		// make a head and tail
		head = new Node(null);
		tail = new Node(null);
		// link them
		head.next = tail;
		tail.previous = head;
		// set size and value
		size = 0;
		value = n;

		long m = n;
		int mult = 0;
		for (int d = 2; d * d <= n; d++) {
			mult = 0;
			// while the division is sucessful
			while (m % d == 0) {
				// divide m by d
				m = m / d;
				// incriment multi
				mult++;
			}
			// add the object to the list
			this.add(d, mult);
			// if (d * d > m) {
			// break;
			// }
		}
	}

	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in
	 * the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf) {
		// PrimeFactorization newPf = new PrimeFactorization();
		// make a node the points to the head.next in pf
		Node temp = pf.head.next;
		// this.value = pf.value();
		// add all items to list
		while (temp.next != null) {
			this.add(temp.next.pFactor.prime, temp.next.pFactor.multiplicity);
			updateValue();
			temp = temp.next;
		}
	}

	/**
	 * Constructs a factorization from an array of prime factors. Useful when the
	 * number is too large to be represented even as a long integer.
	 * 
	 * @param pflist
	 */
	public PrimeFactorization(PrimeFactor[] pfList) {
		// for all the pf in pf list
		for (int i = 1; i < pfList.length; i++) {
			// add to linked list
			this.add(pfList[i].prime, pfList[i].multiplicity);
		}
	}

	// --------------
	// Primality Test
	// --------------

	/**
	 * Test if a number is a prime or not. Check iteratively from 2 to the largest
	 * integer not exceeding the square root of n to see if it divides n.
	 * 
	 * @param n
	 * @return true if n is a prime false otherwise
	 */
	public static boolean isPrime(long n) {
		if (n == 1) {
			return false;
		}
		// search for prime
		// using i * i >= n might be quicker
		for (int i = 2; i >= Math.sqrt(n); i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	// ---------------------------
	// Multiplication and Division
	// ---------------------------

	/**
	 * Multiplies the integer v represented by this object with another number n.
	 * Note that v may be too large (in which case this.value == OVERFLOW). You can
	 * do this in one loop: Factor n and traverse the doubly linked list
	 * simultaneously. For details refer to Section 3.1 in the project description.
	 * Store the prime factorization of the product. Update value and size.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException {
		if (this.value == OVERFLOW) {
			throw new IllegalArgumentException();
		}
// TODO This should work to the project specifications but iterator points to null things
		PrimeFactorizationIterator iter = this.iterator();

		for (int i = 2; i * i <= n; i++) {
			int m = 0;

			while (n % i == 0) {
				n = n / i;
				m++;
			}

			if (iter.next().prime == i) {
				iter.next().multiplicity += m;
			}
			add(i, m);

		}
	}

	/**
	 * Multiplies the represented integer v with another number in the factorization
	 * form. Traverse both linked lists and store the result in this list object.
	 * See Section 3.1 in the project description for details of algorithm.
	 * 
	 * @param pf
	 */
	public void multiply(PrimeFactorization pf) {
// never got to complete due to iterator pointing null
		PrimeFactorizationIterator iterPf = pf.iterator();

		for (int i = 0; i >= pf.size; i++) {
			this.add(iterPf.cursor.pFactor.prime, iterPf.cursor.pFactor.multiplicity);

			iterPf.next();
		}

	}

	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2) {
//	TODO i think this method is pretty close close but i mad issues with doing while (iterPf1.hasNext())
//	throwing a null pointer. Ive looked over iterator a couple time so i think its all right
//	pretty sure im initializing it wrong
		PrimeFactorizationIterator iterPf1 = pf1.iterator();
		PrimeFactorizationIterator iterPf2 = pf2.iterator();

		PrimeFactorization newPf = new PrimeFactorization();

		// while (iterPf1.hasNext()) {
		for (int i = 0; i >= pf1.size; i++) {
			newPf.add(iterPf1.cursor.pFactor.prime, iterPf1.cursor.pFactor.multiplicity);

			iterPf1.next();
		}

		for (int j = 0; j >= pf2.size; j++) {
			newPf.add(iterPf2.cursor.pFactor.prime, iterPf2.cursor.pFactor.multiplicity);

			iterPf2.next();

		}

		return newPf;

	}

	/**
	 * Divides the represented integer v by n. Make updates to the list, value, size
	 * if divisible. No update otherwise. Refer to Section 3.2 in the project
	 * description for details.
	 * 
	 * @param n
	 * @return true if divisible false if not divisible
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException {
		if (this.value() != -1 && this.value() < n) {
			return false;
		}
		PrimeFactorization newPf = new PrimeFactorization(n);
		dividedBy(newPf);
		return true;
	}

	/**
	 * Division where the divisor is represented in the factorization form. Update
	 * the linked list of this object accordingly by removing those nodes housing
	 * prime factors that disappear after the division. No update if this number is
	 * not divisible by pf. Algorithm details are given in Section 3.2.
	 * 
	 * @param pf
	 * @return true if divisible by pf false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf) {
		if ((this.value != -1 && pf.value != -1 && this.value < pf.value) || (this.value != -1 && pf.value == -1)) {
			return false;
		} else if (this.value == pf.value()) {
			clearList();
			value = 1;
			return true;
		}

		PrimeFactorization pfClone = null;

		for (int i = 0; i > this.size; i++) {

		}

		PrimeFactorizationIterator iterPf = pf.iterator();
		PrimeFactorizationIterator iterCopy = pfClone.iterator();

		// TODO again ran into issues with iterator.
		return false;
	}

	/**
	 * Divide the integer represented by the object pf1 by that represented by the
	 * object pf2. Return a new object representing the quotient if divisible. Do
	 * not make changes to pf1 and pf2. No update if the first number is not
	 * divisible by the second one.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible null
	 *         otherwise
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2) {
		// TODO
		return null;
	}

	// -----------------------
	// Greatest Common Divisor
	// -----------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and
	 * an input integer n. Returns the result as a PrimeFactor object. Calls the
	 * method Euclidean() if this.value != OVERFLOW.
	 * 
	 * It is more efficient to factorize the gcd than n, which can be much greater.
	 * 
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		if (this.value != OVERFLOW) {
			Euclidean(n, n);
		}
		// TODO
		return null;
	}

	/**
	 * Implements the Euclidean algorithm to compute the gcd of two natural numbers
	 * m and n. The algorithm is described in Section 4.1 of the project
	 * description.
	 * 
	 * @param m
	 * @param n
	 * @return gcd of m and n.
	 * @throws IllegalArgumentException if m < 1 or n < 1
	 */
	public static long Euclidean(long m, long n) throws IllegalArgumentException {
		if (m < 1 || n < 1) {
			throw new IllegalArgumentException();
		}

		return 0;

	}

	/**
	 * Computes the gcd of the values represented by this object and pf by
	 * traversing the two lists. No direct computation involving value and pf.value.
	 * Refer to Section 4.2 in the project description on how to proceed.
	 * 
	 * @param pf
	 * @return prime factorization of the gcd
	 */
	public PrimeFactorization gcd(PrimeFactorization pf) {
		// TODO
		return null;
	}

	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and
	 *         pf2
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2) {
		// TODO
		return null;
	}

	// ------------
	// List Methods
	// ------------

	/**
	 * Traverses the list to determine if p is a prime factor.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @return true if p is a prime factor of the number v represented by this
	 *         linked list false otherwise
	 * @throws IllegalArgumentException if p is not a prime
	 */
	public boolean containsPrimeFactor(int p) throws IllegalArgumentException {
		Node temp = head.next;
		//iterate list looking for a match
		//works GREAT :)
		while (temp.next != null) {
			if (temp.pFactor.prime == p) {
				return true;
			}
			temp = temp.next;
		}
		return false;
	}

	// The next two methods ought to be private but are made public for testing
	// purpose. Keep
	// them public

//	This was an attempt to use the class PrimeFactorizationIterator
//	iterator and implementation of iterator
//	The issue was i was unable to resolve was to get meaningful data for the latter comparisons
//	such as to compare the prime p with what is already in the list and position the new prime
//	at the correct index. this add method adds nothing to the list! its borked.

//		if (m >= 1) {
//
//			PrimeFactorizationIterator iter = iterator();
//			iter = iterator();
//			
//			while (iter.cursor.next != null && size != 0) {
//				if (iter.cursor.previous.next.pFactor.prime == p) {
//					iter.cursor.pFactor.multiplicity += m;
//					updateValue();
//					return true;
//				}
//				else if (iter.cursor.previous.next.pFactor.prime > p) {
//					Node newNode = new Node(p, m);
//					link(iter.cursor.previous, newNode);
//					link(newNode, iter.cursor.next);
//					size++;
//					updateValue();
//					return true;
//				}else {
//					iter.cursor = iter.cursor.next;
//				}
//
//			}
//
//		}
//		return false;

	/**
	 * Adds a prime factor p of multiplicity m. Search for p in the linked list. If
	 * p is found at a node N, add m to N.multiplicity. Otherwise, create a new node
	 * to store p and m.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p prime
	 * @param m multiplicity
	 * @return true if m >= 1 false if m < 1
	 */
	public boolean add(int p, int m) {
// this is the method that is working for the most part
// i rewrote this method at least 5 times trying to get iterator working
// pls have mercy
		if (m >= 1) {
			Node temp = head.next;
			Node temp2 = head.next;

			for (int i = 1; i <= size; i++) {
				if (temp.pFactor.prime == p) {
					temp.pFactor.multiplicity += m;
					updateValue();
					return true;
				} else {
					temp = temp.next;
				}
			}
			if (!this.containsPrimeFactor(p)) {
				// while (temp2.pFactor.prime > p) {
				// temp2 = temp2.next;

//	This will add any new nodes to the tail
//	instead of to their proper index so it only really works
//	for changing the multiplicity of a prime
				Node temp3 = new Node(p, m);
				link(tail.previous, temp3);
				size++;
				updateValue();
				return true;
			}

		}
		return false;
	}

	/**
	 * Removes m from the multiplicity of a prime p on the linked list. It starts by
	 * searching for p. Returns false if p is not found, and true if p is found. In
	 * the latter case, let N be the node that stores p. If N.multiplicity > m,
	 * subtracts m from N.multiplicity. If N.multiplicity <= m, removes the node N.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @param m
	 * @return true when p is found. false when p is not found.
	 * @throws IllegalArgumentException if m < 1
	 */
	public boolean remove(int p, int m) throws IllegalArgumentException {
		if (m < 1) {
			throw new IllegalArgumentException();
		}
		Node N = head.next;
		for (int i = 1; i <= size; i++) {
			//case to decrement multiplicity
			if (N.pFactor.prime == p) {
				if (N.pFactor.multiplicity > m) {
					N.pFactor.multiplicity -= m;
					updateValue();
					return true;
				//case to remove a node
				} else if (N.pFactor.multiplicity == 1 || N.pFactor.multiplicity == m) {
					//unlink(N);
					link(N.previous, N.next);
					size--;
					updateValue();
					return true;
				}

			}
			N = N.next;
		}
		return false;
	}

	/**
	 * 
	 * @return size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Writes out the list as a factorization in the form of a product. Represents
	 * exponentiation by a caret. For example, if the number is 5814, the returned
	 * string would be printed out as "2 * 3^2 * 17 * 19".
	 */
	@Override
	public String toString() {
		Node temp = head.next;
		String toReturn = "";
		for (int i = 1; i <= size; i++) {
			if (temp.pFactor.multiplicity == 1) {
				toReturn += temp.pFactor.prime;
			} else {
				toReturn += temp.pFactor.prime + "^" + temp.pFactor.multiplicity;
			}
			if (temp.next.pFactor != null) {
				toReturn += " * ";
			}
			temp = temp.next;

		}

		return toReturn;
	}

	// The next three methods are for testing, but you may use them as you like.

	/**
	 * @return true if this PrimeFactorization is representing a value that is too
	 *         large to be within long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if
	 *         valueOverflow()
	 */
	public long value() {
		return value;
	}

	public PrimeFactor[] toArray() {
		PrimeFactor[] arr = new PrimeFactor[size];
		int i = 0;
		for (PrimeFactor pf : this)
			arr[i++] = pf;
		return arr;
	}

	@Override
	public PrimeFactorizationIterator iterator() {
		return new PrimeFactorizationIterator();
	}

	/**
	 * Doubly-linked node type for this class.
	 */
	private class Node {
		public PrimeFactor pFactor; // prime factor
		public Node next;
		public Node previous;

		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node() {
			pFactor = null;
			previous = null;
			next = null;
		}

		/**
		 * Precondition: p is a prime
		 * 
		 * @param p prime number
		 * @param m multiplicity
		 * @throws IllegalArgumentException if m < 1
		 */
		public Node(int p, int m) throws IllegalArgumentException {
			if (m < 1) {
				throw new IllegalArgumentException();
			}
			pFactor = new PrimeFactor(p, m);
			previous = null;
			next = null;
		}

		/**
		 * Constructs a node over a provided PrimeFactor object.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf) {
			pFactor = pf;
			previous = null;
			next = null;
		}

		/**
		 * Printed out in the form: prime + "^" + multiplicity. For instance "2^3".
		 * Also, deal with the case pFactor == null in which a string "dummy" is
		 * returned instead.
		 */
		@Override
		public String toString() {
			if (pFactor == null) {
				return "dummy";
			}
			return this.pFactor.prime + "^" + this.pFactor.multiplicity;
		}
	}

	private class PrimeFactorizationIterator implements ListIterator<PrimeFactor> {
		// Class invariants:
		// 1) logical cursor position is always between cursor.previous and cursor
		// 2) after a call to next(), cursor.previous refers to the node just returned
		// 3) after a call to previous() cursor refers to the node just returned
		// 4) index is always the logical index of node pointed to by cursor

		private Node cursor = head.next;
		private Node pending = null; // node pending for removal
		private int index = 0;

		// other instance variables ...
		private static final int BEHIND = -1;
		private static final int AHEAD = 1;
		private static final int NONE = 0;
		private int direction;

		/**
		 * Default constructor positions the cursor before the smallest prime factor.
		 */
		public PrimeFactorizationIterator() {
			cursor = head.next;
			direction = NONE;
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return cursor != null;
		}

		@Override
		public boolean hasPrevious() {
			return cursor.previous != null;
		}

		@Override
		public PrimeFactor next() {
			pending = cursor;
			cursor = cursor.next;
			direction = AHEAD;
			return pending.pFactor;
		}

		@Override
		public PrimeFactor previous() {
			pending = cursor;
			cursor = cursor.previous;
			direction = BEHIND;
			return pending.pFactor;
		}

		/**
		 * Removes the prime factor returned by next() or previous()
		 * 
		 * @throws IllegalStateException if pending == null
		 */
		@Override
		public void remove() throws IllegalStateException {
			if (pending == null || size == 0) {
				throw new IllegalStateException();
			}
			// unlink pending
			if (pending.previous != null) {
				pending.previous.next = pending.next;
			}
			if (pending.next != null) {
				pending.next.previous = pending.previous;
			}
			// if removing the head, update head reference
			if (pending == head) {
				head = pending.next;
			}
			// decrement size
			--size;
			pending = null;
		}

		/**
		 * Adds a prime factor at the cursor position. The cursor is at a wrong position
		 * in either of the two situations below:
		 * 
		 * a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head.
		 * 
		 * b) pf.prime > cursor.pFactor.prime if cursor != tail.
		 * 
		 * Take into account the possibility that pf.prime == cursor.pFactor.prime.
		 * 
		 * Precondition: pf.prime is a prime.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException if the cursor is at a wrong position.
		 */
		@Override
		public void add(PrimeFactor pf) throws IllegalArgumentException {
			if ((pf == null) || (pf.prime < cursor.previous.pFactor.prime && cursor.previous != head)
					|| (pf.prime > cursor.pFactor.prime && cursor != tail)) {
				throw new IllegalArgumentException();
			} else if (pf.prime == cursor.pFactor.prime) {
				System.out.println("Prime factor already exists");
			}

			Node pfNode = new Node(pf);
			link(cursor, pfNode);
			size++;
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Deprecated
		@Override
		public void set(PrimeFactor pf) {
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
	}

	// --------------
	// Helper methods
	// --------------

	/**
	 * Inserts toAdd into the list after current without updating size.
	 * 
	 * Precondition: current != null, toAdd != null
	 */
	private void link(Node current, Node toAdd) {
		toAdd.previous = current;
		toAdd.next = current.next;
		current.next.previous = toAdd;
		current.next = toAdd;
	}

	/**
	 * Removes toRemove from the list without updating size.
	 */
	private void unlink(Node toRemove) {
		toRemove.previous.next = toRemove.next;
		toRemove.next.previous = toRemove.previous;
	}

	/**
	 * Remove all the nodes in the linked list except the two dummy nodes.
	 * 
	 * Made public for testing purpose. Ought to be private otherwise.
	 */
	public void clearList() {
		head.next = tail;
		tail.previous = head;
		size = 0;
		value = 1;
	}

	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the
	 * represented integer. Use Math.multiply(). If an exception is throw, assign
	 * OVERFLOW to the instance variable value. Otherwise, assign the multiplication
	 * result to the variable.
	 * 
	 */
	private void updateValue() {
		try {
			// this.value = head.next.pFactor.multiplicity * head.next.pFactor.prime;
			Node temp = new Node();
			int tempVal = 0;
			while (temp.next != null) {
				tempVal = Math.multiplyExact(temp.next.pFactor.multiplicity, temp.next.pFactor.prime);
				value = Math.multiplyExact(tempVal, value);
				temp = temp.next;
			}
		}

		catch (ArithmeticException e) {
			value = OVERFLOW;
		}

	}

}
