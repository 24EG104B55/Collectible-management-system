import React, { useState } from 'react';
import { AuthService } from './api';

export default function LoginPage({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await AuthService.login(username, password);
      onLogin(res.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid username or password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#f8fafc',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '24px'
    }}>
      <div style={{
        backgroundColor: '#ffffff',
        border: '1px solid #e2e8f0',
        borderRadius: '16px',
        padding: '48px 40px',
        width: '100%',
        maxWidth: '420px',
        boxShadow: '0 4px 24px rgba(0,0,0,0.06)'
      }}>
        {/* Logo */}
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{
            width: '52px',
            height: '52px',
            borderRadius: '14px',
            background: 'linear-gradient(135deg, #0284c7, #0369a1)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            fontWeight: 800,
            fontSize: '1.4rem',
            margin: '0 auto 16px auto',
            boxShadow: '0 4px 12px rgba(2,132,199,0.3)'
          }}>
            S
          </div>
          <h1 style={{ fontSize: '1.6rem', color: '#0f172a', marginBottom: '6px' }}>Store</h1>
          <p style={{ fontSize: '0.85rem', color: '#64748b' }}>Collectible Items Management System</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '16px' }}>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: '#334155', marginBottom: '6px' }}>
              Username
            </label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter your username"
              required
              style={{ width: '100%' }}
            />
          </div>

          <div style={{ marginBottom: '24px' }}>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: '#334155', marginBottom: '6px' }}>
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              required
              style={{ width: '100%' }}
            />
          </div>

          {error && (
            <div style={{
              padding: '10px 14px',
              backgroundColor: '#fef2f2',
              border: '1px solid #fecdd3',
              borderRadius: '8px',
              color: '#be123c',
              fontSize: '0.82rem',
              marginBottom: '16px'
            }}>
              ⚠️ {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="btn-primary"
            style={{ width: '100%', padding: '12px', fontSize: '0.95rem' }}
          >
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <div style={{
          marginTop: '28px',
          paddingTop: '20px',
          borderTop: '1px solid #f1f5f9',
          fontSize: '0.75rem',
          color: '#94a3b8'
        }}>
          <div style={{ fontWeight: 600, color: '#64748b', marginBottom: '8px' }}>Demo Accounts</div>
          <div style={{ display: 'grid', gap: '4px' }}>
            {[
              ['admin', 'admin123', 'Admin'],
              ['vintage_vault', 'seller123', 'Seller'],
              ['collector_dan', 'buyer123', 'Buyer'],
            ].map(([u, p, role]) => (
              <div key={u} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0' }}>
                <span><strong style={{ color: '#475569' }}>{u}</strong> / {p}</span>
                <span style={{
                  fontSize: '0.7rem',
                  backgroundColor: '#f1f5f9',
                  color: '#475569',
                  padding: '1px 8px',
                  borderRadius: '999px',
                  fontWeight: 600
                }}>{role}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
