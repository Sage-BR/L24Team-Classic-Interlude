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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.Henna;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Zoey76
 */
public class HennaRemoveList extends ServerPacket
{
	private final Player _player;
	
	public HennaRemoveList(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.HENNA_UNEQUIP_LIST.writeId(this);
		writeLong(_player.getAdena());
		writeInt(3); // seems to be max size
		writeInt(3 - _player.getHennaEmptySlots());
		for (Henna henna : _player.getHennaList())
		{
			if (henna != null)
			{
				writeInt(henna.getDyeId());
				writeInt(henna.getDyeItemId());
				writeLong(henna.getCancelCount());
				writeLong(henna.getCancelFee());
				writeInt(henna.isAllowedClass(_player.getClassId()));
			}
		}
	}
}
