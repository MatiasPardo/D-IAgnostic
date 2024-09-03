import React from 'react';

const Footer: React.FC = () => {
  return (
    <footer
      style={{
        backgroundColor: 'var(--primary-color-1)',
        color: 'white',
        padding: '1rem',
        textAlign: 'center',
      }}
    >
      <p>&copy; 2024 D-IAgnostic. Derechos Reservados.</p>
    </footer>
  );
};

export default Footer;