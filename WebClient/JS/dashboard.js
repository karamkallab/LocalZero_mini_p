document.addEventListener("DOMContentLoaded", () => {
  const initiativeList = document.getElementById("initiativeList");
  const dmToggle = document.querySelector('.dm-toggle');
  const dmPanel = document.querySelector('.dm-panel');
  const notifIcon = document.querySelector('.notification-icon');
  const notifDropdown = document.querySelector('.notification-dropdown');
  const messageForm = document.getElementById("messageForm");
  const messageInput = document.getElementById("message");
  const messageArea = document.getElementById("messageArea");

  const username = localStorage.getItem('userId') || "Anonymous";
  const colors = ['#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107', '#ff85af', '#FF9800', '#39bbb0'];

  let stompClient = null;

  // 🟢 WebSocket Setup
  function connectWebSocket() {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
  }

  function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
  }

  function onError(error) {
    console.error("WebSocket error:", error);
  }

  function sendMessage(event) {
    event.preventDefault();
    const content = messageInput.value.trim();
    if (content && stompClient) {
      const chatMessage = {
        sender: username,
        content: content,
        type: 'CHAT'
      };
      stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
      messageInput.value = '';
    }
  }

  function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    const messageElement = document.createElement('li');

    if (message.type === 'JOIN' || message.type === 'LEAVE') {
      messageElement.classList.add('event-message');
      message.content = message.sender + (message.type === 'JOIN' ? ' joined!' : ' left!');
    } else {
      messageElement.classList.add('chat-message');
      const avatar = document.createElement('i');
      avatar.textContent = message.sender[0];
      avatar.style.backgroundColor = getAvatarColor(message.sender);

      const name = document.createElement('span');
      name.textContent = message.sender;

      messageElement.appendChild(avatar);
      messageElement.appendChild(name);
    }

    const text = document.createElement('p');
    text.textContent = message.content;
    messageElement.appendChild(text);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  }

  function getAvatarColor(sender) {
    let hash = 0;
    for (let i = 0; i < sender.length; i++) {
      hash = 31 * hash + sender.charCodeAt(i);
    }
    return colors[Math.abs(hash % colors.length)];
  }

  // 🟢 UI Behaviors
  dmToggle.addEventListener('click', (e) => {
    e.stopPropagation();
    dmPanel.classList.toggle('hidden');
  });

  document.addEventListener('click', (e) => {
    if (!dmPanel.contains(e.target) && !dmToggle.contains(e.target)) {
      dmPanel.classList.add('hidden');
    }
  });

  notifIcon.addEventListener('click', () => {
    notifDropdown.classList.toggle('hidden');
  });

  document.addEventListener('click', (e) => {
    if (!notifIcon.contains(e.target) && !notifDropdown.contains(e.target)) {
      notifDropdown.classList.add('hidden');
    }
  });

  document.querySelector('.signout').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'login.html';
  });

  document.querySelectorAll('.nav button').forEach(button => {
    button.addEventListener('click', () => {
      const page = button.getAttribute('data-page');
      window.location.href = `${page}.html`;
    });
  });

  messageForm.addEventListener('submit', sendMessage);

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
