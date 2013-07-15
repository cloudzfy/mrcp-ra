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

package org.cloudzfy.slee.resource.mrcp.event;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.UUID;

import org.mrcp4j.MrcpEventName;
import org.mrcp4j.MrcpRequestState;
import org.mrcp4j.message.MrcpEvent;
import org.mrcp4j.message.header.ChannelIdentifier;
import org.mrcp4j.message.header.IllegalValueException;
import org.mrcp4j.message.header.MrcpHeader;
import org.mrcp4j.message.header.MrcpHeaderName;

public final class MrcpRequestEvent extends MrcpEvent implements Serializable {
	
	private static final long serialVersionUID = -3122301261245677642L;
	
	private String id;
	private MrcpEvent mrcpRequest;
	
	public MrcpRequestEvent() {
		id = UUID.randomUUID().toString();
	}
	
	public MrcpRequestEvent(MrcpEvent mrcpRequest) {
		id = UUID.randomUUID().toString();
		this.mrcpRequest = mrcpRequest;
	}
	
	protected String getId() {
		return id;
	}
	
	public int hashCode() {
		return getId().hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass()) {
			return ((MrcpRequestEvent)obj).getId() == this.getId();
		} else {
			return false;
		}
	}
	
	@Override
	public MrcpEventName getEventName() {
		if(mrcpRequest != null) {
			return mrcpRequest.getEventName();
		}
		return super.getEventName();
	}
	
	@Override
	public void setEventName(MrcpEventName eventName) {
		if(mrcpRequest != null) {
			mrcpRequest.setEventName(eventName);
		}
		else super.setEventName(eventName);
	}
	
	@Override
	public void setRequestState(MrcpRequestState requestState) {
		if(mrcpRequest != null) {
			mrcpRequest.setRequestState(requestState);
		}
		else super.setRequestState(requestState);
	}
	
	@Override
	public MrcpRequestState getRequestState() {
		if(mrcpRequest != null) {
			return mrcpRequest.getRequestState();
		}
		return super.getRequestState();
	}

	@Override
	public void addHeader(MrcpHeader header) {
		if(mrcpRequest != null) {
			mrcpRequest.addHeader(header);
		}
		else super.addHeader(header);
	}
	
	@Override
	public ChannelIdentifier getChannelIdentifier() throws IllegalValueException {
		if(mrcpRequest != null) {
			return mrcpRequest.getChannelIdentifier();
		}
		return super.getChannelIdentifier();
	}
	
	@Override
	public String getContent() {
		if(mrcpRequest != null) {
			return mrcpRequest.getContent();
		}
		return super.getContent();
	}
	
	@Override
	public String getContentType() {
		if(mrcpRequest != null) {
			return mrcpRequest.getContentType();
		}
		return super.getContentType();
	}
	
	@Override
	public MrcpHeader getHeader(MrcpHeaderName name) {
		if(mrcpRequest != null) {
			return mrcpRequest.getHeader(name);
		}
		return super.getHeader(name);
	}
	
	@Override
	public MrcpHeader getHeader(String name) {
		if(mrcpRequest != null) {
			return mrcpRequest.getHeader(name);
		}
		return super.getHeader(name);
	}
	
	@Override
	public Collection<MrcpHeader> getHeaders() {
		if(mrcpRequest != null) {
			return mrcpRequest.getHeaders();
		}
		return super.getHeaders();
	}
	
	@Override
	public int getMessageLength() {
		if(mrcpRequest != null) {
			return mrcpRequest.getMessageLength();
		}
		return super.getMessageLength();
	}
	
	@Override
	public long getRequestID() {
		if(mrcpRequest != null) {
			return mrcpRequest.getRequestID();
		}
		return super.getRequestID();
	}
	
	@Override
	public String getVersion() {
		if(mrcpRequest != null) {
			return mrcpRequest.getVersion();
		}
		return super.getVersion();
	}
	
	@Override
	public boolean hasContent() {
		if(mrcpRequest != null) {
			return mrcpRequest.hasContent();
		}
		return super.hasContent();
	}
	
	@Override
	public void removeContent() {
		if(mrcpRequest != null) {
			mrcpRequest.removeContent();
		}
		else super.removeContent();
	}
	
	@Override
	public MrcpHeader removeHeader(MrcpHeaderName name) {
		if(mrcpRequest != null) {
			return mrcpRequest.removeHeader(name);
		}
		return super.removeHeader(name);
	}
	
	@Override
	public MrcpHeader removeHeader(String name) {
		if(mrcpRequest != null) {
			return mrcpRequest.removeHeader(name);
		}
		return super.removeHeader(name);
	}
	
	@Override
	public void setContent(String content) {
		if(mrcpRequest != null) {
			mrcpRequest.setContent(content);
		}
		else super.setContent(content);
	}
	
	@Override
	public void setContent(String contentType, String contentId, String content) {
		if(mrcpRequest != null) {
			mrcpRequest.setContent(contentType, contentId, content);
		}
		else super.setContent(contentType, contentId, content);
	}
	
	@Override
	public void setContent(String contentType, String contentId, URL content) throws IOException {
		if(mrcpRequest != null) {
			mrcpRequest.setContent(contentType, contentId, content);
		}
		else super.setContent(contentType, contentId, content);
	}
	
	@Override
	public void setMessageLength(int messageLength) {
		if(mrcpRequest != null) {
			mrcpRequest.setMessageLength(messageLength);
		}
		else super.setMessageLength(messageLength);
	}
	
	@Override
	public void setRequestID(long requestID) {
		if(mrcpRequest != null) {
			mrcpRequest.setRequestID(requestID);
		}
		else super.setRequestID(requestID);
	}
	
	@Override
	public void setVersion(String version) {
		if(mrcpRequest != null) {
			mrcpRequest.setVersion(version);
		}
		else super.setVersion(version);
	}
	
	@Override
	public String toString() {
		if(mrcpRequest != null) {
			return mrcpRequest.toString();
		}
		return super.toString();
	}
}
