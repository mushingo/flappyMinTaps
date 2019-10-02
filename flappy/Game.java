package flappy;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class Game {
	ArrayList<Second> seconds;
	ArrayList<Space> spaces;
	int birdSy;
	int birdSx;
	int totalPipes;
	int maxSec;
	int height_m;
	String filename;
	Pipe firstPipe;
	Pipe lastPipe;
	
	boolean stillBorn;
	boolean survived;
	int totalTaps;
	int clearedPipes;
	
	public Game(String filename) throws Exception{
		spaces = new ArrayList<Space>();
		int x, y, i, taps;
		ArrayList<Space> prevSpaces = new ArrayList<Space>();
		ArrayList<Space> nextSpaces = new ArrayList<Space>();
		
		stillBorn = false;
		
		build_map(filename);
		Space space;
		
		
		Space[][] spaces_2d = new Space[maxSec + 1][height_m + 1];
		
		for (x = 0; x <= maxSec; x++) {
			for (y = 0; y <= height_m; y++) {
				spaces_2d[x][y] = new Space(x, y);
			}
			
		}
		
		Space prevSpace = new Space(birdSx, birdSy);
		spaces_2d[birdSx][birdSy].visit = true; 
		int birdx = birdSx; 
		int birdy = birdSy;
		int birdy_next;
		prevSpaces.add(prevSpace);
		
		int drop = 0;
 		
		for (birdx = birdSx; birdx < maxSec; birdx++) {
			drop = seconds.get(birdx).y;
			for (i = 0; i < prevSpaces.size(); i++) {
				birdy = prevSpaces.get(i).y;
				taps = 0;
				while (birdy - drop < seconds.get(birdx + 1).get_roof()) {
					if (taps == 0)
						birdy_next = birdy - drop;
					else
						birdy_next = birdy;
					
					if (birdy_next > seconds.get(birdx + 1).get_floor() && birdy_next < seconds.get(birdx + 1).get_roof()) {
						space = spaces_2d[birdx + 1][birdy_next];	
						space.visit = true;
						if (!nextSpaces.contains(space))
							nextSpaces.add(space);
						spaces_2d[prevSpaces.get(i).x][prevSpaces.get(i).y].addNextSpace(space, taps);
					}
					birdy = birdy + seconds.get(birdx).x;
					taps++;	
				}
			}
			prevSpaces = new ArrayList<Space>(nextSpaces);
			nextSpaces.clear();
		}
					
		for (x = 0; x <= maxSec; x++) {
			for (y = 0; y <= height_m; y++) {
				space = spaces_2d[x][y];
				if (space.visit == true){
					spaces.add(space);
				}
			}
			
		}
				
	}
	
	/*Reference: Adapted from lecture notes*/
	public void calc_path() throws Exception{
		survived = false;
		Space space;
		HashMap<Space, Integer> d = new HashMap<>();
		HashMap<Space, Integer> cloud = new HashMap<>();
			
		HeapAdaptablePriorityQueue<Integer, Space> pq = new HeapAdaptablePriorityQueue<>(new Intcompare());
		
		HashMap<Space, Entry> pqTokens = new HashMap<>();
	
		space = spaces.get(0);
		d.put(space, 0);
		pqTokens.put(spaces.get(0), pq.insert(d.get(space), space));
		for (int i = 1; i < spaces.size(); i++) {
			space = spaces.get(i);
			d.put(space, Integer.MAX_VALUE);
			pqTokens.put(space, pq.insert(d.get(space), space));
		}

		
		while (!pq.isEmpty()) {
			Entry<Integer, Space> entry = pq.removeMin();
			int key = entry.getKey();
			Space u = entry.getValue();
			cloud.put(u, key);
			pqTokens.remove(u);
			for (int i = 0; i < u.list.size(); i++) {
				Space v = u.list.get(i);
				if (cloud.get(v) == null) { 
					int wgt = u.nextNodes.get(v);
					if(d.get(u) + wgt < d.get(v)) {
						d.remove(v);
						d.put(v, d.get(u) + wgt);
						
						pq.replaceKey(pqTokens.get(v), d.get(v));
					}
				}
			}
		}
		
		
		ArrayList<Space> keys = new ArrayList<Space>(cloud.keySet());
		ArrayList<Space> successSpace = new ArrayList<Space>();
		ArrayList<Space> finalSpaces = new ArrayList<Space>();
		
		int clearedtoX = 0;
		
		for (int i = 0; i < keys.size(); i++) {
			Space test = keys.get(i);
			if  (cloud.get(test) != Integer.MAX_VALUE)
				successSpace.add(test);
		}
		
		for (int i = 0; i < successSpace.size(); i++) {
			Space test = successSpace.get(i);
			if  (test.x > clearedtoX)
				clearedtoX = test.x;
		}
		

		for (int i = 0; i < keys.size(); i++) {
			Space test = keys.get(i);
			if (test.x == clearedtoX) {
				finalSpaces.add(test);
			}
		}
			
		if (clearedtoX == maxSec)
			survived = true;
		
		if (!survived) {
			Pipe curPipe = firstPipe;
			while (curPipe != null && curPipe.second <= clearedtoX) {
				clearedPipes++;
				curPipe = curPipe.next;
			}
			return;
		}
		
		totalTaps = Integer.MAX_VALUE;
		
		for (int i = 0; i < finalSpaces.size(); i++) {
			Space test = finalSpaces.get(i);
			if (cloud.get(test) < totalTaps)
				totalTaps = cloud.get(test);
		}
		
	}
	
	
	public void build_map (String filename)  throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		seconds = new ArrayList<Second>();
		
		this.filename = filename.substring(0, filename.length() - 3);
		int i, time, pfloor, proof;
		Pipe nextPipe, prevPipe, pipe;
		String[] pipe_line;
		prevPipe = null;
		nextPipe = null;
		
		String line = reader.readLine();		
		
		maxSec = Integer.parseInt(line.split(" ")[0]);
		height_m = Integer.parseInt(line.split(" ")[1]);
		totalPipes = Integer.parseInt(line.split(" ")[2]);
		birdSy = Integer.parseInt(reader.readLine());
		birdSx = 0;
		
		String[] x_list = reader.readLine().split("\\s+");
		String[] y_list = reader.readLine().split("\\s+");
		
		for(i = 0; i < maxSec; i++) {
			seconds.add(new Second(Integer.parseInt(x_list[i]), Integer.parseInt(y_list[i]), height_m));
		}
		
		seconds.add(new Second(0, 0, height_m));
			
		if (totalPipes != 0) {
			line = reader.readLine();
			do {
				pipe_line = line.split(" ");
				time = Integer.parseInt(pipe_line[0]);
				pfloor = Integer.parseInt(pipe_line[1]);
				proof = Integer.parseInt(pipe_line[2]);
				pipe = new Pipe(null, time, pfloor, proof);
				seconds.get(time).add_pipe(pipe);
				
			} while((line = reader.readLine()) != null);
		} 
		
		firstPipe = null;
		prevPipe = null;
		nextPipe = null;
	
		for (i = 0; i < seconds.size(); i++) {
			if(seconds.get(i).has_pipe()) {
				nextPipe = seconds.get(i).pipe;
				if (firstPipe == null) 
					firstPipe = nextPipe;
				nextPipe.prev = prevPipe;
				if (prevPipe != null)
					prevPipe.next = nextPipe;
				prevPipe = nextPipe;
			}
		}
				
		nextPipe.next = null;
		lastPipe = nextPipe;
		
		reader.close();
		if (birdSy <= seconds.get(0).get_floor() || birdSy >= seconds.get(0).get_roof()) {
			stillBorn = true;
		} 
		
		reader.close();
		
		
	}
	

}














