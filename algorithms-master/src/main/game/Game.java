package main.game;

import main.game.map.Map;
import main.game.map.Point;
import main.game.map.TreasureChest;
import main.strategies.FewerObstaclesAndShorterDistance;

public class Game {
	private Map map;
	private Player player;
	private static final int MAX_ATTEMPTS = 100;

	public Game() {
		this.map = new Map(8, 8);
		// Aqui você pode definir a estratégia desejada.
		// Use FewerObstacles, FewerObstaclesAndShorterDistance, ShortestDistance
		// ou Sort
		this.player = new Player(new FewerObstaclesAndShorterDistance());
	}

	public void run() {
		this.map.print();
		System.out.println();
		int attempts = 0;

		while (true) {
			Point nextPoint = this.player.evaluatePossbileNextStep(map);
			if (nextPoint == null || attempts >= MAX_ATTEMPTS) {
				System.out.println("Jogo finalizado: Limite de tentativas alcançado ou sem movimentos disponíveis.");
				break;
			} else {
				String space = this.map.get(nextPoint);
				if (space != null && space.equals(TreasureChest.CHARACTER)) {
					this.map.openTreasureChest(nextPoint);
					this.map.print();
					break;
				} else {
					this.map.moveRobot(nextPoint);
				}
			}
			this.map.print();
			System.out.println();
			attempts++;
		}
	}
}
