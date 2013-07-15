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

import javax.slee.*;
import javax.slee.facilities.*;
import javax.slee.resource.*;

import org.cloudzfy.slee.resource.mrcp.event.MrcpRequestEvent;
import org.cloudzfy.slee.resource.mrcp.event.MrcpResponseEvent;
import org.mrcp4j.MrcpEventName;
import org.mrcp4j.client.MrcpChannel;
import org.mrcp4j.client.MrcpProvider;
import org.mrcp4j.message.MrcpResponse;
import org.mrcp4j.message.header.ChannelIdentifier;
import org.mrcp4j.message.header.IllegalValueException;

public class MrcpResourceAdaptor implements ResourceAdaptor {

	private ResourceAdaptorContext raContext;

	private SleeEndpoint sleeEndpoint = null;

	private EventLookupFacility eventLookupFacility;

	private Tracer tracer;

	public static final int DEFAULT_EVENT_FLAGS = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK;

	public static final int UNREFERENCED_EVENT_FLAGS = EventFlags.setRequestEventReferenceReleasedCallback(DEFAULT_EVENT_FLAGS);

	private MrcpProviderImpl mrcpProviderImpl = null;
	private MrcpActivityManager mrcpActivityManager = null;
	
	private EventIDCache eventIDCache = new EventIDCache();
	private EventIDFilter eventIDFilter = new EventIDFilter();
	
	private static final int EVENT_FLAGS = getEventFlags();
	
	private Address address = new Address(AddressPlan.IP, "localhost");
	
	public static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;
	
	private static int getEventFlags() {
		int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
		EventFlags.setRequestProcessingFailedCallback(eventFlags);
		return eventFlags;
	}
	
	public MrcpActivityManager getActivityManager() {
		return mrcpActivityManager;
	}
	
	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	public void setResourceAdaptorContext(ResourceAdaptorContext context) {
		this.raContext = context;
		this.tracer = context.getTracer(MrcpResourceAdaptor.class.getSimpleName());
		this.sleeEndpoint = context.getSleeEndpoint();
		this.eventLookupFacility = context.getEventLookupFacility();
		this.mrcpProviderImpl = new MrcpProviderImpl(this, tracer);
	}

	public void unsetResourceAdaptorContext() {
		this.raContext = null;
		this.tracer = null;
		this.sleeEndpoint = null;
		this.eventLookupFacility = null;
		this.mrcpProviderImpl = null;
	}

	public void raConfigure(ConfigProperties properties) {
		
		if (tracer.isFineEnabled()) {
			tracer.fine("Mrcp Resource Adaptor configured.");
		}
		
	}
	
	public void raUnconfigure() {

		if (tracer.isFineEnabled()) {
			tracer.fine("Mrcp Resource Adaptor unconfigured.");
		}
	}
	
	public void raActive() {

		this.sleeEndpoint = raContext.getSleeEndpoint();
		
		mrcpActivityManager = new MrcpActivityManager();
		
		mrcpProviderImpl.initialize(MrcpProvider.PROTOCOL_TCP_MRCPv2);
		
		if (tracer.isFineEnabled()) {
			tracer.fine("Mrcp Resource Adaptor entity active.");
		}

	}		

	public void raStopping() {

		if (tracer.isFineEnabled()) {
			tracer.fine("Mrcp Resource Adaptor entity stopping.");
		}
	}

	public void raInactive() {
		this.mrcpProviderImpl.delete();

		if (tracer.isFineEnabled()) {
			tracer.fine("Mrcp Resource Adaptor entity inactive.");
		}	   
	}

	// Configuration Management -----------------------------------------------

	public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {

		if (tracer.isFineEnabled()) {
			tracer.fine("Mrcp Resource Adaptor configuration verified.");
		}	   
	}

	public void raConfigurationUpdate(ConfigProperties properties) {
		raConfigure(properties);		
	}

	// Interface Access -------------------------------------------------------

	public Object getResourceAdaptorInterface(String className) {
		return this.mrcpProviderImpl;
	}

	public Marshaler getMarshaler() {
		return null;
	}   

	// Event Filter -----------------------------------------------------------

	public void serviceActive(ReceivableService receivableService) {
		eventIDFilter.serviceActive(receivableService);
	}

	public void serviceStopping(ReceivableService receivableService) {
		eventIDFilter.serviceStopping(receivableService);
	}

	public void serviceInactive(ReceivableService receivableService) {
		eventIDFilter.serviceInactive(receivableService);
	}

	// Mandatory Callbacks ----------------------------------------------------

	public void queryLiveness(ActivityHandle handle) {

	}

	public Object getActivity(ActivityHandle handle) {
		if(handle != null) {
			return mrcpActivityManager.getMrcpChannelActivity((MrcpChannelActivityHandle)handle);
		}
		return null; 
	}
	
	public ActivityHandle getActivityHandle(Object activity) {
		if(activity != null) {
			MrcpChannel mrcpChannel = ((MrcpChannelActivityImpl)activity).getMrcpChannel();
			return mrcpActivityManager.getMrcpChannelActivityHandle(mrcpChannel);
		}
		return null;
	}

	public void administrativeRemove(ActivityHandle handle) {
		
	}
	
