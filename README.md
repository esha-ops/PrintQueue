# PrintQueue – Smart Xerox Shop Management System

PrintQueue is a Java Swing desktop application designed to simplify print shop and xerox shop operations. It helps customers submit and track print jobs while allowing shop operators to manage the job queue efficiently.

## Problem Statement

Customers often have to wait without knowing their queue position, estimated cost, or when their print job will be ready. Shop operators also need an organized way to manage multiple customer requests.

PrintQueue addresses these problems through a digital job submission, billing, and queue management system.

## Features

### Customer Portal
- Submit print, photocopy, scanning, and spiral-binding jobs.
- Upload documents in PDF, JPG, JPEG, and PNG formats.
- Enter customer details, including phone number.
- Select paper size, number of pages, copies, and print mode.
- Calculate estimated printing costs.
- Generate a unique digital job token.
- View queue position and estimated waiting time.
- Track job status using the generated token.

### Shop Owner Portal
- View the live print job queue.
- Monitor customer and job details.
- Start the current job.
- Mark jobs as ready for collection.
- Mark jobs as completed.
- Cancel jobs when required.

### Other Features
- Automatic job status updates.
- Separate Customer and Shop Owner windows.
- Shared queue management between both portals.
- CSV-based job storage support.

## Technologies Used

- **Programming Language:** Java
- **JDK:** Java 17
- **GUI:** Java Swing
- **Data Storage:** CSV
- **IDE:** Visual Studio Code
- **Version Control:** Git and GitHub

## Project Structure

```text
PrintQueue/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── printqueue/
│                   ├── Main.java
│                   │
│                   ├── model/
│                   │   ├── Customer.java
│                   │   ├── PrintJob.java
│                   │   ├── Printer.java
│                   │   └── ServiceRate.java
│                   │
│                   ├── service/
│                   │   ├── BillingService.java
│                   │   └── PrintQueueService.java
│                   │
│                   ├── storage/
│                   │   └── CsvStorage.java
│                   │
│                   └── ui/
│                       ├── MainFrame.java
│                       ├── CustomerFrame.java
│                       ├── OwnerFrame.java
│                       ├── NewJobPanel.java
│                       ├── TrackJobPanel.java
│                       ├── QueuePanel.java
│                       └── OperatorPanel.java
│
├── out/
├── pom.xml
├── README.md
└── .gitignore
```

## Requirements

- JDK 17 or later.
- Visual Studio Code or any Java-compatible IDE.
- Windows, Linux, or macOS.

Maven is not required to compile and run the application using the commands below.

## How to Run

### 1. Check Java Installation

Open a terminal and run:

```powershell
java -version
javac -version
```

Ensure that JDK 17 or later is installed.

### 2. Open the Project Directory

Open the project folder in VS Code and navigate to the project root directory.

### 3. Compile the Project

Run the following commands in the VS Code terminal (PowerShell):

```powershell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory out -Force

javac -d out (Get-ChildItem -Recurse src\main\java -Filter *.java | ForEach-Object { $_.FullName })
```

### 4. Run the Application

```powershell
java -cp out com.printqueue.Main
```

The application will launch the Customer Portal and Shop Owner Portal.

## Job Status Flow

```text
Queued
   |
   v
Printing
   |
   v
Ready for Collection
   |
   v
Completed
```

A job can also be cancelled before completion.

## Future Enhancements

- Role-based customer and shop owner authentication.
- Customer registration and login.
- Shop owner account setup.
- Improved user interface and dashboard.
- Automatic customer notifications for job status changes.

## Author

Developed as a Java Object-Oriented Programming project.

## Repository

GitHub: https://github.com/esha-ops/PrintQueue
