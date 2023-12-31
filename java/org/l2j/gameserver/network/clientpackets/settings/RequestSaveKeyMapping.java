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
package org.l2j.gameserver.network.clientpackets.settings;

import org.l2j.Config;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.ConnectionState;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * Request Save Key Mapping client packet.
 * @author 4Team
 */
public class RequestSaveKeyMapping implements ClientPacket
{
	public static final String SPLIT_VAR = "	";
	
	private byte[] _uiKeyMapping;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int dataSize = packet.readInt();
		if (dataSize > 0)
		{
			_uiKeyMapping = packet.readBytes(dataSize);
		}
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (!Config.STORE_UI_SETTINGS || //
			(player == null) || //
			(_uiKeyMapping == null) || //
			(client.getConnectionState() != ConnectionState.IN_GAME))
		{
			return;
		}
		
		String uiKeyMapping = "";
		for (Byte b : _uiKeyMapping)
		{
			uiKeyMapping += b + SPLIT_VAR;
		}
		player.getVariables().set(PlayerVariables.UI_KEY_MAPPING, uiKeyMapping);
	}
}
