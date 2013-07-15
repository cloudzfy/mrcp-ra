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

import org.mrcp4j.MrcpRequestState;
import org.mrcp4j.message.MrcpResponse;
import org.mrcp4j.message.header.ChannelIdentifier;
import org.mrcp4j.message.header.IllegalValueException;
import org.mrcp4j.message.header.MrcpHeader;
import org.mrcp4j.message.header.MrcpHeaderName;

public final class MrcpResponseEvent extends MrcpResponse implements Serializable {

	private static final long serialVersionUID = -7227326793383891313L;
	
	private String id;
	private MrcpResponse mrcpResponse;
	
	public MrcpResponseEvent(MrcpResponse mrcpResponse) {
		id = UUID.randomUUID().toString();
		this.mrcpResponse = mrcpResponse;
	}
	
	public MrcpResponseEvent() {
		id = UUID.randomUUID().toString();
		this.mrcpResponse = null;
	}
	
	protected String getId() {
		return id;
	}
	
	public int hashCode() {
		return getId().hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass()) {
			return ((MrcpResponseEvent)obj).getId() == this.getId();
		} else {
			return false;
		}
	}
	
	@Override
	public short getStatusCode() {
		if(mrcpResponse != null) {
			return mrcpResponse.getStatusCode();
		}
		return super.getStatusCode();
	}
	
	@Override
	public void setStatusCode(short statusCode) {
		if(mrcpResponse != null)
		{
			mrcpResponse.setStatusCode(statusCode);
		}
		else super.setStatusCode(statusCode);
	}
	
	@Override
	public void setRequestState(MrcpRequestState requestState) {
		if(mrcpResponse != null) {
			mrcpResponse.setRequestState(requestState);
		}
		else super.setRequestState(requestState);
	}
	
	@Override
	public MrcpRequestState getRequestState() {
		if(mrcpResponse != null) {
			return mrcpResponse.getRequestState();
		}
		return super.getRequestState();
	}
	
	@Override
	public void addHeader(MrcpHeader header) {
		if(mrcpResponse != null) {
			mrcpResponse.addHeader(header);
		}
		else super.addHeader(header);
	}
	
	@Override
	public ChannelIdentifier getChannelIdentifier() throws IllegalValueException {
		if(mrcpResponse != null) {
			return mrcpResponse.getChannelIdentifier();
		}
		return super.getChannelIdentifier();
	}
	
	@Override
	public String getContent() {
		if(mrcpResponse != null) {
			return mrcpResponse.getContent();
		}
		return super.getContent();
	}
	
	@Override
	public String getContentType() {
		if(mrcpResponse != null) {
			return mrcpResponse.getContentType();
		}
		return super.getContentType();
	}
	
	@Override
	public MrcpHeader getHeader(MrcpHeaderName name) {
		if(mrcpResponse != null) {
			return mrcpResponse.getHeader(name);
		}
		return super.getHeader(name);
	}
	
	@Override
	public MrcpHeader getHeader(String name) {
		if(mrcpResponse != null) {
			return mrcpResponse.getHeader(name);
		}
		return super.getHeader(name);
	}
	
	@Override
	public Collection<MrcpHeader> getHeaders() {
		if(mrcpResponse != null) {
			return mrcpResponse.getHeaders();
		}
		return super.getHeaders();
	}
	
	@Override
	public int getMessageLength() {
		if(mrcpResponse != null) {
			return mrcpResponse.getMessageLength();
		}
		return super.getMessageLength();
	}
	
	@Override
	public long getRequestID() {
		if(mrcpResponse != null) {
			return mrcpResponse.getRequestID();
		}
		return super.getRequestID();
	}
	
	@Override
	public String getVersion() {
		if(mrcpResponse != null) {
			return mrcpResponse.getVersion();
		}
		return super.getVersion();
	}
	
	@Override
	public boolean hasContent() {
		if(mrcpResponse != null) {
			return mrcpResponse.hasContent();
		}
		return super.hasContent();
	}
	
	@Override
	public void removeContent() {
		if(mrcpResponse != null) {
			mrcpResponse.removeContent();
		}
		else super.removeContent();
	}
	
	@Override
	public MrcpHeader removeHeader(MrcpHeaderName name) {
		if(mrcpResponse != null) {
			return mrcpResponse.removeHeader(name);
		}
		return super.removeHeader(name);
	}
	
	@Override
	public MrcpHeader removeHeader(String name) {
		if(mrcpResponse != null) {
			return mrcpResponse.removeHeader(name);
		}
		return super.removeHeader(name);
	}
	
	@Override
	public void setContent(String content) {
		if(mrcpResponse != null) {
			mrcpResponse.setContent(content);
		}
		else super.setContent(content);
	}
	
	@Override
	public void setContent(String contentType, String contentId, String content) {
		if(mrcpResponse != null) {
			mrcpResponse.setContent(contentType, contentId, content);
		}
		else super.setContent(contentType, contentId, content);
	}
	
	@Override
	public void setContent(String contentType, String contentId, URL content) throws IOException {
		if(mrcpResponse != null) {
			mrcpResponse.setContent(contentType, contentId, content);
		}
		else super.setContent(contentType, contentId, content);
	}
	
	@Override
	public void setMessageLength(int messageLength) {
		if(mrcpResponse != null) {
			mrcpResponse.setMessageLength(messageLength);
		}
		else super.setMessageLength(messageLength);
	}
	
	@Override
	public void setRequestID(long requestID) {
		if(mrcpResponse != null) {
			mrcpResponse.setRequestID(requestID);
		}
		else super.setRequestID(requestID);
	}
	
	@Override
	public void setVersion(String version) {
		if(mrcpResponse != null) {
			mrcpResponse.setVersion(version);
		}
		else super.setVersion(version);
	}
	
	@Override
	public String toString() {
		if(mrcpResponse != null) {
			return mrcpResponse.toString();
		}
		return super.toString();
	}
	
}
