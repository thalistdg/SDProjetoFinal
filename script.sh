#!/bin/bash
rm -f file1.txt
rm -f file2.txt
rm -f file3.txt
javac ThreadBalancer.java
javac ThreadBalancerServer.java
javac Balancer.java
javac Server.java
javac Server.java
javac Server.java
javac Coordinator.java
javac Client.java
gnome-terminal -- bash -c "java Balancer; exec bash"
sleep 5
gnome-terminal -- bash -c "java Server 10000 1 file1.txt; exec bash"
sleep 5
gnome-terminal -- bash -c "java Server 20000 2 file2.txt; exec bash"
sleep 5
gnome-terminal -- bash -c "java Server 30000 3 file3.txt; exec bash"
sleep 5
gnome-terminal -- bash -c "java Coordinator; exec bash"
sleep 5
gnome-terminal -- bash -c "java Client; exec bash"
sleep 5
gnome-terminal -- bash -c "java Client; exec bash"
sleep 5
java Client
