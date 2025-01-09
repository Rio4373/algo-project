package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        // Список для хранения подходящих юнитов
        List<Unit> suitableUnits = new ArrayList<>();

        // Проходим по каждому ряду юнитов противника
        for (List<Unit> row : unitsByRow) {
            // Внутри ряда проверяем каждого юнита
            for (int i = 0; i < row.size(); i++) {
                Unit unit = row.get(i);

                // Если юнит существует и жив
                if (unit != null && unit.isAlive()) {
                    // Для атакующей армии компьютера (левая армия) проверяем, что юнит не закрыт справа
                    if (isLeftArmyTarget) {
                        if (isRightmostUnit(unit, row, i)) {
                            suitableUnits.add(unit);
                        }
                    }
                    // Для атакующей армии игрока (правая армия) проверяем, что юнит не закрыт слева
                    else {
                        if (isLeftmostUnit(unit, row, i)) {
                            suitableUnits.add(unit);
                        }
                    }
                }
            }
        }

        // Возвращаем список подходящих юнитов
        return suitableUnits;
    }

    // Метод проверяет, является ли юнит правым в своем ряду (нет юнитов справа)
    private boolean isRightmostUnit(Unit unit, List<Unit> row, int unitIndex) {
        // Если это последний юнит в ряду или справа от юнита нет других
        for (int i = unitIndex + 1; i < row.size(); i++) {
            if (row.get(i) != null) {
                return false; // Есть другой юнит справа
            }
        }
        return true;
    }

    // Метод проверяет, является ли юнит левым в своем ряду (нет юнитов слева)
    private boolean isLeftmostUnit(Unit unit, List<Unit> row, int unitIndex) {
        // Если это первый юнит в ряду или слева от юнита нет других
        for (int i = unitIndex - 1; i >= 0; i--) {
            if (row.get(i) != null) {
                return false; // Есть другой юнит слева
            }
        }
        return true;
    }
}
