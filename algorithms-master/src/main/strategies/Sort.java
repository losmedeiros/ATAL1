package main.strategies;

import java.util.List;
import java.util.Random;

import main.game.map.Map;
import main.game.map.Point;

public class Sort implements Strategy {
	/**
	 * N is the next location p1 p2 p3 p4 N p5 p6 p7 p8
	 */
	private final Random random = new Random();

	@Override
	public Point evaluatePossbileNextStep(List<Point> possibleNextSteps, Map map) {
		int index = random.nextInt(possibleNextSteps.size());
		return possibleNextSteps.get(index);
	}
}
