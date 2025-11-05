async function fetchBalances() {
    const a = await fetch("/wallets/A").then(r => r.json());
    const b = await fetch("/wallets/B").then(r => r.json());
    document.getElementById("balanceA").textContent = a.toFixed(2);
    document.getElementById("balanceB").textContent = b.toFixed(2);
}

async function fetchBlockchain() {
    const chain = await fetch("/blocks").then(r => r.json());
    document.getElementById("blockchainData").textContent =
        JSON.stringify(chain, null, 2);
}

// Refresh both every few seconds (live updates)
setInterval(() => {
    fetchBalances();
    fetchBlockchain();
}, 5000); // every 5 seconds

document.getElementById("txForm").addEventListener("submit", async e => {
    e.preventDefault();
    const sender = document.getElementById("sender").value;
    const recipient = document.getElementById("recipient").value;
    const amount = parseFloat(document.getElementById("amount").value);
    const status = document.getElementById("status");

    const tx = { from: sender, to: recipient, amount };
    status.textContent = "Submitting transaction...";
    status.className = "status";

    try {
        const res = await fetch("/transaction", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(tx)
        });

        const msg = await res.json();
        if (res.ok) {
            status.textContent = msg;
            status.classList.add("success");
            // Immediately refresh after success
            fetchBalances();
            fetchBlockchain();
        } else {
            status.textContent = msg;
            status.classList.add("error");
        }
    } catch (err) {
        status.textContent = "Network error: " + err.message;
        status.classList.add("error");
    }
});

// Initial load
fetchBalances();
fetchBlockchain();

