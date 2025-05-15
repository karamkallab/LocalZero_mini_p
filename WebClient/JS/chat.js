const messageForm = document.getElementById("messageForm");
const messageInput = document.getElementById("message");
const messageArea = document.getElementById("messageArea");
const dmToggle = document.querySelector('.dm-toggle');
const dmPanel = document.querySelector('.dm-panel');
const contactList = document.getElementById("contactList");

const username = localStorage.getItem('name') || "Anonymous";
const colors = ['#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107', '#ff85af', '#FF9800', '#39bbb0'];

let recipient = null;
let stompClient = null;
let currentSubscription = null;

function connectWebSocket() {
  console.log(username);
  const encodedUsername = encodeURIComponent(username);
  const socket = new SockJS(`http://localhost:8080/ws?username=${encodedUsername}`);
  stompClient = Stomp.over(socket);
  stompClient.connect({}, onConnected, onError);
}

function onConnected() {
  stompClient.subscribe(`/user/queue/messages`, onMessageReceived);
  stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
  console.log("Connected and subscribed to /user/queue/messages");
}

function subscribe(username) {
  if (currentSubscription !== null) {
    currentSubscription.unsubscribe();
    currentSubscription = null;
  }

  currentSubscription = stompClient.subscribe(`/user/${username}/queue/messages`, onMessageReceived);
}

function onError(error) {
  console.error("WebSocket error:", error);
}

function sendMessage(event) {
  event.preventDefault();
  const content = messageInput.value.trim();
  if (content && stompClient && recipient) {
    const chatMessage = {
      sender: username,
      recipient: recipient,
      content: content,
      type: 'CHAT'
    };
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
    //displayMessage(chatMessage); // You must define this method
    messageInput.value = '';
  }

}

function displayMessage(message) {
  const messageArea = document.getElementById('messageArea');
  const messageElement = document.createElement('li');
  const usernameElement = document.createElement('strong');

  messageElement.classList.add('chat-message');
  usernameElement.textContent = message.sender + ': ';
  messageElement.appendChild(usernameElement);
  const textNode = document.createTextNode(message.content);
  messageElement.appendChild(textNode);

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}

function onMessageReceived(payload) {
  console.log("Payload: " + payload);
  const message = JSON.parse(payload.body);

  if (message.type === 'JOIN' || message.type === 'LEAVE') {
    const messageElement = document.createElement('li');
    messageElement.classList.add('event-message');
    messageElement.textContent = recipient + (message.type === 'JOIN' ? ' joined!' : ' left!');
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  } else if (message.recipient === username || message.sender === username) {
    displayMessage(message);
  }
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
    // Load Contact
    if (contactList) {
      fetch("http://localhost:8080/api/FetchAllName")
        .then(res => res.json())
        .then(data => {
          contactList.innerHTML = '';
    
          data.forEach(user => {
            if (user === localStorage.getItem('name')) return;
    
            const li = document.createElement('li');
            li.textContent = user;
            li.onclick = () => openChat(user);
            contactList.appendChild(li);
          });
        })
        .catch(err => console.error("Error fetching names:", err));
    }
});

document.addEventListener('click', (e) => {
  if (!dmPanel.contains(e.target) && !dmToggle.contains(e.target)) {
    dmPanel.classList.add('hidden');
  }
});
messageForm.addEventListener('submit', sendMessage);

function openChat(user) {
  recipient = user.trim();
  subscribe(recipient);
  messageArea.innerHTML = "";
  console.log("Chat opened with:", user);

  const usersToSend = username + "," + recipient;
  console.log(usersToSend);

  fetch('http://127.0.0.1:8080/api/LoadMessageHistory', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: usersToSend
    })
    .then(res => res.json())
    .then(data => {
      data.forEach(message => {
        displayMessage(message);
      });
    })
    .catch(error => {
      console.error('Error fetching messages:', error);
      alert('Error fetching messages');
    });
}