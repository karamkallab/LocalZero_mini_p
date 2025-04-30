document.addEventListener("DOMContentLoaded", function() {
  const registerForm = document.getElementById("registerForm");
  const loginForm = document.getElementById("loginform");

  if (loginForm) {
    loginForm.addEventListener("submit", function (event) {
      event.preventDefault();
      const email = document.getElementById("email").value;
      const password = document.getElementById("password").value;

      fetch("http://127.0.0.1:8080/api/authenticator", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ user_email: email, user_password: password})
      })
      .then(res => res.json())
      .then(result => {
        if (result.success) {
          localStorage.setItem('userId', result.userId);
          window.location.href = "dashboard.html";
        } else {
          alert("Your email or password is incorrect.");
        }
      })
      .catch(error => {
        console.error("Login error:", error);
      });
    });

    var stompClient = null;
    var connectingElement = document.querySelector('.connecting');

    function connect(event) {
        username = localStorage.getItem('userId');

        if(username) {
            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
        }
        event.preventDefault();
    }

    function onConnected() {
      // Subscribe to the Public Topic
      stompClient.subscribe('/topic/public', onMessageReceived);
  
      // Tell your username to the server
      stompClient.send("/app/chat.addUser",
          {},
          JSON.stringify({sender: username, type: 'JOIN'})
      )
  
      connectingElement.classList.add('hidden');
    }
  
    function onError(error) {
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
    }
  }



  let selectedRoles = [];
  if(registerForm){

  document.querySelectorAll('.roleOption').forEach(option => {
  option.addEventListener('click', function () {
    const role = option.textContent.trim();

    if (!selectedRoles.includes(role)) {
      selectedRoles.push(role);
    } else {
      selectedRoles = selectedRoles.filter(r => r !== role);
    }

    document.getElementById('roleButton').textContent = selectedRoles.join(', ');
    console.log(selectedRoles)
  });
});
}

registerForm.addEventListener("submit", function (event) {
  console.log("Form submitted");  

  event.preventDefault();
  const username = document.getElementById("username").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const location = document.getElementById("location").value;

  fetch("http://127.0.0.1:8080/api/registration", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      name: username,
      email: email,
      password: password,
      location: location,
      role: selectedRoles.join(', ')
    })
  })
  .then(res => res.text())
  .then(result => {
    if (result.toLowerCase().includes("success")) {
      window.location.href = "dashboard.html";
    } else {
      alert("Something went wrong: " + result);
    }
  })
  .catch(error => {
    console.error("Registration error:", error);
  });
});
});

const initiativeForm = document.querySelector('.initiative-form');  
if (initiativeForm) {
  initiativeForm.addEventListener('submit', function (e) {
    e.preventDefault();  

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const location = document.getElementById("location").value;
    const category = document.getElementById("category").value;
    const visibility = document.querySelector('input[name="visibility"]:checked').value;

    console.log("Sending data:", {
      title,
      description,
      location,
      category,
      visibility
    });

    fetch("http://localhost:8080/api/CreateInitiative", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        title,
        description,
        location,
        category,
        visibility
      })
    })
    .then(res => res.text())  
    .then(data => {
      alert("Server response: " + data);  
      initiativeForm.reset();
    })
    .catch(err => {
      console.error("Error while sending:", err);
      alert("Something went wrong while submitting the initiative.");
    });
  });
}







