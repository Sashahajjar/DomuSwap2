function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('/api/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ email, password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Login failed');
        }
        return response.json();
    })
    .then(user => {
        console.log('Login successful:', user);
        // Store user data in localStorage (as 'loggedInUser' for consistency)
        localStorage.setItem('loggedInUser', JSON.stringify({
            id: user.id,
            name: user.name,
            email: user.email,
            role: user.role
        }));
        
        // Redirect based on role
        if (user.role === 'owner') {
            window.location.href = '/owner';
        } else {
            window.location.href = '/customer';
        }
    })
    .catch(error => {
        console.error('Login error:', error);
        alert('Login failed. Please check your credentials.');
    });
} 