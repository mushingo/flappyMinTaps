package flappy;
import java.util.*;

public class Space {
	HashMap<Space, Integer> nextNodes;
	ArrayList<Space> list;
	int x;
	int y;
	boolean visit;
	
	public Space(int x, int y) {
		this.x = x;
		this.y = y;
		nextNodes = new HashMap<Space, Integer>();
		list = new ArrayList<Space>(); 
		visit = false; 
	}
	
	public void addNextSpace(Space space, int minJumps) {
		nextNodes.put(space, minJumps);
		list.add(space);
	}
		
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj.getClass() != getClass())
			return false;
		
		Space comp = (Space) obj;
		
		if (comp.x == this.x && comp.y == this.y) 
			return true;
		return false;		
	}
	
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + x;
		hash = 13 * hash + y;
		return hash;
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	

}
 
