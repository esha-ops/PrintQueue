# Data Model

## PrintJob

| Field | Type |
|---|---|
| jobId | String |
| token | String |
| customerName | String |
| phoneNumber | String |
| serviceType | String |
| paperSize | String |
| pages | int |
| copies | int |
| printMode | String |
| estimatedCost | double |
| estimatedMinutes | int |
| submittedTime | String |
| status | String |

## Status Values

- Queued
- Printing
- Ready for Collection
- Completed
- Cancelled

## Cost Formula

```text
Total Cost = Pages x Copies x Rate
```

A3 paper uses double the base rate.
