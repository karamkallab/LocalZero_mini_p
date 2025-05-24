# LocalZero – A Neighborhood Sustainability Platform

**LocalZero** is a collaborative platform that enables neighborhoods to work together on local sustainability initiatives. The idea is to connect residents so they can co-create, share, and track community-driven actions such as:

- Tool sharing  
- Food swaps  
- Community gardening  
- Recycling drives  
- Ride-sharing  

It includes features like real-time messaging, initiative management, and progress tracking — all designed to foster eco-friendly collaboration at the neighborhood level.

---

## Getting Started

### Start the Server (Backend)

You can run the backend using **any Java IDE** (such as IntelliJ IDEA or Eclipse):

1. Navigate to the LocalZero_mini_p folder.
2. Open the `LocalZero` project folder in your IDE.
3. Navigate to:  
   `Localzero/localzero/src/main/java/com/example/localzero`
4. Run the `LocalzeroApplication.java` file.

This starts the Spring Boot WebSocket and REST server on port **8080**.

---

### Start the Client (Frontend)

You can launch the frontend using **Live Server** in **Visual Studio Code**:

1. Open the `Webclient` folder in VS Code.
2. Inside VS Code, navigate to the `HTML` folder.
3. Open the `login.html` file.
4. Click the **"Go Live"** button (bottom-right) to launch it in your browser.

This runs the client at `http://127.0.0.1:5500`.

---

### Open Multiple Clients

To simulate multiple users:

1. In VS Code, go to **File > Duplicate Workspace**.
2. In the new workspace, open the `login.html` file again.
3. Click **Go Live** to open another browser window with a second user.

---

## Key Features

- Community-based sustainability initiatives
- Direct messaging between users
- Initiative creation and tracking
- Chat history persistence via PostgreSQL
- REST API for user and message operations

---

## Technologies Used

- Java 17
- Spring Boot (WebSocket + REST)
- PostgreSQL
- HTML/CSS/JavaScript (Vanilla)
- SockJS & STOMP
- Visual Studio Code (Live Server)

