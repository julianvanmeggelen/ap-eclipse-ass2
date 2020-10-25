package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

public class Main {
	InterpreterInterface<Set<BigInteger>> interpreter;
	PrintStream out;
	Main(){
		interpreter = new Interpreter<Set<BigInteger>>();
		out = new PrintStream(System.out);
	}
	
	private void start() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		Scanner in = new Scanner(System.in);
		while(in.hasNextLine()) {
			String line = in.nextLine();
			Set result = (Set) interpreter.eval(line);
			if(result != null) {
				out.println(result);
			}
		}
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
