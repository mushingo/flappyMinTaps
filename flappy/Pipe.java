package flappy;

public class Pipe {
	int proof;
	int pfloor;
	int second;
	Pipe prev;
	Pipe next;
	
	
	public Pipe(Pipe prev, int second, int pfloor, int proof) {
		this.proof = proof;
		this.pfloor = pfloor;
		this.prev = prev;
		this.next = null;
		this.second = second;
	}
	
	
}
