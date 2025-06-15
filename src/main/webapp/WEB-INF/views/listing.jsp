<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Listing Page - DomuSwap</title>
  <link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@500;700&family=Judson&display=swap" rel="stylesheet">
<link rel="stylesheet" href="/styles/common.css">
  <link href="https://cdn.jsdelivr.net/npm/@fancyapps/ui/dist/fancybox/fancybox.css" rel="stylesheet" />
  
  <style>
    :root {
      --bg: #FAF9F7;
      --green-dark: #193E15;
      --neutral-dark: #2A2729;
      --green-soft: #7EA780;
      --accent: #E4D9C7;
      --font-title: 'Josefin Sans', sans-serif;
      --font-body: 'Judson', serif;
      --back-default: #f5f5f5;
      --back-hover: #e0e0e0;
      --user-blue: #3578e5;
    }
    body {
      background-color: var(--bg);
      background-image: url("/images/logo_opaque_beige.png");
      background-repeat: no-repeat;
      background-size: 600px;
      background-position: left 5%;
      margin: 0;
      padding: 0;
      font-family: var(--font-body);
    }
    header {
      width: 100vw;
      min-width: 100vw;
      background: #fff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 0 0 0;
      height: 80px;
      box-sizing: border-box;
      border-bottom: 2px solid #e4d9c7;
    }
    .header-left {
      display: flex;
      align-items: center;
      gap: 10px;
      height: 100%;
      margin-left: 24px;
    }
    .logo-link {
      display: flex;
      align-items: center;
      height: 100%;
      margin-right: 0;
      padding: 0;
      text-decoration: none;
      color: inherit;
    }
    .middle-logo {
      height: 48px;
      width: auto;
      display: block;
      margin: 0;
      padding: 0;
    }
    .logo-text {
      font-family: var(--font-title);
      font-size: 1.5rem;
      color: var(--neutral-dark);
      margin-left: 10px;
      font-weight: bold;
      letter-spacing: -1px;
    }
    .back-link {
      color: var(--neutral-dark);
      font-family: var(--font-title);
      font-size: 14px;
      text-decoration: none;
      background: var(--back-default);
      border: none;
      cursor: pointer;
      margin-left: 18px;
      padding: 7px 18px;
      border-radius: 7px;
      transition: background 0.2s;
      font-weight: bold;
      box-shadow: none;
      outline: none;
      display: inline-block;
    }
    .back-link:hover {
      background: var(--back-hover);
    }
    .account-box {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-right: 32px;
    }
    .user-icon {
      width: 34px;
      height: 34px;
      border-radius: 50%;
      background: var(--user-blue);
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.3rem;
      font-family: var(--font-title);
      font-weight: bold;
      cursor: pointer;
      margin-right: 6px;
      border: none;
      outline: none;
      transition: background 0.2s;
    }
    .user-icon:hover {
      background: #285bb5;
    }
    .logout-button {
      color: #fff;
      background: var(--green-dark);
      font-family: var(--font-title);
      font-size: 15px;
      text-decoration: none;
      cursor: pointer;
      border: none;
      border-radius: 20px;
      padding: 8px 22px;
      transition: background 0.2s;
      font-weight: bold;
      display: inline-block;
    }
    .logout-button:hover {
      background: #145c1b;
    }
    h1, h2, h3, h4 {
      font-family: var(--font-title);
      margin: 0;
      color: var(--neutral-dark);
    }
    .container {
      max-width: 1300px;
      margin: 20px auto;
      display: grid;
      grid-template-columns: 2fr 1fr;
      gap: 30px;
      padding: 0 30px;
      align-items: start;
       margin-top: 87px
    }
    .photo-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 8px;
      margin-bottom: 15px;
    }
    .photo-grid div {
      background-color: #eee;
      height: 120px;
      border-radius: 8px;
      background-size: cover;
      background-position: center;
    }
    .section, .sidebar-section {
      background: white;
      padding: 20px;
      border-radius: 8px;
      margin-bottom: 15px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }
    .right-sidebar {
      position: sticky;
      top: 20px;
      height: fit-content;
    }
    .services-grid {
      padding: 10px;
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 8px;
      margin-top: 12px;
      margin-bottom: 20px;
      border-bottom: 1px solid #eee;
    }
    .service-item {
      background: #f5f5f5;
      padding: 10px;
      border-radius: 6px;
      font-size: 0.9rem;
      text-align: center;
      font-weight: bold;
    }
    .booking-form {
      display: flex;
      flex-direction: column;
      gap: 10px;
    }
    .booking-form label {
      font-weight: bold;
      font-size: 0.9rem;
    }
    .booking-form input, .booking-form select {
      padding: 8px 12px;
      border: 1px solid #ddd;
      border-radius: 6px;
      font-family: var(--font-body);
    }
    .date-row {
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
      margin-top: -20px;
    }
    input[type="date"] {
      width: 100%;
      max-width: 100%;
      box-sizing: border-box;
    }
    .date-input {
      flex: 1;
      min-width: 0;
    }
    .date-input label {
      display: block;
      margin-bottom: 5px;
    }
    .btn {
      padding: 12px;
      font-family: var(--font-title);
      background-color: var(--neutral-dark);
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 15px;
      cursor: pointer;
      margin-top: 10px;
      font-weight: bold;
      width: 100%;
    }
    .reviews-list {
      margin-top: 12px;
    }
    .review-item {
      padding: 10px 0;
      border-bottom: 1px solid #eee;
      font-size: 0.9rem;
    }
    .review-item:last-child {
      border-bottom: none;
    }
    .review-author {
      font-weight: bold;
      color: var(--neutral-dark);
    }
    .rating-box {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 8px;
      background: #f0f8f0;
      border-radius: 8px;
      padding: 8px;
      margin-top: 10px;
    }
    .rating-item {
      padding: 8px;
      background: #e8f5e9;
      border-radius: 4px;
      text-align: center;
      font-size: 0.9rem;
    }
    .compact-title {
      font-size: 1.1rem;
      margin-bottom: 8px;
    }
    .amenities-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 8px;
      background: #f0f8f0;
      border-radius: 8px;
      padding: 8px;
      margin-top: 10px;
    }
    .amenity {
      padding: 8px;
      background: #e8f5e9;
      border-radius: 4px;
      text-align: center;
      font-size: 0.9rem;
    }
    .compact-text {
      font-size: 0.85rem;
      line-height: 1.4;
    }
    .combined-section {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }
    .reviews-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
    }
    .review-count {
      font-size: 0.9rem;
      color: #666;
    }
    .rating-number {
      font-weight: bold;
      color: var(--neutral-dark);
    }
    .amenities-title {
      color: var(--neutral-dark);
      font-size: 1rem;
      font-weight: bold;
      margin-bottom: 10px;
    }
    .back-button {
      background: none;
      border: none;
      color: white;
      font-family: var(--font-title);
      font-size: 16px;
      cursor: pointer;
      padding: 10px 15px;
      display: flex;
      align-items: center;
      gap: 5px;
    }
    
    .back-button:hover {
      opacity: 0.9;
    }
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 20px;
    }
    
    .back-link {
      color: white;
      font-family: var(--font-title);
      font-size: 14px;
      text-decoration: none;
      margin-left: 10px;
    }
    
    .back-link:hover {
      opacity: 0.9;
    }
    
    .account-box {
      display: flex;
      align-items: center;
      gap: 15px;
    }
    
    .account-button {
      color: white;
      font-family: var(--font-title);
      font-size: 14px;
      text-decoration: none;
      cursor: pointer;
    }
    
    .account-button:hover {
      opacity: 0.9;
    }

    .rating-criteria {
      display: flex;
      flex-direction: column;
      gap: 12px;
      margin-bottom: 15px;
    }

    .rating-criteria label {
      font-weight: bold;
      color: var(--neutral-dark);
      margin-bottom: 4px;
    }

    .star-rating select {
      width: 100%;
      padding: 8px;
      border: 1px solid #ddd;
      border-radius: 6px;
      background-color: white;
      font-family: var(--font-body);
    }

    .review-form textarea {
      width: 100%;
      padding: 8px;
      border: 1px solid #ddd;
      border-radius: 6px;
      font-family: var(--font-body);
      resize: vertical;
      min-height: 100px;
    }

    .review-form .btn {
      background-color: var(--green-dark);
      color: white;
      padding: 12px;
      border: none;
      border-radius: 6px;
      font-family: var(--font-title);
      font-weight: bold;
      cursor: pointer;
      transition: background-color 0.2s;
    }

    .review-form .btn:hover {
      background-color: #145c1b;
    }

    .modal {
      display: none;
      position: fixed;
      z-index: 1000;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0,0,0,0.5);
    }

    .modal-content {
      background-color: #fefefe;
      margin: 15% auto;
      padding: 20px;
      border: 1px solid #888;
      width: 80%;
      max-width: 500px;
      border-radius: 8px;
      position: relative;
    }

    .close {
      color: #aaa;
      float: right;
      font-size: 28px;
      font-weight: bold;
      cursor: pointer;
    }

    .close:hover {
      color: black;
    }

    .image-upload-row {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 10px;
    }
    .preview-img {
      width: 80px;
      height: 80px;
      object-fit: cover;
      border-radius: 8px;
      border: 1.5px solid #e4d9c7;
      background: #f5f5f5;
      margin-left: 10px;
    }
  </style> 
