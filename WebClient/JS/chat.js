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
let notisSubscription = null;

function connectWebSocket() {
  console.log(username);
  const encodedUsername = encodeURIComponent(username);
  const socket = new SockJS(`http://localhost:8080/ws?username=${encodedUsername}`);
  stompClient = Stomp.over(socket);
  stompClient.connect({}, onConnected, onError);
}

function onConnected() {
  stompClient.subscribe(`/topic/messages.${username}`, onMessageReceived);
  stompClient.subscribe("/topic/initiative-notifications", (payload) => {onMessageReceived(payload);
  });
  stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
}

/*function subscribe(username) {
  if (currentSubscription !== null) {
    currentSubscription.unsubscribe();
    currentSubscription = null;
  }

  currentSubscription = stompClient.subscribe(`/user/${username}/queue/messages`, onMessageReceived);
}*/

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
    messageInput.value = '';
  }
}

function onMessageReceived(payload) {
  const message = JSON.parse(payload.body);
  if (message.type === 'CHAT' && message.recipient === username || message.sender === username) {
    displayMessage(message);
    console.log("Message" + message);
    const messageNotis = {
      sender: message.sender,
      content: message.content,
      type: message.type
    }
    if(message.sender != localStorage.getItem('name')){
      showNotification(messageNotis, "New message from: ");
    }
    
  }
  else if (message.type === 'INI_NOTIS') {
  console.log("Received initiative notification:", message);
  showNotification(message, "New initiative:");
  }
  else if (message.type === 'UPDATE_NOTIS') {
  console.log("Received initiative notification:", message);
  showNotification(message, "New update:");
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

if(messageForm){
messageForm.addEventListener('submit', sendMessage);
}

//Connect to the user and load chat history
function openChat(user) {
  recipient = user.trim();
  //subscribe(recipient);
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

connectWebSocket();

//Send a notification to server and then the server will send it to all user
function sendNotis(title, messageType) {
  if (stompClient) {
    const notisMessage = {
      sender: username,
      content: title,
      type: messageType
    };
    console.log("Sending notis... " + notisMessage)
    stompClient.send("/app/notis.initiative", {}, JSON.stringify(notisMessage));
    console.log("Notis sent!" + notisMessage)
  }
}

//This method show the notification on receiving
function showNotification(message, updateType) {
  console.log(message)
  const notifDropdown = document.querySelector('.notification-dropdown');
  const notifList = notifDropdown.querySelector('ul');
  const notifCount = document.querySelector('.notification-count');

  const notifItem = document.createElement("li");
  if(message.type == "CHAT"){
    notifItem.textContent = updateType + " " + message.sender;
  }
  else{
    notifItem.textContent = updateType + " " + message.content;
  }
  
  notifList.prepend(notifItem);

  let currentCount = parseInt(notifCount.textContent) || 0;
  notifCount.textContent = currentCount + 1;

  notifDropdown.classList.remove('hidden');

  setTimeout(() => {
    notifDropdown.classList.add('hidden');
  }, 5000);
}

