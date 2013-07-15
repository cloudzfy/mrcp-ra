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

package org.cloudzfy.slee.resource.mrcp;

import org.mrcp4j.MrcpMethodName;
import org.mrcp4j.client.MrcpChannel;
import org.mrcp4j.message.header.ChannelIdentifier;
import org.mrcp4j.message.request.MrcpRequest;

public interface MrcpChannelActivity {

	public MrcpRequest createRequest(MrcpMethodName methodName);
	
	public void sendRequest(MrcpRequest request);
	
	public void release();

	public MrcpChannel getMrcpChannel();
	
	public ChannelIdentifier getChannelIdentifier();
	
}
