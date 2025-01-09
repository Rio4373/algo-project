package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Создаем объект армии, которая будет содержать выбранных юнитов
        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();

        // Текущие затраченные очки на юнитов
        int currentPoints = 0;

        // Словарь для отслеживания количества юнитов каждого типа
        Map<String, Integer> unitCounts = new HashMap<>();

        // Перебор всех юнитов из списка
        for (Unit unit : unitList) {
            // Рассчитываем эффективность юнита (атака/стоимость + здоровье/стоимость)
            double efficiency = (double) unit.getBaseAttack() / unit.getCost() + (double) unit.getHealth() / unit.getCost();

            // Проверяем, сколько юнитов этого типа можно добавить
            unitCounts.putIfAbsent(unit.getUnitType(), 0);
            int maxUnitsForType = Math.min(11, maxPoints / unit.getCost());

            // Добавляем юниты этого типа, пока не исчерпаем очки или лимит по количеству юнитов
            while (unitCounts.get(unit.getUnitType()) < maxUnitsForType && currentPoints + unit.getCost() <= maxPoints) {
                selectedUnits.add(new Unit(
                        unit.getName(),
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        unit.getxCoordinate(),
                        unit.getyCoordinate()
                ));

                // Обновляем количество добавленных юнитов этого типа
                unitCounts.put(unit.getUnitType(), unitCounts.get(unit.getUnitType()) + 1);

                // Увеличиваем потраченные очки на стоимость этого юнита
                currentPoints += unit.getCost();
            }
        }

        // Устанавливаем в армию список выбранных юнитов и текущие потраченные очки
        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);

        // Возвращаем сформированную армию
        return computerArmy;
    }
}