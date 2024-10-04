document.addEventListener('DOMContentLoaded', async () => {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        handleUnauthenticated();
        return;
    }

    // Initialize app
    await loadChats();

    // Event listeners
    document.getElementById('sendButton').addEventListener('click', sendMessage);
    document.getElementById('editContactButton').addEventListener('click', editContact);
    document.getElementById('deleteContactButton').addEventListener('click', deleteContact);

    async function loadChats() {
        try {
            const response = await fetch('/api/v1/chats', {
                headers: { 'Authorization': `Bearer ${accessToken}` }
            });
            if (!response.ok) throw new Error('Failed to load chats');
            const chats = await response.json();
            displayChats(chats);
        } catch (error) {
            console.error('Error loading chats:', error);
            handleRequestError();
        }
    }

    async function loadChat(chatId, partnerId) {
        try {
            const [chatResponse, messagesResponse, partnerInfoResponse] = await Promise.all([
                fetch(`/api/v1/chats/${chatId}`, { headers: { 'Authorization': `Bearer ${accessToken}` } }),
                fetch(`/api/v1/messages/chat/${chatId}?page=0&size=50`, { headers: { 'Authorization': `Bearer ${accessToken}` } }),
                fetch(`/api/v1/contacts/${partnerId}`, { headers: { 'Authorization': `Bearer ${accessToken}` } })
            ]);
            if (!chatResponse.ok || !messagesResponse.ok || !partnerInfoResponse.ok) throw new Error('Failed to load chat details');
            const [chat, messagesPage, partner] = await Promise.all([
                chatResponse.json(),
                messagesResponse.json(),
                partnerInfoResponse.json()
            ]);
            displayChat(chat, messagesPage.content, partner);
        } catch (error) {
            console.error('Error loading chat:', error);
            handleRequestError();
        }
    }

    async function sendMessage() {
        const messageInput = document.getElementById('messageInput');
        const messageContent = messageInput.value.trim();
        const chatId = getCurrentChatId();

        if (!chatId || !messageContent) {
            alert('Please select a contact and enter a message.');
            return;
        }

        try {
            const response = await fetch('/api/v1/messages', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ chatId, content: messageContent })
            });
            if (!response.ok) throw new Error('Failed to send message');
            const sentMessage = await response.json();
            displayMessage(sentMessage);
            messageInput.value = ''; // Clear input
        } catch (error) {
            console.error('Error sending message:', error);
            handleRequestError();
        }
    }

    async function editContact() {
        const partnerId = document.getElementById('contactAvatar').getAttribute('data-partner-id');
        const newName = prompt('Enter new name:');
        if (!partnerId || !newName) {
            alert('Invalid input.');
            return;
        }
        try {
            const response = await fetch(`/api/v1/contacts/${partnerId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: newName })
            });
            if (!response.ok) throw new Error('Failed to update contact');
            alert('Contact updated successfully.');
            await loadChats(); // Reload contacts
            await loadChat(getCurrentChatId(), partnerId); // Reload current chat
        } catch (error) {
            console.error('Error updating contact:', error);
            handleRequestError();
        }
    }

    async function deleteContact() {
        const partnerId = document.getElementById('contactAvatar').getAttribute('data-partner-id');
        const confirmDelete = confirm('Are you sure you want to delete this contact?');
        if (!partnerId || !confirmDelete) return;
        try {
            const response = await fetch(`/api/v1/contacts/${partnerId}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${accessToken}` }
            });
            if (!response.ok) throw new Error('Failed to delete contact');
            alert('Contact deleted successfully.');
            await loadChats(); // Reload contacts
            clearChat(); // Clear current chat
        } catch (error) {
            console.error('Error deleting contact:', error);
            handleRequestError();
        }
    }

    function displayChats(chats) {
        const contactsList = document.getElementById('contactsList');
        contactsList.innerHTML = '';
        chats.forEach(chat => {
            const contactItem = document.createElement('div');
            contactItem.classList.add('contact__item');
            contactItem.setAttribute('data-chat-id', chat.id);
            contactItem.setAttribute('data-partner-id', chat.partner.id);
            contactItem.textContent = chat.partner.name;
            contactItem.addEventListener('click', () => loadChat(chat.id, chat.partner.id));
            contactsList.appendChild(contactItem);
        });
    }

    function displayChat(chat, messages, partner) {
        document.getElementById('chatHeader').textContent = `Chat with ${partner.name}`;
        displayMessages(messages);
        displayContactInfo(partner);
    }

    function displayMessages(messages) {
        const messagesArea = document.getElementById('messagesArea');
        messagesArea.innerHTML = '';
        messages.forEach(message => {
            const messageDiv = document.createElement('div');
            messageDiv.classList.add('message');
            messageDiv.classList.add(message.isSent ? 'message--sent' : 'message--received');
            messageDiv.textContent = message.content;
            messagesArea.appendChild(messageDiv);
        });
        messagesArea.scrollTop = messagesArea.scrollHeight;
    }

    function displayContactInfo(contact) {
        const contactAvatar = document.getElementById('contactAvatar');
        const contactName = document.getElementById('contactName');
        const contactEmail = document.getElementById('contactEmail');
        const contactPhone = document.getElementById('contactPhone');

        contactName.textContent = contact.name;
        contactEmail.textContent = contact.email || '-';
        contactPhone.textContent = contact.phone || '-';

        generateAvatar(contactAvatar, contact.name);
        contactAvatar.setAttribute('data-partner-id', contact.id);
    }

    function displayMessage(message) {
        const messagesArea = document.getElementById('messagesArea');
        const messageDiv = document.createElement('div');
        messageDiv.classList.add('message');
        messageDiv.classList.add(message.isSent ? 'message--sent' : 'message--received');
        messageDiv.textContent = message.content;
        messagesArea.appendChild(messageDiv);
        messagesArea.scrollTop = messagesArea.scrollHeight;
    }

    function generateAvatar(avatarElement, name) {
        const initial = name.charAt(0).toUpperCase();
        avatarElement.textContent = initial;
        const colors = ['#FF6B6B', '#4ECDC4', '#F7FFF7', '#FFE66D', '#1A535C'];
        avatarElement.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
    }

    function handleUnauthenticated() {
        window.location.href = '/login';
    }

    function handleRequestError() {
        alert('An error occurred. Please try again.');
    }

    function clearChat() {
        document.getElementById('chatHeader').textContent = 'Select a contact to start chatting';
        document.getElementById('messagesArea').innerHTML = '';
        document.getElementById('contactDetails').innerHTML = '';
    }

    function getCurrentChatId() {
        const chatHeader = document.getElementById('chatHeader');
        return chatHeader.getAttribute('data-chat-id') || null;
    }
});