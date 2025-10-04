// src/api.js
const API_BASE_URL = 'http://localhost:8080/api';

export const uploadFile = (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return fetch(`${API_BASE_URL}/upload`, {
        method: 'POST',
        body: formData,
    });
};

export const getFiles = () => {
    return fetch(`${API_BASE_URL}/files`);
};

export const getFileDownloadUrl = (fileId) => {
    return `${API_BASE_URL}/files/${fileId}`;
};