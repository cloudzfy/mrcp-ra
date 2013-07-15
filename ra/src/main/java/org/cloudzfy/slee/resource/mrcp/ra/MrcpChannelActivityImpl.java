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

import java.util.UUID;

import org.apache.log4j.Logger;
import org.cloudzfy.slee.resource.mrcp.MrcpChannelActivity;
import org.cloudzfy.slee.resource.mrcp.event.MrcpResponseEvent;
import org.mrcp4j.MrcpMethodName;
import org.mrcp4j.client.MrcpChannel;
import org.mrcp4j.message.header.ChannelIdentifier;
import org.mrcp4j.message.request.MrcpRequest;

public class MrcpChannelActivityImpl implements MrcpChannelActivity {

	private Logger logger = Logger.getLogger(MrcpChannelActivityImpl.class);
	
	private String id;
	private MrcpChannel mrcpChannel;
	private MrcpResourceAdaptor ra;
	private MrcpChannelActivityHandle activityHandle = null;
	
	public MrcpChannelActivityImpl(MrcpChannel mrcpChannel, MrcpResourceAdaptor ra) {
		this.id = UUID.randomUUID().toString();
		this.mrcpChannel = mrcpChannel;
		this.ra = ra;
		this.activityHandle = new MrcpChannelActivityHandle(id);
	}
	
	public void setMrcpChannel(MrcpChannel mrcpChannel) {
		this.mrcpChannel = mrcpChannel;
	}
	
	@Override
	public MrcpChannel getMrcpChannel() {
		return this.mrcpChannel;
	}

	@Override
	public void release() {
		ra.endActivity(activityHandle);
	}

	public MrcpChannelActivityHandle getActivityHandle() {
		return activityHandle;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	public String getId() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass()) {
			return ((MrcpChannelActivityImpl)obj).getId().equals(this.getId());
		} else {
			return false;
		}
	}

	@Override
	public MrcpRequest createRequest(MrcpMethodName methodName) {
		return mrcpChannel.createRequest(methodName);
	}

	@Override
	public void sendRequest(MrcpRequest request) {
		try {
			MrcpResponseEvent response = new MrcpResponseEvent(mrcpChannel.sendRequest(request));
			ra.processMrcpResponseEvent(response);
		} catch (Exception e) {
			logger.error("Unable to send MRCP Request. ", e);
		}
	}

	@Override
	public ChannelIdentifier getChannelIdentifier() {
		return mrcpChannel.getChannelID();
	}

}
