/*
 * This file is part of the L2J Mobius project.
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
package quests.not_done;

import org.l2j.Config;
import org.l2j.gameserver.model.quest.Quest;

/**
 * @author Mobius
 */
public class Q10868_TheDarkSideOfPower extends Quest
{
	private static final int START_NPC = 34022;
	
	public Q10868_TheDarkSideOfPower()
	{
		super(10868);
		addStartNpc(START_NPC);
		addTalkId(START_NPC);
		addCondMinLevel(Config.PLAYER_MAXIMUM_LEVEL /* 70 */, getNoQuestMsg(null));
	}
}
