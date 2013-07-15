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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.ServiceID;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ReceivableService.ReceivableEvent;

public class EventIDFilter {

	private final ConcurrentHashMap<EventTypeID, Set<ServiceID>> eventID2serviceIDs = new ConcurrentHashMap<EventTypeID, Set<ServiceID>>();
	private final ConcurrentHashMap<EventTypeID, Set<ServiceID>> initialEvents = new ConcurrentHashMap<EventTypeID, Set<ServiceID>>();
	
	public boolean filterEvent(FireableEventType eventType) {
		return eventID2serviceIDs.contains(eventType);
	}
	
	public boolean isInitialEvent(FireableEventType eventType) {
		return initialEvents.contains(eventType);
	}
	
	public void serviceActive(ReceivableService receivableService) {
		
		for(ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			Set<ServiceID> serviceIDs = eventID2serviceIDs.get(receivableEvent.getEventType());
			if(serviceIDs == null) {
				serviceIDs = new HashSet<ServiceID>();
				Set<ServiceID> anotherServiceIDs = eventID2serviceIDs.putIfAbsent(receivableEvent.getEventType(), serviceIDs);
				if(anotherServiceIDs != null) {
					serviceIDs = anotherServiceIDs;
				}
			}
			synchronized (serviceIDs) {
				serviceIDs.add(receivableService.getService());
			}
		}
		
		for(ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			if(receivableEvent.isInitialEvent()) {
				Set<ServiceID> serviceIDs = initialEvents.get(receivableEvent.getEventType());
				if(serviceIDs == null) {
					serviceIDs = new HashSet<ServiceID>();
					Set<ServiceID> anotherServiceIDs = initialEvents.putIfAbsent(receivableEvent.getEventType(), serviceIDs);
					if(anotherServiceIDs != null) {
						serviceIDs = anotherServiceIDs;
					}
				}
				synchronized (serviceIDs) {
					serviceIDs.add(receivableService.getService());
				}
			}
		}
	}
	
	 public void serviceInactive(ReceivableService receivableService) {
		 
		 for(ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			 Set<ServiceID> serviceIDs = eventID2serviceIDs.get(receivableEvent.getEventType());
			 if(serviceIDs != null) {
				 synchronized(serviceIDs) {
					 serviceIDs.remove(receivableService.getService());
					 if(serviceIDs.size() == 0) {
						 eventID2serviceIDs.remove(receivableEvent.getEventType());
					 }
				 }
			 }
		 }
		 
		 for(ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			 if(receivableEvent.isInitialEvent()) {
				 Set<ServiceID> serviceIDs = initialEvents.get(receivableEvent.getEventType());
				 if(serviceIDs != null) {
					 synchronized(serviceIDs) {
						 serviceIDs.remove(receivableService.getService());
						 if(serviceIDs.size() == 0) {
							 initialEvents.remove(receivableEvent.getEventType());
						 }
					 }
				 }
			 }
		 }
	 }
	 
	 public void serviceStopping(ReceivableService receivableService) {
		 //do nothing
	 }
}
