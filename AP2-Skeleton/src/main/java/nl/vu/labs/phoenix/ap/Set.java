package nl.vu.labs.phoenix.ap;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	LinkedList list;
	int amountOfElements;

	public Set(){
		this.list = new LinkedList();
		this.amountOfElements = 0;
	}

	@Override
	public boolean add(T t) {
		// TODO Auto-generated method stub
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
		boolean found = false;
		while(list.find(t)) {
			list.remove();
			found = true;
		}
		return found;
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
	public SetInterface<T> difference(Set secondSet) {
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
  
    public SetInterface<T> intersection(Set secondSet) {
    	SetInterface<T> union = this.union(secondSet);
    	SetInterface<T> difference1 = this.difference(secondSet);
    	SetInterface<T> difference2 = secondSet.difference(this);
    	SetInterface<T> result = union.difference(difference1);
        result = result.difference(difference2);
        return result;
    }

    public SetInterface<T> symmetricDifference(Set secondSet){
        Set union = this.union(secondSet);
        Set intersection = this.intersection(secondSet);
        Set result = union.difference(intersection);
        return result;
	}
	
}
