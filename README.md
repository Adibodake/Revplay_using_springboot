RevPlay is a full-stack music streaming web application that allows users to browse, search, and play songs, manage playlists, follow artists, and track listening history. Artists can upload songs and manage albums through a secure dashboard.

🚀 Tech Stack
🔹 Backend

Spring Boot

Spring Security

JWT Authentication

Spring Data JPA (Hibernate)

MySQL

REST APIs

🔹 Frontend

Angular (Standalone Components)

RxJS

Angular Routing

HTTP Client

Signals (State Management)

🔹 Tools

IntelliJ IDEA

VS Code

Postman

MySQL Workbench

Git & GitHub

📌 Features
👤 User Features

Register & Login (JWT-based authentication)

Browse all songs

Search songs by name and genre

Play songs with global audio player

Create and manage playlists

Follow artists

View listening history

🎤 Artist Features

Artist Dashboard

Upload songs (audio + cover image)

Create and manage albums

View analytics (basic stats)

🔐 Security

JWT-based authentication

Role-based access control (USER / ARTIST)

Route guards (AuthGuard, ArtistGuard)

🏗 System Architecture

Frontend (Angular)
⬇ REST API
Backend (Spring Boot)
⬇
MySQL Database

Layered Backend Architecture:

Controller Layer

Service Layer

Repository Layer

Database Layer

📂 Project Structure
Backend (Spring Boot)
controller/
service/
repository/
model/
security/
config/
Frontend (Angular)
core/
  services/
  guards/

features/
  auth/
  songs/
  playlists/
  albums/
  history/
  follow/

shared/
  player-bar/
🗄 Database Entities

User

Song

Playlist

Album

ArtistProfile

Follow

ListeningHistory

⚙️ Setup Instructions
1️⃣ Clone the Repository
git clone https://github.com/your-username/revplay.git
2️⃣ Backend Setup

Configure MySQL database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/revplay
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

Run Spring Boot application

3️⃣ Frontend Setup
cd frontend
npm install
ng serve

Open in browser:

http://localhost:4200
🔑 Authentication Flow

User logs in

Backend generates JWT token

Token stored in localStorage

Token sent in Authorization header

Spring Security validates token

📊 Key Learnings

Full-stack development integration

JWT-based authentication

REST API design best practices

Role-based access control

Angular state management

Performance optimization
