const express = require('express');
const app = express();
const port = 3000;

// Mock user data
const users = [
    { id: 1, name: 'John Doe', username: 'john_doe', email: 'john@example.com' },
    { id: 2, name: 'Jane Smith', username: 'jane_smith', email: 'jane@example.com' },
    // Add more mock users as needed
];

app.get('/users/:id', (req, res) => {
    const userId = parseInt(req.params.id, 10);
    const user = users.find(u => u.id === userId);

    if (user) {
        res.json(user);
    } else {
        res.status(404).send('User not found');
    }
});

app.listen(port, () => {
    console.log(`REST service running at http://localhost:${port}`);
});
