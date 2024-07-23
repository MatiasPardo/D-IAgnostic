import React from 'react';

interface AppLogoProps {
    size: string;
    transparent?: boolean,
}

const AppLogo: React.FC<AppLogoProps> = ({ size = "250", transparent = false }) => {
  const logoStyle = {
    width: `${size}px`,
  };
 
  const logoSrc = transparent 
    ? "/images/logo-transparent.png" 
    : "/images/logo.jpg";

  return (
    <img
        src={logoSrc}
        alt="App Logo"
        style={logoStyle}
    />
    );
};

export default AppLogo;
