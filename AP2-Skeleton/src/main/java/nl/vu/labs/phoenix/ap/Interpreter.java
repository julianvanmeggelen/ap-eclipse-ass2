package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
	
	HashMap<Identifier, Set<BigInteger> > storage; 

	public Interpreter(){
		storage = new HashMap<Identifier, Set<BigInteger>>();
	}

	@Override
	public T getMemory(String v) {
		// TODO Implement me
		return storage.get(v.hashCode()); //error
	}

	@Override
	public T eval(String s) {
		// TODO Implement me
		Scanner stringScanner = new Scanner(s);
		stringScanner.useDelimiter("");
		return statement(stringScanner);
	}

	T statement(Scanner input) throws APException{
		//statement = assignment | print statement | comment ;
		if(nextCharIs(input, '?')){
			//statement is print statement
			return printStatement(input);
		}else if(nextCharIs(input, '/')){
			//statement is comment
			return comment(input);
		}else{
			//statement is assignment
			return assignment(input);
		}
	}

	T assignment(Scanner input) throws APException{//stores the input in memory
		//assignment = identifier ’=’ expression <eoln> ;
		identifier(input);
		character(input, '=');
		expression(input);
		eoln(input);
		return null;
	}

	T printStatement(Scanner input) throws APException{//prints sets from memory
		//print statement = ’?’ expression <eoln>
		character(input, '?');
		T set = expression(input);
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
				identifier.addCharacter((char) number(input));
			}
		}
		return identifier;
	}

	T expression(Scanner input) throws APException{
		//expression = term { additive_operator term } ;
		term(input);
		if(nextCharIs(input,'+') || nextCharIs(input,'|') || nextCharIs(input,'-')){//function isadditiveoperator
			additiveOperator(input);
			term(input);
		}
		return null;
	}

	void term(Scanner input) throws APException{
		//term = factor { multiplicative_operator factor }
		factor(input);
		if(nextCharIs(input, '*')){
			multiplicativeOperator(input);
			factor(input);
		}
	}

	Set factor(Scanner input) throws APException{ //returns set
		//identifier | complex factor | set
		if(nextCharIs(input, '(')){
			//complex_factor
			complexFactor(input);
		}else if(nextCharIsDigit(input)){
			//set
			set(input); // -> get set from set()
		}else{
			//identifier -> getmemory
			identifier(input);
		}
		return null;
	}

	void complexFactor(Scanner input) throws APException{
		//complex factor = ’(’ expression ’)’
		character(input,'(');
		expression(input);
		character(input, ')');
	}

	void set(Scanner input) throws APException{ //returns set
		//set = ’{’ row_natural_numbers ’}’ 
		character(input, '{');
		rowNaturalNumbers(input);
		character(input, '}');
	}

	void rowNaturalNumbers(Scanner input) throws APException{//make set here
		//row natural numbers = [ natural number { ’,’ natural number } ]
		if(nextCharIsDigit(input)){
			naturalNumber(input);
		}else{
			//throw exception
		}
		while(nextCharIsDigit(input)){
			naturalNumber(input);
			character(input,',');
		}
	}

	char additiveOperator(Scanner input) throws APException{
		//additive operator = ’+’ | ’|’ | ’−’ ;
		if(nextCharIs(input,'+') || nextCharIs(input,'|') || nextCharIs(input,'-')){
			return nextChar(input);
		}else{
			throw new APException("additive operator invalid.");
		}
	}

	void multiplicativeOperator(Scanner input) throws APException{
		//multiplicative operator = ’∗’ ;
	}

	void naturalNumber(Scanner input) throws APException{
		//natural number = positive number | zero ;
	}

	void positiveNumber(Scanner input) throws APException{
		//positive number = not zero { number } ;
	}

	BigInteger number(Scanner input) throws APException{
		//number = zero | not zero ;
		return null;
	}

	void zero(Scanner input) throws APException{
		//zero = ’0’ ;
	}

	void notZero(Scanner input) throws APException{
		//not zero = ’1’ | ’2’ | ’3’ | ’4’ | ’5’ | ’6’ | ’7’ | ’8’ | ’9’ ;
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
		}nextChar(input);
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
}
