console.log('notifications.js loaded');
// WebSocket connection and notification handling
class NotificationHandler {
    constructor() {
        this.stompClient = null;
        this.connected = false;
        this.notificationCount = parseInt(localStorage.getItem('unreadClientMessages') || '0');
        console.log('Initializing NotificationHandler');
        this.initializeWebSocket();
        this.updateBadge();
    }

    initializeWebSocket() {
        console.log('Attempting to connect to WebSocket...');
        const socket = new SockJS('/ws', null, {
            transports: ['websocket', 'xhr-streaming', 'xhr-polling'],
            withCredentials: true
        });
        this.stompClient = Stomp.over(socket);
        
        // Enable debug logging temporarily
        this.stompClient.debug = function(str) {
            console.log('STOMP Debug:', str);
        };

        this.stompClient.connect({}, (frame) => {
            console.log('Successfully connected to WebSocket');
            this.connected = true;
            
            // Subscribe to user-specific notifications
            const userId = this.getCurrentUserId();
            console.log('Current user ID:', userId);
            
            if (userId) {
                const subscriptionPath = `/user/${userId}/topic/notifications`;
                console.log('Subscribing to:', subscriptionPath);
                
                this.stompClient.subscribe(subscriptionPath, (notification) => {
                    console.log('Received WebSocket message:', notification);
                    this.handleNotification(JSON.parse(notification.body));
                });
            } else {
                console.warn('No user ID found, cannot subscribe to notifications');
            }
            // Subscribe to broadcast topic for debugging
            this.stompClient.subscribe('/topic/notifications', (notification) => {
                console.log('Received BROADCAST WebSocket message:', notification);
                const notifObj = JSON.parse(notification.body);
                const userId = this.getCurrentUserId();
                if (notifObj.userId && notifObj.userId !== userId) {
                    // Not for this user, ignore
                    return;
                }
                this.handleNotification(notifObj);
            });
        }, (error) => {
            console.error('WebSocket connection error:', error);
            this.connected = false;
            // Try to reconnect after 5 seconds
            setTimeout(() => this.initializeWebSocket(), 5000);
        });
    }

    getCurrentUserId() {
        const user = JSON.parse(localStorage.getItem('loggedInUser'));
        return user ? user.id : null;
    }

    updateBadge() {
        const badge = document.getElementById('clientMessagesBadge');
        if (badge) {
            if (this.notificationCount > 0) {
                badge.textContent = this.notificationCount;
                badge.style.display = 'flex';
            } else {
                badge.style.display = 'none';
            }
            console.log('Badge updated:', {
                count: this.notificationCount,
                display: badge.style.display
            });
        } else {
            console.warn('Notification badge element not found');
        }
    }

    handleNotification(notification) {
        console.log('Processing notification:', notification);
        
        if (notification.type === 'NEW_MESSAGE') {
            // Update notification count
            this.notificationCount++;
            localStorage.setItem('unreadClientMessages', this.notificationCount.toString());
            
            // Update the badge display
            this.updateBadge();
            
            // Show notification toast
            this.showNotificationToast(notification);
            
            // Update messages list if it exists
            this.updateMessagesList(notification);
        }
    }

    showNotificationToast(notification) {
        console.log('Showing notification toast:', notification);
        // Create toast element
        const toast = document.createElement('div');
        toast.className = 'notification-toast';
        toast.innerHTML = `
            <div class="notification-content">
                <h4>${notification.type}</h4>
                <p>${notification.message}</p>
            </div>
        `;

        // Add to document
        document.body.appendChild(toast);
        console.log('Toast element added to document');

        // Show toast
        setTimeout(() => {
            toast.classList.add('show');
            console.log('Toast shown');
        }, 100);

        // Remove after 5 seconds
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                toast.remove();
                console.log('Toast removed');
            }, 300);
        }, 5000);
    }

    updateMessagesList(notification) {
        console.log('Attempting to update messages list');
        const messagesList = document.querySelector('.messages-list');
        if (messagesList && notification.data) {
            console.log('Found messages list, adding new message');
            const messageElement = this.createMessageElement(notification.data);
            messagesList.appendChild(messageElement);
            console.log('New message added to list');
        } else {
            console.log('Messages list not found or no data in notification');
        }
    }

    createMessageElement(messageData) {
        console.log('Creating message element:', messageData);
        const div = document.createElement('div');
        div.className = 'message-item';
        div.innerHTML = `
            <div class="message-content">
                <p>${messageData.content}</p>
                <small>${new Date(messageData.createdAt).toLocaleString()}</small>
            </div>
        `;
        return div;
    }
}

// Initialize notification handler when document is ready
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM loaded, initializing notification handler');
    window.notificationHandler = new NotificationHandler();
});

// Notifications functionality
class Notifications {
    constructor() {
        this.notifications = [];
        this.container = null;
        this.init();
    }

    init() {
        // Create notifications container if it doesn't exist
        if (!document.getElementById('notifications-container')) {
            this.container = document.createElement('div');
            this.container.id = 'notifications-container';
            this.container.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 9999;
                display: flex;
                flex-direction: column;
                gap: 10px;
            `;
            document.body.appendChild(this.container);
        } else {
            this.container = document.getElementById('notifications-container');
        }
    }

    show(message, type = 'info', duration = 5000) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.style.cssText = `
            padding: 15px 20px;
            border-radius: 4px;
            background: white;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            display: flex;
            align-items: center;
            gap: 10px;
            animation: slideIn 0.3s ease-out;
        `;

        // Add icon based on type
        const icon = document.createElement('span');
        icon.className = 'notification-icon';
        switch (type) {
            case 'success':
                icon.textContent = '✓';
                notification.style.borderLeft = '4px solid #28a745';
                break;
            case 'error':
                icon.textContent = '✕';
                notification.style.borderLeft = '4px solid #dc3545';
                break;
            case 'warning':
                icon.textContent = '⚠';
                notification.style.borderLeft = '4px solid #ffc107';
                break;
            default:
                icon.textContent = 'ℹ';
                notification.style.borderLeft = '4px solid #17a2b8';
        }

        // Add message
        const messageEl = document.createElement('span');
        messageEl.textContent = message;

        // Add close button
        const closeBtn = document.createElement('button');
        closeBtn.textContent = '×';
        closeBtn.style.cssText = `
            background: none;
            border: none;
            font-size: 20px;
            cursor: pointer;
            padding: 0;
            margin-left: 10px;
            color: #666;
        `;
        closeBtn.onclick = () => this.remove(notification);

        // Assemble notification
        notification.appendChild(icon);
        notification.appendChild(messageEl);
        notification.appendChild(closeBtn);
        this.container.appendChild(notification);

        // Auto remove after duration
        if (duration > 0) {
            setTimeout(() => this.remove(notification), duration);
        }

        // Add to notifications array
        this.notifications.push(notification);
    }

    remove(notification) {
        notification.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => {
            if (notification.parentNode === this.container) {
                this.container.removeChild(notification);
            }
            const index = this.notifications.indexOf(notification);
            if (index > -1) {
                this.notifications.splice(index, 1);
            }
        }, 300);
    }

    success(message, duration) {
        this.show(message, 'success', duration);
    }

    error(message, duration) {
        this.show(message, 'error', duration);
    }

    warning(message, duration) {
        this.show(message, 'warning', duration);
    }

    info(message, duration) {
        this.show(message, 'info', duration);
    }
}

// Add notification styles
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }

    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Create global notifications instance
window.notifications = new Notifications(); 