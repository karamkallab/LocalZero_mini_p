document.addEventListener("DOMContentLoaded", () => {
  updateCurrentUser();
  const email = localStorage.getItem("userEmail");

    const initiativeList = document.getElementById("initiativeList");
    if (!initiativeList) {
      console.warn("Element #initiativeList not found.");
      return;
    }
  
    fetch(`http://localhost:8080/api/FetchInitiatives?email=${encodeURIComponent(email)}`)
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

  document.querySelector('.manage-roles-btn')?.addEventListener('click', () => {
    const email = localStorage.getItem("userEmail");

    if (!email) {
      alert("User ID not found in local storage.");
      return;
    }

    fetch(`http://localhost:8080/api/user-role?email=${encodeURIComponent(email)}`, {
      method: 'GET'
    })
  
      .then(res => {
        if (!res.ok) throw new Error("Failed to check role");
        return res.json();
      })
      .then(isOrganizer => {
        if (isOrganizer === true) {
          window.location.href = 'communityOrganizer.html';
        } else {
          alert('You do not have the permission to manage user roles');
        }
      })
      .catch(err => {
        console.error("Error checking role:", err);
        alert("You do not have the permission to manage user roles");
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

//Update Current User, get from databse instead for localStorage !!!!!!!!!!!
function updateCurrentUser() {
  const email = localStorage.getItem('userEmail');
  if (!email) {
    console.warn("No email found in localStorage");
    return;
  }

  fetch("http://127.0.0.1:8080/api/FetchCurrentRole", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(email)
  })
  .then(res => {
    if (!res.ok) throw new Error("Failed to fetch role");
    return res.text();
  })
  .then(role => {
    localStorage.setItem('role', role);
    const currentUserP = document.querySelector('.current-user');
    if (currentUserP) {
      currentUserP.textContent = `Current User: ${localStorage.getItem('name')} - ${role}`;
    }
    console.log("Rolse: " + role);
  })
  .catch(err => {
    console.error("Error fetching current role:", err);
  });
}