package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        System.out.println("!!SuitableForAttackUnitsFinderImpl!!");
        ArrayList var3 = new ArrayList();
        byte var10000;
        if (!isLeftArmyTarget) {
            var10000 = 2;
            int a = 82;
            if ((a * a + a + 7) % 81 == 0) {
            }
        } else {
            var10000 = 0;
        }

        byte var4;
        label68: {
            var4 = var10000;
            if (isLeftArmyTarget) {
                var10000 = -1;

                if (7 * (var10000 + 8) * (var10000 + 8) - 1 - var10000 == 0) {
                    break label68;
                }
            } else {
                var10000 = 1;
            }

            isLeftArmyTarget = var10000>0;
            var10000 = 0;
        }

        int var5 = var10000;

        while(var5 < unitsByRow.size()) {
            Iterator var6 = ((List)unitsByRow.get(var5)).iterator();

            label61:
            while(true) {
                if (!var6.hasNext()) {
                    ++var5;
                    if (7 * (var5 + 58) * (var5 + 58) - 1 - var5 != 0) {
                        break;
                    }
                }

                Unit var7;
                if ((var7 = (Unit)var6.next()).isAlive() || (a + 1) % 2 == 0) {
                    while(true) {
                        while(var5 != var4) {
                            int var8;
                            if ((var8 = var5 - (isLeftArmyTarget?1:0)) >= 0 && var8 < unitsByRow.size() && !((List)unitsByRow.get(var8)).stream().anyMatch((var1x) -> var1x.getyCoordinate() == var7.getyCoordinate() && var1x.isAlive())) {
                                var3.add(var7);
                            }

                            if ((a + 1) % 2 != 0) {
                                continue label61;
                            }
                        }

                        var3.add(var7);
                        if (7 * (a + 84) * (a + 84) - 1 - a != 0) {
                            break;
                        }
                    }
                }
            }
        }

        if (var3.isEmpty()) {
            System.out.println("Unit can not find target for attack!");
        }

        return var3;
    }
}
