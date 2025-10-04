// src/components/FileUpload.js
import React, { useState } from 'react';
import { uploadFile } from '../services/api';
import './FileUpload.css';
const SUPPORTED_FORMATS = ['.txt', '.jpg', '.png', '.json', '.pdf', '.docx', '.xlsx', '.jpeg'];
const SUPPORTED_FORMATS_STRING = SUPPORTED_FORMATS.join(', ');

const FileUpload = ({ onUploadSuccess }) => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [isDragging, setIsDragging] = useState(false);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);

    };

    const handleDrop = (event) => {
        event.preventDefault();
        event.stopPropagation();
        setIsDragging(false);

        if (event.dataTransfer.files && event.dataTransfer.files.length > 0) {
            setSelectedFile(event.dataTransfer.files[0]);
            event.dataTransfer.clearData();
        }
    };

    const handleDragOver = (event) => {
        event.preventDefault();
        event.stopPropagation();
        setIsDragging(true);
    };

    const handleDragLeave = (event) => {
        event.preventDefault();
        event.stopPropagation();
        setIsDragging(false);
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            alert('Please select a file first!');
            return;
        }else if(!SUPPORTED_FORMATS.some(ext => selectedFile.name.endsWith(ext))) {
            alert('Unsupported file format');
            setSelectedFile(null);
            return;
        }
        try {
            console.log(selectedFile);
            const response = await uploadFile(selectedFile);
            if (response.ok) {
                alert('File uploaded successfully!');
                onUploadSuccess();
                setSelectedFile(null);
            } else if(response.status === 409) {
                alert('The file already exists.');
                setSelectedFile(null);
            }
            else {
                throw new Error('Upload failed');
            }
        } catch (error) {
            console.error('Error uploading file:', error);
            alert('Error uploading file.');
        }
    };

    return (
        <div className="file-upload-container"> 
            <div
                className='file-drop'
                onDragOver={handleDragOver}
                onDragLeave={handleDragLeave}
                onDrop={handleDrop}
                onClick={() => document.getElementById('fileInput').click()}
            >
                {selectedFile ? (
                    <p>Selected File: {selectedFile.name}</p>
                ) : (
                    <div style={{cursor: 'pointer'}}>
                        <p>Drag and drop a file here, or click to select a file</p>
                        <p className='supported-formats'>Supported formats: {SUPPORTED_FORMATS_STRING}</p>
                    </div>
                )}
            </div>
            <input type="file" onChange={handleFileChange} style={{ display: 'none' }} id="fileInput" />
            <button onClick={handleUpload} className='upload-button' style={{ display: 'block', marginTop: '10px' }}>
                Upload
            </button>
        </div>
    );
};

export default FileUpload;