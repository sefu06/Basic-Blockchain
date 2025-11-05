//connect to backend
async function fetchBalances() {
    const a = await fetch("/balance/A").then(r => r.json());
    const b = await fetch("/balance/B").then(r => r.json());
    document.getElementById("balanceA").textContent = a.balance.toFixed(2);
    document.getElementById("balanceB").textContent = b.balance.toFixed(2);
}

async function fetchBlockchain() {
    const chain = await fetch("/blockchain").then(r => r.json());
    document.getElementById("blockchainData").textContent =
        JSON.stringify(chain, null, 2);
}

document.getElementById("txForm").addEventListener("submit", async e => {
    e.preventDefault();
    const sender = document.getElementById("sender").value;
    const recipient = document.getElementById("recipient").value;
    const amount = parseFloat(document.getElementById("amount").value);

    const tx = { sender, recipient, amount };
    const res = await fetch("/transaction", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(tx)
    });

    if (res.ok) {
        alert("Transaction sent!");
        fetchBalances();
        fetchBlockchain();
    } else {
        alert("Error sending transaction.");
    }
});

// Initial load
fetchBalances();
fetchBlockchain();