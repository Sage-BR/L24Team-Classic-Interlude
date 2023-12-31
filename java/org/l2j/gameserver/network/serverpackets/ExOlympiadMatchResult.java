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

import java.util.List;

import org.l2j.gameserver.model.olympiad.OlympiadInfo;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author JIV
 */
public class ExOlympiadMatchResult extends ServerPacket
{
	private final boolean _tie;
	private int _winTeam; // 1,2
	private int _loseTeam = 2;
	private final List<OlympiadInfo> _winnerList;
	private final List<OlympiadInfo> _loserList;
	
	public ExOlympiadMatchResult(boolean tie, int winTeam, List<OlympiadInfo> winnerList, List<OlympiadInfo> loserList)
	{
		_tie = tie;
		_winTeam = winTeam;
		_winnerList = winnerList;
		_loserList = loserList;
		if (_winTeam == 2)
		{
			_loseTeam = 1;
		}
		else if (_winTeam == 0)
		{
			_winTeam = 1;
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_RECEIVE_OLYMPIAD.writeId(this);
		writeInt(1); // Type 0 = Match List, 1 = Match Result
		writeInt(_tie); // 0 - win, 1 - tie
		writeString(_winnerList.get(0).getName());
		writeInt(_winTeam);
		writeInt(_winnerList.size());
		for (OlympiadInfo info : _winnerList)
		{
			writeString(info.getName());
			writeString(info.getClanName());
			writeInt(info.getClanId());
			writeInt(info.getClassId());
			writeInt(info.getDamage());
			writeInt(info.getCurrentPoints());
			writeInt(info.getDiffPoints());
			writeInt(0); // Helios
		}
		writeInt(_loseTeam);
		writeInt(_loserList.size());
		for (OlympiadInfo info : _loserList)
		{
			writeString(info.getName());
			writeString(info.getClanName());
			writeInt(info.getClanId());
			writeInt(info.getClassId());
			writeInt(info.getDamage());
			writeInt(info.getCurrentPoints());
			writeInt(info.getDiffPoints());
			writeInt(0); // Helios
		}
	}
}
