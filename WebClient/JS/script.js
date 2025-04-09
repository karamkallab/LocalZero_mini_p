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
      .then(res => res.text())
      .then(result => {
        if (result.trim() === "true") {
          window.location.href = "dashboard.html";
        } else {
          alert("Your email or password is incorrect.");
        }
      })
      .catch(error => {
        console.error("Login error:", error);
      });
    });
  }

  if(registerForm){
    let selectedRoles = [];

  document.querySelectorAll('.roleOption').forEach(option => {
  option.addEventListener('click', function () {
    const role = option.textContent.trim();

    if (!selectedRoles.includes(role)) {
      selectedRoles.push(role);
    } else {
      selectedRoles = selectedRoles.filter(r => r !== role);
    }

    document.getElementById('roleButton').textContent = selectedRoles.join(', ');
  });
});

registerForm.addEventListener("submit", function (event) {
  console.log("Form submitted");  // <--- Lägg till den

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

  }

  document.getElementById("initiative-form").addEventListener("submit", function(e) {
    e.preventDefault();
  
    const formData = new FormData(e.target);
    const jsonData = {
      title: formData.get("title"),
      description: formData.get("description"),
      location: formData.get("location"),
      category: formData.get("category"),
      visibility: formData.get("visibility")
    };
  
    fetch("http://localhost:8080/api/initiative", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(jsonData)
    })
    .then(res => res.text())
    .then(data => {
      alert("Svar från servern: " + data);
    })
    .catch(err => {
      console.error("Fel vid skickande:", err);
      alert("Något gick fel!");
    });
  });

});



