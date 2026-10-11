import React, { useState, useEffect } from 'react';
import { AdminService } from './api';

export default function AdminPanel({ onClose }) {
  const [stats, setStats] = useState(null);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadAdminData = async () => {
    try {
      setLoading(true);
      const [statsRes, usersRes] = await Promise.all([
        AdminService.getStats(),
        AdminService.getUsers()
      ]);
      setStats(statsRes.data);
      setUsers(usersRes.data || []);
    } catch (err) {
      console.error('Failed to load admin stats:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAdminData();
  }, []);

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()} style={{ maxWidth: '900px' }}>
        <div style={{
          padding: '18px 24px',
          borderBottom: '1px solid #e2e8f0',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          backgroundColor: '#ffffff'
        }}>
          <div>
            <h3 style={{ margin: 0, fontSize: '1.2rem', color: '#0f172a' }}>🛠️ Administrator Platform Console</h3>
            <p style={{ margin: '4px 0 0 0', fontSize: '0.78rem', color: '#64748b' }}>
              System metrics, user registry, and inventory threshold monitoring
            </p>
          </div>
          <button onClick={onClose} style={{ background: '#f1f5f9', color: '#64748b', fontSize: '1.2rem', padding: '4px 10px', borderRadius: '6px' }}>
            ✕
          </button>
        </div>

        <div style={{ padding: '24px', backgroundColor: '#ffffff' }}>
          {loading ? (
            <div style={{ textAlign: 'center', padding: '40px', color: '#64748b' }}>Loading platform telemetry...</div>
          ) : (
            <>
              {/* Stat Cards */}
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '16px', marginBottom: '24px' }}>
                <div style={{ padding: '16px', backgroundColor: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: '10px' }}>
                  <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>Catalog Items</div>
                  <div style={{ fontSize: '1.6rem', fontWeight: 800, color: '#0284c7' }}>{stats?.totalItems || 0}</div>
                </div>
                <div style={{ padding: '16px', backgroundColor: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: '10px' }}>
                  <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>Active Auctions & Sales</div>
                  <div style={{ fontSize: '1.6rem', fontWeight: 800, color: '#be123c' }}>{stats?.activeListings || 0}</div>
                </div>
                <div style={{ padding: '16px', backgroundColor: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: '10px' }}>
                  <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>Total Bids Placed</div>
                  <div style={{ fontSize: '1.6rem', fontWeight: 800, color: '#059669' }}>{stats?.totalBids || 0}</div>
                </div>
                <div style={{ padding: '16px', backgroundColor: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: '10px' }}>
                  <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>Active Market Volume</div>
                  <div style={{ fontSize: '1.6rem', fontWeight: 800, color: '#d97706' }}>${stats?.totalTradingVolume?.toFixed(2) || '0.00'}</div>
                </div>
              </div>

              {/* Users Table */}
              <div style={{ backgroundColor: '#ffffff', border: '1px solid #e2e8f0', borderRadius: '10px', overflow: 'hidden' }}>
                <div style={{ padding: '12px 18px', borderBottom: '1px solid #e2e8f0', fontWeight: 700, fontSize: '0.9rem', color: '#0f172a', backgroundColor: '#f8fafc' }}>
                  Platform User Registry ({users.length})
                </div>
                <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.85rem' }}>
                  <thead>
                    <tr style={{ backgroundColor: '#f8fafc', color: '#64748b', borderBottom: '1px solid #e2e8f0' }}>
                      <th style={{ padding: '10px 16px', textAlign: 'left' }}>Username</th>
                      <th style={{ padding: '10px 16px', textAlign: 'left' }}>Full Name</th>
                      <th style={{ padding: '10px 16px', textAlign: 'left' }}>Email</th>
                      <th style={{ padding: '10px 16px', textAlign: 'left' }}>Role</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map(u => (
                      <tr key={u.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                        <td style={{ padding: '10px 16px', color: '#0f172a', fontWeight: 600 }}>{u.username}</td>
                        <td style={{ padding: '10px 16px', color: '#475569' }}>{u.fullName || '-'}</td>
                        <td style={{ padding: '10px 16px', color: '#64748b' }}>{u.email}</td>
                        <td style={{ padding: '10px 16px' }}>
                          <span style={{
                            fontSize: '0.72rem',
                            padding: '2px 8px',
                            borderRadius: '999px',
                            backgroundColor: u.role === 'ADMIN' ? '#fee2e2' : u.role === 'SELLER' ? '#e0f2fe' : '#dcfce7',
                            color: u.role === 'ADMIN' ? '#b91c1c' : u.role === 'SELLER' ? '#0369a1' : '#15803d',
                            fontWeight: 700
                          }}>
                            {u.role}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
