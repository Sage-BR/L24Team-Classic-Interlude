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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Collection;
import java.util.logging.Logger;

import org.l2j.commons.database.DatabaseFactory;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.loginserver.GameServerTable;
import org.l2j.loginserver.GameServerTable.GameServerInfo;
import org.l2j.loginserver.GameServerThread;

/**
 * @author Nik
 */
public class ChangePassword extends ReadablePacket
{
	protected static final Logger LOGGER = Logger.getLogger(ChangePassword.class.getName());
	private static GameServerThread gst = null;
	
	public ChangePassword(byte[] decrypt)
	{
		super(decrypt);
		readByte(); // id (already processed)
		
		final String accountName = readString();
		final String characterName = readString();
		final String curpass = readString();
		final String newpass = readString();
		
		// get the GameServerThread
		final Collection<GameServerInfo> serverList = GameServerTable.getInstance().getRegisteredGameServers().values();
		for (GameServerInfo gsi : serverList)
		{
			if ((gsi.getGameServerThread() != null) && gsi.getGameServerThread().hasAccountOnGameServer(accountName))
			{
				gst = gsi.getGameServerThread();
			}
		}
		
		if (gst == null)
		{
			return;
		}
		
		if ((curpass == null) || (newpass == null))
		{
			gst.changePasswordResponse((byte) 0, characterName, "Invalid password data! Try again.");
		}
		else
		{
			try
			{
				final MessageDigest md = MessageDigest.getInstance("SHA");
				final byte[] raw = md.digest(curpass.getBytes(StandardCharsets.UTF_8));
				final String curpassEnc = Base64.getEncoder().encodeToString(raw);
				String pass = null;
				int passUpdated = 0;
				
				// SQL connection
				try (Connection con = DatabaseFactory.getConnection();
					PreparedStatement ps = con.prepareStatement("SELECT password FROM accounts WHERE login=?"))
				{
					ps.setString(1, accountName);
					try (ResultSet rs = ps.executeQuery())
					{
						if (rs.next())
						{
							pass = rs.getString("password");
						}
					}
				}
				
				if (curpassEnc.equals(pass))
				{
					final byte[] password = md.digest(newpass.getBytes(StandardCharsets.UTF_8));
					// SQL connection
					try (Connection con = DatabaseFactory.getConnection();
						PreparedStatement ps = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?"))
					{
						ps.setString(1, Base64.getEncoder().encodeToString(password));
						ps.setString(2, accountName);
						passUpdated = ps.executeUpdate();
					}
					
					LOGGER.info("The password for account " + accountName + " has been changed from " + curpassEnc + " to " + Base64.getEncoder().encodeToString(password));
					if (passUpdated > 0)
					{
						gst.changePasswordResponse((byte) 1, characterName, "You have successfully changed your password!");
					}
					else
					{
						gst.changePasswordResponse((byte) 0, characterName, "The password change was unsuccessful!");
					}
				}
				else
				{
					gst.changePasswordResponse((byte) 0, characterName, "The typed current password doesn't match with your current one.");
				}
			}
			catch (Exception e)
			{
				LOGGER.warning("Error while changing password for account " + accountName + " requested by player " + characterName + "! " + e);
			}
		}
	}
}