/*
 * mrcp-ra, An Implementation of MRCP Resource Adaptor of JAIN SLEE
 * Copyright (C) 2013, Cloudzfy
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cloudzfy.slee.resource.mrcp.ra;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

public class MrcpChannelActivityHandle implements Serializable, ActivityHandle {

	private static final long serialVersionUID = 5486457496528367327L;
	private final String id;
	
	public MrcpChannelActivityHandle(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass()) {
			return ((MrcpChannelActivityHandle)obj).getId().equals(this.getId());
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "MrcpChannelActivityHandle(id=" + this.getId() + ")";
	}

}
