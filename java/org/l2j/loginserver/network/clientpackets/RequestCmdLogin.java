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
package org.l2j.loginserver.network.clientpackets;

import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import org.l2j.Config;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.loginserver.GameServerTable.GameServerInfo;
import org.l2j.loginserver.LoginController;
import org.l2j.loginserver.enums.AccountKickedReason;
import org.l2j.loginserver.enums.LoginFailReason;
import org.l2j.loginserver.model.data.AccountInfo;
import org.l2j.loginserver.network.ConnectionState;
import org.l2j.loginserver.network.LoginClient;
import org.l2j.loginserver.network.serverpackets.AccountKicked;
import org.l2j.loginserver.network.serverpackets.LoginOk;
import org.l2j.loginserver.network.serverpackets.ServerList;

public class RequestCmdLogin implements LoginClientPacket
{
	private static final Logger LOGGER = Logger.getLogger(RequestCmdLogin.class.getName());
	
	private final byte[] _raw = new byte[128];
	
	@Override
	public void read(ReadablePacket packet)
	{
		if (packet.getRemainingLength() >= 128)
		{
			packet.readInt();
			packet.readBytes(_raw);
		}
	}
	
	@Override
	public void run(LoginClient client)
	{
		if (!Config.ENABLE_CMD_LINE_LOGIN)
		{
			return;
		}
		
		final byte[] decrypted = new byte[128];
		try
		{
			final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.DECRYPT_MODE, client.getScrambledKeyPair().getPrivateKey());
			rsaCipher.doFinal(_raw, 0, 128, decrypted, 0);
		}
		catch (GeneralSecurityException e)
		{
			LOGGER.log(Level.INFO, "", e);
			return;
		}
		
		String user;
		String password;
		try
		{
			user = new String(decrypted, 0x40, 14).trim();
			password = new String(decrypted, 0x60, 16).trim();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "", e);
			return;
		}
		
		final String clientAddr = client.getIp();
		final LoginController lc = LoginController.getInstance();
		final AccountInfo info = lc.retriveAccountInfo(clientAddr, user, password);
		if (info == null)
		{
			// user or pass wrong
			// client.close(LoginFailReason.REASON_SYSTEM_ERROR);
			// above message crashes client
			// REASON_ACCOUNT_INFO_INCORRECT_CONTACT_SUPPORT seems ok as well
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
			return;
		}
		
		switch (lc.tryCheckinAccount(client, clientAddr, info))
		{
			case AUTH_SUCCESS:
			{
				client.setAccount(info.getLogin());
				client.setConnectionState(ConnectionState.AUTHED_LOGIN);
				client.setSessionKey(lc.assignSessionKeyToClient(info.getLogin(), client));
				lc.getCharactersOnAccount(info.getLogin());
				if (Config.SHOW_LICENCE)
				{
					client.sendPacket(new LoginOk(client.getSessionKey()));
				}
				else
				{
					client.sendPacket(new ServerList(client));
				}
				break;
			}
			case INVALID_PASSWORD:
			{
				client.close(LoginFailReason.REASON_USER_OR_PASS_WRONG);
				break;
			}
			case ACCOUNT_BANNED:
			{
				client.close(new AccountKicked(AccountKickedReason.REASON_PERMANENTLY_BANNED));
				return;
			}
			case ALREADY_ON_LS:
			{
				final LoginClient oldClient = lc.getAuthedClient(info.getLogin());
				if (oldClient != null)
				{
					// kick the other client
					oldClient.close(LoginFailReason.REASON_ACCOUNT_IN_USE);
					lc.removeAuthedLoginClient(info.getLogin());
				}
				// kick also current client
				client.close(LoginFailReason.REASON_ACCOUNT_IN_USE);
				break;
			}
			case ALREADY_ON_GS:
			{
				final GameServerInfo gsi = lc.getAccountOnGameServer(info.getLogin());
				if (gsi != null)
				{
					client.close(LoginFailReason.REASON_ACCOUNT_IN_USE);
					// kick from there
					if (gsi.isAuthed())
					{
						gsi.getGameServerThread().kickPlayer(info.getLogin());
					}
				}
				break;
			}
		}
	}
}
