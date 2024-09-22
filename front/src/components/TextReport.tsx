import React from 'react';

const formatBoldedWords = (text: string) => {
    return text
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')// Bold formatting
        .replace(/\n\n/g, '<br />'); 
};

const TextReport: React.FC<{ report: string }> = ({ report }) => {
    const formattedReport = formatBoldedWords(report);

    return (
        <>
            <p className="text-muted" dangerouslySetInnerHTML={{ __html: formattedReport }} />
        </>
    );
};

export default TextReport;
