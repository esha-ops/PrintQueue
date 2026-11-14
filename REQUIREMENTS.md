# Requirements

## Functional Requirements

1. Customer can enter name and phone number.
2. Customer can select a service: Black & White Printout, Color Printout, Photocopy, Scanning, or Spiral Binding.
3. Customer can enter pages, copies, paper size, and print mode.
4. System generates a unique token such as PRT-001.
5. System calculates estimated cost.
6. System displays queue position and estimated waiting time.
7. Operator can start, mark ready, complete, or cancel the current job.
8. System displays active jobs in a JTable.
9. System saves jobs to data/jobs.csv.

## Non-Functional Requirements

- Simple and responsive Swing interface
- Business logic separated from UI
- CSV-based persistence
- Input validation
- Easy to extend with new services
