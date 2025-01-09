package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Размеры игрового поля
        final int WIDTH = 27;
        final int HEIGHT = 21;

        // Координаты начальной и целевой точек
        Edge start = new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Edge goal = new Edge(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        // Проверка: если начальная и целевая точки совпадают
        if (start.equals(goal)) {
            return List.of(start);
        }

        // Множество занятых клеток
        Set<Edge> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()) {
                occupiedCells.add(new Edge(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }

        // Карта для хранения минимальных расстояний
        Map<Edge, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        // Карта для восстановления пути
        Map<Edge, Edge> cameFrom = new HashMap<>();

        // Очередь с приоритетом для поиска минимального расстояния
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(start);

        // Основной цикл алгоритма Дейкстры
        while (!queue.isEmpty()) {
            Edge current = queue.poll();

            // Если достигли цели, восстанавливаем путь
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            // Обрабатываем соседей
            for (Edge neighbor : getNeighbors(current, WIDTH, HEIGHT)) {
                // Пропускаем занятые клетки
                if (occupiedCells.contains(neighbor)) continue;

                // Вычисляем новое расстояние до соседа
                int newDistance = distances.getOrDefault(current, Integer.MAX_VALUE) + 1;

                // Если нашли более короткий путь к соседу
                if (newDistance < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDistance);
                    cameFrom.put(neighbor, current);

                    // Добавляем соседа в очередь
                    if (!queue.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Если путь не найден, возвращаем пустой список
        return Collections.emptyList();
    }

    // Восстановление пути
    private List<Edge> reconstructPath(Map<Edge, Edge> cameFrom, Edge current) {
        List<Edge> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        path.add(0, current); // Добавляем стартовую точку
        return path;
    }

    // Получение соседей
    private List<Edge> getNeighbors(Edge edge, int width, int height) {
        List<Edge> neighbors = new ArrayList<>();
        int x = edge.getX();
        int y = edge.getY();

        if (x > 0) neighbors.add(new Edge(x - 1, y));
        if (x < width - 1) neighbors.add(new Edge(x + 1, y));
        if (y > 0) neighbors.add(new Edge(x, y - 1));
        if (y < height - 1) neighbors.add(new Edge(x, y + 1));

        return neighbors;
    }
}
