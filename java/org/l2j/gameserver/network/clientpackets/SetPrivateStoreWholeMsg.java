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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ExPrivateStoreSetWholeMsg;
import org.l2j.gameserver.util.Util;

/**
 * @author KenM
 */
public class SetPrivateStoreWholeMsg implements ClientPacket
{
	private static final int MAX_MSG_LENGTH = 29;
	
	private String _msg;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_msg = packet.readString();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if ((player == null) || (player.getSellList() == null))
		{
			return;
		}
		
		if ((_msg != null) && (_msg.length() > MAX_MSG_LENGTH))
		{
			Util.handleIllegalPlayerAction(player, player + " tried to overflow private store whole message", Config.DEFAULT_PUNISH);
			return;
		}
		
		player.getSellList().setTitle(_msg);
		player.sendPacket(new ExPrivateStoreSetWholeMsg(player));
	}
}
