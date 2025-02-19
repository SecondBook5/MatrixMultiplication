# **Lab 1: Strassen Multiplication**
### **EN.605.620 - Algorithms for Bioinformatics**
**Developer:** AJ Book  
**Instructor:** Dr. Rubey

---

## **Overview**
This project implements and compares two matrix multiplication algorithms:
1. **Naive Matrix Multiplication** (**O(n³)**)
2. **Strassen's Algorithm** (**O(n^2.8074)**)

The objective is to evaluate the efficiency of Strassen’s algorithm compared to the standard naive approach. The program:
- Measures execution time and multiplication counts.
- Verifies the correctness of Strassen’s algorithm against naive multiplication.
- Generates formatted tables of results.
- Produces performance graphs comparing expected and actual complexity trends.

![img_1.png](img_1.png)

---

## **Enhancements and Features**
- **Performance Metrics:** Measures runtime and number of multiplications for both algorithms.
- **Automated Comparisons:** Checks whether Strassen and naive multiplication results match.
- **Graph Generation:** Plots multiplication counts and expected complexity curves.
- **Debug Mode:** Enables verbose logging for tracking execution details.
- **Flexible File Handling:**
   - **Input files are read from the `input/` directory.**
   - **Output files (logs and plots) are stored in the `output/` directory.**
   - **Supports user-specified filenames for logs and plots via command-line arguments.**
- **Matrix Operations:** Supports addition, subtraction, and splitting/merging for Strassen’s algorithm.

---

## **Project Structure**
The project is organized into packages based on functionality:
```
.
├── input/                  # Input matrices
│   ├── LabStrassenInput.txt
├── output/                 # Output results and performance plots
│   ├── matrix_comparison.txt
│   ├── matrix_performance.png
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/jhu/algos/
│   │   │       ├── Main.java                # Entry point
│   │   │       ├── algorithms/
│   │   │       │   ├── MatrixMultiplier.java    # Abstract class for multiplication
│   │   │       │   ├── NaiveMultiplication.java # Naive O(n³) multiplication
│   │   │       │   ├── StrassenMultiplication.java # Strassen O(n^2.8074) algorithm
│   │   │       ├── compare/
│   │   │       │   ├── ComparisonDriver.java      # Runs and logs comparisons
│   │   │       │   ├── ComparisonTableGenerator.java # Generates formatted output tables
│   │   │       │   ├── CurveFitter.java           # Fits performance trends
│   │   │       │   ├── PerformanceRecord.java     # Data structure for storing performance results
│   │   │       ├── io/
│   │   │       │   ├── MatrixFileHandler.java     # Reads matrix input files
│   │   │       ├── models/
│   │   │       │   ├── Matrix.java                # Matrix representation
│   │   │       ├── operations/
│   │   │       │   ├── MatrixOperations.java      # Add, subtract, split, merge matrices
│   │   │       ├── utils/
│   │   │       │   ├── DebugConfig.java           # Handles debug mode
│   │   │       │   ├── MatrixUtils.java           # Helper functions for matrices
│   │   │       │   ├── MatrixValidator.java       # Validates matrices
│   │   │       │   ├── PerformanceMetrics.java    # Handles execution timing
│   │   │       ├── visualization/
│   │   │       │   ├── GraphGenerator.java        # Generates performance plots
│   ├── test/                                        # Unit tests
├── pom.xml                 # Maven build file
└── README.md               # Project documentation (this file)
```

---

## **Key Components**
### **1. Main Execution (`Main.java`)**
- Reads input matrices.
- Calls `ComparisonDriver.java` to run the multiplication algorithms.
- Optionally enables debug mode (`--debug`).
- Handles `--plot` flag to generate performance graphs.

### **2. Comparison (`ComparisonDriver.java`)**
- Runs both naive and Strassen multiplication.
- Measures execution time and multiplication count.
- Validates correctness of Strassen’s results against naive multiplication.
- Writes results to `matrix_comparison.txt`.

### **3. Matrix Representation (`Matrix.java`)**
- Stores matrix data as a `2D int array`.
- Provides helper functions for accessing and modifying matrix elements.

### **4. Matrix Operations (`MatrixOperations.java`)**
Implements core matrix functions needed for Strassen’s algorithm:
- **Addition:** Used in Strassen’s intermediate calculations.
- **Subtraction:** Used for computing submatrices.
- **Splitting:** Divides a matrix into four submatrices.
- **Merging:** Combines four submatrices into a single matrix.

