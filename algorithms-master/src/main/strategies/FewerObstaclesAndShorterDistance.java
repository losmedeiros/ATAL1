package main.strategies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.game.map.Map;
import main.game.map.Point;

public class FewerObstaclesAndShorterDistance implements Strategy {

	private Set<Point> visitedPoints = new HashSet<>(); // Rastrear os pontos visitados

	@Override
	public Point evaluatePossbileNextStep(List<Point> possibleSteps, Map map) {
	    if (possibleSteps == null || possibleSteps.isEmpty()) {
	        return null; // Nenhum passo possível
	    }

	    Point robotLocation = map.getRobotLocation();
	    visitedPoints.add(robotLocation); // Marcar o ponto atual como visitado

	    Point treasureMapLocation = map.findPointByChar('B'); // Local do mapa do tesouro
	    Point treasureLocation = map.findPointByChar('F'); // Local do tesouro, se já revelado

	    // Priorizar alcançar "B"
	    if (treasureMapLocation != null && !visitedPoints.contains(treasureMapLocation)) {
	        return findShortestPathAvoidingVisited(possibleSteps, treasureMapLocation);
	    }

	    // Após alcançar "B", revelar "F"
	    if (treasureMapLocation != null && robotLocation.equals(treasureMapLocation)) {
	        revealAndMoveToTreasure(robotLocation, map); // Revela e move para "F"
	        treasureLocation = map.findPointByChar('F'); // Atualiza a posição de "F" após revelação
	    }

	    // Seguir para "F" após ele ser revelado
	    if (treasureLocation != null) {
	        return findShortestPathAvoidingVisited(possibleSteps, treasureLocation);
	    }

	    // Caso improvável, continuar explorando até encontrar "B"
	    return explore(possibleSteps);
	}


	private void openTreasureChest(Point location, Map map) {
		// Verifica se o tesouro foi revelado e o atualiza no mapa
		map.openTreasureChest(location); // Revela o tesouro "F"
		System.out.println("Tesouro revelado no mapa!"); // Log para depuração
		// O tesouro "F" será colocado no local apropriado no mapa, por exemplo
		map.revealTreasure(location);
	}

	private Point findShortestPathAvoidingVisited(List<Point> possibleSteps, Point target) {
		Point bestStep = null;
		double shortestDistance = Double.MAX_VALUE;

		for (Point step : possibleSteps) {
			if (!visitedPoints.contains(step)) { // Evitar pontos já visitados
				double distance = calculateDistance(step, target);
				if (distance < shortestDistance) {
					shortestDistance = distance;
					bestStep = step;
				}
			}
		}

		return bestStep;
	}

	private Point explore(List<Point> possibleSteps) {
		for (Point step : possibleSteps) {
			if (!visitedPoints.contains(step)) {
				return step; // Explorar um ponto não visitado
			}
		}
		return possibleSteps.get(0); // Padrão caso todos já tenham sido visitados
	}

	private double calculateDistance(Point from, Point to) {
		return Math.sqrt(Math.pow(from.getPositionX() - to.getPositionX(), 2)
				+ Math.pow(from.getPositionY() - to.getPositionY(), 2));
	}
	private void revealAndMoveToTreasure(Point location, Map map) {
	    // Revela o baú e substitui pelo tesouro
	    map.openTreasureChest(location);  // Revela o baú
	    System.out.println("Tesouro revelado no mapa!");  // Log para depuração
	    
	    // Substitui o baú por "F" no mapa
	    map.revealTreasure(location); // Coloca "F" no local do baú
	    // Move o robô para o local do tesouro
	    map.moveRobot(location);
	}
}
