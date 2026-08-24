<div align="center">
  <h1> CT6049: Assignment 2</h1>
    <p>Distributed Database Management & Data Warehousing</p>
    <p><strong>Grade Awarded:</strong> 75/100</p>
</div>

> [!IMPORTANT] 
> This repository contains the finalised, submitted works for Assignment 2 of the CT6049 Module. The content here is preserved "as is" in its submitted state, with no further corrections or feedback applied.
>
> Intended for Archival and Portfolio purposes [^1]**only**

---

## Overview 
Building directly upon the operational demonstration from [Assignment 1](https://github.com/PeaterPita/CT6049A1), this submission focuses on the **Data Warehousing** side of database Management. The core objective was to use the transactional database implemented in that first submission to feed a multi-dimension data warehouse; with the intent of answering complex queries from "Decision Makers".

Similarly to before, the application comes conjoined with a technical report, critically evaluating data modeling, the ETL process, Optimisation strategies, and all manners of administration tools. 

## Relevant Skills  
- Implanted secure **Authentication** and **Authorisation** systems using **Role Based** access. 
- In-depth database optimisations were deployed, through the likes of: **Indexing**, **Partitioning**, and expanded use cases of prepared statements.

<div align="center">
    <p><strong>Tools Used</strong></p>
    [ Java, SpringBoot, Docker, Svelte,]
</div>

# Preserved README. 
As this assignment was submitted with its own README, what follows below is the preserved instructions. 

---

## Requirements
* Docker / Docker Compose
* Java 17
* Postgresql 18
* Npm / Node.JS

## How to run
### 1. Start the development servers

```cmd
./run.sh or .\run.bat
```

The supplied run scripts *should* work. However if they do fail:

- Install frontend dependencies (cd frontend && npm install)
- Spin up docker containers (docker compose up -d)

- Open a terminal each in `frontend/` and `backend/` and run the commands:
frontend:   npm run dev
backend:    mvn spring-boot:run


### 2. Populate operational data
The python script provided in scripts/data.py is the seeder for the operational database tables. Download the dependicies and run like any 
normal python script. 


### 3. Dashboard
Navigate to localhost:5173
Log into the desired account. Account details are provided in the appendices section of the main report. 
Depending on what account you log into, different features will be available (student accounts are functionally identical to users in DBA1)


### 4. ETL
To cause an ETL event, an admin must manually initiate one. Log into the admin account (if not alread) and click the `LOAD ETL` button.


## Additional info
ss -lputn 'sport = :20550

---

[^1]: All rights reserved. Reproduction or use of this material for academic submissions, wholly or in part, is strictly prohibited. All works from copyright holders other than myself, such as University Of Gloucestershire, have been omitted, and or, summarised.
