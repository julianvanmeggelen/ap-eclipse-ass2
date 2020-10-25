package nl.vu.labs.phoenix.ap;

/* [1] Identifier Specification
 * 	   -- Complete the specification for an Identifier interface.
 * 		  See the List interface for inspiration
 */
/* [1] Identifier Specification
 * 	   -- Complete the specification for an Identifier interface.
 * 		  See the List interface for inspiration
 */
public interface IdentifierInterface {
	 /* * * Elements: characters
    * Structure: linear
    * Domain: Objects of type char 

    /*
    * * constructors 
    * *     Identifier(); 
    *          PRE  - 
    *          POST - An empty Identifier-object has been created and its value is undefined.
    * *     Identifier(Identifier identifier); 
    *          PRE  - 
    *          POST - A copy of the Identifier-object has been created and contains a copy of the value of the parameter Identifier. 
	
	
	/* 
	 * [2] Mandatory methods. Make sure you do not modify these!
	 * 	   -- Complete the specifications of these methods
	 */
	
	String value();/*
		PRE - the identifier's string value is defined.
		POST - the identifier's string value has been returned.
	
	*/IdentifierInterface init (char c);/* 
        PRE  - c is a letter  
        POST - The Identier has the value c. The size is 1. 

	*/boolean equals(Identifier identifier2);/*
        PRE - 
        POST - true: the value of the Identifier is equal to the value of identifier2.
               false: the value of the Identifier is not equal to the value of the identifier2.

    */int size();/*
        PRE -
        POST - The size of the Identifier, i.e the number of characters, has been returned.

    */IdentifierInterface addCharacter (char c);/* 
        PRE  - the character is alphanumeric
        POST - the character has been added to the back of the value of the Identifier.

    */char getCharAtIndex(int i);/*
        PRE - A character at index i is defined. i has to be greater or equal to 0 and smaller than size().
        POST - The character at index i has been returned.
    
    */int hashCode();/*
        PRE - 
        POST - The hashcode has been returned.
    
    */
}

