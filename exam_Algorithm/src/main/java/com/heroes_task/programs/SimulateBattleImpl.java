package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Симуляция битвы
 * Реализация ходов игроков по очереди
 */
public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    /**
     * Метод симуляции битвы
     * Сложность: O(n+m). (n - размер армии игрока, m - размер армии компьютера)
     * По сути реализованы ходы по очереди, боец каждый раз выбирается случайным образом
     *  - выполняем цикл, пока одна из армий не опустеет
     *  - делаем ход игрока, и удаляем вражеский юнит, если нужно
     *  - делаем ход компьютера, и удаляем вражеский юнит, если нужно
     *
     * @param playerArmy армия игрока
     * @param computerArmy армия компьютера
     * @throws InterruptedException если получили ошибку при вызове метода атаки
     */
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        var playerUnits = playerArmy.getUnits();
        var computerUnits = computerArmy.getUnits();

        try {
            var random = new Random();
            do {
                doAttackRandomUnit(playerUnits, computerUnits, random);
                doAttackRandomUnit(computerUnits, playerUnits, random);
            } while (!(playerUnits.isEmpty() || computerUnits.isEmpty()));
        } catch (Exception e) {
            System.err.println("SimulateBattleImpl: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Реализация атаки случайного юнита
     *
     * @param attackingArmy атакующая армия
     * @param randomGenerator генератор случайных чисел
     * @throws InterruptedException если получили ошибку при вызове метода атаки
     */
    private void doAttackRandomUnit(final List<Unit> attackingArmy, final List<Unit> anotherArmy,
                                    final RandomGenerator randomGenerator) throws InterruptedException {
        var unitCount = attackingArmy.size();
        if (unitCount == 0) {
            return;
        }

        var indexUnit = randomGenerator.nextInt(unitCount);
        var unit = attackingArmy.get(indexUnit);
        if (!unit.isAlive()) {
            // Сюда мы по идее попадать не должны, но игровой движок для меня черный ящик,
            // и нет уверенности в гарантированном удалении юнитов,
            // так что если выбранный юнит не очень живой, то повторяем попытку найти живого юнита, а этого удаляем
            attackingArmy.remove(unit);
            doAttackRandomUnit(attackingArmy, anotherArmy, randomGenerator);
            return;
        }

        var victim = unit.getProgram().attack();
        if (victim != null) {
            printBattleLog.printBattleLog(unit, victim);
            if (!victim.isAlive()) {
                anotherArmy.remove(victim);
            }
        }
    }
}