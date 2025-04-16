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
    body: JSON.stringify({ id: initiativeId })
  })
  .then(res => {
  })
  .then(data => {
    console.log("Fetched initiative: test");
    console.log("Fetched initiative:", data);
    // Clear existing content
    initiativePost.innerHTML = '';

    // Example structure from backend: adjust based on actual response
    // data = {
    //   title: "Tool Sharing in Block C",
    //   location: "Block C",
    //   startDate: "2025-05-01",
    //   endDate: "2025-12-31",
    //   category: "Recycling",
    //   description: "Share tools with neighbors...",
    //   likes: 7
    // };

    // Create new content
    initiativePost.innerHTML = `
      <h2>Title: ${data.title}</h2>
      <p><strong>Location:</strong> ${data.location}</p>
      <p><strong>Date:</strong> ${formatDate(data.startDate)} to ${formatDate(data.endDate)}</p>
      <p><strong>Category:</strong> ${data.category}</p>
      <p><strong>Description:</strong> ${data.description}</p>

      <button class="like-button">❤️ Like (${data.likes})</button>
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

// Helper to format date as DD-MM-YYYY
function formatDate(dateStr) {
  const date = new Date(dateStr);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth()+1).padStart(2, '0');
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
}

