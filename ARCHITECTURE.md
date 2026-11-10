# Architecture

PrintQueue uses a simplified MVC structure.

```text
UI Layer -> Service Layer -> Model Layer -> CSV Storage
```

| Layer | Package | Responsibility |
|---|---|---|
| Model | com.printqueue.model | Customer, PrintJob, Printer, ServiceRate |
| Service | com.printqueue.service | Queue and billing logic |
| UI | com.printqueue.ui | Swing screens |
| Storage | com.printqueue.storage | CSV read/write |

A single shared `PrintQueueService` object is passed to all UI panels so that submitted jobs appear in the same queue.
