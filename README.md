# PrintQueue – Smart Xerox Shop Management System

PrintQueue is a Java Swing desktop application designed to simplify and digitize print shop and Xerox shop operations. It provides a dedicated Customer Portal for submitting and tracking print jobs and a Shop Owner Portal for managing orders, monitoring the queue, updating job statuses, and handling billing.

The application demonstrates how a traditional Xerox shop can use a digital system to improve job management, reduce waiting uncertainty, and provide a more organized customer experience.

## Problem Statement

Customers visiting print shops often have to wait without knowing their queue position, estimated cost, or when their documents will be ready. Shop operators also need an organized way to manage multiple customer requests, track their progress, and calculate charges.

PrintQueue addresses these challenges through a digital job submission, queue management, tracking, notification, and billing system.

## Features

### 1. Customer Portal

- Customer registration and login.
- Password validation with a minimum requirement of 8 characters.
- Submit print, photocopy, scanning, and spiral-binding jobs.
- Upload documents in PDF, JPG, JPEG, and PNG formats.
- Enter customer details, including name and phone number.
- Select paper size (A4, A3, Legal), number of pages, copies, and print mode.
- Calculate estimated printing costs.
- Generate a unique digital job token.
- View queue position and estimated waiting time.
- Track job status using the generated token.
- Receive a completion notification when the tracked job is completed.
- View, print, and save the bill after job completion.

### 2. Shop Owner Portal

- Shop owner login and first-time account setup.
- View the live print job queue.
- Monitor customer details and job information.
- Access uploaded document details.
- Start the current print job.
- Update job status to Ready for Collection.
- Mark jobs as Completed.
- Cancel jobs when required.
- Manage customer orders through a dedicated interface.

### 3. Billing and Notifications

- Automatic cost calculation based on the selected service and job details.
- Generate a bill containing customer information, job details, and estimated cost.
- View and print bills.
- Save bills as text files.
- Display a Java Swing popup notification when a tracked job is completed.

### 4. Additional Features

- Separate Customer and Shop Owner windows.
- Simultaneous two-portal demonstration.
- Shared job queue between both portals.
- Unique job IDs and tracking tokens.
- Automatic job status updates.
- CSV-based job storage support.
- Logout functionality.
- Demo option to launch both portals without logging in.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Core programming language |
| Java Swing | Graphical User Interface |
| CSV | Job data storage |
| JDK 17 | Compilation and execution |
| Visual Studio Code | Development environment |
| Git | Version control |
| GitHub | Source code hosting |

## Project Structure

```text
PrintQueue/
│
├── .gitignore
├── README.md
├── pom.xml
│
├── docs/
│   ├── ARCHITECTURE.md
│   ├── DATA_MODEL.md
│   ├── DEVELOPMENT_ROADMAP.md
│   ├── PROJECT_PLAN.md
│   ├── REQUIREMENTS.md
│   ├── TESTING_PLAN.md
│   └── UI_DESIGN.md
│
└── src/
    └── main/
        └── java/
            └── com/
                └── printqueue/
                    │
                    ├── Main.java
                    │
                    ├── model/
                    │   ├── Customer.java
                    │   ├── Printer.java
                    │   ├── PrintJob.java
                    │   └── ServiceRate.java
                    │
                    ├── service/
                    │   ├── AccountService.java
                    │   ├── BillingService.java
                    │   └── PrintQueueService.java
                    │
                    ├── storage/
                    │   └── CsvStorage.java
                    │
                    └── ui/
                        ├── LoginFrame.java
                        ├── MainFrame.java
                        ├── CustomerFrame.java
                        ├── OwnerFrame.java
                        ├── NewJobPanel.java
                        ├── TrackJobPanel.java
                        ├── QueuePanel.java
                        └── OperatorPanel.java
```

## Requirements

- JDK 17 or later.
- Visual Studio Code or any Java-compatible IDE.
- Windows, Linux, or macOS.

Maven is not required to compile and run the application using the commands below.

## How to Run

### 1. Check Java Installation

Open a terminal and execute:

```bash
java -version
javac -version
```

Ensure that JDK 17 or later is installed.

### 2. Clone the Repository

```bash
git clone https://github.com/esha-ops/PrintQueue.git
```

Navigate into the project directory:

```bash
cd PrintQueue
```

### 3. Compile the Project

Open the terminal in the project root directory.

For **Windows PowerShell**, execute:

```powershell
New-Item -ItemType Directory out -Force

javac -d out (Get-ChildItem -Recurse src\main\java -Filter *.java | ForEach-Object { $_.FullName })
```

### 4. Run the Application

```bash
java -cp out com.printqueue.Main
```

The application will launch the login interface, from which users can access the Customer Portal or Shop Owner Portal. The demo option can be used to launch both portals simultaneously.

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

A job can also be cancelled when required.

## Future Enhancements

- Database integration for persistent and centralized storage.
- Real-time communication between customer and shop owner systems running on separate computers.
- Automatic customer notifications without requiring manual job tracking.
- Enhanced reporting and analytics for shop owners.
- Improved security and password management.

## Author

Developed as a Java Object-Oriented Programming project.

## Repository

GitHub: https://github.com/esha-ops/PrintQueue