	protected void endActivity(ActivityHandle handle) {
		if(handle != null && mrcpActivityManager.containsActivityHandle(handle)) {
			this.getSleeEndpoint().endActivity(handle);
		}
	}

	// Optional Callbacks -----------------------------------------------------

	public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {

	}

	public void eventProcessingFailed(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags, FailureReason reason) {

	}

	public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {

	}

	public void activityEnded(ActivityHandle handle) {

	}

	public void activityUnreferenced(ActivityHandle handle) {
		
	}

	public void processMrcpSynthesizerEvent(MrcpRequestEvent event) {
		MrcpEventName name = event.getEventName();
		switch (name) {
		case SPEECH_MARKER:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.SPEECH_MARKER", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		case SPEAK_COMPLETE:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.SPEAK_COMPLETE", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		default:
			tracer.severe("Unexpected event name: " + event.getEventName());
		}
	}
	
	public void processMrcpRecognizerEvent(MrcpRequestEvent event) {
		MrcpEventName name = event.getEventName();
		switch (name) {
		case START_OF_INPUT:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.START_OF_INPUT", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		case RECOGNITION_COMPLETE:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.RECOGNITION_COMPLETE", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		case INTERPRETATION_COMPLETE:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.INTERPRETATION_COMPLETE", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		default:
			tracer.severe("UNexpected event name: " + event.getEventName());
		}
	}
	
	public void processMrcpRecorderEvent(MrcpRequestEvent event) {
		MrcpEventName name = event.getEventName();
		switch (name) {
		case START_OF_INPUT:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.START_OF_INPUT", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		case RECORD_COMPLETE:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.RECORD_COMPLETE", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		default:
			tracer.severe("UNexpected event name: " + event.getEventName());
		}
	}
	
	public void processMrcpVerifierEvent(MrcpRequestEvent event) {
		MrcpEventName name = event.getEventName();
		switch (name) {
		case START_OF_INPUT:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.START_OF_INPUT", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		case VERIFICATION_COMPLETE:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Request.VERIFICATION_COMPLETE", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		default:
			tracer.severe("UNexpected event name: " + event.getEventName());
		}
	}
	
	public void processMrcpResponseEvent(MrcpResponseEvent event) {
		short statusCode = event.getStatusCode();
		switch(statusCode) {
		case MrcpResponse.STATUS_SUCCESS:
		case MrcpResponse.STATUS_SUCCESS_SOME_OPTIONAL_HEADERS_IGNORED:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Response.SUCCESS", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		case MrcpResponse.STATUS_METHOD_NOT_ALLOWED:
		case MrcpResponse.STATUS_METHOD_NOT_VALID_IN_STATE:
		case MrcpResponse.STATUS_UNSUPPORTED_HEADER:
		case MrcpResponse.STATUS_ILLEGAL_VALUE_FOR_HEADER:
		case MrcpResponse.STATUS_RESOURCE_NOT_ALLOCATED:
		case MrcpResponse.STATUS_MANDATORY_HEADER_MISSING:
		case MrcpResponse.STATUS_OPERATION_FAILED:
		case MrcpResponse.STATUS_UNRECOGNIZED_MESSAGE_ENTITY:
		case MrcpResponse.STATUS_UNSUPPORTED_HEADER_VALUE:
		case MrcpResponse.STATUS_NON_MONOTONIC_SEQUENCE_NUMBER:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Response.CLIENT_ERROR", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		case MrcpResponse.STATUS_SERVER_INTERNAL_ERROR:
		case MrcpResponse.STATUS_PROTOCOL_VERSION_NOT_SUPPORTED:
		case MrcpResponse.STATUS_PROXY_TIMEOUT:
		case MrcpResponse.STATUS_MESSAGE_TOO_LARGE:
			try {
				ChannelIdentifier ci = event.getChannelIdentifier();
				processMrcpEvent(ci, "org.cloudzfy.mrcp.Response.SERVER_ERROR", event);
			} catch (IllegalValueException e) {
				tracer.severe("Unable to get Channel Identifier. ", e);
			}
			break;
			
		default:
			tracer.severe("UNexpected status code: " + event.getStatusCode());
		}
		
	}
	
	protected void processMrcpEvent(ChannelIdentifier channelIdentifier, String eventName, Object eventObject) {
		
		MrcpChannelActivityHandle handle = mrcpActivityManager.getMrcpChannelActivityHandle(channelIdentifier);
		if(handle != null) {
			fireEvent(eventName, handle, eventObject);
			
		}
		
	}
	
	private void fireEvent(String eventName, ActivityHandle handle, Object event) {
		FireableEventType eventID = eventIDCache.getEventID(eventLookupFacility, eventName);
		if(eventID == null) {
			tracer.severe("No Event ID was found.");
		} else {
			if(eventIDFilter.filterEvent(eventID)) {
				tracer.fine("Event ID: " + eventID + " filtered. ");
			} else {
				try {
					this.raContext.getSleeEndpoint().fireEvent(handle, eventID, event, address, null, EVENT_FLAGS);
				} catch (Exception e) {
					tracer.severe("Error firing event.", e);
				}
			}
		}
		
	}
}
