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
package org.l2j.gameserver.network.serverpackets.attendance;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author 4Team
 */
public class ExConfirmVipAttendanceCheck extends ServerPacket
{
	boolean _available;
	int _index;
	
	public ExConfirmVipAttendanceCheck(boolean rewardAvailable, int rewardIndex)
	{
		_available = rewardAvailable;
		_index = rewardIndex;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_CONFIRM_VIP_ATTENDANCE_CHECK.writeId(this);
		writeByte(_available); // can receive reward today? 1 else 0
		writeByte(_index); // active reward index
		writeInt(0);
		writeInt(0);
	}
}
