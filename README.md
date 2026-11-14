# PrintQueue

PrintQueue is a Java Swing desktop application for managing printouts, photocopies, scanning, and spiral-binding jobs in a xerox shop.

## Problem

Customers often wait without knowing their queue position, estimated cost, or when their job will be ready.

## Features

- Submit print, photocopy, scanning, or binding jobs
- Generate a digital token
- Calculate estimated cost
- Show queue position and estimated waiting time
- Live queue table
- Operator can start, mark ready, complete, or cancel jobs
- Save jobs to CSV

## Run

Requires JDK 25 or later.

```bash
mvn clean compile
mvn exec:java
```

Or without Maven:

```bash
javac --release 25 -d out *.java
java -cp out com.printqueue.Main
```

## Job Status Flow

```text
Queued -> Printing -> Ready for Collection -> Completed
```
