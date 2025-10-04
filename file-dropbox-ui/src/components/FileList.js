// src/components/FileList.js
import React from 'react';
import { getFileDownloadUrl } from '../services/api';
import './FileList.css';

const FileList = ({ files }) => {
    return files.length > 0 ? (
        <div className="file-list-container">
            <h2 className="file-list-title">Uploaded Files</h2>
            <ul className="file-list">
                {files.map((file) => (
                    <li key={file.id} className="file-list-item">
                        <a 
                            href={getFileDownloadUrl(file.id)} 
                            target="_blank" 
                            rel="noopener noreferrer"
                            className="file-link"
                        >
                            {file.fileName}
                        </a>
                        <span className="file-size"> ({(file.size / 1024).toFixed(2)} KB)</span>
                    </li>
                ))}
            </ul>
        </div>
    ) : (
        <p className="no-files-message">No files uploaded yet.</p>
    );
};

export default FileList;