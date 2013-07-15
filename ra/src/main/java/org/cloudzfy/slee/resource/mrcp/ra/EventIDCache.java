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

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.FireableEventType;

public class EventIDCache {

	private ConcurrentHashMap<String, FireableEventType> eventIds = new ConcurrentHashMap<String, FireableEventType>();
	private String EVENT_TYPE_VENDOR = "org.cloudzfy";
	private String EVENT_TYPE_VERSION = "1.0";
	protected FireableEventType getEventID(EventLookupFacility eventLookupFacility, String eventName) {
		FireableEventType eventType = eventIds.get(eventName);
		if(eventType == null) {
			try {
				eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName, EVENT_TYPE_VENDOR, EVENT_TYPE_VERSION));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(eventType != null) {
				eventIds.put(eventName, eventType);
			}
		}
		return eventType;
	}
}
