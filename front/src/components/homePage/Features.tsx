import React from 'react';
import './styles/features.css'

const Features: React.FC = () => {
  return (
    <section
      id="features"
      className="sectionStyles"
    >
      <div className="featureContainer" style={{ backgroundColor: 'var(--primary-color-1-light)' }}>
      <img 
          src="/images/medic2.jpg"
          alt="Feature 1"
          className="imageContainer"
        />
        <h2 style={{ color: 'var(--accent-color)' }}>Diagnósticos precisos</h2>
        <p>La herramienta utiliza modelos de Inteligencia Artificial entrenados con miles de casos y reportes de profesionales en el área, logrando resultados más precisos.</p>
      </div>

      <div className="featureContainer" style={{ backgroundColor: 'var(--primary-color-1-light)' }}>
      <img 
          src="/images/ai1.jpg"
          alt="Feature 2"
          className="imageContainer"
        />
        <h2 style={{ color: 'var(--accent-color)' }}>Análisis veloz</h2>
        <p>Generación de resultados del análisis veloces, lo que garantiza intervenciones médicas oportunas.</p>
      </div>

      <div className="featureContainer" style={{ backgroundColor: 'var(--primary-color-1-light)' }}>
      <img 
          src="/images/medic3.png"
          alt="Feature 3"
          className="imageContainer"
        />
        <h2 style={{ color: 'var(--accent-color)' }}>Integración fácil</h2>
        <p>La aplicación es flexible y está preparada para integrarse con otros sistemas médicos.</p>
      </div>
    </section>
  );
};

export default Features;
