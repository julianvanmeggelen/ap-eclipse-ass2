package nl.vu.labs.phoenix.ap;

import java.io.PrintStream;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	LinkedList list;
	int amountOfElements;
	PrintStream out;
	
	public Set(){
		this.list = new LinkedList();
		this.amountOfElements = 0;
		out = new PrintStream(System.out);
	}

	@Override
	public boolean add(T t){
		// TODO Auto-generated method stub
		//make sure not in the list
		if(!list.find(t)) {
			list.insert(t);
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
		
		if(list.find(t)) {
			list.remove();
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return list.size;
	}

	@Override
	public SetInterface<T> copy() {
		Set result = new Set();
		while(this.list.goToNext()) {
			result.add(list.retrieve());//ask ruby
		}
		return result;
	}

	//implement 
	public SetInterface<T> difference(SetInterface<T> set2) {
		SetInterface<T> result1 = this.copy();
		Set secondSet = (Set) set2;
        while (secondSet.list.goToNext()) {
            result1.remove((T) secondSet.get());
        }
        return result1;
    }
	
  public SetInterface<T> union(SetInterface<T> secondSet) {
	  	SetInterface<T> result = secondSet.copy();
        while (this.list.goToNext()) {
        	result.add(this.get()); 
        }
        return result;
    }
  
    public SetInterface<T> intersection(SetInterface<T> secondSet) {
    	SetInterface<T> union = this.union(secondSet);
    	SetInterface<T> difference1 = this.difference(secondSet);
    	SetInterface<T> difference2 = secondSet.difference(this);
    	SetInterface<T> result = union.difference(difference1);
        result = result.difference(difference2);
        return result;
    }
    
    public SetInterface<T> symmetricDifference(SetInterface<T> secondSet){
    	SetInterface<T> union = this.union(secondSet);
    	SetInterface<T> intersection = this.intersection(secondSet);
    	SetInterface<T> result = union.difference(intersection);
        return result;
	}
    
    public boolean contains(T element){
    	return list.find(element);
    }
    
    private LinkedList list() {
    	return this.list;
    }
    
}