</head>

<body>
 <header>
  <div class="header-left">
    <a href="/main_page.html" class="logo-link">
      <img src="/images/logo_white.png" alt="DomuSwap Logo" class="middle-logo">
      <span class="logo-text">DomuSwap</span>
    </a>
    <a href="/house_selection.html" class="back-link">back</a>
  </div>
   <div class="account-box">
      <span class="account-text">Sign in</span>
    </div>
  </header>
  <!-- Login Modal -->
<div id="loginModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="closeModal()">&times;</span>
    <h2 id="modalTitle">Login to DomuSwap</h2>

    <div id="loginFormContainer">
      <form id="loginForm">
        <label for="email">Email</label>
        <input type="email" id="email" placeholder="Enter your email" required />

        <label for="password">Password</label>
        <input type="password" id="password" placeholder="Enter your password" required />

        <button type="submit">Login</button>
      </form>
      <p>Don't have an account? <a href="#" onclick="showSignUp()">Sign Up</a></p>
    </div>

    <div id="signUpFormContainer" style="display: none;">
      <form id="signUpForm">
        <label for="fullName">Full Name</label>
        <input type="text" id="fullName" placeholder="Enter your full name" required />

        <label for="signUpEmail">Email</label>
        <input type="email" id="signUpEmail" placeholder="Enter your email" required />

        <label for="signUpPassword">Password</label>
        <input type="password" id="signUpPassword" placeholder="Create a password" required />

        <button type="submit">Sign Up</button>
      </form>
      <p>Already have an account? <a href="#" onclick="showLogin()">Login</a></p>
    </div>
  </div>
