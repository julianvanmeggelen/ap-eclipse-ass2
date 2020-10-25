package nl.vu.labs.phoenix.ap;

public class LinkedList<E extends Comparable<E>> implements ListInterface<E> {

	int size;
	Node list;
	Node current;
	Node last;
	

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

	public LinkedList() {
		this.size = 0;
		this.last = null;
		this.list = null;
		this.current = null;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public ListInterface<E> init() {
		this.size = 0;
		this.current = this.list = null;
		return this;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public ListInterface<E> insert(E d) {
		this.size ++;
		if (list == null) {
			current = list = new Node(d, null, null);
			return this;
		}

		if (d.compareTo(list.data) <= 0) {
			current = list = list.prior = new Node(d, null, list); // add d to the front
			return this;
		}

		Node n = d.compareTo(current.data) < 0 ? list : current;
		while (d.compareTo(n.data) > 1 && n.next != null) {
			n = n.next;
		}

		if (n.next == null && d.compareTo(n.data) > 0) {
			last = current = n.next = new Node(d, n, null);// add d to the back
		} else
			current = n.next = n.next.prior = new Node(d, n, n.next);//add d after n
		return this;
	}

	@Override
	public E retrieve() {
		return current.data;
	}

	@Override
	public ListInterface<E> remove() {

		if (isEmpty()) {
			return this;
		}
		if (size() == 1) {
			current = list = last = new Node(null);
		} else if (current == list) {
			current = list = current.next;
			current.prior = null;
		} else if (current == last) {
			current = last = current.prior;
			current.next = null;
		} else {
			current.next.prior = current.prior;
			current = current.prior.next = current.next;
		}

		size--;
		return this;
	}

	@Override
	public boolean find(E d) {
		if(size == 0) return false;
		current = d.compareTo(current.data)<0? list: current;
		do{
			if (current.data.compareTo(d) == 0) return true;
		}while (goToNext());
		return false;
	}

	@Override
	public boolean goToFirst() {
		if (list == null) return false;
		current = list;
		return true;
	}

	@Override
	public boolean goToLast() {
		if (list == null) {
			return false;
		}
		while (current.next != null) {
			current = current.next;
		}
		return true;
	}

	@Override
	public boolean goToNext() {
		if (list == null || current.next == null) {
			return false;
		}
		current = current.next;
		return true;
	}

	@Override
	public boolean goToPrevious() {
		if (list == null || current.prior == null) {
			return false;
		}
		current = current.prior;
		return true;
	}

	@Override
	public ListInterface<E> copy() {
		LinkedList copy = new LinkedList();
		// save current and restore at the end
		Node currentBackup = new Node(current.data, current.prior, current.next);
		goToFirst();
		copy.insert(current.data);
		while (goToNext()) {
			copy.insert(current.data);
		}
		current = currentBackup;
		return copy;
	}
}