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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanMember;
import org.l2j.gameserver.model.clan.ClanPrivilege;
import org.l2j.gameserver.network.GameClient;

/**
 * Format: (ch) dSdS
 * @author -Wooden-
 */
public class RequestPledgeReorganizeMember implements ClientPacket
{
	private int _isMemberSelected;
	private String _memberName;
	private int _newPledgeType;
	private String _selectedMember;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_isMemberSelected = packet.readInt();
		_memberName = packet.readString();
		_newPledgeType = packet.readInt();
		_selectedMember = packet.readString();
	}
	
	@Override
	public void run(GameClient client)
	{
		if (_isMemberSelected == 0)
		{
			return;
		}
		
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan clan = player.getClan();
		if ((clan == null) || !player.hasClanPrivilege(ClanPrivilege.CL_MANAGE_RANKS))
		{
			return;
		}
		
		final ClanMember member1 = clan.getClanMember(_memberName);
		if ((member1 == null) || (member1.getObjectId() == clan.getLeaderId()))
		{
			return;
		}
		
		final ClanMember member2 = clan.getClanMember(_selectedMember);
		if ((member2 == null) || (member2.getObjectId() == clan.getLeaderId()))
		{
			return;
		}
		
		final int oldPledgeType = member1.getPledgeType();
		if (oldPledgeType == _newPledgeType)
		{
			return;
		}
		
		member1.setPledgeType(_newPledgeType);
		member2.setPledgeType(oldPledgeType);
		clan.broadcastClanStatus();
	}
}
