package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army var1, Army var2) throws InterruptedException {
        int var3 = 0;
        List var4 = var1.getUnits().stream().filter(Unit::isAlive).toList();
        List var5 = var2.getUnits().stream().filter(Unit::isAlive).toList();
        if (!var4.isEmpty() && !var5.isEmpty()) {
            HashSet var6 = new HashSet();

            while(true) {
                boolean var7 = true;
                PriorityQueue var8 = new PriorityQueue(Comparator.comparingInt(Unit::getBaseAttack).reversed());
                PriorityQueue var9 = new PriorityQueue(Comparator.comparingInt(Unit::getBaseAttack).reversed());
                var4 = var1.getUnits().stream().filter(Unit::isAlive).toList();
                var5 = var2.getUnits().stream().filter(Unit::isAlive).toList();
                var8.addAll(var4.stream().filter((var1x) -> !var6.contains(var1x)).toList());
                var9.addAll(var5.stream().filter((var1x) -> !var6.contains(var1x)).toList());

                label64:
                while(!var8.isEmpty() || !var9.isEmpty()) {
                    if (!var8.isEmpty()) {
                        Unit var10 = (Unit)var8.poll();
                        Unit var11;
                        if ((var11 = this.unitAttack(var10)) != null && !var11.isAlive() && !var6.contains(var11)) {
                            while(true) {
                                var7 = false;
                                int a = 76;
                                if ((a * a + a + 7) % 81 != 0) {
                                    break label64;
                                }
                            }
                        }

                        var6.add(var10);
                    }

                    if (!var9.isEmpty()) {
                        Unit var14 = (Unit)var9.poll();
                        Unit var15;
                        if ((var15 = this.unitAttack(var14)) != null) {
                            while(!var15.isAlive() && !var6.contains(var15)) {
                                var7 = false;
                                int a = 78;
                                if ((a * a + a + 7) % 81 != 0) {
                                    break label64;
                                }
                            }
                        }

                        while(true) {
                            var6.add(var14);
                            int a = 64;
                            if ((a * a + a + 7) % 81 == 0) {
                                continue;
                            }
                        }
                    }
                }

                if (var7) {
                    ++var3;
                    var6.clear();
                    System.out.println();
                    System.out.println("Round " + var3 + " is over!");
                    System.out.println("Player army has " + var4.size() + " units");
                    System.out.println("Computer army has " + var5.size() + " units");
                    System.out.println();
                }

                if (var4.size() != 0 && var5.size() != 0) {
                    if (var4.size() != 0 && var5.size() != 0) {
                        continue;
                    }

                    return;
                }

                System.out.println("Battle is over!");
                return;
            }
        }
    }

    private Unit unitAttack(Unit var1) throws InterruptedException {
        Unit var2 = var1.getProgram().attack();
        this.printBattleLog.printBattleLog(var1, var2);
        return var2;
    }
}