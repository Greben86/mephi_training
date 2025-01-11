package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Определение пути между юнитами
 */
public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    /**
     * Метод определяет кратчайший маршрут между атакующим и атакуемым юнитом и возвращает его в виде списка объектов
     * содержащих координаты каждой точки данного кратчайшего пути.
     * Сложность: O(n * m * log(n * m)). (n - ширина, m - высота поля)
     *  - использую цикл, количество итераций не может превышать количество клеток игрового поля (n * m)
     *  - для каждой клетки добавляется значение дистанции в приоритетную очередь, тут сложность получается ~ log(n * m)
     *
     * @param attackUnit атакующий юнит
     * @param victimUnit атакуемый юнит
     * @param unitList список всех существующих юнитов
     * @return коллекция точек пути между юнитами
     */
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit victimUnit, List<Unit> unitList) {
        // Массив расстояний
        var distance = new int[WIDTH][HEIGHT];
        for (var row : distance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        // Цепочка точек пути, каждый элемент содержит ссылку на предыдущий
        var chain = new Edge[WIDTH][HEIGHT];

        // Заполнение занятых клеток
        var grid = new Unit[WIDTH][HEIGHT];
        unitList.forEach(unit ->
                grid[unit.getxCoordinate()][unit.getyCoordinate()] =
                        unit.equals(attackUnit) || unit.equals(victimUnit) || !unit.isAlive() ? null : unit);

        var queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));
        // Стартовая точка - расположение атакующего юнита
        distance[attackUnit.getxCoordinate()][attackUnit.getyCoordinate()] = 0;
        var startPoint = new EdgeDistance(attackUnit.getxCoordinate(), attackUnit.getyCoordinate(),
                distance[attackUnit.getxCoordinate()][attackUnit.getyCoordinate()]);
        queue.add(startPoint);

        // Выполняем алгоритм поиска пути
        while (queue.peek() != null) {
            var current = queue.poll();
            if (current.getX() == victimUnit.getxCoordinate() && current.getY() == victimUnit.getyCoordinate()) {
                // Цель достигнута
                return buildPath(chain, attackUnit, victimUnit);
            }

            getNeighbors(current).forEach(neighbor -> checkCellNear(current, neighbor, grid, distance, chain, queue));
        }

        return Collections.emptyList();
    }

    /**
     * Поиск соседних клеток
     *
     * @param current текущие координаты
     * @return список соседних клеток
     */
    private List<Edge> getNeighbors(final EdgeDistance current) {
        List<Edge> neighbors = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();

        // Сосед слева
        if (x > 0) {
            neighbors.add(new Edge(x - 1, y));
        }
        // Сосед справа
        if (x < WIDTH - 1) {
            neighbors.add(new Edge(x + 1, y));
        }
        // Сосед сверху
        if (y > 0) {
            neighbors.add(new Edge(x, y - 1));
        }
        // Сосед снизу
        if (y < HEIGHT - 1) {
            neighbors.add(new Edge(x, y + 1));
        }

        return neighbors;
    }

    /**
     * Проверка соседней клетки и поиск оптимального пути
     *
     * @param current текущий элемент пути
     * @param neighbor соседняя клетка
     * @param grid массив заполненности игрового поля
     * @param distance массив дистанций
     * @param chain цепочка точек пути
     * @param queue очередь элементов пути
     */
    private void checkCellNear(EdgeDistance current, Edge neighbor, Unit[][] grid, int[][] distance,
                               Edge[][] chain, PriorityQueue<EdgeDistance> queue) {
        int nearX = neighbor.getX();
        int nearY = neighbor.getY();
        if (nearX < 0 || nearX >= WIDTH || nearY < 0 || nearY >= HEIGHT) {
            // Вышли за пределы игрового поля
            return;
        }
        if (grid[nearX][nearY] != null) {
            // В клетке уже кто-то есть
            return;
        }

        int newDistance = distance[current.getX()][current.getY()] + 1;
        var newEdgeDist = new EdgeDistance(nearX, nearY, newDistance);
        if (newDistance < distance[nearX][nearY] && !queue.contains(newEdgeDist)) {
            distance[nearX][nearY] = newDistance;
            chain[nearX][nearY] = new Edge(current.getX(), current.getY());
            queue.add(newEdgeDist);
        }
    }

    /**
     * Постройка пути
     *
     * @param chain цепочка точек пути
     * @param attackUnit атакующий юнит
     * @param victimUnit атакуемый юнит
     * @return коллекция точек пути
     */
    private List<Edge> buildPath(Edge[][] chain, Unit attackUnit, Unit victimUnit) {
        var path = new ArrayDeque<Edge>();

        int valueX = victimUnit.getxCoordinate();
        int valueY = victimUnit.getyCoordinate();
        while (valueX != attackUnit.getxCoordinate() || valueY != attackUnit.getyCoordinate()) {
            path.push(new Edge(valueX, valueY));
            // получаем предыдущие координаты
            var prev = chain[valueX][valueY];
            if (prev == null) {
                // Если координата обрывается, то считаем что путь не найден
                return Collections.emptyList();
            }
            valueX = prev.getX();
            valueY = prev.getY();
        }
        path.push(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));
        return new ArrayList<>(path);
    }
}
