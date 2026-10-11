import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

export const AuthService = {
  login: (username, password) => api.post('/auth/login', { username, password }),
};

export const ItemService = {
  create: (itemData) => api.post('/items', itemData),
};

export const ListingService = {
  getAll: () => api.get('/listings'),
  getById: (id) => api.get(`/listings/${id}`),
  create: (listingData) => api.post('/listings', listingData),
  placeBid: (listingId, bidderId, bidAmount) =>
    api.post(`/listings/${listingId}/bid`, { bidderId, bidAmount }),
  buyNow: (listingId, buyerId) =>
    api.post(`/listings/${listingId}/buy?buyerId=${buyerId}`),
  getPriceHistory: (listingId) => api.get(`/listings/${listingId}/history`),
  getBids: (listingId) => api.get(`/listings/${listingId}/bids`),
  getCompareSellers: (listingId) => api.get(`/listings/${listingId}/compare`),
};

export const AdminService = {
  getStats: () => api.get('/admin/stats'),
  getUsers: () => api.get('/admin/users'),
};
