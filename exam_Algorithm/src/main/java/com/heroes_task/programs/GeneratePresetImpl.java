package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        var random = new Random();
        var countOfUnits = new int[unitList.size()];
        var grid = new Unit[DIMENSION_X][DIMENSION_Y];
        var allCosts = 0;
        var indexType = 0;
        var selectedUnits = new ArrayList<Unit>();
        for (int index = 0; index < MAX_COUNT_UNITS_FOR_TYPE * unitList.size(); index++) {
            var localIndex = indexType;
            var template = unitList.get(indexType);
            indexType++;
            if (indexType >= unitList.size()) {
                indexType = 0;
            }

            // Проверяем хватит ли ресурсов на покупку
            if (maxPoints - allCosts < template.getCost()) {
                continue;
            }

            // Проверяем не превышен ли лимит по типу юнитов
            var count = countOfUnits[localIndex];
            if (count == MAX_COUNT_UNITS_FOR_TYPE) {
                continue;
            }

            count++;
            countOfUnits[localIndex] = count;
            selectedUnits.add(buildUnitFromTemplate(template, count, random, grid));
            allCosts += template.getCost();
        }

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
     * @param grid сетка размещения юнитов на карте
     * @return готовый юнит
     */
    private Unit buildUnitFromTemplate(final Unit template, final int index, final Random random, final Unit[][] grid) {
        int xCoordinate, yCoordinate;
        do {
            xCoordinate = random.nextInt(DIMENSION_X);
            yCoordinate = random.nextInt(DIMENSION_Y);
        } while (grid[xCoordinate][yCoordinate] != null);
        var name = template.getUnitType() + " " + index;
        grid[xCoordinate][yCoordinate] = new Unit(
                name,
                template.getUnitType(),
                template.getHealth(),
                template.getBaseAttack(),
                template.getCost(),
                template.getAttackType(),
                template.getAttackBonuses(),
                template.getDefenceBonuses(),
                xCoordinate,
                yCoordinate);
        return grid[xCoordinate][yCoordinate];
    }
}