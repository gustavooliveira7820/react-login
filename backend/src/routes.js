const express = require('express');

const routes = express.Router();
const users = [{
    id: 1,
    name: 'Gustavo',
    "email": "gustavomatosdeoliveira550@gmail.com",
	"password" : "123456",
/* 
    id: 2,
    name: 'Jeferson',
    "email": "jsrgodoy@gmail.com",
    "password" : "654321" */
}];

routes.post('/login', (req, res) => {
    const { email, password } = req.body;

    const user = users.find(user => user.email === email && user.password === password);
    if (user)
    {
        return res.status(200).json(user);
    }

    return res.status(401).json({ message: 'Credenciais inválidas' });
});

module.exports = routes;