#!/bin/bash

# Launch script for the 100 Prisoners Problem GUI Visualization - Modern Java Edition
# Usage: ./launch-gui.sh

echo "🎮 Launching 100 Prisoners Problem GUI Visualization (Modern Java)..."
echo "📋 Building project..."

# Build the project
./mvnw clean compile exec:java@gui

echo "✅ GUI application finished."