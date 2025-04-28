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
  localStorage.setItem('initiativeId', initiativeId);

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

      <button class="like-button">❤️ Like (7) </button>
      <button class="show-comment">💬 Show Comments</button>
      <button class="update-initiative-btn">📝 Update initiative</button>
      <button class="join-initiative">✚ Join initiative</button>
    `;
    const joinButton = document.querySelector('.join-initiative');

    if (joinButton) {
      const userId = localStorage.getItem('userId');
      const initiativeId = localStorage.getItem('initiativeId');
    
      fetch('http://127.0.0.1:8080/api/CheckJoinStatus', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: userId, initiativeId: initiativeId })
      })
      .then(res => res.text())
      .then(response => {
        if (response.trim() === "true") {
      
          joinButton.innerText = "✅ Joined";
          joinButton.disabled = true;
        } else {
          
          joinButton.addEventListener('click', function () {
            fetch('http://127.0.0.1:8080/api/JoinInitiative', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ userId: userId, initiativeId: initiativeId })
            })
            .then(res => res.text())
            .then(data => {
              alert(data);
              if (data.trim().toLowerCase().includes("success")) {
                joinButton.innerText = "✅ Joined";
                joinButton.disabled = true;
              }
            })
            .catch(error => {
              console.error('Error joining initiative:', error);
            });
          });
        }
      })
      .catch(error => {
        console.error('Error checking join status:', error);
      });
    }
    
    const likeButton = document.querySelector('.like-button');

    if (likeButton) {
      likeButton.addEventListener('click', function () {
        const userId = localStorage.getItem('userId');
        const initiativeId = localStorage.getItem('initiativeId');
      
        console.log("Trying to like: ", userId, initiativeId);
      
        fetch('http://127.0.0.1:8080/api/LikeInitiative', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userId: userId, initiativeId: initiativeId })
        })
        .then(res => res.text())
        .then(data => {
          alert(data);
        
          if (data.trim().toLowerCase().includes("success")) {
            const likeText = likeButton.innerText;
            const likeMatch = likeText.match(/\d+/);
        
            if (likeMatch) {
              const currentLikes = parseInt(likeMatch[0]);
              const newLikes = currentLikes + 1;
              likeButton.innerText = `❤️ Like (${newLikes})`;
            }
          }
        })
        .catch(error => {
          console.error('Error liking initiative:', error);
        });        
      });
    }

    document.querySelector('.update-initiative-btn').addEventListener('click', () => {
      window.location.href = `update_initiative.html?id=${initiativeId}`;
    });
  })
  .catch(err => {
    console.error("Error fetching initiative:", err);
    initiativePost.innerHTML = "<p style='color:red;'>Failed to load initiative details.</p>";
  });
});
