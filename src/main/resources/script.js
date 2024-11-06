previousPrisonerNumber = 0;

document.addEventListener("DOMContentLoaded", () => {
    const boxContainer = document.getElementById("box-container");
    const prisoner = document.getElementById("prisoner");
    const freePrisonersContainer = document.createElement("div");
    freePrisonersContainer.id = "free-prisoners-container";
    document.body.appendChild(freePrisonersContainer);

    socket = new WebSocket("ws://localhost:8081");

    // Create 100 boxes
    for (let i = 1; i <= 100; i++) {
        const box = document.createElement("div");
        box.className = "box";
        box.dataset.number = i;

        const identifier = document.createElement("div");
        identifier.className = "identifier";
        identifier.textContent = `Box ${i}`;

        const hiddenNumber = document.createElement("div");
        hiddenNumber.className = "hidden-number";
        hiddenNumber.textContent = i; // Assuming hidden number is the same as the box number

        box.appendChild(identifier);
        box.appendChild(hiddenNumber);
        boxContainer.appendChild(box);
        box.classList.add("visible");
    }

    socket.onopen = () => {
        console.log("WebSocket connection established");
    };

    socket.onmessage = (event) => {
        const data = JSON.parse(event.data);
        const box = document.querySelector(`.box[data-number='${data.boxNumber}']`);
        const hiddenNumber = box.querySelector(".hidden-number");

        // Display the hidden number
        hiddenNumber.textContent = data.hiddenNumber;
        hiddenNumber.style.display = "block"; // Ensure the hidden number is visible

        // Display the prisoner's number inside its div
        prisoner.textContent = data.prisonerNumber;

        // if this prisoner's number is different from the previous, let's clear all boxes
        if (data.prisonerNumber !== previousPrisonerNumber) {
            document.querySelectorAll(".box").forEach(box => {
                box.style.backgroundColor = "";
            });
            previousPrisonerNumber = data.prisonerNumber;
        }

        // Move prisoner to the exact top-left corner of the box
        const boxRect = box.getBoundingClientRect();
        const containerRect = boxContainer.getBoundingClientRect();

        prisoner.style.position = "absolute";
        prisoner.style.transition = "top 1s, left 1s";
        prisoner.style.top = `${boxRect.top - containerRect.top}px`;
        prisoner.style.left = `${boxRect.left - containerRect.left}px`;

        // Check if the hidden number matches the prisoner's number
        if (data.hiddenNumber === data.prisonerNumber) {
            prisoner.style.backgroundColor = "blue";
            setTimeout(() => {
                const freeRect = freePrisonersContainer.getBoundingClientRect();
                prisoner.style.top = `${freeRect.top - containerRect.top}px`;
                prisoner.style.left = `${freeRect.left - containerRect.left}px`;
            }, 500); // Delay to show the prisoner moving to the free area
        } else {
            box.style.backgroundColor = "grey";
            // Wait for the next event to move the prisoner to the next box
        }
    };

    socket.onerror = (error) => {
        console.error("WebSocket error:", error);
    };

    socket.onclose = () => {
        console.log("WebSocket connection closed");
    };

    // Start the experiment -- DO NOT DELETE THIS CODE
    startButton.addEventListener('click', () => {
        socket.send(JSON.stringify({ action: 'start' }));
    });

    // Stop the experiment -- DO NOT DELETE THIS CODE
    stopButton.addEventListener('click', () => {
        socket.send(JSON.stringify({ action: 'stop' }));
        socket.close();
    });

});
