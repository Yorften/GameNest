import React from "react";
import { Link, useNavigate } from "react-router-dom";
import Button from "../components/miscs/Button";

export default function Home() {
  const navigate = useNavigate();

  return (
    <section className="relative overflow-hidden z-10 -mb-1 bg-[#263256]">
      <div className='absolute w-full h-full bg-cover bg-no-repeat bg-fixed bg-blend-darken bg-[50%] opacity-60 bg-[url("/assets/images/bg-1.webp")]'></div>
      <div className="max-w-[1200px] relative my-0 mx-auto pt-56 px-5 pb-32">
        <div className="relative ml-0 z-10 w-[90vw] max-w-[1200px] mx-auto">
          <h1 className="text-7xl text-white mb-5 drop-shadow-2xl font-extrabold">Welcome to your platform<br /> GameNest</h1>
          <p className="text-[20px] my-5 font-medium drop-shadow-xl text-white">
            Effortlessly deploy and host your personal games. Integrate with GitHub for automatic builds, real-time
            logs, and seamless game deployment.
          </p>
          <div className="mt-8 flex space-x-4">
            <Button title="Get Started" onClick={() => navigate("/signup")} />

            <Button
              title="Learn More"
              onClick={() => {
                window.location.hash = "#about_us";
              }}
              className="border border-white text-white bg-transparent hover:!bg-white hover:text-primary"
            />
          </div>
        </div>
      </div>
    </section>
  );
}
