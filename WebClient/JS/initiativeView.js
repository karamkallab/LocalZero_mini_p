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


const initiativePost = document.querySelector(".initiative-post .initiative-header");

document.addEventListener("DOMContentLoaded", () => {
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const initiativeId = urlParams.get('id');

  fetch("http://127.0.0.1:8080/api/FetchInitiativeByID", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: initiativeId
  })
  .then(res => res.json())
  .then(data => {
    console.log("Fetched initiative:", data);
    initiativePost.innerHTML = '';

    initiativePost.innerHTML = `
      <h2>Title: ${data.title}</h2>
      <p><strong>Description:</strong> ${data.description}</p>
      <p><strong>Location:</strong> ${data.location}</p>
      <p><strong>Category:</strong> ${data.category}</p>

      <button class="show-comment">💬 Show Comments</button>
      <button class="show-comment">📝 Update initiative</button>
      <button class="show-comment">✚ Join initiative</button>
    `;
  })
  .catch(err => {
    console.error("Error fetching initiative:", err);
    initiativePost.innerHTML = "<p style='color:red;'>Failed to load initiative details.</p>";
  });
});


