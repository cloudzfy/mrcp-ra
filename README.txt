=======================================================
Readme for mrcp-ra
An Implementation of MRCP Resource Adaptor of JAIN SLEE
=======================================================


Overview
--------
This project is a part of Gryffin-as, for the use of MRCPv2 Protocol in JAIN SLEE. It can be used to communicate with Speech Server by MRCPv2 Protocol according to RFC 6787. The design patterns is followed the standard JAIN SLEE Specification, so you can easily comprehend the source code and help us improve the system.


The Origin
----------
At first, the author search many materials for a MRCPv2 Resource Adaptor in JAIN SLEE, but there is no one met the need. So this project is designed to implement a MRCPv2 Resource Adaptor in JAIN SLEE for the use in Gryffin-as, IVR Media Resource Controller in JAIN SLEE. In the meantime, this project can also be used in other JAIN SLEE project if the API design level of protocol stack is fit for your need anyway.


Design & Technology
-------------------
This project is a Resource Adaptor in JAIN SLEE, which is followed the design patterns of JAIN SLEE Specifications. It includes the following modules:

* MRCP Resource Adaptor Type (RAType)
* MRCP Resource Adaptor (RA)
* MRCP Event
* MRCP Library

For the implementation of the lower level of protocol stack, this project uses MRCP4J Project as MRCPv2 Protocol Stack, and changes some design patterns that conflict with JAIN SLEE. You can find more information on MRCP4J here: http://www.speechforge.org/projects/mrcp4j/.


Vision
------
This project not only finishes the requirement of MRCPv2 Protocol Stack Plugin in Gryffin-as Project, but also provides a reference of implementation for SLEE-based MRCPv2 Resource Adaptor design in IMS area.


About Author
------------
This project is designed by @cloudzfy as a graduation project in Beijing University of Posts and Telecommunications, China. And it is also a prospective study on self-service access control system, which belongs the Communication Software Laboratory, School of Software Engineering, Beijing University of Posts and Telecommunications.


Support & Contact
-----------------
If you have any trouble on this project, please submit it to our Issue Tracker, thank you.
