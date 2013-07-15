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

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.resource.ActivityHandle;

import org.apache.log4j.Logger;
import org.mrcp4j.client.MrcpChannel;
import org.mrcp4j.message.header.ChannelIdentifier;

public class MrcpActivityManager {

	private static Logger logger = Logger.getLogger(MrcpActivityManager.class);
	
	private String mrcpRaEntityName = null;
	
	private ConcurrentHashMap<MrcpChannelActivityHandle, MrcpChannelActivityImpl> channelActivities = new ConcurrentHashMap<MrcpChannelActivityHandle, MrcpChannelActivityImpl>();
	private ConcurrentHashMap<MrcpChannel, MrcpChannelActivityHandle> mrcpChannel2ActivityHandleMap = new ConcurrentHashMap<MrcpChannel, MrcpChannelActivityHandle>();
	private ConcurrentHashMap<ChannelIdentifier, MrcpChannelActivityHandle> channelId2ActivityHandleMap = new ConcurrentHashMap<ChannelIdentifier, MrcpChannelActivityHandle>();
	
	protected MrcpChannelActivityHandle putMrcpChannelActivity(MrcpChannelActivityImpl activity) {
		MrcpChannelActivityHandle handle = activity.getActivityHandle();
		if(activity.getMrcpChannel() != null) {
			mrcpChannel2ActivityHandleMap.put(activity.getMrcpChannel(), handle);
			channelId2ActivityHandleMap.put(activity.getChannelIdentifier(), handle);
		}
		channelActivities.put(handle, activity);
		return handle;
	}
	
	protected MrcpChannelActivityImpl getMrcpChannelActivity(MrcpChannelActivityHandle handle) {
		return channelActivities.get(handle);
	}
	
	protected MrcpChannelActivityHandle getMrcpChannelActivityHandle(MrcpChannel mrcpChannel) {
		return mrcpChannel2ActivityHandleMap.get(mrcpChannel);
	}
	
	protected MrcpChannelActivityHandle getMrcpChannelActivityHandle(ChannelIdentifier channelIdentifier) {
		return channelId2ActivityHandleMap.get(channelIdentifier);
	}
	
	protected boolean containsActivityHandle(ActivityHandle handle) {
		if(handle instanceof MrcpChannelActivityHandle) {
			return channelActivities.containsKey((MrcpChannelActivityHandle)handle);
		}
		return false;
	}
	
	protected void removeMrcpActivity(MrcpChannelActivityHandle handle) {
		MrcpChannelActivityImpl activity = channelActivities.remove(handle);
		if(activity != null) {
			if(activity.getMrcpChannel() != null) {
				if(mrcpChannel2ActivityHandleMap.remove(activity.getMrcpChannel()) != null) {
					if(logger.isDebugEnabled()) {
						logger.debug(this.getMrcpRaEntityName() + " : removed mrcp channel mapping for handle " + handle);
					}
				}
				if(channelId2ActivityHandleMap.remove(activity.getChannelIdentifier()) != null) {
					if(logger.isDebugEnabled()) {
						logger.debug(this.getMrcpRaEntityName() + " : removed channel identifier mapping for handle " + handle);
					}
				}
			}
		}
	}
	
	protected String getMrcpRaEntityName() {
		return mrcpRaEntityName;
	}
	
	protected void setMrcpRaEntityName(String mrcpRaEntityName) {
		this.mrcpRaEntityName = mrcpRaEntityName;
	}
}
