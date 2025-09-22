# 🔐 100 Prisoners Problem Simulation - Modern Java Edition

[![Java CI with Maven](https://github.com/brunoborges/100-prisoners/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/brunoborges/100-prisoners/actions)
[![CodeQL](https://github.com/brunoborges/100-prisoners/workflows/CodeQL/badge.svg)](https://github.com/brunoborges/100-prisoners/actions)
[![Java 21+](https://img.shields.io/badge/Java-21%2B-orange)](https://openjdk.java.net/projects/jdk/21/)
[![Stable Features Only](https://img.shields.io/badge/Features-Stable%20Only-green)](https://openjdk.java.net/projects/jdk/21/)

A Java implementation demonstrating the **optimal strategy** for the famous 100 Prisoners Problem, achieving a remarkable **~31% success rate** instead of the seemingly impossible odds.

**🚀 Now powered by Modern Java** with stable language features including:
- 📝 **Text Blocks** for cleaner multi-line strings
- 🎯 **Pattern Matching** with instanceof and switch expressions  
- 🔧 **Records** for immutable data structures
- 🧵 **Virtual Threads** for improved performance (Java 21+)
- 💎 **Enhanced Switch Expressions** and modern syntax
- 📦 **Improved Collections** and Stream API enhancements

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

### 🚀 **Modern Java Stable Features**

This implementation showcases stable modern Java features:

```java
// Text Blocks for better formatting
String results = String.format("""
    🎯 SIMULATION RESULTS:
    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    Prisoners:     %d
    Success rate:  %.2f%%
    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    """, prisoners, successRate());

// Enhanced Switch Expressions (Java 14+, stable)
return switch (validateInputs()) {
    case null -> runExperiment();
    case String error -> {
        logger.severe(error);
        yield 1;
    }
};

// Records with computed properties (Java 14+, stable)  
public record ExperimentStats(int total, int successes, boolean allEscaped) {
    public double successRate() {
        return total > 0 ? (successes * 100.0) / total : 0.0;
    }
}

// Pattern Matching with instanceof (Java 16+, stable)
if (obj instanceof Box other) {
    return this.label == other.label && this.hiddenNumber == other.hiddenNumber;
}

// Virtual Threads for improved performance (Java 21+, stable)
executorService = Executors.newVirtualThreadPerTaskExecutor();
```

## 🎯 The Optimal Strategy

The key insight is to follow a **"chain strategy"**:

1. **Each prisoner starts** by opening the box numbered with their own prisoner number
2. **Follow the chain**: If the box contains a different number, open the box with that number
3. **Continue following** the chain until either:
   - ✅ They find their own number (success!)
   - ❌ They've opened 50 boxes without success (failure)

### Why Does This Work?

This strategy transforms the problem into finding the **longest cycle in a random permutation**. The prisoners succeed if and only if there's no cycle longer than 50 in the permutation of numbers in boxes. This happens with probability approximately **ln(2) ≈ 0.693**, giving a success rate of about **31%** - much better than the intuitive approach!

### 🔗 Learn More
- 📖 [Wikipedia Article](https://en.wikipedia.org/wiki/100_prisoners_problem)
- 🎥 [Veritasium Video Explanation](https://www.youtube.com/watch?v=iSNsgj1OCLA)
- 📊 [Mathematical Analysis](https://en.wikipedia.org/wiki/100_prisoners_problem#Strategy)
- ☕ [Modern Java Features](https://openjdk.java.net/projects/jdk/21/)

## 🚀 Quick Start

### Prerequisites
- **Java 21** or higher
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

### 🎮 GUI Visualization 🌟

Experience the experiment visually with the **stunning modern desktop application**:

```bash
# Launch the modern GUI application
./launch-gui.sh

# Or run directly with Maven
./mvnw clean compile exec:java@gui
```

**🎨 Modern UI Features:**
- **🎨 Flat, modern design** with card-based layout and smooth animations
- **🎯 Real-time visualization** of prisoners following the chain strategy
- **🎛️ Interactive controls** with beautifully styled buttons and inputs
- **📊 Animated statistics panels** showing success rates and progress
- **🔧 Configurable parameters** with modern spinners and controls
- **🎨 Color-coded visual feedback** for different prisoner states
- **📈 Live progress tracking** with modern progress bars
- **✨ Hover effects and animations** for better user engagement
- **🖼️ Card-based layout** with subtle shadows and rounded corners
- **🎯 Enhanced typography** using modern system fonts

**🎨 Visual States:**
- **🔵 Blue highlight** = Currently opened box with pulsing animation
- **🔷 Light blue** = Part of the current prisoner's path
- **🟢 Green with checkmark** = Prisoner successfully found their number
- **⚡ Animated transitions** = Smooth state changes and visual feedback

**🎛️ Modern Controls:**
- **Modern styled buttons** with hover effects and shadows
- **Animated statistics cards** that pulse when values update  
- **Responsive grid layout** that adapts to different prisoner counts
- **Smooth animations** with configurable speed (50ms to 2000ms)
- **Real-time progress indicators** with color-coded success rates

## 📊 Example Output

```
2025-09-22 11:58:18 INFO prisoners.App call Running exercise with 100 prisoners and 1000 attempts. Please wait...
Prison exercise attempts 100% ━━━━━━━━━━━━━━━━━━ 1000/1000 (0:00:00 / 0:00:00) 
2025-09-22 11:58:18 INFO prisoners.App call Number of cycles tested: 1000
2025-09-22 11:58:18 INFO prisoners.App call How often did prisoners escape? 33.2%
```

## 🎮 Visualization Options

The project includes two visualization options:

### 🖥️ **Modern Desktop GUI Application (Java Swing)**
A **stunning, modern desktop application** with flat design and smooth animations:
- 🎨 **Modern flat design** with card-based layout and subtle shadows
- ⚡ **Smooth animations** and hover effects throughout the interface
- 🎯 **Real-time step-by-step visualization** of the chain strategy
- 📊 **Animated statistics panels** with live updates and visual feedback
- 🎛️ **Modern controls** with beautifully styled buttons and inputs
- 🖼️ **Responsive layout** that adapts to different screen sizes
- ✨ **Enhanced user experience** with intuitive interactions

### 🌐 **Web Interface (WebSocket)**
A web-based visualization with real-time updates:
- Browser-based interface with HTML5/JavaScript
- WebSocket server for live experiment streaming
- Suitable for remote viewing and demonstrations

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
│   ├── FreedomExperimentWebSocketServer.java # WebSocket server
│   └── gui/
│       ├── PrisonersVisualizationApp.java   # Modern GUI application with flat design
│       ├── ModernBoxPanel.java              # Enhanced box visualization with animations
│       ├── ModernButton.java                # Styled button component with hover effects
│       ├── ModernProgressBar.java           # Animated progress bar with gradient
│       └── AnimatedStatsPanel.java          # Statistics panel with pulse animations
├── main/resources/
│   ├── index.html                           # Web visualization
│   ├── script.js                            # JavaScript for animation
│   └── styles.css                           # Styling for web interface
└── test/java/prisoners/
    ├── TestApp.java                         # Application tests
    ├── TextFreedomExperiment.java           # Experiment logic tests
    ├── TestBox.java                         # Box functionality tests
    └── gui/
        └── TestBoxPanel.java                # Enhanced GUI component tests
```

## ⚙️ Command Line Options

| Option | Description | Default | Example |
|--------|-------------|---------|---------|
| `-p` | Number of prisoners | 100 | `-p 50` |
| `-a` | Number of simulation attempts | 1000 | `-a 2000` |
| `-h` | Show help message | - | `-h` |
| `-V` | Show version | - | `-V` |

### 🎮 GUI Application Options

The modern desktop GUI application provides comprehensive interactive controls:
- **🎛️ Prisoners spinner**: Adjust number of prisoners (4-1000, must be even)
- **⚡ Animation speed**: Control visualization speed (50-2000ms) 
- **🎮 Control buttons**: Modern styled Start/Stop/Reset with visual feedback
- **📊 Live statistics**: Animated success rate tracking across multiple runs
- **🎨 Visual feedback**: Color-coded progress bars and status indicators
- **✨ Smooth animations**: Hover effects, transitions, and visual state changes
- **🖼️ Card-based design**: Modern flat interface with subtle shadows and rounded corners

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

- 🌐 Enhanced web visualization with modern frameworks (React, Vue.js)
- 🎮 Additional GUI features (different strategies, 3D visualization, data export)
- 📈 Advanced statistical analysis and machine learning insights
- 🔧 Performance optimizations for massive prisoner counts (10K+)
- 📚 Interactive tutorials and educational content
- 🧮 Support for comparing different mathematical strategies
- 🎨 Even more stunning visual effects and animations
- 📊 Real-time data export capabilities (CSV, JSON, database integration)
- 🎯 Accessibility improvements (screen readers, keyboard navigation)
- 🌍 Internationalization and localization support

## 📜 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📚 References

1. Curtin, Eugene (2007). "The 100 Prisoners Problem". Mathematics Magazine
2. [OEIS Sequence A000670](https://oeis.org/A000670) - Fubini numbers related to permutation cycles
3. [Mathematical analysis on Wikipedia](https://en.wikipedia.org/wiki/100_prisoners_problem)

---

⭐ **Found this interesting?** Give it a star and share with fellow math enthusiasts!
