package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;

public class LinkedList<E extends Comparable<E>> implements ListInterface<E> {

    int size;
    Node list;
    Node current;
    PrintStream out; //remove
    //add last

    private class Node {

        E data;
        Node prior, next;

        public Node(E data) {
            this(data, null, null);
        }

        public Node(E data, Node prior, Node next) {
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }

    }

    public LinkedList(){
        this.size = 0;
        
        list = null;
        current = null;
        out =new PrintStream(System.out); //remove
        out.println("List created");
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public ListInterface<E> init() { 
        this.size = 0;
        this.list = null;
        return this;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ListInterface<E> insert(E d) {
        this.size += 1;
        if(list == null){
            current = list = new Node(d, null, null);
            return this;
        }
    
        Node n = list;   
        while (d.compareTo(n.data) > 1 ) { //use find
        	n = n.next;
        }
        
     
        Node newNode  = new Node(d, n.prior, n);
        if(n.prior != null) {
        	n.prior.next = newNode;
        }
        n.prior = newNode;
        current= newNode;
        
        printList(); //debugging
        return this;
    }

    @Override
    public E retrieve() {
        return current.data;
    }

    @Override
    public ListInterface<E> remove() {
        //if one item --> init
    	
    	if(size == 1) {
    		this.init();
    		size--;
    		return null;
    	}
        current.prior.next = current.next;
        current.next.prior = current.prior;
        if(current.next != null){
            current = current.next;
        }else{
            current = current.prior;
        }
        size--;
        return null;
    }

    @Override
    public boolean find(E d) {
        current = list;
        //compare current and if current > data --> beginning otherwise start from current
        while(current.data.compareTo(d) != 0){
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean goToFirst() {
        //can be done in one line
        if (list == null){
            return false;
        }
        current = list;
        return true;
    }

    @Override
    public boolean goToLast() {
        if (list == null){
            return false;
        }
        while(current.next != null){
            current = current.next;
        }
        return true;
    }

    @Override
    public boolean goToNext() {
        if (list == null || current.next == null){
            return false;
        }
        current = current.next;
        return true;
    }

    @Override
    public boolean goToPrevious() {
        if (list == null || current.prior == null){
            return false;
        }
        current = current.prior;
        return true;
    }

    @Override
    public ListInterface<E> copy() {
        LinkedList copy = new LinkedList();
        //save current and restore at the end
        Node currentBackup = new Node(current.data, current.prior, current.next);
        goToFirst();
        copy.insert(current.data);
        while(goToNext()){
            copy.insert(current.data);
        }
        current = currentBackup;
        return copy;
    }
    
    void printList() { //for debugging
    	Node n = list;
    	out.println("----");
    	while(n!=null) {
    		out.print(n.data);
    		n=n.next;
    	}
    }
}