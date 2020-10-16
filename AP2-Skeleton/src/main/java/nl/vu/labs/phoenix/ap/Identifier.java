package nl.vu.labs.phoenix.ap;

public class Identifier implements IdentifierInterface{
	
	StringBuffer value;
	int size;

	Identifier(){
		this.value = new StringBuffer();
		this.size = 0;
	}

	Identifier(Identifier i){
		this.value = new StringBuffer(i.toString());
		this.size = this.value.length();
	}
	
	@Override
	public String value() {
		// TODO Auto-generated method stub
		return value.toString();
	}
    
    public Identifier init(char c){ 
		this.value = new StringBuffer(c);
		this.size = 1;
		return this;
    }

    public int size(){
        return this.size();
    }

	public boolean equals(Identifier identifier2){
		return this.value.equals(identifier2.value());
	}

	public int hashCode(){
		return this.value.toString().hashCode();
	}

	public Identifier addCharacter(char c){
		this.value.append(c);
		return this;
	}

	public char getCharAtIndex(int i){
		return this.value.charAt(i);
	}
	
	public String toString() {
		return value.toString();
	}

}
