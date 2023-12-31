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
package org.l2j.gameserver.network.clientpackets.attributechange;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.attributechange.ExChangeAttributeInfo;

/**
 * @author 4Team
 */
public class SendChangeAttributeTargetItem implements ClientPacket
{
	private int _crystalItemId;
	private int _itemObjId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_crystalItemId = packet.readInt();
		_itemObjId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item item = player.getInventory().getItemByObjectId(_itemObjId);
		if ((item == null) || !item.isWeapon())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		player.sendPacket(new ExChangeAttributeInfo(_crystalItemId, item));
	}
}
