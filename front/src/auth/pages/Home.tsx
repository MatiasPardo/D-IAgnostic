import React, { useState, useEffect } from 'react';
import { findAnalytics } from '../../services/AnalyticsClient';

import Hero from '../../components/homePage/Hero';
import Features from '../../components/homePage/Features';
import Testimonials from '../../components/homePage/Testimonials';
import Footer from '../../components/homePage/Footer';
import './home.css';


export const Home = () => {
  const [ordersQuantity, setValue1] = useState(0);
  const [usersQuantity, setValue2] = useState(0);

  useEffect(() => {
    let maxValueOrdersQuantity = 0;
    let maxValueUsersQuantity = 0;

    findAnalytics()
      .then((r) => {
        maxValueOrdersQuantity = r.ordersQuantity;
        maxValueUsersQuantity = r.usersQuantity;
      })
      .catch((e) => {
      })
      .finally(() => {
        const interval1 = setInterval(() => {
          if (ordersQuantity < maxValueOrdersQuantity) {
            setValue1((prevValue) => Math.min(prevValue + 1, maxValueOrdersQuantity));
          }
        }, 100);

        const interval2 = setInterval(() => {
          if (usersQuantity < maxValueUsersQuantity) {
            setValue2((prevValue) => Math.min(prevValue + 1, maxValueUsersQuantity));
          }
        }, 100);

        return () => {
          clearInterval(interval1);
          clearInterval(interval2);
        };
      });
  }, [ordersQuantity, usersQuantity]);


    return (
    //     <div className="container mt-5">
    //     <div className="row">
    //     <div className="col">
    //         <h1>Bienvenido a la página de inicio</h1>
    // <p>Esta es la página de inicio de mi aplicación.</p>
    // </div>
    // </div>


    // <div className="container mt-4">
    //   <div className="row">
    //     <div className="col-md-6">
    //       <div className="card bg-primary text-white">
    //         <div className="card-body">
    //           <i className="bi bi-person-circle fs-3"></i>
    //           <h2 className="card-title">{ordersQuantity}</h2>
    //           <p className="card-text">Cantidad de pedidos</p>
    //         </div>
    //       </div>
    //     </div>
    //     <div className="col-md-6">
    //       <div className="card bg-success text-white">
    //         <div className="card-body">
    //           <i className="bi bi-hamburger fs-3"></i>
    //           <h2 className="card-title">{usersQuantity}</h2>
    //           <p className="card-text">Cantidad de usuarios</p>
    //         </div>
    //       </div>
    //     </div>
    //   </div>
    // </div>
    // </div>

      <div className="container">
        <Hero />
        <Features />
        {/* <div
        style={{
          backgroundImage: "url('/images/medic1.jpg')",
          height: '500px',
          opacity: '50%',
          backgroundSize: 'cover',         // Escala la imagen para cubrir el contenedor
          backgroundRepeat: 'no-repeat',   // Evita que la imagen se repita
          backgroundPosition: 'center',    // Centra la imagen en el contenedor
          backgroundAttachment: 'fixed',   // Efecto parallax
        }}
      ></div> */}
        <Testimonials /><br/>
        <Footer />
      </div>

);
};

