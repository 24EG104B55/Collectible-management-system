# Collectible Items Management System

A clean, full-stack web application for collectible items, multi-seller price comparisons, auctions, and price tracking. Built with **Spring Boot 3 (Java 21)**, **React (Vite)**, and **H2 In-Memory / MySQL Database**.

---

## 🌟 Key Features

1. **Clean White UI with Grey Accents**:
   - Modern, humanized interface with minimal text clutter, clean item cards, and smooth modal flows.

2. **Authentication & User Management**:
   - Dedicated **Login & Registration** screens on entry.
   - Self-registration directly from the site for new users (`BUYER`, `SELLER`, `ADMIN`).
   - Role-based capabilities and dedicated logout handling.

3. **Live Auctions & Direct Purchases**:
   - Auction bidding engine with server-side validation (`bid > currentHighestBid`).
   - Fixed-price "Buy Now" option with automated stock reduction.
   - Interactive price curve tracking and timestamped bid history.

4. **Multi-Seller Price Comparison**:
   - Compare competing seller offers for identical or equivalent items.
   - Automatic calculation of price differences and highlight of lowest offers.

5. **Administrator Console**:
   - Add and delete inventory items in real-time.
   - Add, suspend, and manage platform user accounts.
   - Live system metrics (total items, active listings, total bids placed, registered users) and low-stock monitor.

---

## 🏗️ Architecture & Project Structure

The project uses a consolidated, flat architecture designed for simplicity and maintainability:

```text
collectible-items-management-system/
├── .gitignore
├── README.md
├── backend/
│   ├── pom.xml                                   # Spring Boot 3 Maven configuration (Java 21)
│   └── src/main/java/com/raretrack/
│       ├── RareTrackApplication.java            # App entrypoint + CORS configuration
│       ├── Models.java                           # All JPA Entities (User, Item, Listing, Bid, etc.)
│       ├── Repositories.java                     # Consolidated Spring Data JPA Repositories
│       ├── DTOs.java                             # Request and response data transfer objects
│       ├── Exceptions.java                       # Unified exceptions & GlobalExceptionHandler
│       ├── RareTrackService.java                 # Complete business logic service
│       ├── RareTrackController.java              # REST API controllers (/api/*)
│       └── DataInitializer.java                  # Seeds demo accounts and listings on startup
│   └── src/main/resources/
│       └── application.properties                # H2 in-memory DB + MySQL configuration
└── frontend/
    ├── package.json                              # React 18, Vite, Axios
    ├── vite.config.js
    ├── index.html
    └── src/
        ├── App.jsx                               # Main application shell & authentication gate
        ├── index.css                             # Clean white & soft-grey styling
        ├── components/
        │   ├── Navbar.jsx                        # Header with search, role-actions, & logout
        │   ├── LoginPage.jsx                     # Sign in & new user registration modal
        │   ├── ItemCard.jsx                      # Minimalist inventory card
        │   ├── ItemDetailModal.jsx               # Details, bidding, and purchase modal
        │   ├── SellerModal.jsx                   # New listing creation modal
        │   ├── SellerComparisonTable.jsx         # Multi-vendor pricing comparison table
        │   ├── PriceChart.jsx                    # SVG historical price curves
        │   └── AdminPanel.jsx                    # Admin console: Add/Delete users & items
        └── services/
            └── api.js                            # Axios API client
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 21+**
- **Maven 3.8+**
- **Node.js 18+** & **npm**

---

### 1. Run the Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```
- API Base URL: `http://localhost:8080/api`
- Database: Embedded H2 in-memory DB (`collectibles_db`)
- H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:collectibles_db`, User: `sa`, Password: *(blank)*)

---

### 2. Run the Frontend (React + Vite)

In a separate terminal:

```bash
cd frontend
npm install
npm run dev
```
- Frontend runs at: `http://localhost:5173`

---

## 🔑 Demo Accounts

The database is automatically pre-seeded with demo accounts:

| Role | Username | Password |
|---|---|---|
| **ADMIN** | `admin` | `admin123` |
| **SELLER** | `vintage_vault` | `seller123` |
| **BUYER** | `collector_dan` | `buyer123` |

*You can also register a brand new account anytime using the **Register New Account** tab on the login screen.*
