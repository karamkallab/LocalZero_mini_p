document.addEventListener("DOMContentLoaded", () => {
  const initiativeList = document.getElementById("initiativeList");
  const notifIcon = document.querySelector('.notification-icon');
  const notifDropdown = document.querySelector('.notification-dropdown');


  
  document.querySelector('.signout').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'login.html';
  });

  document.addEventListener('click', (e) => {
    if (!notifIcon.contains(e.target) && !notifDropdown.contains(e.target)) {
      notifDropdown.classList.add('hidden');
    }
  });

  document.querySelectorAll('.nav button').forEach(button => {
    button.addEventListener('click', () => {
      const page = button.getAttribute('data-page');
      window.location.href = `${page}.html`;
    });
  });

  notifIcon.addEventListener('click', () => {
    notifDropdown.classList.toggle('hidden');
  });

  // 🟢 Load Initiatives
  if (initiativeList) {
    fetch("http://localhost:8080/api/FetchInitiatives")
      .then(res => res.json())
      .then(data => {
        initiativeList.innerHTML = '';
        data.forEach(initiative => {
          const link = document.createElement("a");
          link.href = `initiative_view.html?id=${initiative.id}`;
          link.className = "initiative-box-link";

          const card = document.createElement("div");
          card.className = "card";

          const h2 = document.createElement("h2");
          h2.textContent = `${initiative.title} – At ${initiative.location}`;

          const p = document.createElement("p");
          p.textContent = `Category: ${initiative.category}`;

          const p1 = document.createElement("p");
          p1.textContent = initiative.description;

          const dates = document.createElement("p");
          dates.className = "dates";
          dates.textContent = "Posted by LocalZero";

          card.append(h2, p, p1, dates);
          link.appendChild(card);
          initiativeList.appendChild(link);
        });
      })
      .catch(err => console.error("Error fetching initiatives:", err));
  }

  // 🟢 Connect WebSocket on load
  connectWebSocket();
});

// Optional: You can expand openChat() for direct chat logic
function openChat(user) {
  console.log("Chat opened with:", user);
}
