package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Генерация пресета
 */
public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_COUNT_UNITS_FOR_TYPE = 11;
    private static final int DIMENSION_X = 3;
    private static final int DIMENSION_Y = 21;

    /**
     * Метод генерации пресета, сложность O(n*m), где n - число типов юнитов, а m - максимальное число юнитов в
     * армии одного типа, так как максимально возможное количество юнитов на карте это 11 * [количество типов],
     * по этому алгоритм такой:
     *  - выполняю 11 * [количество типов] итераций в цикле
     *  - постепенно добавляю юниты каждого типа, пока их не станет 11 или не кончатся ресурсы
     *  - это сделано чтобы типы юнитов распределились равномерно
     *
     * @param unitList список типов юнитов
     * @param maxPoints максимальная стоимость армии
     * @return сгенерированная армия
     */
    @Override
    public Army generate(final List<Unit> unitList, final int maxPoints) {
        // Выполняем сортировку типов юнитов, так чтобы сначала шли типы юнитов с самой сильной атакой и самым большим здоровьем
        unitList.sort((unitLeft, unitRight) -> {
            double attackLeft = (double) unitLeft.getBaseAttack() / unitLeft.getCost();
            double attackRight = (double) unitRight.getBaseAttack() / unitRight.getCost();
            if (Double.compare(attackRight, attackLeft) != 0) {
                return Double.compare(attackRight, attackLeft);
            } else {
                double healthLeft = (double) unitLeft.getHealth() / unitLeft.getCost();
                double healthRight = (double) unitRight.getHealth() / unitRight.getCost();
                return Double.compare(healthRight, healthLeft);
            }
        });

        var random = new Random();
        var selectedUnits = new ArrayList<Unit>(MAX_COUNT_UNITS_FOR_TYPE * unitList.size());
        // Список всех строчек на карте с количеством свободных мест (максимум 3)
        var lines = Stream.iterate(0, n -> ++n)
                .limit(DIMENSION_Y)
                .map(Line::new)
                .collect(Collectors.toList());
        var allCosts = 0;
        for (var template : unitList) {
            // Максимальное количество юнитов этого типа - или 11 и на сколько хватит ресурсов
            int maxUnitsForType = Math.min(MAX_COUNT_UNITS_FOR_TYPE, (maxPoints - allCosts) / template.getCost());
            for (int index = 1; index <= maxUnitsForType; index++) {
                selectedUnits.add(buildUnitFromTemplate(template, index, random, lines));
                allCosts += template.getCost();
            }
        }

        System.out.printf("Добавлено юнитов: %d, использовано ресурсов: %d%n", selectedUnits.size(), allCosts);
        var computerArmy = new Army();
        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(allCosts);
        return computerArmy;
    }

    /**
     * Создание нового юнита с уникальным именем и координатами
     *
     * @param template шаблон юнита
     * @param index номер юнита
     * @param random генератор случайных чисел
     * @param lines список доступных строк для добавления юнитов в них,
     *              если в строке юнитов становится 3, то такая строка удаляется из набора
     * @return готовый юнит
     */
    private Unit buildUnitFromTemplate(final Unit template,
                                       final int index,
                                       final Random random,
                                       final List<Line> lines) {
        int position = random.nextInt(lines.size());
        var name = template.getUnitType() + " " + index;
        Unit newUnit = new Unit(
                name,
                template.getUnitType(),
                template.getHealth(),
                template.getBaseAttack(),
                template.getCost(),
                template.getAttackType(),
                template.getAttackBonuses(),
                template.getDefenceBonuses(),
                lines.get(position).decrementAndGet(),
                lines.get(position).getIndex());
        if (newUnit.getxCoordinate() == 0) {
            // Этот юнит уже в самой левой колонке, больше юнитов в эту строку не влезет - удаляем из набора
            lines.remove(position);
        }
        return newUnit;
    }

    /**
     * Линия юнитов на карте
     * содержит номер строки и количество свободных мест
     */
    private final class Line {
        private final int index;
        private int count = DIMENSION_X;

        public Line(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public int decrementAndGet() {
            count--;
            return count;
        }
    }
}