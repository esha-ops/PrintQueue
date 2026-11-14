# Testing Plan

| Test | Expected Result |
|---|---|
| Submit valid job | Token generated and job added to queue |
| Leave name blank | Warning message appears |
| Leave phone blank | Warning message appears |
| Submit 10 pages, 2 copies, B&W A4 | Cost is Rs. 40 |
| Submit 10 pages, 1 copy, color A4 | Cost is Rs. 80 |
| Start current job | Status becomes Printing |
| Mark ready | Status becomes Ready for Collection |
| Complete job | Job is removed from active queue |
| Cancel job | Job is removed from active queue |
