package main.strategies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.game.map.Map;
import main.game.map.Point;

public class FewerObstacles implements Strategy {

	/**
	 * N is the next location p1 p2 p3 p4 N p5 p6 p7 p8
	 */

	private Set<Point> visitedPoints = new HashSet<>(); // Armazena pontos já visitados

	@Override
	public Point evaluatePossbileNextStep(List<Point> possibleNextSteps, Map map) {
		// Obtém todas as localizações dos tesouros
		List<Point> treasures = map.getAllTreasures();
		Point safestTreasure = null;
		int fewestMonsters = Integer.MAX_VALUE;

		// Avalia cada tesouro em relação ao número de monstros ao seu redor
		for (Point treasure : treasures) {
			int monsterCount = countNearbyMonsters(treasure, map); // Conta os monstros ao redor do tesouro
			if (monsterCount < fewestMonsters) {
				fewestMonsters = monsterCount; // Atualiza a menor contagem de monstros
				safestTreasure = treasure; // Atualiza o tesouro mais seguro
			}
		}

		// Se houver um tesouro seguro, escolha o melhor passo em direção a ele
		if (safestTreasure != null) {
			Point bestStep = getBestStepTowardsTreasure(possibleNextSteps, safestTreasure, map);
			if (bestStep != null) {
				visitedPoints.add(bestStep); // Adiciona o ponto escolhido aos pontos visitados
			}
			return bestStep;
		}

		return null; // Retorna null se não houver tesouros
	}

	// Método para contar monstros ao redor de um ponto
	private int countNearbyMonsters(Point point, Map map) {
		int count = 0;
		List<Point> neighbors = map.getNeighbors(point);

		// Conta os monstros ao redor do ponto
		for (Point neighbor : neighbors) {
			if (map.isMonster(neighbor)) {
				count++; // Incrementa a contagem se for um monstro
			}
		}

		return count; // Retorna o número total de monstros encontrados
	}

	// Método para obter o melhor passo em direção ao tesouro mais seguro
	private Point getBestStepTowardsTreasure(List<Point> possibleNextSteps, Point treasure, Map map) {
		Point bestStep = null;
		double shortestDistance = Double.MAX_VALUE;

		// Itera sobre os passos seguros possíveis
		for (Point step : possibleNextSteps) {
			if (isStepValid(step, map) && !visitedPoints.contains(step)) { // Verifica se o passo é válido e não
																			// visitado
				double distance = calculateDistance(step, treasure); // Calcula a distância até o tesouro mais seguro

				// Se a distância for menor que a menor distância registrada
				if (distance < shortestDistance) {
					shortestDistance = distance;
					bestStep = step; // Atualiza o melhor passo
				}
			}
		}

		return bestStep; // Retorna o passo com a menor distância ao tesouro mais seguro
	}

	// Método para calcular a distância euclidiana entre dois pontos
	private double calculateDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.getPositionX() - p2.getPositionX(), 2)
				+ Math.pow(p1.getPositionY() - p2.getPositionY(), 2));
	}

	// Método para verificar se um passo é válido (sem barreiras)
	private boolean isStepValid(Point step, Map map) {
		return map.isSafePoint(step); // Retorna true se o ponto for seguro (sem monstros e sem barreiras)
	}
}