</div>
  
  <div class="container">
    <div class="left-content">
      <h2 style="margin-bottom: 15px;">📍 ${housing.location}, ${housing.title}</h2>
      <div class="photo-grid">
        <c:if test="${not empty housing.photo_1}">
          <a data-fancybox="gallery" href="${housing.photo_1}">
            <img src="${housing.photo_1}" alt="Photo 1" style="width:100%; height:120px; object-fit:cover; border-radius:8px; cursor:pointer;" />
          </a>
        </c:if>
        <c:if test="${not empty housing.photo_2}">
          <a data-fancybox="gallery" href="${housing.photo_2}">
            <img src="${housing.photo_2}" alt="Photo 2" style="width:100%; height:120px; object-fit:cover; border-radius:8px; cursor:pointer;" />
          </a>
        </c:if>
        <c:if test="${not empty housing.photo_3}">
          <a data-fancybox="gallery" href="${housing.photo_3}">
            <img src="${housing.photo_3}" alt="Photo 3" style="width:100%; height:120px; object-fit:cover; border-radius:8px; cursor:pointer;" />
          </a>
        </c:if>
      </div>

      <div class="section">
        <h3>Place Description</h3>
        <p class="compact-text">${housing.description}</p>
      </div>

      <div class="section">
        <h3>Previous tenants have rated this place:</h3>
        <div class="rating-box">
          <div class="rating-item">Cleanliness: ${averageCleanliness} ⭐️</div>
          <div class="rating-item">Location: ${averageLocation} ⭐️</div>
          <div class="rating-item">Check-In Experience: ${averageCheckin} ⭐️</div>
          <div class="rating-item">Value For Money: ${averageValue} ⭐️</div>
        </div>

        <h3 class="amenities-title">What this place offers</h3>
        <div class="amenities-grid">
          <c:forEach items="${housing.amenities.split(',')}" var="amenity">
            <c:choose>
              <c:when test="${amenity == 'tv'}">
                <div class="amenity">📺 TV</div>
              </c:when>
              <c:when test="${amenity == 'wifi'}">
                <div class="amenity">🌐 Wi-Fi</div>
              </c:when>
              <c:when test="${amenity == 'ac'}">
                <div class="amenity">❄️ AC</div>
              </c:when>
              <c:when test="${amenity == 'balcony'}">
                <div class="amenity">🪟 Balcony</div>
              </c:when>
              <c:when test="${amenity == 'laundry'}">
                <div class="amenity">🧺 Laundry</div>
              </c:when>
            </c:choose>
          </c:forEach>
        </div>
      </div>
    </div>

    <div class="right-sidebar">
      <div class="sidebar-section">
        <h3 class="compact-title">Services To Accomplish</h3>
		<div class="services-grid">
		  <c:forEach var="service" items="${housing.services}">
		    <div class="service-item">${service.title}</div>
		  </c:forEach>
		</div>

        <form id="bookingForm" class="booking-form">
          <div class="date-row">
            <div class="date-input">
              <label>CHECK-IN</label>
              <input type="date" name="checkIn" required>
            </div>
            <div class="date-input">
              <label>CHECK-OUT</label>
              <input type="date" name="checkOut" required>
            </div>
          </div>
          <label>GUEST</label>
          <select name="guestCount" required>
            <option value="1">1 Person</option>
            <option value="2">2 Persons</option>
            <option value="6">6 Persons</option>
            <option value="8">8+ Persons</option>
          </select>
          <button type="submit" class="btn">BOOK STAY</button>
        </form>

        <c:url var="chatUrl" value="/message/owner/${housing.id}" />
        <button class="btn" style="background: #28A745; color: white; margin-top: 10px;" onclick="saveListing('${housing.id}')">
          Save Listing
        </button>
      </div>

      <div class="sidebar-section">
        <div class="reviews-header">
          <h3 class="compact-title">Reviews</h3>
          <div class="review-count">
            <span class="rating-number">${averageRating} ⭐️</span> · ${reviews.size()} reviews
          </div>
        </div>
        <div class="reviews-list">
          <c:forEach var="review" items="${reviews}">
            <div class="review-item">
              <span class="review-author">${review.user.name}</span> — ${review.createdAt}<br>
              <span class="compact-text">${review.comment}</span>
            </div>
          </c:forEach>
        </div>

        <c:if test="${canLeaveReview}">
          <!-- Review functionality moved to reservations page -->
        </c:if>
      </div>
    </div>
  </div>

  <!-- JS script for booking and account handling -->
  <script>
    // Handle reservation submission
    const form = document.getElementById("bookingForm");
    if (form) {
      form.addEventListener("submit", function (e) {
        e.preventDefault();
        fetch("/api/reservations/book", {
          method: "POST",
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          body: new URLSearchParams({
            housingId: '${housing.id}',
            userId: JSON.parse(localStorage.getItem('loggedInUser')).id,
            checkIn: form.checkIn.value,
            checkOut: form.checkOut.value,
            guestCount: form.guestCount.value
          })
        }).then(async response => {
          if (response.ok) {
            const btn = form.querySelector("button[type='submit']");
            btn.innerText = "✅ Booking Sent";
            btn.disabled = true;
            btn.style.backgroundColor = "#7EA780";
          } else {
            const text = await response.text();
            alert("❌ Booking failed: " + text);
          }
        }).catch(() => {
          alert("❌ Server error. Please try again.");
        });
      });
    }

    function saveListing(housingId) {
      const user = JSON.parse(localStorage.getItem('loggedInUser'));
      if (!user) {
        alert('Please log in to save listings');
        window.location.href = '/main_page';
        return;
      }

      fetch('/api/saved-listings', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          userId: user.id,
          housingId: housingId
        })
      })
      .then(response => {
        if (response.ok) {
          alert('✅ Listing saved successfully!');
        } else {
          throw new Error('Failed to save listing');
        }
      })
      .catch(error => {
        alert('❌ Error: ' + error.message);
      });
    }

    // Handle login/logout UI
    window.onload = () => {
  const user = JSON.parse(localStorage.getItem('loggedInUser'));
  if (user) {
    updateAccountUI(user.name);
  }
};


    function updateAccountUI(name) {
      const user = JSON.parse(localStorage.getItem("loggedInUser"));
      if (!user) return;
      const role = user.role;
      let accountLink = "#";
      if (role === "owner") accountLink = "/owner.html";
      else if (role === "customer") accountLink = "/customer.html";
      const accountBox = document.querySelector(".account-box");
      accountBox.innerHTML = `
        <a class="account-name" id="account-link" style="cursor:pointer;">👤 ${name}</a>
        <a class="account-name" onclick="logout()" style="margin-left: 10px; cursor: pointer;">Logout</a>
      `;
      document.getElementById('account-link').addEventListener('click', function(e) {
        e.preventDefault();
        window.location.assign(accountLink);
        return false;
      });
    }
    
    // === Logout and redirect to homepage ===
    function logout() {
      localStorage.removeItem('loggedInUser');
      
      // Reset account UI to "Sign in"
      const accountBox = document.querySelector(".account-box");
      accountBox.innerHTML = `<span class="account-text">Sign in</span>`;
      
      // Reattach click event listener to reopen login modal
      accountBox.addEventListener('click', function () {
        const user = JSON.parse(localStorage.getItem('loggedInUser'));
        if (!user) {
          document.getElementById('loginModal').style.display = 'block';
        }
      });

      // Optionally redirect if you want:
      // window.location.href = 'main_page.html';
    }

    // === Open modal if not logged in ===
    document.querySelector('.account-box').addEventListener('click', function () {
      const user = JSON.parse(localStorage.getItem('loggedInUser'));
      if (!user) {
        document.getElementById('loginModal').style.display = 'block';
      }
    });

    // === Close modal ===
    function closeModal() {
      document.getElementById('loginModal').style.display = 'none';
    }

    // === Toggle login/signup modal ===
    function showSignUp() {
      document.getElementById('modalTitle').textContent = "Sign Up for DomuSwap";
      document.getElementById('loginFormContainer').style.display = 'none';
      document.getElementById('signUpFormContainer').style.display = 'block';
    }

    function showLogin() {
      document.getElementById('modalTitle').textContent = "Login to DomuSwap";
      document.getElementById('signUpFormContainer').style.display = 'none';
      document.getElementById('loginFormContainer').style.display = 'block';
    }

    // === LOGIN request ===
    document.getElementById('loginForm').addEventListener('submit', function (e) {
      e.preventDefault();

      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;

      fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      })
        .then(response => {
          if (response.ok) return response.json();
          throw new Error('Login failed');
        })
        .then(user => {
          localStorage.setItem('loggedInUser', JSON.stringify(user));
          updateAccountUI(user.name);
          closeModal();
        })
        .catch(error => {
          console.error('Login error:', error);
          alert('❌ Login failed. Please check credentials.');
        });
    });

    // === SIGNUP request ===
    document.getElementById('signUpForm').addEventListener('submit', function (e) {
      e.preventDefault();

      const name = document.getElementById('fullName').value;
      const email = document.getElementById('signUpEmail').value;
      const password = document.getElementById('signUpPassword').value;

      fetch('http://localhost:8080/api/users/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password })
      })
        .then(response => {
          if (response.ok) {
            alert('✅ Signup successful!');
            closeModal();
          } else {
            alert('❌ Signup failed. Try a different email.');
          }
        })
        .catch(error => {
          console.error('Signup error:', error);
          alert('❌ Signup error occurred.');
        });
    });

    // === Prefill booking form with check-in/out dates and guest count from localStorage ===
    window.addEventListener('DOMContentLoaded', () => {
      const checkIn = localStorage.getItem('checkInDate');
      const checkOut = localStorage.getItem('checkOutDate');
      const guestCount = localStorage.getItem('guestCount');

      const checkInInput = document.querySelector('input[name="checkIn"]');
      const checkOutInput = document.querySelector('input[name="checkOut"]');
      const guestSelect = document.querySelector('select[name="guestCount"]');

      if (checkIn && checkInInput) checkInInput.value = checkIn;
      if (checkOut && checkOutInput) checkOutInput.value = checkOut;

      if (guestCount && guestSelect) {
        const trimmed = guestCount.trim();
        const match = Array.from(guestSelect.options).some(option => option.value === trimmed);

        if (!match) {
          const customOption = document.createElement("option");
          customOption.value = trimmed;
          customOption.text = `${trimmed} Persons`;
          customOption.selected = true;
          guestSelect.appendChild(customOption);
        } else {
          guestSelect.value = trimmed;
        }
      }
    });

    function previewImage(input, num) {
      const file = input.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
          const img = document.getElementById('preview' + num);
          img.src = e.target.result;
          img.style.display = 'block';
        };
        reader.readAsDataURL(file);
      }
    }
  </script>
  <script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui/dist/fancybox/fancybox.umd.js"></script>
  <script>
    Fancybox.bind("[data-fancybox='gallery']", {});
  </script>
</body>
</html>
