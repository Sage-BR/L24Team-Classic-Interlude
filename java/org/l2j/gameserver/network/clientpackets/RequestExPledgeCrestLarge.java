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
import org.l2j.gameserver.data.sql.CrestTable;
import org.l2j.gameserver.model.Crest;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ExPledgeEmblem;

/**
 * @author -Wooden-, Sdw
 */
public class RequestExPledgeCrestLarge implements ClientPacket
{
	private int _crestId;
	private int _clanId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_crestId = packet.readInt();
		_clanId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Crest crest = CrestTable.getInstance().getCrest(_crestId);
		final byte[] data = crest != null ? crest.getData() : null;
		if (data != null)
		{
			for (int i = 0; i <= 4; i++)
			{
				final int size = Math.max(Math.min(14336, data.length - (14336 * i)), 0);
				if (size == 0)
				{
					continue;
				}
				final byte[] chunk = new byte[size];
				System.arraycopy(data, (14336 * i), chunk, 0, size);
				player.sendPacket(new ExPledgeEmblem(_crestId, chunk, _clanId, i));
			}
		}
	}
}
