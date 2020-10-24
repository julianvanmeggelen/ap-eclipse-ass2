package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
	
	HashMap<Identifier, Set<BigInteger> > storage; 
	PrintStream out;

	public Interpreter(){
		storage = new HashMap<Identifier, Set<BigInteger>>();
		out = new PrintStream(System.out);
	}

	@Override
	public T getMemory(String v) {
		// TODO Implement me
		//needs more//check if it exists
		if(storage.containsKey(v)) {
			return (T) storage.get(v);
		}else {
			return null; //we would like to return an exception but this method was predefined
		}
	}

	@Override
	public T eval(String s) { //catch exceptions 
		// TODO Implement me
		Scanner stringScanner = new Scanner(s);
		stringScanner.useDelimiter("");
		try {
			return statement(stringScanner);
		}catch(APException e) {
			out.println(e.getMessage());
			return null;
		}
	}

	T statement(Scanner input) throws APException{
		//statement = assignment | print statement | comment 
		spaces(input);
		if(nextCharIs(input, '?')){  //constants
			//statement is print statement
			return printStatement(input);
		}else if(nextCharIs(input, '/')){ //constants
			//statement is comment
			return comment(input);
		}else if(nextCharIsLetter(input)){
			//statement is assignment
			return assignment(input);
		}else {
			throw new APException("");
		}
	}
	
	T assignment(Scanner input) throws APException{//stores the input in memory
		//assignment = identifier ’=’ expression <eoln> ;
		Identifier identifier = identifier(input);
		spaces(input);
		character(input, '=');
		spaces(input);
		Set set  = (Set) expression(input);
		spaces(input);
		eoln(input);
		//syntax correct -> store in memory
		storage.put(identifier, set);
		return null;
	}

	T printStatement(Scanner input) throws APException{//prints sets from memory
		//print statement = ’?’ expression <eoln>
		character(input, '?');
		spaces(input);
		T set = expression(input);
		spaces (input);
		eoln(input);
		return set;
	}

	T comment(Scanner input) throws APException{//does nothing
		//comment = ’/’ <a line of text> <eoln> ;
		character(input, '/');
		return null;
	}

	Identifier identifier(Scanner input) throws APException{
		//identifier = letter { letter | number }
		Identifier identifier = new Identifier();
		if(nextCharIsLetter(input)){
			identifier.addCharacter(letter(input));
		}else{
			throw new APException("Identifier must start with a letter");
		}

		while(nextCharIsLetter(input) || nextCharIsDigit(input)){
			if(nextCharIsLetter(input)){
				identifier.addCharacter(letter(input));
			}else{
				identifier.addCharacter((number(input)));
			}
		}
		out.println(identifier.value());
		return identifier;
	}

	T expression(Scanner input) throws APException{
		//expression = term { additive_operator term } ;
		T set1 = term(input);
		spaces(input);
		if(nextCharIsAdditiveOperator(input)){
			spaces(input);
			char operator = additiveOperator(input);
			spaces(input);
			T set2 = term(input);
			switch (operator) {
				case '+': //constants
					//union
					return (T) set1.union(set2);
				case '|':
					//symmetric difference
					return (T) set1.symmetricDifference(set2);
				case '-':
					//difference
					return (T) set1.difference(set2);
			}
			
		}
		return set1;
	}

	T term(Scanner input) throws APException{
		//term = factor { multiplicative_operator factor }
		T set = factor(input);
		spaces(input);
		if(nextCharIs(input, '*')){ //constant
			multiplicativeOperator(input);
			spaces(input);
			return (T) set.intersection(factor(input));
		}
		return set;
	}

	T factor(Scanner input) throws APException{ //returns set
		//identifier | complex factor | set
		T result;
		if(nextCharIs(input, '(')){//constant
			//complex_factor
			spaces(input);
			result = complexFactor(input);
		}else if(nextCharIsDigit(input)){
			//set
			result = set(input); // -> get set from set()
		}else if (nextCharIsLetter(input)){
			//identifier -> getmemory
			Identifier identifier = identifier(input);
			result = (T) storage.get(identifier);  //get out of hashmap directly;
		}else {
			throw new APException("Incorrect syntax in factor.");
		}
		return (T) result;
	}

	T complexFactor(Scanner input) throws APException{
		//complex factor = ’(’ expression ’)’
		character(input,'(');
		spaces(input);
		T result = expression(input);
		spaces(input);
		character(input, ')');
		return result;
	}

	T set(Scanner input) throws APException{ //returns set
		//set = ’{’ row_natural_numbers ’}’ 
		character(input, '{');
		spaces(input);
		T result = rowNaturalNumbers(input);
		spaces(input);
		character(input, '}');
		return result;
	}

	T rowNaturalNumbers(Scanner input) throws APException{//make set here
		//row natural numbers = [ natural number { ’,’ natural number } ]
		Set result = new Set();
		if(!nextCharIsDigit(input)){
			throw new APException("Sets must consist of natural numbers");
		}
		while(nextCharIsDigit(input)){
			spaces(input);
			result.add(naturalNumber(input));
			spaces(input);
			character(input,',');
		}
		return (T) result;
	}

	char additiveOperator(Scanner input) throws APException{
		//additive operator = ’+’ | ’|’ | ’−’ ;
		if(nextCharIs(input,'+') || nextCharIs(input,'|') || nextCharIs(input,'-')){
			return nextChar(input);
		}else{
			throw new APException("Invalid additive operator.");
		}
	}

	char multiplicativeOperator(Scanner input) throws APException{
		//multiplicative operator = ’∗’ ;
		if(!nextCharIs(input,'*')) {
			throw new APException("Invalidt multiplicative operator");
		}else {
			return '*';
		}
	}

	BigInteger naturalNumber(Scanner input) throws APException{
		//natural number = positive number | zero ;
		String stringValue = "";
		if(nextCharIs(input,'0')) {
			stringValue += zero(input);
		}else {
			stringValue += notZero(input);
		}
		return new BigInteger(stringValue);
	}

	String positiveNumber(Scanner input) throws APException{
		//positive number = not zero { number } ;
		String result = "" + notZero(input);
		while(nextCharIsDigit(input)) {
			result += number(input);
		}
		return result;
	}

	char number(Scanner input) throws APException{
		//number = zero | not zero ;
		return nextCharIs(input,'0')? zero(input): notZero(input);
	}

	char zero(Scanner input) throws APException{
		//zero = ’0’ ;
		if(!nextCharIs(input, '0')) throw new APException("A zero must be a zero");
		return '0';
		
	}

	char notZero(Scanner input) throws APException{
		//not zero = ’1’ | ’2’ | ’3’ | ’4’ | ’5’ | ’6’ | ’7’ | ’8’ | ’9’ ;
		if(!input.hasNext("[1-9]")) {
			throw new APException("A not_zero must be a number in range 1,2,3,4,5,6,7,8,9");
		}else {
			return nextChar(input);
		}
	}

	char letter(Scanner input) throws APException{
		//letter = ’A’ | ’B’ | ’C’ | ’D’ | ’E’ | ’F’ | ’G’ | ’H’ | ’I’ | ’J’ | ’K’ | ’L’ | ’M’ | ’N’ | ’O’ | ’P’ | ’Q’ | ’R’ | ’S’ | ’T’ | ’U’ | ’V’ | ’W’ | ’X’ | ’Y’ | ’Z’ | ’a’ | ’b’ | ’c’ | ’d’ | ’e’ | ’f’ | ’g’ | ’h’ | ’i’ | ’j’ | ’k’ |’l’ | ’m’ | ’n’ | ’o’ | ’p’ | ’q’ | ’r’ | ’s’ | ’t’ | ’u’ | ’v’ | ’w’ | ’x’ | ’y’ | ’z’ ;
		if (nextCharIsLetter(input)){
			return nextChar(input);
		}else{
			throw new APException("Non-letter found where a letter was expected");
		}
	}

	void eof(Scanner input) throws APException{

	}

	void eoln(Scanner input) throws APException{
		if (input.hasNext()) {
			throw new APException("........");
		}
	}

	void character(Scanner input, char c) throws APException{
		//read next char or return exception if it is not c
		if (! nextCharIs(input, c)) {
			throw new APException("........");
		} nextChar(input);
	}

	// Method to read 1 character.
	char nextChar(Scanner in) {
        return in.next().charAt(0);
    }

    // Method to check if the next character to be read when // calling nextChar()
    // is equal to the provided character.
    boolean nextCharIs(Scanner in, char c) {
        return in.hasNext(Pattern.quote(c + ""));
    }

    // Method to check if the next character to be read when // calling nextChar()
    // is a digit.
    boolean nextCharIsDigit(Scanner in) {
        return in.hasNext("[0-9]");
    }

    // Method to check if the next character to be read when // calling nextChar()
    // is a letter.
    boolean nextCharIsLetter(Scanner in) {
        return in.hasNext("[a-zA-Z]");
    } 
    
    boolean nextCharIsAdditiveOperator(Scanner in) {
    	return nextCharIs(in,'+') || nextCharIs(in,'|') || nextCharIs(in,'-');
    }
    
    void spaces(Scanner in) throws APException{
    	while (nextCharIs(in,' ')) {
    		nextChar(in);
    	}
    }
}
