# CSUBatchGroup1

---

# README - CSUbatch - A Batch Scheduling System - Group 1

## Team Members
- [@Himanshu-Bohra](https://github.com/Himanshu-Bohra)
- [@John-Morales](https://github.com/john-dym)
- [@Akshith-Nukala](https://github.com/Akshith-github)
- [@Dinessh-Venigalla](https://github.com/Coolboy02)

## Overview

CSUbatch is a batch scheduling system designed to simulate job scheduling using multithreading. It consists of two threads: the scheduling thread (producer) and the dispatching thread (consumer). The system implements three scheduling Model.algorithms: **First Come First Served (FCFS)**, **Shortest Model.datastructures.Job First (SJF)**, and **Priority-based Scheduling**. It uses mutexes and condition variables to ensure thread synchronization.

## System Architecture

The system is comprised of the following modules:
- **Scheduling Module**: Manages job submission and enforces scheduling policies.
- **Dispatching Module**: Runs the scheduled jobs and measures execution and response times.

### Data Flow
- The scheduling module adds jobs to the job queue, enforcing a selected scheduling policy.
- The dispatching module picks jobs from the job queue, executes them, and measures their performance metrics such as turnaround time and CPU time.

## Features

- **Scheduling Algorithms**: FCFS, SJF, and Priority.
- **Synchronization**: Utilizes condition variables and mutexes for thread synchronization.
- **Model.datastructures.Job Model.datastructures.Queue**: Handles job submission and execution in a queue with configurable parameters (job name, execution time, and priority).
- **User Interface**: Command-line interface for job submission, listing jobs, changing scheduling policies, and performance evaluation.
- **Performance Evaluation**: Measures response time, throughput, and job performance under various workload conditions.

## Setup and Installation

1. **Linux Environment**: Ensure the system is running on a Linux platform. The project is designed to work specifically on Linux. We are assuming Ubuntu 24.04 for the rest of the instructions.
2. **Compilation**: Compile the project using a Java compiler.
3. **Dependencies**:
      - Java 21 or higher.

**Install Java JDK 21**: Required to compile and run the program. The two below commands are to install the Java 21 JDK on your system on Ubuntu 24.04.
```ubuntu
sudo apt update
sudo apt install openjdk-21-jdk-headless -y
```

**Install Git**: Required to download.
```ubuntu
sudo apt install git -y
```

**Confirm Java installiation**: The expected output is "openJDK version 21.x.x" and "javac 21.x.x"
```
java -version
javac -version
```

**Download the Project**: Clone the repository to your local machine.

```sh
git clone https://github.com/john-dym/csubatch.git
````

**Change Directory into the Project**
```sh
cd csubatch
```

**Compile the Project**: Navigate to the project directory and compile the Java files.
```sh
mkdir -p bin
javac -d bin -sourcepath src src/Main.java
```

**Run the Project**: After compilation, launch the program using the following command:
```sh
java -cp bin Main
```

## Commands

### Help Command
Displays the available commands.
```sh
> help
```

### Job Submission
Submits a job with the specified name, execution time, and priority.
```sh
> run <job_name> <execution_time> <priority>
```

### List Jobs
Displays the list of jobs in the queue, including their status.
```sh
> list
```

### Change Scheduling Policy
Switches between scheduling policies.
```sh
> fcfs   # First Come First Served
> sjf    # Shortest Job First
> priority # Priority-based Scheduling
```

### Performance Test
Performs a test with a given benchmark, scheduling policy, and job parameters.
```sh
> test <benchmark> <policy> <num_of_jobs> <priority_levels> <min_cpu_time> <max_cpu_time>
```

### Quit
Exits the scheduler, displaying performance results.
```sh
> quit
```

## Detailed command descriptions

### 1. **help**
   - Displays a list of available commands and their descriptions.
   ```sh
   > help
   ```

### 2. **run <job_name> <execution_time> <priority>**
   - Submits a job with the specified **job name**, **execution time**, and **priority**.
   - Example:
   ```sh
   > run sample_job 10 3
   ```

### 3. **list**
   - Displays the list of jobs in the job queue, showing details such as job name, execution time, priority, and status.
   - Example:
   ```sh
   > list
   ```

### 4. **fcfs**
   - Changes the scheduling policy to **First Come First Served (FCFS)**. Jobs are processed in the order they are submitted.
   - Example:
   ```sh
   > fcfs
   ```

### 5. **sjf**
   - Changes the scheduling policy to **Shortest Model.datastructures.Job First (SJF)**. The job with the shortest execution time is processed first.
   - Example:
   ```sh
   > sjf
   ```

### 6. **priority**
   - Changes the scheduling policy to **Priority-based Scheduling**. Jobs are processed based on their assigned priority (lower priority number means higher priority).
   - Example:
   ```sh
   > priority
   ```

### 7. **test <benchmark> <policy> <num_of_jobs> <priority_levels> <min_cpu_time> <max_cpu_time>**
   - Runs an automated performance test with a given **benchmark** and **scheduling policy**. You can specify the number of jobs, priority levels, and CPU time range for jobs.
   - Example:
   ```sh
   > test mybenchmark fcfs 5 3 10 20
   ```

### 8. **quit**
   - Exits the scheduler, displaying performance results such as average turnaround time, average CPU time, waiting time, and throughput.
   - Example:
   ```sh
   > quit
   ```

These commands allow you to interact with the CSUbatch system, submit jobs, change scheduling policies, run performance tests, and view results.

## Optional Programs
**Compile microbenchmark and test runner**: Capital letters are required

```sh
javac -d bin -sourcepath src src/MicroBenchmark.java
javac -d bin -sourcepath src src/tests/TestRunner.java
```

**Run microbenchmark**: You must be pass a whole number to microbenchmark. In the example we will use 5 seconds.

```sh
java -cp bin MicroBenchmark 5
```

**Run TestRunner**: This will run the unit tests among the source code.

```sh
java -cp bin tests/TestRunner
```

## Performance Metrics

- **Average Response Time**
- **Throughput**

## Project Structure

- **src/**: Contains the main project code, including modules for scheduling, dispatching, and performance evaluation.
- **tests/**: Unit tests for the modules.
- **logs/** : Program will generate logs as it runs.

## Conclusion

This project demonstrates multithreaded programming concepts like synchronization, thread safety, and scheduling Model.algorithms, which are crucial in many real-world applications such as operating systems and job scheduling systems.

---
