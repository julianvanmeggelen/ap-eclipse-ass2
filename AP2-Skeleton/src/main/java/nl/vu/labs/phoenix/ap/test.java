package nl.vu.labs.phoenix.ap;
import org.junit.*;



import java.io.PrintStream;
import java.util.Scanner;

public class test {
	PrintStream out;
	public test() {
		out = new PrintStream(System.out);
	}
	
	void start() {
		Scanner scan = new Scanner("a b c d e f g h").useDelimiter("");
		scan.skip(" ");
		while(scan.hasNext()) {
			out.print(scan.next());
		}
		
	}
	
	public static void main(String[] args) {
		new test().start();

	}

}
