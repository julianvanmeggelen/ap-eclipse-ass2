package nl.vu.labs.phoenix.ap;
import org.junit.*;



import java.io.PrintStream;

public class test {
	PrintStream out;
	public test() {
		out = new PrintStream(System.out);
	}
	
	void start() {
		LinkedList<Integer> list = new LinkedList<>();
		out.print(list.size());
		list.insert(5);
		out.print(list.size());
	}
	
	public static void main(String[] args) {
		new test().start();

	}

}
