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
package handlers.playeractions;

import org.l2j.gameserver.ai.SummonAI;
import org.l2j.gameserver.handler.IPlayerActionHandler;
import org.l2j.gameserver.model.ActionDataHolder;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * Servitor passive/defend mode player action handler.
 * @author Nik
 */
public class ServitorMode implements IPlayerActionHandler
{
	@Override
	public void useAction(Player player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (!player.hasServitors())
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_SERVITOR);
			return;
		}
		
		switch (data.getOptionId())
		{
			case 1: // Passive mode
			{
				player.getServitors().values().forEach(s ->
				{
					if (s.isBetrayed())
					{
						player.sendPacket(SystemMessageId.YOUR_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
						return;
					}
					
					((SummonAI) s.getAI()).setDefending(false);
				});
				break;
			}
			case 2: // Defending mode
			{
				player.getServitors().values().forEach(s ->
				{
					if (s.isBetrayed())
					{
						player.sendPacket(SystemMessageId.YOUR_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
						return;
					}
					
					((SummonAI) s.getAI()).setDefending(true);
				});
			}
		}
	}
}
