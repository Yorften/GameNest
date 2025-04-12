import React, { useEffect, useRef } from 'react';

interface BuildLogRowDetailProps {
    logs: string | undefined;
}

export default function BuildLogRowDetail({ logs }: BuildLogRowDetailProps) {
    const preRef = useRef<HTMLPreElement>(null);

    useEffect(() => {
        const preElement = preRef.current;
        if (preElement) {
            preElement.scrollTop = preElement.scrollHeight;
        }
    }, [logs]);

    return (
        <div>
            <pre
                ref={preRef}
                style={{
                    maxHeight: '310px',
                    overflowY: 'auto',
                    whiteSpace: 'pre-wrap',
                    wordBreak: 'break-word',
                    padding: '8px',
                    background: '#111',
                    border: '1px solid #333',
                    borderRadius: '4px',
                }}
            >
                {logs || 'No logs available.'}
            </pre>
        </div>
    );

}