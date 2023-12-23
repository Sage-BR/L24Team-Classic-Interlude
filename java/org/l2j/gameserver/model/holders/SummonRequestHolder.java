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
package org.l2j.gameserver.model.holders;

import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Player;

/**
 * @author 4Team
 */
public class SummonRequestHolder
{
	private final Player _summoner;
	private final Location _location;
	
	public SummonRequestHolder(Player summoner)
	{
		_summoner = summoner;
		_location = summoner == null ? null : new Location(summoner.getX(), summoner.getY(), summoner.getZ(), summoner.getHeading());
	}
	
	public Player getSummoner()
	{
		return _summoner;
	}
	
	public Location getLocation()
	{
		return _location;
	}
}
