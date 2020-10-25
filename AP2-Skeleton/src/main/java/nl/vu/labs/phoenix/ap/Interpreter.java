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

	HashMap<Identifier, Set<BigInteger>> setStorage;
	PrintStream out;
	private static final char PRINT_STATEMENT = '?', COMMENT = '/', ASSIGNMENT = '=', SET_OPEN = '{', SET_CLOSE = '}',
			COMPLEX_OPEN = '(', COMPLEX_CLOSE = ')', NATURALNUMBER_SEPARATOR = ',', UNION_OPERATOR = '+',
			COMPLEMENT_OPERATOR = '-', SYMMETRIC_OPERATOR = '|', INTERSECTION_OPERATOR = '*';

	public Interpreter() {
		setStorage = new HashMap<Identifier, Set<BigInteger>>();
		out = new PrintStream(System.out);
	}

	@Override
	public T getMemory(String v) {
		// TODO Implement me
		Identifier id = new Identifier();
		for (int i = 0; i < v.length(); i++) {
			id.addCharacter(v.charAt(i));
		}
		if (setStorage.containsKey(id)) {
			return (T) setStorage.get(id);
		} else {
			return null; // we would like to return an exception but this method heading was predefined
							// this way.
		}
	}

	@Override
	public T eval(String s) { 
		// TODO Implement me
		Scanner stringScanner = new Scanner(s);
		scanSpaces(stringScanner, false);
		try {
			return statement(stringScanner);
		} catch (APException e) {
			out.println(e.getMessage());
			return null;
		}
	}

	private T statement(Scanner input) throws APException {
		// statement = assignment | print statement | comment
		if (nextCharIs(input, PRINT_STATEMENT)) { 
			// statement is print statement
			T printStatement = printStatement(input);
			if (printStatement != null)
				return printStatement;
		} else if (nextCharIs(input, COMMENT)) { 
			// statement is comment
			comment(input);
		} else if (nextCharIsLetter(input)) {
			// statement is assignment
			assignment(input);
		} else {
			throw new APException("Unknown operation detected.");
		}
		return null;
	}

	private void assignment(Scanner input) throws APException {
		// assignment = identifier ’=’ expression <eoln> ;
		Identifier identifier = identifier(input);
		character(input, ASSIGNMENT);
		Set set = (Set) expression(input);
		eoln(input);
		// syntax correct -> store in memory
		setStorage.put(identifier, set);
	}

	private T printStatement(Scanner input) throws APException {
		// print statement = ’?’ expression <eoln>
		character(input, '?');
		T set = expression(input);
		eoln(input);
		return set;
	}

	private T comment(Scanner input) throws APException {// does nothing
		// comment = ’/’ <a line of text> <eoln> ;
		character(input, COMMENT);
		return null;
	}

	private Identifier identifier(Scanner input) throws APException {
		// identifier = letter { letter | number }
		Identifier identifier = new Identifier();
		if (nextCharIsLetter(input)) {
			identifier.addCharacter(letter(input));
		} else {
			throw new APException("Identifier must start with a letter");
		}
		scanSpaces(input, true);
		while (nextCharIsLetter(input) || nextCharIsDigit(input)) {
			if (nextCharIsLetter(input)) {
				identifier.addCharacter(letter(input));
			} else if (nextCharIsDigit(input)) {
				identifier.addCharacter((number(input)));
			} else {
				throw new APException("An identifier must contain alphanumeric characters.");
			}
		}
		scanSpaces(input, false);
		return identifier;
	}

	private T expression(Scanner input) throws APException {
		// expression = term { additive_operator term } ;
		T set1 = term(input);
		while (nextCharIsAdditiveOperator(input)) {
			char operator = additiveOperator(input);
			T set2 = term(input);
			if (operator == UNION_OPERATOR) {
				// union
				set1 = (T) set1.union(set2);
			} else if (operator == SYMMETRIC_OPERATOR) {
				// symmetric difference
				set1 = (T) set1.symmetricDifference(set2);
			} else {
				// must be difference, because we already know that the operator must be a correct
				// operator.
				set1 = (T) set1.difference(set2);
			}
		}

		return set1;
	}

	private T term(Scanner input) throws APException {
		// term = factor { multiplicative_operator factor }
		T factor = factor(input);
		while (nextCharIs(input, INTERSECTION_OPERATOR)) { 
			multiplicativeOperator(input);

			T factor2 = factor(input);
			factor = (T) factor.intersection(factor2);

		}

		return factor;
	}

	private T factor(Scanner input) throws APException { 
		// identifier | complex factor | set
		if (nextCharIs(input, COMPLEX_OPEN)) {
			// complex_factor
			return complexFactor(input);
		} else if (nextCharIs(input, SET_OPEN)) {
			// set
			return set(input); 
		} else if (nextCharIsLetter(input)) {
			// identifier -> getmemory
			Identifier identifier = identifier(input);
			if (setStorage.containsKey(identifier)) {
				return (T) setStorage.get(identifier);
			} else {
				throw new APException("No set with identifier " + identifier.value() + " found");
			}
		} else {
			throw new APException("Incorrect syntax in factor.");
		}
	}

	private T complexFactor(Scanner input) throws APException {
		// complex factor = ’(’ expression ’)’
		character(input, COMPLEX_OPEN);
		T result = expression(input);
		character(input, COMPLEX_CLOSE);
		return result;
	}

	private T set(Scanner input) throws APException { 
		// set = ’{’ row_natural_numbers ’}’
		character(input, SET_OPEN);
		T result = rowNaturalNumbers(input);
		character(input, SET_CLOSE);
		return result;
	}

	private T rowNaturalNumbers(Scanner input) throws APException {
		// row natural numbers = [ natural number { ’,’ natural number } ]
		Set<BigInteger> result = new Set();

		while (nextCharIsDigit(input)) {
			result.add(naturalNumber(input));
			if (!nextCharIs(input, SET_CLOSE)) {
				character(input, NATURALNUMBER_SEPARATOR);
			}else if(nextCharIsLetter(input)) {
				throw new APException("A set must consist of natural numbers.");
			}
		}
		return (T) result;
	}

	private char additiveOperator(Scanner input) throws APException {
		// additive operator = ’+’ | ’|’ | ’−’ ;
		if (nextCharIs(input, UNION_OPERATOR) || nextCharIs(input, SYMMETRIC_OPERATOR)
				|| nextCharIs(input, COMPLEMENT_OPERATOR)) {
			return nextChar(input);
		} else {
			throw new APException("Invalid additive operator.");
		}
	}

	private void multiplicativeOperator(Scanner input) throws APException {
		// multiplicative operator = ’∗’ ;
		if (!nextCharIs(input, INTERSECTION_OPERATOR)) {
			throw new APException("Invalid multiplicative operator");
		} else {
			nextChar(input);
		}
	}

	private BigInteger naturalNumber(Scanner input) throws APException {
		// natural number = positive number | zero ;
		String stringValue = "";
		if (nextCharIs(input, '0')) {
			stringValue += zero(input);
		} else {
			stringValue += positiveNumber(input);
		}
		return new BigInteger(stringValue);
	}

	private String positiveNumber(Scanner input) throws APException {
		// positive number = not zero { number } ;
		String result = "" + notZero(input);
		scanSpaces(input, true);
		while (nextCharIsDigit(input)) {
			result += number(input);
		}
		scanSpaces(input, false);
		return result;
	}

	private char number(Scanner input) throws APException {
		// number = zero | not zero ;
		return nextCharIs(input, '0') ? zero(input) : notZero(input);
	}

	private char zero(Scanner input) throws APException {
		// zero = ’0’ ;
		if (!nextCharIs(input, '0'))
			throw new APException("A zero must be a zero");
		return nextChar(input);

	}

	private char notZero(Scanner input) throws APException {
		// not zero = ’1’ | ’2’ | ’3’ | ’4’ | ’5’ | ’6’ | ’7’ | ’8’ | ’9’ ;
		if (!input.hasNext("[1-9]")) {
			throw new APException("A not_zero must be a number in range 1,2,3,4,5,6,7,8,9");
		} else {
			return nextChar(input);
		}
	}

	private char letter(Scanner input) throws APException {
		// letter = ’A’ | ’B’ | ’C’ | ’D’ | ’E’ | ’F’ | ’G’ | ’H’ | ’I’ | ’J’ | ’K’ |
		// ’L’ | ’M’ | ’N’ | ’O’ | ’P’ | ’Q’ | ’R’ | ’S’ | ’T’ | ’U’ | ’V’ | ’W’ | ’X’ |
		// ’Y’ | ’Z’ | ’a’ | ’b’ | ’c’ | ’d’ | ’e’ | ’f’ | ’g’ | ’h’ | ’i’ | ’j’ | ’k’
		// |’l’ | ’m’ | ’n’ | ’o’ | ’p’ | ’q’ | ’r’ | ’s’ | ’t’ | ’u’ | ’v’ | ’w’ | ’x’
		// | ’y’ | ’z’ ;
		if (nextCharIsLetter(input)) {
			return nextChar(input);
		} else {
			throw new APException("Non-letter found where a letter was expected");
		}
	}

	private void eoln(Scanner input) throws APException {
		if (input.hasNext()) {
			throw new APException("Incorrect end of line.");
		}
	}

	private void character(Scanner input, char c) throws APException {
		// read next char or return exception if it is not c
		if (nextCharIs(input, ' ')) {
			throw new APException("Expected '" + c + "' but no character was given.");
		}
		if (!input.hasNext()) {
			throw new APException("Unexpected end of input");
		}
		if (!nextCharIs(input, c)) {
			throw new APException("Expected '" + c + "' but '" + nextChar(input)+ "' was given.");
		}
		nextChar(input);
	}

	// Method to read 1 character.
	private char nextChar(Scanner in) {
		return in.next().charAt(0);
	}

	// Method to check if the next character to be read when // calling nextChar()
	// is equal to the provided character.
	private boolean nextCharIs(Scanner in, char c) {
		return in.hasNext(Pattern.quote(c + ""));
	}

	// Method to check if the next character to be read when // calling nextChar()
	// is a digit.
	private boolean nextCharIsDigit(Scanner in) {
		return in.hasNext("[0-9]");
	}

	// Method to check if the next character to be read when // calling nextChar()
	// is a letter.
	private boolean nextCharIsLetter(Scanner in) {
		return in.hasNext("[a-zA-Z]");
	}

	private boolean nextCharIsAdditiveOperator(Scanner in) {
		return nextCharIs(in, UNION_OPERATOR) || nextCharIs(in, SYMMETRIC_OPERATOR)
				|| nextCharIs(in, COMPLEMENT_OPERATOR);
	}

	private void scanSpaces(Scanner input, boolean use) {
		if (!use)
			input.useDelimiter("\\s*");
		if (use)
			input.useDelimiter("");
	}

}
