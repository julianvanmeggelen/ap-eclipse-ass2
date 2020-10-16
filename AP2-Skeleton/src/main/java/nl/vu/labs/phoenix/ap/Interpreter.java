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

	Set statement(Scanner input) throws APException{
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

	Set assignment(Scanner input) throws APException{//stores the input in memory
		//assignment = identifier ’=’ expression <eoln> ;
		identifier(input);
		character(input, '=');
		expression(input);
		eoln(input);
		return null;
	}

	Set printStatement(Scanner input) throws APException{//prints sets from memory
		//print statement = ’?’ expression <eoln>
		character(input, '?');
		Set set = expression(input);
		eoln(input);
		return set;
	}

	Set comment(Scanner input) throws APException{//does nothing
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
				identifier.addCharacter((number(input));
			}
		}
		return identifier;
	}

	Set expression(Scanner input) throws APException{
		//expression = term { additive_operator term } ;
		Set set1 = term(input);
		if(nextCharIsAdditiveOperator(input)){
			char operator = additiveOperator(input);
			Set set2 = term(input);
			switch (operator) {
				case '+':
					//union
					return set1.union(set2);
				case '|':
					//symmetric difference
					return set1.symmetricDifference(set2);
					break;
				case '-':
					//difference
					return set1.difference(set2);
					break;
			}
			
		}else {
			return set1;
		}
	}

	Set term(Scanner input) throws APException{
		//term = factor { multiplicative_operator factor }
		factor(input);
		if(nextCharIs(input, '*')){
			multiplicativeOperator(input);
			factor(input);
		}
	}

	Set factor(Scanner input) throws APException{ //returns set
		//identifier | complex factor | set
		Set result;
		if(nextCharIs(input, '(')){
			//complex_factor
			result = complexFactor(input);
		}else if(nextCharIsDigit(input)){
			//set
			result = set(input); // -> get set from set()
		}else{
			//identifier -> getmemory
			result = getMemory(identifier(input).value());
		}
		return result;
	}

	Set complexFactor(Scanner input) throws APException{
		//complex factor = ’(’ expression ’)’
		character(input,'(');
		Set result = expression(input);
		character(input, ')');
		return result;
	}

	Set set(Scanner input) throws APException{ //returns set
		//set = ’{’ row_natural_numbers ’}’ 
		character(input, '{');
		Set result = rowNaturalNumbers(input);
		character(input, '}');
		return result;
	}

	Set rowNaturalNumbers(Scanner input) throws APException{//make set here
		//row natural numbers = [ natural number { ’,’ natural number } ]
		Set result = new Set();
		if(!nextCharIsDigit(input)){
			throw new APException("Sets must consist of natural numbers");
		}
		while(nextCharIsDigit(input)){
			result.add(naturalNumber(input));
			character(input,',');
		}
		return result;
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
}
