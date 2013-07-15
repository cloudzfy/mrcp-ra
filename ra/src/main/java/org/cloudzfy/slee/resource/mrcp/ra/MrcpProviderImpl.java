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

import java.net.InetAddress;

import javax.slee.facilities.Tracer;

import org.cloudzfy.slee.resource.mrcp.MrcpChannelActivity;
import org.cloudzfy.slee.resource.mrcp.MrcpProvider;
import org.cloudzfy.slee.resource.mrcp.event.MrcpRequestEvent;
import org.mrcp4j.MrcpResourceType;
import org.mrcp4j.client.MrcpChannel;
import org.mrcp4j.client.MrcpEventListener;
import org.mrcp4j.client.MrcpFactory;
import org.mrcp4j.message.MrcpEvent;
import org.mrcp4j.message.header.IllegalValueException;

public class MrcpProviderImpl implements MrcpProvider {
	
	private org.mrcp4j.client.MrcpProvider provider;
	private MrcpFactory mrcpFactory;
	private MrcpStackListener listener;
	private String protocol;
	
	private MrcpResourceAdaptor ra;
	private Tracer tracer;
	
	public MrcpProviderImpl(MrcpResourceAdaptor ra, Tracer tracer) {
		this.ra = ra;
		this.tracer = tracer;
		
	}
	
	public void initialize(String protocol) {
		this.mrcpFactory = MrcpFactory.newInstance();
		this.provider = mrcpFactory.createProvider();
		this.protocol = protocol;
		this.listener = new MrcpStackListener();
	}
	
	private class MrcpStackListener implements MrcpEventListener {

		@Override
		public void eventReceived(MrcpEvent event) {
			try {
				MrcpResourceType type = event.getChannelIdentifier().getResourceType();
				MrcpRequestEvent requestEvent = new MrcpRequestEvent(event);
				switch (type) {
				case SPEECHSYNTH:
					ra.processMrcpSynthesizerEvent(requestEvent);
					break;
				case SPEECHRECOG:
					ra.processMrcpRecognizerEvent(requestEvent);
					break;
				case SPEAKVERIFY:
					ra.processMrcpVerifierEvent(requestEvent);
					break;
				case RECORDER:
					ra.processMrcpRecorderEvent(requestEvent);
					break;
				default:
					tracer.severe("Unexpected value for event resource type.");
				}
			} catch (IllegalValueException e) {
				tracer.severe("Couldn't get Channel Identifier in MrcpStackListener.", e);
			}
		}
	}

	@Override
	public MrcpChannelActivity getChannelActivity(MrcpChannel mrcpChannel) {
		return getChannelActivity(mrcpChannel, true);
	}
	
	protected MrcpChannelActivity getChannelActivity(MrcpChannel mrcpChannel, boolean startActivity) {
		MrcpChannelActivityHandle handle = ra.getActivityManager().getMrcpChannelActivityHandle(mrcpChannel);
		if(handle != null) {
			return ra.getActivityManager().getMrcpChannelActivity(handle);
		} else {
			MrcpChannelActivityImpl activity = new MrcpChannelActivityImpl(mrcpChannel, ra);
			handle = ra.getActivityManager().putMrcpChannelActivity(activity);
			if(startActivity) {
				try {
					ra.getSleeEndpoint().startActivity(handle, activity, MrcpResourceAdaptor.ACTIVITY_FLAGS);
				} catch (Exception e) {
					tracer.severe("Failed to start Activity. ", e);
					if(handle != null) {
						ra.getActivityManager().removeMrcpActivity(handle);
					}
					return null;
				}
			}
			return activity;
		}
	}
	
	void delete() {
		
	}

	@Override
	public MrcpChannelActivity getNewChannel(String channelId, InetAddress host, int port) {
		MrcpChannel mrcpChannel = null;
		try {
			mrcpChannel = provider.createChannel(channelId, host, port, protocol);
			mrcpChannel.addEventListener(listener);
			return this.getChannelActivity(mrcpChannel, true);
		} catch (Exception e) {
			tracer.severe("Unable to create Channel. ", e);
		}
		return null;
	}
	
}
