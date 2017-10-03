# sample-connectors
This project is a sample desgined to be a based for the developpment of a new ARender HMI connector.

Maven stucture :  
-> A parent module  
--> A child module arender-sample-connector-jar : create a jar that is the connector  
--> A child module arender-sample-connector-war : Fetch ARender HMI WAR, modify it by including the jar above in its lib (overlay)
