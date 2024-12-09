package main.game.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.game.Player;

public class Map {
	private static final int[] ROCK_POSITIONS_X = { 0, 1, 2, 3, 4, 5, 6 };
	private static final int[] ROCK_POSITIONS_Y = { 0, 1, 2, 3, 4, 5, 6 };

	private String[][] scenario;
	private Point robotLocation;
	private HashMap<String, Point> treasureChests;
	private int mapWidth; // Declaração de mapWidth
	private int mapHeight; // Declaração de mapHeight

	public Map(int scenarioSizeX, int scenarioSizeY) {
		this.treasureChests = new HashMap<>();
		this.scenario = new String[scenarioSizeX][scenarioSizeY];
		this.robotLocation = new Point(0, 0);
		this.mapWidth = scenarioSizeX; // Inicialização de mapWidth
		this.mapHeight = scenarioSizeY; // Inicialização de mapHeight
		this.generateMap();
	}

	private String[][] generateMap() {
		this.scenario[this.robotLocation.getPositionX()][this.robotLocation.getPositionY()] = Player.CHARACTER;
		generateRocks();
		generateTreasureChests();
		generateMapOfTreasure();
		generateMonsters();

		return scenario;

	}

	private void generateMapOfTreasure() {
		Random random = new Random();
		int mapOfTreasureCount = 0;
		while (mapOfTreasureCount < 1) {
			int mapRandomX = random.nextInt(2, this.scenario.length);
			int mapRandomY = random.nextInt(2, this.scenario.length);
			if (scenario[mapRandomX][mapRandomY] == null) {
				this.scenario[mapRandomX][mapRandomY] = MapOfTreasure.CHARACTER;
				mapOfTreasureCount++;
			}
		}
	}

	private void generateTreasureChests() {
		Random random = new Random();
		int treasureChestCount = 0;
		List<String> treasureCharacters = new LinkedList<>();
		treasureCharacters.add(TreasureChest.CHEST_EMPTY_CHARACTER);
		treasureCharacters.add(TreasureChest.CHEST_TRAP_CHARACTER);
		treasureCharacters.add(TreasureChest.CHEST_TRESURE_CHARACTER);
		while (treasureChestCount < 3) {
			int treasureChestsX = random.nextInt(this.scenario.length);
			int treasureChestsY;
			if (treasureChestsX == (this.scenario[0].length - 1)) {
				treasureChestsY = random.nextInt(this.scenario[0].length);
			} else {
				treasureChestsY = this.scenario.length - 1;
			}

			if (scenario[treasureChestsX][treasureChestsY] == null) {
				scenario[treasureChestsX][treasureChestsY] = TreasureChest.CHARACTER;

				int index = random.nextInt(treasureCharacters.size());
				treasureChests.put(treasureCharacters.get(index), new Point(treasureChestsX, treasureChestsY));
				treasureCharacters.remove(index);
				treasureChestCount++;
			}
		}
	}

	private void generateMonsters() {
		Random random = new Random();
		List<Monster> monsters = new ArrayList<>();
		int monsterCount = 0;
		while (monsterCount < 3) {
			int monsterRandomX = random.nextInt(2, this.scenario.length - 1);
			int monsterRandomY = random.nextInt(2, this.scenario[0].length - 1);

			if (this.scenario[monsterRandomX][monsterRandomY] == null) {
				this.scenario[monsterRandomX][monsterRandomY] = Monster.CHARACTER;
				monsterCount++;
			}
		}

		for (int i = 0; i < monsters.size(); i++) {
			Point coordinate = monsters.get(i).getPoints();
			this.scenario[coordinate.getPositionX()][coordinate.getPositionY()] = Monster.CHARACTER;
		}
	}

	private void generateRocks() {

		Random random = new Random();

		List<Rock> rocks = new ArrayList<>();
		int rockCount = 0;
		while (rockCount < 3) {

			int indexRandomX = random.nextInt(ROCK_POSITIONS_X.length);
			int indexRandomY;
			if (indexRandomX < 2) {
				indexRandomY = random.nextInt(2, ROCK_POSITIONS_Y.length);
			} else {
				indexRandomY = random.nextInt(ROCK_POSITIONS_Y.length);
			}

			int positionX1 = ROCK_POSITIONS_X[indexRandomX];
			int positionY1 = ROCK_POSITIONS_X[indexRandomY];

			int positionX2 = ROCK_POSITIONS_X[indexRandomX];
			int positionY2 = ROCK_POSITIONS_X[indexRandomY] + 1;

			int positionX3 = ROCK_POSITIONS_X[indexRandomX] + 1;
			int positionY3 = ROCK_POSITIONS_X[indexRandomY];

			int positionX4 = ROCK_POSITIONS_X[indexRandomX] + 1;
			int positionY4 = ROCK_POSITIONS_X[indexRandomY] + 1;

			List<Point> rockPoints = new LinkedList<>();
			rockPoints.add(new Point(positionX1, positionY1));
			rockPoints.add(new Point(positionX2, positionY2));
			rockPoints.add(new Point(positionX3, positionY3));
			rockPoints.add(new Point(positionX4, positionY4));

			if (!rocks.isEmpty()) {
				boolean conflict = false;

				for (int i = 0; i < rocks.size(); i++) {
					Rock c = rocks.get(i);
					if (c.hasConflict(rockPoints)) {
						conflict = true;
						break;
					}
				}
				if (conflict) {
					continue;
				}
			}

			rocks.add(new Rock(rockPoints));
			rockCount++;

		}

		for (int i = 0; i < rocks.size(); i++) {
			List<Point> points = rocks.get(i).getPoints();
			for (int j = 0; j < points.size(); j++) {
				Point point = points.get(j);
				this.scenario[point.getPositionX()][point.getPositionY()] = Rock.CHARACTER;
			}
		}
	}

