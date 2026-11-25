**PPT:** https://drive.google.com/file/d/1mlkBog2B6fanzrXbr5ORS-9B_UVU9vT9/view?usp=sharing

**Report:** 
# Loan EMI Priority Manager

A console-based Java program that manages loan applications and processes the most urgent ones first.

## Problem Statement
Manage loan applications and prioritise them for review. A priority queue processes urgent applications first, a BST organises loans by amount, hashing gives fast application lookup, and sorting generates a review report. Applications are saved to a file so they are kept between runs.

## Data Structures and Algorithms Used

| Concept | Implementation | Used for | Time complexity |
|---|---|---|---|
| Priority Queue | `PriorityQueue` | Picking the next application to process (highest priority first) | Add / remove: O(log n) |
| Binary Search Tree | Own `Node` class, keyed by loan amount | Storing loans by amount; in-order traversal prints them sorted | Insert: O(log n) average, O(n) worst. Traversal: O(n) |
| Hashing | `HashMap` | Finding an application by its ID | Lookup: O(1) average |
| Insertion Sort | Written by hand | Sorted review report (priority, then amount) | O(n²) |
| File Handling | `BufferedReader` / `PrintWriter` | Loading at startup and saving on exit | O(n) |

## Features

- Submit a loan application (ID is generated automatically: L101, L102, ...)
- Search an application by ID
- Process the next application (highest priority first)
- Display all loans sorted by amount (BST)
- Sorted review report (priority, then amount)
- Applications and their status are loaded at startup and saved on exit

## Rules

- A higher priority score is processed first (priority is from 1 to 10)
- If two applications have the same priority, the one that arrived first is processed first
- Loan amount must be positive; zero and negative amounts are rejected
- Invalid priorities, empty names and non-numeric input are rejected

## Sample Run

1. Submit application
2. Search application
3. Process next
4. Display all (by amount)
5. Sorted report
0. Save and exit
Choice: 1
Name: Aman
Loan amount: 100000
Priority (1-10, higher = more urgent): 3
Application received: L101 | Aman | 100000

Choice: 3
Processing L101 | Aman

## Test Cases

| # | Scenario | Input | Expected output |
|---|---|---|---|
| 1 | Normal input | Submit Aman, 100000, priority 3 | `Application received: L101 \| Aman \| 100000` |
| 2 | Boundary input | Only one application queued; process next | `Processing L101 \| Aman` |
| 3 | Invalid input | Search application L999 | `Application L999 not found.` |
| 4 | Different valid scenario | Priorities 2, 5 and 3; process next | The priority 5 application is processed first |

## Data File Format

`loan_applications.txt` holds one application per line:

ID|Name|Amount|Priority|Status
L101|Aman|100000|3|PROCESSED

## Project Structure

.
├── LoanEMIManager.java  
├── loan_applications.txt    
└── README.md
