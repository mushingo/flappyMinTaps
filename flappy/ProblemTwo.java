package flappy;
import java.io.*;

public class ProblemTwo {

	public static void main(String[] args) throws Exception {
			String ls = System.getProperty("line.separator");

			long startTime = System.nanoTime();
			
			Game game = new Game(args[0] + ".in");
			
			String filename = game.filename + ".out";
			System.out.println(filename);
			FileWriter output = new FileWriter(filename);
			
			
			if (game.stillBorn == true) {
				output.write(0 + ls + 0);
			} else {
				game.calc_path();		
				
				if (game.survived) {
					output.write(1 + ls + game.totalTaps);
				} else {
					output.write(0 + ls + game.clearedPipes);
				}
			}
			
			long elapsedTime = System.nanoTime() - startTime;
			
			double milli = elapsedTime / 1000000000.0;
			
			System.out.println(milli + "s\n");
			output.close();


	}
}
