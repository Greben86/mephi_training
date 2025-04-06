package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Симуляция битвы
 * Реализация ходов игроков по очереди
 * Реализация атаки случайного юнита
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
    public void simulate(final Army playerArmy, final Army computerArmy) throws InterruptedException {
        var iterator = new BatleIterator(playerArmy.getUnits(), computerArmy.getUnits());
        while (iterator.hasNext()) {
            var unit = iterator.next();
            var victim = unit.getProgram().attack();
            if (victim != null) {
                printBattleLog.printBattleLog(unit, victim);
                if (!victim.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Итератор юнитов из двух армий, возвращает юнитов из одной и другой армии по очереди
     */
    private static class BatleIterator implements Iterator<Unit> {
        private final Random random = new Random();
        private final List<Unit> playerArmy;
        private final List<Unit> computerArmy;
        private boolean switchFlag = true;

        public BatleIterator(final List<Unit> playerArmy, final List<Unit> computerArmy) {
            this.playerArmy = playerArmy;
            this.computerArmy = computerArmy;
        }

        @Override
        public boolean hasNext() {
            return !playerArmy.isEmpty() && !computerArmy.isEmpty();
        }

        @Override
        public Unit next() {
            var attackingArmy = switchFlag ? playerArmy : computerArmy;
            switchFlag = !switchFlag;
            var index = random.nextInt(attackingArmy.size());
            return attackingArmy.get(index);
        }

        /**
         * Удаляем всех павших юнитов в армии которую атаковали последний раз
         * Сложность O(N), можно было бы прямо сюда передавать атакованный юнит, но сложность бы не изменилась
         * {@code java.util.Collection#remove(java.lang.Object)} все равно имеет сложность O(N),
         * а так контракт итератора сохраняется
         */
        @Override
        public void remove() {
            var attackedArmy = switchFlag ? playerArmy : computerArmy;
            attackedArmy.removeIf(this::isDied);
        }

        private boolean isDied(Unit unit) {
            return unit == null || !unit.isAlive();
        }
    }
}