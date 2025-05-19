document.addEventListener("DOMContentLoaded", () => {
  const ecoActionsContainer = document.getElementById("ecoActionsContainer");

  if (ecoActionsContainer) {
    fetch("http://localhost:8080/api/ecoactions")
      .then(res => res.json())
      .then(data => {
        ecoActionsContainer.innerHTML = '';
        data.forEach(renderEcoactionCard);
      })
      .catch(err => console.error("Kunde inte hämta eco actions:", err));
  }
});

function renderEcoactionCard(action) {
  const container = document.getElementById("ecoActionsContainer");

  const card = document.createElement("div");
  card.className = "card";

  const title = document.createElement("h2");
  title.textContent = `${action.action} – ${action.location}`;

  const category = document.createElement("p");
  category.textContent = `Kategori: ${action.category}`;

  const description = document.createElement("p");
  description.textContent = action.description;

  const postedBy = document.createElement("p");
  postedBy.className = "posted-by";
  postedBy.textContent = `Registrerad av: ${action.user || "LocalZero"}`;

  const date = document.createElement("p");
  date.className = "date";
  date.textContent = `Datum: ${new Date(action.date).toLocaleDateString()}`;

  card.append(title, category, description, postedBy, date);
  container.prepend(card); 
}
