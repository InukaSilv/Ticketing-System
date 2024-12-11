import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API_BASE_URL from '../apiconfig';

function SignUp() {
   // State declarations
   const [username, setUsername] = useState('');
   const [password, setPassword] = useState('');
   const [error, setError] = useState('');
 
  const navigate = useNavigate();

  const handleSignUp = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch(`${API_BASE_URL}/user/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }), // Fixed shorthand property usage
      });

      if (response.ok) {
        alert('Signup Successful!');
        navigate('/login');
      } else {
        setError('Invalid username or password');
      }
    } catch (err) {
      console.error(err);
      setError('An error occurred while signing up.');
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100 px-6 py-12 lg:px-8">
      <div className="w-full max-w-md space-y-8">
        <h2 className="text-center text-2xl font-bold tracking-tight text-gray-900">
          Sign up for an account
        </h2>
        {error && <p className="text-red-500 text-center">{error}</p>}
        <form onSubmit={handleSignUp} className="mt-8 space-y-6 bg-white p-8 rounded-md shadow-md">
          <div>
            <label htmlFor="username" className="block text-sm/6 font-medium text-gray-700">
              Username
            </label>
            <div className="mt-2">
              <input
                id="username"
                name="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                className="block w-full rounded-md border border-gray-300 px-3 py-1.5 text-base text-gray-900 shadow-sm placeholder-gray-400 focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm/6"
              />
            </div>
          </div>

          <div>
            <label htmlFor="password" className="block text-sm/6 font-medium text-gray-700">
              Password
            </label>
            <div className="mt-2">
              <input
                id="password"
                name="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="block w-full rounded-md border border-gray-300 px-3 py-1.5 text-base text-gray-900 shadow-sm placeholder-gray-400 focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm/6"
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              className="flex w-full justify-center rounded-md bg-indigo-600 px-4 py-1.5 text-sm/6 font-semibold text-white shadow-sm hover:bg-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
            >
              Sign up
            </button>
          </div>
        </form>

        <p className="mt-6 text-center text-sm/6 text-gray-600">
          Already have an account?{' '}
          <button
            onClick={() => navigate('/')}
            className="font-medium text-indigo-600 hover:text-indigo-500"
          >
            Login
          </button>
        </p>
      </div>
    </div>
  );
}

export default SignUp;
