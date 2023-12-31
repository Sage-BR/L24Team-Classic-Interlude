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
import org.l2j.gameserver.data.xml.EnchantItemData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.model.item.enchant.EnchantScroll;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExPutEnchantScrollItemResult;

/**
 * @author Sdw
 */
public class RequestExAddEnchantScrollItem implements ClientPacket
{
	private int _scrollObjectId;
	private int _enchantObjectId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_scrollObjectId = packet.readInt();
		_enchantObjectId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final EnchantItemRequest request = player.getRequest(EnchantItemRequest.class);
		if ((request == null) || request.isProcessing())
		{
			return;
		}
		
		request.setEnchantingItem(_enchantObjectId);
		request.setEnchantingScroll(_scrollObjectId);
		
		final Item item = request.getEnchantingItem();
		final Item scroll = request.getEnchantingScroll();
		if ((item == null) || (scroll == null))
		{
			// message may be custom
			player.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS);
			player.sendPacket(new ExPutEnchantScrollItemResult(0));
			request.setEnchantingItem(Player.ID_NONE);
			request.setEnchantingScroll(Player.ID_NONE);
			return;
		}
		
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		if ((scrollTemplate == null))
		{
			// message may be custom
			player.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS);
			player.sendPacket(new ExPutEnchantScrollItemResult(0));
			request.setEnchantingScroll(Player.ID_NONE);
			return;
		}
		
		request.setTimestamp(System.currentTimeMillis());
		player.sendPacket(new ExPutEnchantScrollItemResult(_scrollObjectId));
	}
}
