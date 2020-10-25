package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	LinkedList list;
	int amountOfElements;
	PrintStream out;

	public Set() {
		this.list = new LinkedList();
		this.amountOfElements = 0;
		out = new PrintStream(System.out);
	}

	@Override
	public boolean add(T t) {
		// TODO Auto-generated method stub
		// make sure not in the list
		if (amountOfElements == 0) {
			list.insert(t);
			amountOfElements++;
			return true;
		} else if (!list.find(t)) {
			list.insert(t);
			amountOfElements++;
			return true;
		}
		return false;
	}

	@Override
	public T get() {
		// TODO Auto-generated method stub
		return (T) list.retrieve();
	}

	@Override
	public boolean remove(T t) {
		if (list == null)
			return false;
		if (list.find(t)) {
			list.remove();
			amountOfElements--;
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return this.amountOfElements;
	}

	@Override
	public SetInterface<T> copy() {
		Set result = new Set();
		if (list.size() == 0) {
			return result;
		}
		list.goToFirst();
		do {
			result.add(list.retrieve());
		} while (list.goToNext());
		return result;
	}

	// implement
	public SetInterface<T> difference(SetInterface<T> set2) {
		Set result1 = (Set) this.copy();
		Set secondSet = (Set) set2.copy();
		secondSet.list.goToFirst();
		while (secondSet.amountOfElements > 0) {
			result1.remove(secondSet.get());
			secondSet.remove(secondSet.get());
		}
		return result1;
	}

	public SetInterface<T> union(SetInterface<T> secondSet) {
		SetInterface<T> result = secondSet.copy();
		if (amountOfElements == 0) {
			return result;
		}
		list.goToFirst();
		do {
			result.add(this.get());
		} while (this.list.goToNext());
		return result;
	}

	public SetInterface<T> intersection(SetInterface<T> set2) {
		SetInterface<T> diff1 = this.difference(set2);//
		SetInterface<T> diff2 = set2.difference(this);//
		return this.union(set2).difference(diff2).difference(diff1);
	}

	public SetInterface<T> symmetricDifference(SetInterface<T> secondSet) {
		SetInterface<T> union = this.union(secondSet);
		SetInterface<T> intersection = this.intersection(secondSet);
		SetInterface<T> result = union.difference(intersection);
		return result;
	}

	public boolean contains(T element) {
		return list.find(element);
	}

	public String toString() {
		if (amountOfElements == 0) {
			return "{}";
		}
		String res = "{";
		list.goToFirst();
		do {
			res += list.retrieve().toString() + ",";
		} while (list.goToNext());
		res = res.substring(0, res.length() - 1) + '}';
		return res;
	}
}
