package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Поиск юнитов, подходящих для атаки
 */
public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    private static final int DIMENSION_Y = 21;

    /**
     * Метод определяет список юнитов, подходящих для атаки, для атакующего юнита одной из армий.
     * Цель метода — исключить ненужные попытки найти кратчайший путь между юнитами,
     * которые не могут атаковать друг друга.
     * Сложность: O(n*m) - (n - количество колонок, m - количество юнитов в колонке)
     *  - делаю обход колонок в цикле, в зависимости от того с какой стороны армия, это будет прямой или обратный обход
     *  - при обходе запоминаю юниты колонки в массив, при следующей итерации использую сохраненные данные,
     *    чтоб понять есть ли юнит перед текущим в этой же строке
     *
     * @param unitsByRow юниты по строкам
     * @param isLeftArmyTarget параметр, указывающий, юниты какой армии подвергаются атаке
     * @return список юнитов, подходящих для атаки
     */
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        var resultList = new ArrayList<Unit>();

        var prevRow = new Unit[DIMENSION_Y];
        var iterator = getIterator(unitsByRow, isLeftArmyTarget);
        while(iterator.hasNext()) {
            List<Unit> row = iterator.next();
            row.forEach(unit -> {
                // Если по этой линии нет другого юнита (видимость по координате Y), считаем что юнит доступен
                if (prevRow[unit.getyCoordinate()] == null) {
                    resultList.add(unit);
                }

                prevRow[unit.getyCoordinate()] = unit;
            });
        }

        return resultList;
    }

    /**
     * Метод определяет итератор для обхода строк
     * Если атакуется левая армия - нужен обратный итератор
     * Если атакуется правая армия - нужен прямой итератор
     *
     * @param unitsByRow юниты по строкам
     * @param isLeftArmyTarget параметр, указывающий, юниты какой армии подвергаются атаке
     * @return прямой или обратный итератор
     */
    private <T> Iterator<T> getIterator(List<T> unitsByRow, boolean isLeftArmyTarget) {
        if (isLeftArmyTarget) {
            // Доступные для атаки юниты у левой армии - нужен обратный итератор
            return new ReverseIterator<>(unitsByRow);
        } else {
            // Доступные для атаки юниты у правой армии - нужен прямой итератор
            return unitsByRow.iterator();
        }
    }

    /**
     * Обратный итератор
     *
     * @param <T> тип элементов итератора
     */
    public static class ReverseIterator<T> implements Iterator<T> {
        private final List<T> list;
        private int position;

        public ReverseIterator(List<T> list) {
            this.list = list;
            this.position = list.size();
        }

        @Override
        public boolean hasNext() {
            return position > 0;
        }

        @Override
        public T next() {
            position--;
            return list.get(position);
        }
    }
}
