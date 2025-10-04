// src/App.js
import React, { useState, useEffect } from 'react';
import FileList from './components/FileList';
import FileUpload from './components/FileUpload';
import { getFiles } from './services/api';
import './App.css';

function App() {
    const [files, setFiles] = useState([]);
    const fetchFiles = async () => {
        try {
            const response = await getFiles();
            const data = await response.json();
            setFiles(data);
        } catch (error) {
            console.error("Error fetching files:", error);
        }
    };

    useEffect(() => {
        fetchFiles();
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <h2>Dropbox - File Manager</h2>
                <FileUpload onUploadSuccess={fetchFiles} />
                <FileList files={files} />
            </header>
        </div>
    );
}

export default App;