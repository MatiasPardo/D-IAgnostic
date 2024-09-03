// src/components/Testimonials.tsx
import React from 'react';

const Testimonials: React.FC = () => {
  return (
    <section
      id="testimonials"
      style={{
        backgroundColor: 'var(--primary-color-2)',
        padding: '2rem 0',
        textAlign: 'center',
      }}
    >
      <h2 style={{ color: 'var(--primary-color-1)' }}>Qué dicen nuestros clientes</h2><br/>
      <div>
        <p style={{ color: 'var(--text-color)' }}>"Esta herramienta de inteligencia artificial ha mejorado drásticamente nuestra precisión y eficiencia en el diagnóstico."</p>
        <p style={{ color: 'var(--primary-color-1)' }}>- Dr. Álvarez</p>
      </div>
      <div>
        <p style={{ color: 'var(--text-color)' }}>"Un cambio radical para nuestro departamento de análisis por imágenes!"</p>
        <p style={{ color: 'var(--primary-color-1)' }}>- Dra. Dominguez</p>
      </div>
    </section>
  );
};

export default Testimonials;
