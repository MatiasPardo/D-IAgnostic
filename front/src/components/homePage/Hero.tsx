import React from 'react';
import { NavLink } from 'react-router-dom';

const Hero: React.FC = () => {
  return (
    <section
      style={{
        position: 'relative',
        color: 'var(--primary-color-1)',
        padding: '6rem 1rem 2rem',
        textAlign: 'center',
        height: '500px',
        overflow: 'hidden',
      }}
    >
      {/* Div para la imagen de fondo */}
      <div
        style={{
          backgroundImage: "url('/images/medic1.jpg')",
          opacity: 0.5,
          backgroundSize: 'cover',
          backgroundRepeat: 'no-repeat',
          backgroundPosition: 'center',
          backgroundAttachment: 'fixed',
          position: 'absolute',
          top: '4rem',
          left: 0,
          width: '100%',
          height: '100%',
          zIndex: -2,
        }}
      ></div>

      {/* Recuadro semitransparente detrás del texto */}
      <div style={{
          backgroundColor: 'rgba(255, 255, 255, 0.6)',
          padding: '1rem',
          borderRadius: '8px',
          position: 'relative',
          display: 'inline-block',
          zIndex: 1,
        }}
      >
        <h1>Análisis de Tomografía Computarizada impulsado por IA</h1>
        <p style={{color:'black'}}>Revolucionando el diagnóstico médico por imágenes con inteligencia artificial de vanguardia.</p>
        <br/>
        <NavLink className="btn" style={{ backgroundColor: 'var(--background-color)' }} to="/orders">Comenzar</NavLink>
      </div>
    </section>
  );
};

export default Hero;