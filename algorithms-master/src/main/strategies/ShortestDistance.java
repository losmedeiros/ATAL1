package main.strategies;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import main.game.map.Map;
import main.game.map.Point;

public class ShortestDistance implements Strategy {

    @Override
    public Point evaluatePossbileNextStep(List<Point> possibleNextSteps, Map map) {
        Point bestStep = null;
        int minSteps = Integer.MAX_VALUE;

        // Obtém a localização do robô
        Point robotLocation = map.getRobotLocation();

        // Obtém todos os tesouros do mapa
        List<Point> treasureLocations = map.getAllTreasures();

        // Para cada tesouro, calcula o menor caminho a partir da localização do robô
        for (Point treasure : treasureLocations) {
            int steps = getShortestPath(robotLocation, treasure, map);
            if (steps < minSteps) {
                minSteps = steps;
                bestStep = treasure; // Armazena o melhor tesouro encontrado
            }
        }

        // Se encontramos um tesouro e ele está em um dos passos possíveis, escolhemos o melhor passo
        if (bestStep != null) {
            return findBestNextStep(possibleNextSteps, robotLocation, bestStep, map);
        }

        return null; // Retorna null se não houver passos válidos
    }

    // Método para calcular o menor caminho utilizando busca em largura
    private int getShortestPath(Point start, Point target, Map map) {
        if (start.equals(target)) {
            return 0; // Se já estiver no tesouro
        }

        // Fila para explorar os pontos
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        Queue<Integer> stepsQueue = new LinkedList<>(); // Fila para armazenar o número de passos

        // Inicializa a fila com o ponto de partida
        queue.add(start);
        visited.add(start);
        stepsQueue.add(0); // Adiciona 0 passos

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int steps = stepsQueue.poll(); // Obtém o número de passos para o ponto atual

            // Verifica se chegou ao tesouro
            if (current.equals(target)) {
                return steps; // Retorna o número de passos
            }

            // Adiciona vizinhos
            for (Point neighbor : map.getNeighbors(current)) {
                if (map.isSafePoint(neighbor) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    stepsQueue.add(steps + 1); // Incrementa o número de passos
                }
            }
        }

        return Integer.MAX_VALUE; // Retorna um valor alto se o tesouro não for alcançável
    }

    // Método para encontrar o melhor próximo passo em direção ao tesouro mais próximo
    private Point findBestNextStep(List<Point> possibleNextSteps, Point robotLocation, Point bestTreasure, Map map) {
        // Escolhe o passo seguro que leva mais perto do tesouro
        Point bestStep = null;
        int minDistance = Integer.MAX_VALUE;

        // Itera sobre os passos seguros possíveis
        for (Point step : possibleNextSteps) {
            int distance = getShortestPath(step, bestTreasure, map); // Calcula a distância até o tesouro mais próximo
            if (distance < minDistance) {
                minDistance = distance;
                bestStep = step;
            }
        }

        return bestStep; // Retorna o passo que leva mais perto do tesouro
    }
}
