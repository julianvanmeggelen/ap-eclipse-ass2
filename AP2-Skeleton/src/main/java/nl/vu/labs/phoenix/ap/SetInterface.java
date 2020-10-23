package nl.vu.labs.phoenix.ap;

/** @elements
 *    objects of type T
 *  @structure
 *    no structure
 *  @domain
 *    The elements in the set are unordered.
 *    All collections of elements of type T are valid values for a set.
 *  @constructor
 *    There is a default constructor that creates an empty list and a copy constructor that copies a constructor.
 *  @precondition
 *    --
 *  @postcondition
 *    The new List-object is the empty list
 *
 **/
public interface SetInterface<T extends Comparable<T>> {


	boolean add(T t);/*
		 PRE - 
		 POST - true: - element was inserted
		 		false: - element was already present 
	
	*/T get();/*
		 PRE - 
		 POST - An element T has been returned.
	
	*/boolean remove(T t);/*
		 PRE - 
		 POST - true: -t is removed from the set
		 	  - false -t was not in the set
	
	*/int size();/*
		PRE - 
		POST - the number of elements in the set has been returned

	*/SetInterface<T> copy();/*
		PRE - 
		POST - a copy of the set has been returned


	/*
	 * [3] Methods for set operations 
	 * 	   -- Add methods to perform the 4 basic set operations 
	 * 		  (union, intersection, difference, symmetric difference)
	 */

	SetInterface<T> union(SetInterface<T> set2);/*
		PRE - 
		POST - the union set of this and set2 has been returned. is there a max amount of elements in a set?

	*/SetInterface<T> difference(SetInterface<T> set2);/*
		PRE - 
		POST - the difference of this and set2 has been returned.

	*/SetInterface<T> intersection(SetInterface<T> set2);/*
		PRE - 
		POST - the intersection of this and set2 has been returned.

	*/SetInterface<T> symmetricDifference(SetInterface<T> set2);/*
		PRE - 
		POST -the symmetric differnce of this and set2 had been returned.
	*/

	
	// your code here
	
	
	/* 
	 * [4] Add anything else you think belongs to this interface 
	 */
	
	 boolean contains(T t);/*
		 PRE-
		 POST- 	true: the set contains an element equal to t, as per compareTo
				 false: the set does not contain an element equal to t
	*/
	
}