	public void print() {
		for (int i = 0; i < scenario.length; i++) {
			for (int j = 0; j < scenario[i].length; j++) {
				if (scenario[i][j] == null) {
					scenario[i][j] = "*";
				}
				if (j == (scenario[i].length - 1)) {
					System.out.println(scenario[i][j]);
				} else {
					System.out.print(scenario[i][j] + " ");
				}
			}
		}

	}

	public Point getRobotLocation() {
		return this.robotLocation;
	}

	public String get(Point point) {
		return this.scenario[point.getPositionX()][point.getPositionY()];
	}

	public void moveRobot(Point nextPoint) {
		this.scenario[nextPoint.getPositionX()][nextPoint.getPositionY()] = Player.CHARACTER;
		this.scenario[this.robotLocation.getPositionX()][this.robotLocation.getPositionY()] = "*";
		this.robotLocation = nextPoint;
	}

	public void openTreasureChest(Point nextPoint) {
		Iterator<String> it = treasureChests.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (treasureChests.get(key).equals(nextPoint)) {
				if (key.equals(TreasureChest.CHEST_TRESURE_CHARACTER)) {
					System.out.println("Parabéns você encontrou o tesouro!");
				} else if (key.equals(TreasureChest.CHEST_TRAP_CHARACTER)) {
					System.out.println("O jogo acabou! Você morreu, caiu em uma armadilha");
				} else {
					System.out.println("Aqui não tem nada");
				}
				this.scenario[nextPoint.getPositionX()][nextPoint.getPositionY()] = key;
				break;
			}
		}
	}

	public int[] getScenarioSize() {
		int[] size = { this.scenario.length, this.scenario[0].length };
		return size;
	}

	public Point getTreasureLocation() {
		for (String key : treasureChests.keySet()) {
			if (key.equals(TreasureChest.CHEST_TRESURE_CHARACTER)) {
				return treasureChests.get(key);
			}
		}
		return null; // Retorna null se não houver baú com tesouro
	}

	public List<Point> getNeighbors(Point point) {
		List<Point> neighbors = new ArrayList<>();
		int x = point.getPositionX();
		int y = point.getPositionY();

		// Verifica e adiciona cada vizinho ao redor (N, S, E, O)
		if (x > 0)
			neighbors.add(new Point(x - 1, y)); // Norte
		if (x < scenario.length - 1)
			neighbors.add(new Point(x + 1, y)); // Sul
		if (y > 0)
			neighbors.add(new Point(x, y - 1)); // Oeste
		if (y < scenario[0].length - 1)
			neighbors.add(new Point(x, y + 1)); // Leste

		return neighbors;
	}

	public boolean isMonster(Point point) {
		return scenario[point.getPositionX()][point.getPositionY()] != null
				&& scenario[point.getPositionX()][point.getPositionY()].equals(Monster.CHARACTER);
	}

	public boolean isSafePoint(Point point) {
		String space = this.get(point);
		return space == null || (!space.equals(Monster.CHARACTER) && !space.equals(Rock.CHARACTER));
	}

	public boolean isWithinBounds(Point point) {
		return point.getPositionX() >= 0 && point.getPositionX() < mapWidth && point.getPositionY() >= 0
				&& point.getPositionY() < mapHeight;
	}

	public List<Point> getAllTreasures() {
		List<Point> treasureLocations = new ArrayList<>();
		for (String key : treasureChests.keySet()) {
			// Verifica se o baú contém um tesouro
			if (key.equals(TreasureChest.CHEST_TRESURE_CHARACTER)) {
				treasureLocations.add(treasureChests.get(key));
			}
		}
		return treasureLocations; // Retorna a lista de locais dos tesouros
	}

	public Point findPointByChar(char targetChar) {
		for (int i = 0; i < scenario.length; i++) {
			for (int j = 0; j < scenario[i].length; j++) {
				if (scenario[i][j] != null && scenario[i][j].equals(String.valueOf(targetChar))) {
					return new Point(i, j); // Retorna o ponto onde o caractere foi encontrado
				}
			}
		}
		return null; // Retorna null se o caractere não for encontrado
	}

	public void revealTreasure(Point location) {
		// Substitui o conteúdo do baú com o tesouro no mapa
		this.scenario[location.getPositionX()][location.getPositionY()] = "F"; // Marca o tesouro
	}
	


}
