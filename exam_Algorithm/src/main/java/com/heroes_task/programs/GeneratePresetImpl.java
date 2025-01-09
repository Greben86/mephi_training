package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army var3 = new Army();
        ArrayList var4 = new ArrayList();
        HashMap var5 = new HashMap();
        Random random = new Random();
        int var7 = 0;
        unitList.sort((var0, var1x) -> {
            double var2 = (double)var0.getBaseAttack() / (double)var0.getCost();
            double temp;
            if (Double.compare(temp = (double)var1x.getBaseAttack() / (double)var1x.getCost(), var2) != 0) {
                return Double.compare(temp, var2);
            } else {
                double var6 = (double)var0.getHealth() / (double)var0.getCost();
                return Double.compare((double)var1x.getHealth() / (double)var1x.getCost(), var6);
            }
        });
        LinkedList var14 = new LinkedList(unitList);

        while(maxPoints > 0 && !var14.isEmpty()) {
            Unit var8;
            String var9 = (var8 = (Unit)var14.peek()).getUnitType();
            int var10 = var8.getCost();
            int var11;
            if ((var11 = (Integer)var5.getOrDefault(var9, 0)) < 11 && maxPoints >= var10) {
                int[] var12;
                if ((var12 = this.findAvailableCoordinates(var4, var9, random, 0)) != null) {
                    int var13 = var12[0];
                    int var17 = var12[1];
                    ++var11;
                    var5.put(var9, var11);
                    var9 = var9 + " " + var11;
                    var8 = new Unit(var9, var8.getUnitType(), var8.getHealth(), var8.getBaseAttack(), var8.getCost(), var8.getAttackType(), var8.getAttackBonuses(), var8.getDefenceBonuses(), var13, var17);

                    while(true) {
                        var4.add(var8);
                        System.out.println("Added " + var4.size() + " unit");
                        var3.getUnits().add(var8);
                        maxPoints -= var10;
                        var7 += var10;
                        int a = 0;
                        if ((a + 1) % 2 == 0) {
                            continue;
                        }
                    }
                } else {
                    System.out.println("Not found units coordinates: " + var9);
                }
            }

            while(true) {
                if (var11 >= 11) {
                    var14.poll();
                }
                int a = 0;
                if ((a + 1) % 2 != 0) {
                    break;
                }
            }
        }

        System.out.println("Used points: " + var7);
        return var3;
    }

    private int[] findAvailableCoordinates(List<Unit> var1, String var2, Random var3, int var4) {
        while(var4 < 100) {
            int var5 = var3.nextInt(21);
            int var6 = var3.nextInt(3);
            if (!var1.stream().anyMatch((var2x) -> var2x.getxCoordinate() == var6 && var2x.getyCoordinate() == var5)) {
                return new int[]{var6, var5};
            }

            while(true) {
                ++var4;
                int a = 0;
                if ((a + 1) % 2 != 0) {
                    break;
                }
            }
        }

        return null;
    }
}