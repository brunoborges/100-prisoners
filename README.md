# 🔐 100 Prisoners Problem Simulation

[![Java CI with Maven](https://github.com/brunoborges/100-prisoners/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/brunoborges/100-prisoners/actions)
[![CodeQL](https://github.com/brunoborges/100-prisoners/workflows/CodeQL/badge.svg)](https://github.com/brunoborges/100-prisoners/actions)

A Java implementation demonstrating the **optimal strategy** for the famous 100 Prisoners Problem, achieving a remarkable **~31% success rate** instead of the seemingly impossible odds.

## 🧩 The Problem

The 100 Prisoners Problem is a fascinating mathematical puzzle in probability theory and combinatorics:

> **Scenario**: 100 numbered prisoners are imprisoned and must find their own numbers in 100 numbered boxes to gain freedom.
> 
> **Rules**:
> - Each prisoner may open only **50 boxes** (half of all boxes)
> - Prisoners **cannot communicate** with each other during the process
> - **ALL prisoners must find their numbers** for anyone to be freed
> - If even one prisoner fails, they all remain imprisoned

At first glance, this seems hopeless - the naive strategy gives virtually 0% chance of success. However, there exists an optimal strategy that dramatically improves the odds!

### 🔗 Learn More
- 📖 [Wikipedia Article](https://en.wikipedia.org/wiki/100_prisoners_problem)
- 🎥 [Veritasium Video Explanation](https://www.youtube.com/watch?v=iSNsgj1OCLA)
- 📊 [Mathematical Analysis](https://en.wikipedia.org/wiki/100_prisoners_problem#Strategy)

## 🎯 The Optimal Strategy

The key insight is to follow a **"chain strategy"**:

1. **Each prisoner starts** by opening the box numbered with their own prisoner number
2. **Follow the chain**: If the box contains a different number, open the box with that number
3. **Continue following** the chain until either:
   - ✅ They find their own number (success!)
   - ❌ They've opened 50 boxes without success (failure)

### Why Does This Work?

This strategy transforms the problem into finding the **longest cycle in a random permutation**. The prisoners succeed if and only if there's no cycle longer than 50 in the permutation of numbers in boxes. This happens with probability approximately **ln(2) ≈ 0.693**, giving a success rate of about **31%** - much better than the intuitive approach!

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Maven** (or use the included `mvnw` wrapper)

### Building the Project

```bash
# Clone the repository
git clone https://github.com/brunoborges/100-prisoners.git
cd 100-prisoners

# Build using Maven wrapper
./mvnw clean package

# Or if you have Maven installed
mvn clean package
```

### Running the Simulation

```bash
# Run with default settings (100 prisoners, 1000 attempts)
java -jar target/100-prisoners-1.0.jar

# Run with custom parameters
java -jar target/100-prisoners-1.0.jar -p 50 -a 500

# Show help
java -jar target/100-prisoners-1.0.jar -h
```

## 📊 Example Output

```
2025-09-22 11:58:18 INFO prisoners.App call Running exercise with 100 prisoners and 1000 attempts. Please wait...
Prison exercise attempts 100% ━━━━━━━━━━━━━━━━━━ 1000/1000 (0:00:00 / 0:00:00) 
2025-09-22 11:58:18 INFO prisoners.App call Number of cycles tested: 1000
2025-09-22 11:58:18 INFO prisoners.App call How often did prisoners escape? 33.2%
```

## 🎮 Web Visualization (Coming Soon)

The project includes a web-based visualization with real-time WebSocket updates to watch the prisoners navigate through the boxes. This feature demonstrates the chain-following strategy in action.

## 🏗️ Project Structure

```
src/
├── main/java/prisoners/
│   ├── App.java                              # Main CLI application
│   ├── FreedomExperiment.java                # Core simulation logic
│   ├── Prisoner.java                         # Prisoner representation
│   ├── Box.java                             # Box with hidden numbers
│   ├── StepListener.java                    # Observer interface
│   ├── ExperimentSession.java               # WebSocket session management
│   └── FreedomExperimentWebSocketServer.java # WebSocket server
├── main/resources/
│   ├── index.html                           # Web visualization
│   ├── script.js                            # JavaScript for animation
│   └── styles.css                           # Styling for web interface
└── test/java/prisoners/
    ├── TestApp.java                         # Application tests
    ├── TextFreedomExperiment.java           # Experiment logic tests
    └── TestBox.java                         # Box functionality tests
```

## ⚙️ Command Line Options

| Option | Description | Default | Example |
|--------|-------------|---------|---------|
| `-p` | Number of prisoners | 100 | `-p 50` |
| `-a` | Number of simulation attempts | 1000 | `-a 2000` |
| `-h` | Show help message | - | `-h` |
| `-V` | Show version | - | `-V` |

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run tests with coverage
./mvnw test jacoco:report
```

The test suite includes:
- ✅ Input validation (even numbers, minimum prisoners)
- ✅ Strategy correctness verification
- ✅ Edge case handling
- ✅ Box functionality testing

## 🎭 Mathematical Insights

### Success Probability Formula

For `n` prisoners and `n/2` allowed box openings, the success probability is:

```
P(success) = 1 - P(cycle > n/2)
```

Where the probability of having a cycle longer than `n/2` in a random permutation is approximately `ln(2) ≈ 0.693` for large `n`.

### Experimental Validation

Running the simulation consistently produces results around **31-33%**, confirming the theoretical prediction.

## 🔧 Dependencies

- **[picocli](https://picocli.info/)** - Command-line interface framework
- **[progressbar](https://github.com/ctongfei/progressbar)** - Console progress visualization
- **[Spark Java](http://sparkjava.com/)** - Lightweight web framework
- **[Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)** - WebSocket server implementation
- **[Gson](https://github.com/google/gson)** - JSON serialization
- **[JUnit Jupiter](https://junit.org/junit5/)** - Testing framework

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. Areas for improvement:

- 🌐 Enhanced web visualization
- 📈 Additional statistical analysis
- 🔧 Performance optimizations
- 📚 More comprehensive documentation
- 🧮 Support for different strategies comparison

## 📜 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📚 References

1. Curtin, Eugene (2007). "The 100 Prisoners Problem". Mathematics Magazine
2. [OEIS Sequence A000670](https://oeis.org/A000670) - Fubini numbers related to permutation cycles
3. [Mathematical analysis on Wikipedia](https://en.wikipedia.org/wiki/100_prisoners_problem)

---

⭐ **Found this interesting?** Give it a star and share with fellow math enthusiasts!
