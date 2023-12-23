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

import org.l2j.Config;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.HennaData;
import org.l2j.gameserver.enums.PlayerCondOverride;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.Henna;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.HennaEquipList;
import org.l2j.gameserver.network.serverpackets.InventoryUpdate;
import org.l2j.gameserver.util.Util;

/**
 * @author Zoey76
 */
public class RequestHennaEquip implements ClientPacket
{
	private int _symbolId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_symbolId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if ((player == null) || !client.getFloodProtectors().canPerformTransaction())
		{
			return;
		}
		
		if (player.getHennaEmptySlots() == 0)
		{
			player.sendPacket(SystemMessageId.NO_SLOT_EXISTS_TO_DRAW_THE_SYMBOL);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final Henna henna = HennaData.getInstance().getHenna(_symbolId);
		if (henna == null)
		{
			PacketLogger.warning("Invalid Henna Id: " + _symbolId + " from " + player);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final long _count = player.getInventory().getInventoryItemCount(henna.getDyeItemId(), -1);
		if (henna.isAllowedClass(player.getClassId()) && (_count >= henna.getWearCount()) && (player.getAdena() >= henna.getWearFee()) && player.addHenna(henna))
		{
			player.destroyItemByItemId("Henna", henna.getDyeItemId(), henna.getWearCount(), player, true);
			player.getInventory().reduceAdena("Henna", henna.getWearFee(), player, player.getLastFolkNPC());
			final InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(player.getInventory().getAdenaInstance());
			player.sendInventoryUpdate(iu);
			player.sendPacket(new HennaEquipList(player));
			player.sendPacket(SystemMessageId.THE_SYMBOL_HAS_BEEN_ADDED);
		}
		else
		{
			player.sendPacket(SystemMessageId.THE_SYMBOL_CANNOT_BE_DRAWN);
			if (!player.canOverrideCond(PlayerCondOverride.ITEM_CONDITIONS) && !henna.isAllowedClass(player.getClassId()))
			{
				Util.handleIllegalPlayerAction(player, "Exploit attempt: Character " + player.getName() + " of account " + player.getAccountName() + " tryed to add a forbidden henna.", Config.DEFAULT_PUNISH);
			}
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
}