### **5. Multiplication Algorithms**
#### **Naive Multiplication (`NaiveMultiplication.java`)**
Implements the standard **O(n³)** algorithm using three nested loops:
```text
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        for (int k = 0; k < n; k++) {
            result[i][j] += A[i][k] * B[k][j];
        }
    }
}
```
Each element in the result matrix is computed using row-by-column multiplication.

#### **Strassen Multiplication (`StrassenMultiplication.java`)**
Strassen’s method reduces multiplication operations by recursively dividing the matrices into smaller submatrices and using **seven** multiplication steps instead of eight.

**Step 1: Divide matrices into four submatrices**
Given matrix `A` and `B`, split them into:
```
A = | A11  A12 |    B = | B11  B12 |
    | A21  A22 |        | B21  B22 |
```
**Step 2: Compute seven intermediate matrices**
```
M1 = (A11 + A22) * (B11 + B22)
M2 = (A21 + A22) * B11
M3 = A11 * (B12 - B22)
M4 = A22 * (B21 - B11)
M5 = (A11 + A12) * B22
M6 = (A21 - A11) * (B11 + B12)
M7 = (A12 - A22) * (B21 + B22)
```
**Step 3: Compute result submatrices**
```
C11 = M1 + M4 - M5 + M7
C12 = M3 + M5
C21 = M2 + M4
C22 = M1 - M2 + M3 + M6
```
**Step 4: Merge `C11, C12, C21, C22` to form final matrix**

Strassen’s method replaces eight multiplications with seven at each recursion level, leading to an asymptotic complexity of **O(n^2.8074)**.

---

## **Compiling and Running**
### **With Maven**
```sh
mvn clean package
java -jar target/MatrixMultiplication-1.0-SNAPSHOT.jar LabStrassenInput.txt
```
Enable debug mode:
```sh
java -jar target/MatrixMultiplication-1.0-SNAPSHOT.jar LabStrassenInput.txt --debug
```
Generate performance graphs:
```sh
java -jar target/MatrixMultiplication-1.0-SNAPSHOT.jar LabStrassenInput.txt --plot
```

### **Without Maven**
#### **Compile all Java files**
```sh
javac -d target/classes $(find src/main/java -name "*.java")
```
#### **Package into a JAR**
```sh
jar cfm target/MatrixMultiplication-1.0-SNAPSHOT.jar src/main/resources/META-INF/MANIFEST.MF -C target/classes .
```
#### **Run the JAR**
```sh
java -jar target/MatrixMultiplication-1.0-SNAPSHOT.jar LabStrassenInput.txt
```

---

## **Project Setup & Execution Guide**

### **1. Environment Requirements**
This project was developed and tested in a **WSL2 Ubuntu 22.04 environment** on Windows. The following software versions were used:

- **Java Version:** OpenJDK 17
- **Maven Version:** Apache Maven 3.8.8
- **JUnit Version:** JUnit 5.8.2
- **JFreeChart Version:** 1.5.3
- **Build System:** Maven
- **IDE Used:** IntelliJ IDEA 2024.3 (compatible with Eclipse, VS Code, or CLI-based compilation)

**Important:** The project assumes a **Linux-compatible shell environment** for running scripts. If you are using **Windows natively**, you may need to adapt shell commands accordingly.

---

### **2. Installation and Build Instructions**

This project is structured as a **Maven project**, which handles dependency management and compilation.

#### **a) Installing Required Software**
Ensure you have the required software installed before proceeding:

**For Ubuntu (WSL2 or native Linux):**
```sh
sudo apt update && sudo apt install openjdk-17-jdk maven
```

**Verify installations:**
```sh
java -version
mvn -version
```
Expected output:
```
openjdk version "17.0.9" 2024-01-01
Apache Maven 3.8.8 (latest)
```

#### **b) Building the JAR File**
To compile the project and create an **executable JAR file**, navigate to the project root directory and run:
```sh
mvn clean package
```
This will generate:
- **Executable JAR:** `target/MatrixMultiplication-1.0-SNAPSHOT.jar`
- **Compiled Classes:** Inside `target/classes/`

---

### **3. Running the Program**

