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
package org.l2j.loginserver.network.gameserverpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.loginserver.GameServerThread;
import org.l2j.loginserver.LoginController;

/**
 * Thanks to mochitto.
 * @author mrTJO
 */
public class ReplyCharacters extends ReadablePacket
{
	public ReplyCharacters(byte[] decrypt, GameServerThread server)
	{
		super(decrypt);
		readByte(); // id (already processed)
		
		final String account = readString();
		final int chars = readByte();
		final int charsToDel = readByte();
		final long[] charsList = new long[charsToDel];
		for (int i = 0; i < charsToDel; i++)
		{
			charsList[i] = readLong();
		}
		LoginController.getInstance().setCharactersOnServer(account, chars, charsList, server.getServerId());
	}
}
