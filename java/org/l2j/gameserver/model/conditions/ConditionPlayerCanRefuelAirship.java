/*
 * This file is part of the L2J 4Team project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2j.gameserver.model.conditions;

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.ControllableAirShip;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.skill.Skill;

/**
 * Player Can Refuel Airship condition implementation.
 * @author Adry_85
 */
public class ConditionPlayerCanRefuelAirship extends Condition
{
	private final int _value;
	
	public ConditionPlayerCanRefuelAirship(int value)
	{
		_value = value;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, ItemTemplate item)
	{
		boolean canRefuelAirship = true;
		final Player player = effector.getActingPlayer();
		if ((player == null) || (player.getAirShip() == null) || !(player.getAirShip() instanceof ControllableAirShip) || ((player.getAirShip().getFuel() + _value) > player.getAirShip().getMaxFuel()))
		{
			canRefuelAirship = false;
		}
		return canRefuelAirship;
	}
}