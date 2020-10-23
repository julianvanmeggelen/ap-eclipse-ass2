package nl.vu.labs.phoenix.ap;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	LinkedList list;
	int amountOfElements;

	public Set(){
		this.list = new LinkedList();
		this.amountOfElements = 0;
	}

	@Override
	public boolean add(T t){
		// TODO Auto-generated method stub
		//make sure not in the list
		list.insert(t);
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
		SetInterface<T> result = new Set();
		while(this.list.goToNext()) {
			result.add(list.retrieve());//ask ruby
		}
		return result;
	}

	//implement 
	public SetInterface<T> difference(SetInterface<T> secondSet) {
		SetInterface<T> result1 = this.copy();
        while (secondSet.list.goToNext()) {
            result1.remove(secondSet.get());
        }
        return result1;
    }
	
  public SetInterface<T> union(SetInterface<T> secondSet) {
	  	SetInterface<T> result = this.copy();
        while (secondSet.list.goToNext()) {
            try {
                result.add(secondSet.retrieve());
            } catch (Exception e) {
                return e;//print errpr
            }
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
    
    public boolean contains(T){
    	
    }
    
	
}
