#!/bin/bash

# Launch script for the 100 Prisoners Problem GUI Visualization
# Usage: ./launch-gui.sh

echo "🎮 Launching 100 Prisoners Problem GUI Visualization..."
echo "📋 Building project..."

# Build the project
./mvnw clean compile exec:java@gui

echo "✅ GUI application finished."