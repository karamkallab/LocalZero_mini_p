document.addEventListener("DOMContentLoaded", () => {
    const initiativeList = document.getElementById("initiativeList");
    if (!initiativeList) {
      console.warn("Element #initiativeList not found.");
      return;
    }
  
    fetch("http://localhost:8080/api/FetchInitiatives")
      .then(res => {
        if (!res.ok) throw new Error(`HTTP error! Status: ${res.status}`);
        return res.json();
      })
      .then(data => {
        initiativeList.innerHTML = '';
  
        data.forEach(initiative => {
          const link = document.createElement("a");
          link.href = `initiative_view.html?id=${initiative.id || 1}`;
          link.className = "initiative-box-link";
  
          const card = document.createElement("div");
          card.className = "card";
  
          const h2 = document.createElement("h2");
          h2.textContent = `${initiative.title} – At ${initiative.location}`;

          const p = document.createElement("p")
          p.textContent = `Category: ${initiative.category}`;
          
          const p1 = document.createElement("p");
          p1.textContent = initiative.description;
  
          const dates = document.createElement("p");
          dates.className = "dates";
          dates.textContent = "Posted by LocalZero";
  
          card.appendChild(h2);
          card.appendChild(p);
          card.appendChild(p1);
          card.appendChild(dates);
          link.appendChild(card);
          initiativeList.appendChild(link);
        });
      })
      .catch(err => {
        console.error("Error fetching initiatives:", err);
      });
  });

      

  document.querySelector('.signout').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'login.html';
  });

  document.querySelectorAll('.nav button').forEach(button => {
    button.addEventListener('click', () => {
      const page = button.getAttribute('data-page');
      console.log(page)
      window.location.href = `${page}.html`;
    });
  });

  const notifIcon = document.querySelector('.notification-icon');
const notifDropdown = document.querySelector('.notification-dropdown');

notifIcon.addEventListener('click', () => {
notifDropdown.classList.toggle('hidden');
});

document.addEventListener('click', (event) => {
if (!notifIcon.contains(event.target) && !notifDropdown.contains(event.target)) {
  notifDropdown.classList.add('hidden');
}
});

const dmToggle = document.querySelector('.dm-toggle');
      const dmPanel = document.querySelector('.dm-panel');

      dmToggle.addEventListener('click', (event) => {
      event.stopPropagation(); 
      dmPanel.classList.toggle('hidden');
    });

    document.addEventListener('click', (event) => {
      if (!dmToggle.contains(event.target) && !dmPanel.contains(event.target)) {
        dmPanel.classList.add('hidden');
      }
    });
    
    function openChat(user) {
      chatWith.innerText = `Chattar med: ${user}`;
      chatBox.innerHTML = `<p><em>Startade chatt med ${user}</em></p>`;
    }
    
    // --- WEBSOCKET CHAT ---
    const socket = new WebSocket("ws://localhost:8080/LocalZero_mini_p/chat");

    socket.onmessage = function(event) {
      const chatBox = document.getElementById("chatBox");
      chatBox.innerHTML += `<div>${event.data}</div>`;
      chatBox.scrollTop = chatBox.scrollHeight;
    };

    function sendMessage() {
      const input = document.getElementById("messageInput");
      const message = input.value;
      if (message && socket.readyState === WebSocket.OPEN) {
        const recipient = document.getElementById("chatWith").innerText.replace("Chattar med: ", "");
        socket.send(`${recipient}: ${message}`);
        input.value = "";
      }
    }

    function openChat(user) {
      const chatWith = document.getElementById("chatWith");
      const chatBox = document.getElementById("chatBox");
      chatWith.innerText = `Chattar med: ${user}`;
      chatBox.innerHTML = `<p><em>Startade chatt med ${user}</em></p>`;
    }