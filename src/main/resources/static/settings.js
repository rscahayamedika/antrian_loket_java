const form = document.getElementById("print-settings-form");
const nameInput = document.getElementById("institution-name");
const addressInput = document.getElementById("institution-address");
const feedbackElement = document.getElementById("settings-feedback");

async function loadSettings() {
    try {
        const response = await fetch("/api/settings/ticket-print");
        if (!response.ok) {
            throw new Error("Gagal memuat pengaturan");
        }
        const data = await response.json();
        nameInput.value = data.institutionName ?? "";
        addressInput.value = data.address ?? "";
    } catch (error) {
        showFeedback(error.message, true);
    }
}

form.addEventListener("submit", async event => {
    event.preventDefault();
    const payload = {
        institutionName: nameInput.value.trim(),
        address: addressInput.value.trim()
    };
    try {
        const response = await fetch("/api/settings/ticket-print", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.error || "Gagal menyimpan pengaturan");
        }
        showFeedback("Pengaturan berhasil disimpan.");
    } catch (error) {
        showFeedback(error.message, true);
    }
});

function showFeedback(message, isError = false) {
    feedbackElement.textContent = message;
    feedbackElement.className = `feedback ${isError ? "error" : ""}`;
    feedbackElement.classList.toggle("hidden", !message);
}

loadSettings();
