document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const initiativeId = urlParams.get('id');

    fetch('http://127.0.0.1:8080/api/FetchInitiativeByID', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: initiativeId
    })
    .then(res => res.json())
    .then(data => {
      document.getElementById('title').value = data.title || '';
      document.getElementById('description').value = data.description || '';
      document.getElementById('location').value = data.location || '';
      document.getElementById('category').value = data.category || '';

      const visibilityRadios = document.querySelectorAll('input[name="visibility"]');
      visibilityRadios.forEach(radio => {
        if (radio.value === data.visibility) {
          radio.checked = true;
        }
      });
    })
    .catch(error => {
      console.error('Error fetching initiative:', error);
      alert('Failed to load initiative data.');
    });
  });


const form = document.querySelector('.initiative-form');
form.addEventListener('submit', (e) => {
    e.preventDefault();
    const urlParams = new URLSearchParams(window.location.search);
    const initiativeId = urlParams.get('id');
    const updatedInitiative = {
    id: initiativeId,
    title: document.getElementById('title').value,
    description: document.getElementById('description').value,
    location: document.getElementById('location').value,
    category: document.getElementById('category').value,
    visibility: document.querySelector('input[name="visibility"]:checked')?.value
  };

  fetch('http://127.0.0.1:8080/api/UpdateInitiative', {
    method: 'POST', 
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(updatedInitiative)
  })
  .then(response => response.text()) 
    .then(message => {
        console.log(message);
        alert(message);
    })
    .catch(err => {
        console.error("Error:", err);
        alert("An error occurred while updating the initiative.");
    });
});