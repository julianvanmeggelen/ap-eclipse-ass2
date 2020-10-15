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
		return null;
	}

	@Override
	public boolean remove(T t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SetInterface<T> copy() {
		// TODO Auto-generated method stub
		return null;
	}

	//implement 
	public SetInterface<T> difference(Set secondSet) {
        Set result1 = new Set(this);
        for (int i = 0; i < secondSet.amountOfElements; i++) {
            result1.removeFromSet(secondSet.identifierAtPos(i));
        }
        return result1;
    }

    public SetInterface<T> intersection(Set secondSet) {
        Set union = this.union(secondSet);
        Set difference1 = this.difference(secondSet);
        Set difference2 = secondSet.difference(this);
        Set result = union.difference(difference1);
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
