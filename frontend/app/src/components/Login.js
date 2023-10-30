import axios from 'axios';
import { useState } from 'react';

function Login() {

const [email, setEmail] = useState('');
const [password, setPassword] = useState('');
const [error, setError] = useState('');
const [user, setUser] = useState(null);

const handleLoginAsync = async (email, password) => {
  try {
    const response = await axios.post('http://localhost:3000/login',
      JSON.stringify({email, password}),
      {
        headers: { 'Content-Type': 'application/json' }
      }
    );

    setUser(response.data);

    return true;
  } catch (error) {
    if (!error?.response) {
      setError('Erro ao acessar o servidor');
    } else if (error.response.status === 401) {
      setError('Usuário ou senha inválidos');
    }

    return false;
  }
};

const handleLogin = async (e) => {
  e.preventDefault();

  const success = await handleLoginAsync(email, password);

  if (success) {
    // A segunda ação só é executada se a primeira ação for bem-sucedida
    window.location.href = window.location.origin + "/logout.html";
  }
};

//const handleLogout = async (e) => {
//  e.preventDefault();
//  setUser(null);
//  setError('');
//};

return (
    <div class="box">
      <div className="login-form-wrap">
        {user == null ? (
          <div>
            <h2>Login</h2>
            <form className="login-form">
              <input
                type="email"
                name="email"
                placeholder="Email"
                required
                onChange={(e) => setEmail(e.target.value)}
              />
              <input
                type="password"
                name="password"
                placeholder="Password"
                required
                onChange={(e) => setPassword(e.target.value)}
              />
              <button
                type="submit"
                className="btn-login"
                onClick={(e) => handleLoginAsync && handleLogin(e)}
              >
                Login
              </button>
            </form>
            <p>{error}</p>
          </div>
        ) : (
          <div>
            
          </div>
        )}
      </div>
    </div>
  )};
  



export default Login;
  

//estudar https://developer.mozilla.org/pt-BR/docs/Web/API/EventTarget/addEventListener ações dos botões//

