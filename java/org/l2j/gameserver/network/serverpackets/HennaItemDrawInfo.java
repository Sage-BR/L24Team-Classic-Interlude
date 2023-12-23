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
import org.l2j.gameserver.model.stats.BaseStat;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Zoey76
 */
public class HennaItemDrawInfo extends ServerPacket
{
	private final Player _player;
	private final Henna _henna;
	
	public HennaItemDrawInfo(Henna henna, Player player)
	{
		_henna = henna;
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.HENNA_ITEM_INFO.writeId(this);
		writeInt(_henna.getDyeId()); // symbol Id
		writeInt(_henna.getDyeItemId()); // item id of dye
		writeLong(_henna.getWearCount()); // total amount of dye require
		writeLong(_henna.getWearFee()); // total amount of Adena require to draw symbol
		writeInt(_henna.isAllowedClass(_player.getClassId())); // able to draw or not 0 is false and 1 is true
		writeLong(_player.getAdena());
		writeInt(_player.getINT()); // current INT
		writeShort(_player.getINT() + _player.getHennaValue(BaseStat.INT)); // equip INT
		writeInt(_player.getSTR()); // current STR
		writeShort(_player.getSTR() + _player.getHennaValue(BaseStat.STR)); // equip STR
		writeInt(_player.getCON()); // current CON
		writeShort(_player.getCON() + _player.getHennaValue(BaseStat.CON)); // equip CON
		writeInt(_player.getMEN()); // current MEN
		writeShort(_player.getMEN() + _player.getHennaValue(BaseStat.MEN)); // equip MEN
		writeInt(_player.getDEX()); // current DEX
		writeShort(_player.getDEX() + _player.getHennaValue(BaseStat.DEX)); // equip DEX
		writeInt(_player.getWIT()); // current WIT
		writeShort(_player.getWIT() + _player.getHennaValue(BaseStat.WIT)); // equip WIT
		writeInt(0); // TODO: Find me!
	}
}
