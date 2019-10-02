package flappy;

public class Second {
	int x;
	int y;
	Pipe pipe;
	private int roof;
	
	
	public Second(int x, int y, int roof) {
		this.x = x;
		this.y = y;		
		this.pipe = null;
		this.roof = roof;
	}
	
	public void add_pipe(Pipe pipe) {
		this.pipe = pipe;
		
	}
	
	public boolean has_pipe() {
		if (pipe == null)
			return false;
		return true;
	}

	
	public int get_roof() {
		if (pipe == null)
			return roof;
		if (pipe.proof <= roof)
			return pipe.proof;
		return roof;
	}
	
	public int get_floor() {
		if (pipe == null)
			return 0;
		if (pipe.pfloor >= 0)
			return pipe.pfloor;
		return 0;
	}
	
}
