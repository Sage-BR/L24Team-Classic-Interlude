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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;

/**
 * @author -Wooden-
 */
public class SnoopQuit implements ClientPacket
{
	private int _snoopID;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_snoopID = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player target = World.getInstance().getPlayer(_snoopID);
		if (target == null)
		{
			return;
		}
		
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		target.removeSnooper(player);
		player.removeSnooped(target);
	}
}
