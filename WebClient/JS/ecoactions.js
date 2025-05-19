document.addEventListener('DOMContentLoaded', () => {
  const container = document.getElementById('ecoActionsContainer');

  fetch('http://127.0.0.1:8080/api/ecoActions') // Kontrollera att din backend har denna route
    .then(res => {
      if (!res.ok) throw new Error('Kunde inte hämta ecoActions');
      return res.json();
    })
    .then(actions => {
      if (actions.length === 0) {
        container.innerHTML = '<p>Inga miljövänliga handlingar ännu.</p>';
        return;
      }

      actions.forEach(action => {
        const card = document.createElement('div');
        card.classList.add('card');

        card.innerHTML = `
          <h2>${action.category}</h2>
          <p>${action.action}</p>
          <p class="dates">${action.username || 'Okänd'} – ${new Date(action.date_submitted).toLocaleDateString('sv-SE')}</p>
        `;

        container.appendChild(card);
      });
    })
    .catch(err => {
      console.error(err);
      container.innerHTML = '<p>Fel vid hämtning av data.</p>';
    });
});
