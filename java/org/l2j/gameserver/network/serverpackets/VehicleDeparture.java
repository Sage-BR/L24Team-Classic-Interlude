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

import org.l2j.gameserver.model.actor.instance.Boat;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Maktakien
 */
public class VehicleDeparture extends ServerPacket
{
	private final int _objId;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _moveSpeed;
	private final int _rotationSpeed;
	
	public VehicleDeparture(Boat boat)
	{
		_objId = boat.getObjectId();
		_x = boat.getXdestination();
		_y = boat.getYdestination();
		_z = boat.getZdestination();
		_moveSpeed = (int) boat.getMoveSpeed();
		_rotationSpeed = (int) boat.getStat().getRotationSpeed();
	}
	
	@Override
	public void write()
	{
		ServerPackets.VEHICLE_DEPARTURE.writeId(this);
		writeInt(_objId);
		writeInt(_moveSpeed);
		writeInt(_rotationSpeed);
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
	}
}
