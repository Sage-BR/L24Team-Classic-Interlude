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
import org.l2j.gameserver.model.CommandChannel;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * format: (ch) d
 * @author -Wooden-
 */
public class RequestExAcceptJoinMPCC implements ClientPacket
{
	private int _response;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_response = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player != null)
		{
			final Player requestor = player.getActiveRequester();
			SystemMessage sm;
			if (requestor == null)
			{
				return;
			}
			
			if (_response == 1)
			{
				boolean newCc = false;
				if (!requestor.getParty().isInCommandChannel())
				{
					new CommandChannel(requestor); // Create new CC
					sm = new SystemMessage(SystemMessageId.THE_COMMAND_CHANNEL_HAS_BEEN_FORMED);
					requestor.sendPacket(sm);
					newCc = true;
				}
				requestor.getParty().getCommandChannel().addParty(player.getParty());
				if (!newCc)
				{
					sm = new SystemMessage(SystemMessageId.YOU_HAVE_JOINED_THE_COMMAND_CHANNEL);
					player.sendPacket(sm);
				}
			}
			else
			{
				requestor.sendMessage("The player declined to join your Command Channel.");
			}
			
			player.setActiveRequester(null);
			requestor.onTransactionResponse();
		}
	}
}
