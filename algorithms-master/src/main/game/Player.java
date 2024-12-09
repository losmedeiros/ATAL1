package main.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import main.game.map.Map;
import main.game.map.Point;
import main.strategies.Strategy;

public class Player {
	public static final String CHARACTER = "W";
	private Strategy strategy;

	public Player(Strategy strategy) {
		this.strategy = strategy;
	}

	public Point evaluatePossbileNextStep(Map map) {
		Point robotLocation = map.getRobotLocation();
		List<Point> possibleNextSteps = new ArrayList<>();

		// Definir movimentos possíveis
		possibleNextSteps.add(new Point(robotLocation.getPositionX(), robotLocation.getPositionY() + 1)); // direita
		possibleNextSteps.add(new Point(robotLocation.getPositionX() + 1, robotLocation.getPositionY())); // abaixo
		possibleNextSteps.add(new Point(robotLocation.getPositionX() - 1, robotLocation.getPositionY())); // acima
		possibleNextSteps.add(new Point(robotLocation.getPositionX(), robotLocation.getPositionY() - 1)); // esquerda

		// Filtrar pontos seguros (sem monstros ou rochas)
		List<Point> safeSteps = new LinkedList<>();
		for (Point step : possibleNextSteps) {
			if (map.isWithinBounds(step) && map.isSafePoint(step)) { // Verifica limites antes de verificar segurança
				safeSteps.add(step);
			}
		}

		// Avaliar próximo passo com a estratégia
		return this.strategy.evaluatePossbileNextStep(safeSteps, map);
	}

}