The program requires a **formatted input file** to run. The input file should be located in the **input/** directory.

#### **a) Running with a Default Input File**
Execute the program using an input file:
```sh
java -jar target/MatrixMultiplication-1.0-SNAPSHOT.jar input/LabStrassenInput.txt
```
This will:
- Read matrix pairs from `LabStrassenInput.txt`
- Compute results using **Naive Multiplication** and **Strassen’s Algorithm**
- Print performance metrics to the console
- Generate an **output comparison file** in `output/`

#### **b) Running with Debug Mode**
To enable verbose logging, add the `--debug` flag:
```sh
java -jar target/MatrixMultiplication-1.0-SNAPSHOT.jar input/LabStrassenInput.txt --debug
```

#### **c) Running with Performance Graph Generation**
To generate a **performance comparison plot**, use:
```sh
java -jar target/MatrixMultiplication-1.0-SNAPSHOT.jar input/scaling_test_cases.txt --plot
```
This produces:
- **Graph Output:** `output/matrix_performance.png`
- **Text-based Performance Table:** `output/matrix_comparison.txt`

---

### **4. Input File Format**
The program requires an input file structured as follows:

```
n
Matrix_A (row-major format)
Matrix_B (row-major format)

n
Matrix_A (row-major format)
Matrix_B (row-major format)
```
Where:
- `n` is the matrix size (**must be a power of 2**).
- Matrices are listed in **row-major order**.
- A blank line separates each matrix pair.

**Example Input File (`LabStrassenInput.txt`)**
```
4
3 2 1 4
-1 2 0 1
2 3 -1 -2
5 1 1 0

-1 2 -1 0
3 -1 0 2
-4 0 -3 1
0 -2 1 2
```

**Example Output (`LabStrassenOutput.txt`)**
```
Matrix Pair #1
Matrix A (size 4):
   3    2    1    4
  -1    2    0    1
   2    3   -1   -2
   5    1    1    0

Matrix B (size 4):
  -1    2   -1    0
   3   -1    0    2
  -4    0   -3    1
   0   -2    1    2

Naive Multiplication Result:
  0   10   -2   16
  2   -3   -5    1
 -15   12   -6   -4
 -13   -1  -11    6

Naive Time (ms): 3
Naive Multiplications: 64

Strassen Multiplication Result:
  0   10   -2   16
  2   -3   -5    1
 -15   12   -6   -4
 -13   -1  -11    6

Strassen Time (ms): 2
Strassen Multiplications: 49
```

---

### **5. Project Structure**
```
MatrixMultiplication/
│── input/                             # Input matrices
│   ├── LabStrassenInput.txt
│   ├── scaling_test_cases.txt
│── output/                            # Output results and performance plots
│   ├── matrix_comparison.txt
│   ├── matrix_performance.png
│── src/
│   ├── main/java/edu/jhu/algos/       # Source code
│   │   ├── Main.java                  # Entry point
│   │   ├── algorithms/                # Naive & Strassen implementations
│   │   ├── compare/                    # Performance tracking & output
│   │   ├── io/                         # File handling utilities
│   │   ├── models/                     # Matrix data structures
│   │   ├── utils/                      # Helper functions
│   │   ├── visualization/              # Graph generation
│── pom.xml                             # Maven build file
│── README.md                           # Project documentation
```

---

### **6. Testing**
This project includes **JUnit tests** for correctness verification.

#### **Running Tests**
```sh
mvn test
```
This will run all unit tests located in `src/test/` and print a summary.

---

### **7. Troubleshooting**
#### **a) Missing Dependencies**
If dependencies fail to resolve, run:
```sh
mvn dependency:resolve
mvn clean package
```

#### **b) Graph Generation Issues**
Ensure:
- Input matrices are correctly formatted.
- JFreeChart is installed (`mvn dependency:resolve` can help).

#### **c) Program Hangs or Exits Unexpectedly**
- Check `DebugConfig.log()` output for exceptions.
- Validate input matrices are square and a **power of 2**.

---

### **8. Contact & Affiliation**
- **Institution:** Johns Hopkins University
- **Course:** EN.605.620 - Foundations of Algorithms for Bioinformatics
- **Developer:** AJ Book
- **Instructor:** Dr. Rubey
- **For questions, contact:** abook3@jh.edu

---

