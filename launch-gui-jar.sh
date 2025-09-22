#!/bin/bash

# Alternative launcher for GUI using the packaged JAR - Modern Java Edition
# Usage: ./launch-gui-jar.sh

echo "🎮 Launching 100 Prisoners Problem GUI from JAR (Modern Java)..."

# Check if JAR exists
if [ ! -f "target/100-prisoners-1.0.jar" ]; then
    echo "📦 JAR not found, building project first..."
    ./mvnw clean package -DskipTests -q
fi

echo "🚀 Starting GUI application..."
java -cp target/100-prisoners-1.0.jar prisoners.gui.PrisonersVisualizationApp

echo "✅ GUI application finished."