import { Link, useLocation, useNavigate } from "react-router-dom";
import Button from "../components/miscs/Button";
import { useEffect, useMemo } from "react";

let lastBackgroundIndex: number | null = null;

function getRandomIndexExcluding(exclude: number | null, length: number): number {
  let index: number;
  do {
    index = Math.floor(Math.random() * length);
  } while (index === exclude && length > 1);
  return index;
}

export default function Home() {
  const navigate = useNavigate();
  const location = useLocation();

  const backgrounds = [
    "/assets/images/bg-1.webp",
    "/assets/images/bg-2.webp",
    "/assets/images/bg-3.webp",
    "/assets/images/bg-4.webp",
  ];

  const backgroundIndex = useMemo(() => {
    const index = getRandomIndexExcluding(lastBackgroundIndex, backgrounds.length);
    lastBackgroundIndex = index;
    return index;
  }, [backgrounds.length]);

  const backgroundImageUrl = backgrounds[backgroundIndex];


  useEffect(() => {
    if (location.hash) {
      const id = location.hash.replace('#', '');
      const element = document.getElementById(id);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    }
  }, [location]);

  return (
    <>
      <section className="relative overflow-hidden z-10 -mb-1 bg-[#263256]">
        <div
          className="absolute w-full h-full bg-cover bg-no-repeat bg-fixed bg-blend-darken bg-[50%] opacity-60"
          style={{ backgroundImage: `url("${backgroundImageUrl}")` }}
        ></div>
        <div className="max-w-[1200px] relative my-0 mx-auto pt-56 px-5 pb-32">
          <div className="relative ml-0 z-10 w-[90vw] max-w-[1200px] mx-auto">
            <h1 className="text-7xl text-white mb-5 drop-shadow-2xl font-extrabold">
              Welcome to your platform
              <br /> GameNest
            </h1>
            <p className="text-[20px] my-5 font-medium drop-shadow-xl text-white">
              Effortlessly deploy and host your personal games. Integrate with GitHub for automatic builds, real-time
              logs, and seamless game deployment.
            </p>
            <div className="mt-8 flex space-x-4">
              <Button title="Get Started" />

              <Button
                title="Learn More"
                onClick={() => navigate("/#about_us")}
                className="border border-white text-white bg-transparent hover:!bg-white hover:text-primary"
              />
            </div>
          </div>
        </div>
      </section>
      <section id="about_us" className="py-32 pt-40 relative">
        <div className="w-full max-w-[1320px] px-4 md:px-5 lg:px-5 mx-auto">
          <div className="w-full justify-start items-center gap-12 grid lg:grid-cols-2 grid-cols-1">
            <div className="w-full justify-center items-start gap-6 grid sm:grid-cols-2 grid-cols-1 lg:order-first order-last">
              <div className="pt-24 lg:justify-center sm:justify-end justify-start items-start gap-2.5 flex">
                <img
                  className="rounded-xl object-cover h-[400px]"
                  src="/assets/images/about-1.avif"
                  alt="GameNest Platform"
                />
              </div>
              <img
                className="sm:ml-0 ml-auto rounded-xl object-cover object-[75%_75%] h-[400px]"
                src="/assets/images/about-2.webp"
                alt="GameNest Development"
              />
            </div>
            <div className="w-full flex-col justify-center lg:items-start items-center gap-10 inline-flex">
              <div className="w-full flex-col justify-center items-start gap-8 flex">
                <div className="w-full flex-col justify-start lg:items-start items-center gap-3 flex">
                  <h2 className="text-white text-4xl font-bold font-manrope leading-normal lg:text-start text-center">
                    Powering Indie Game Developers
                  </h2>
                  <p className="text-white/90 text-base font-normal leading-relaxed lg:text-start text-center">
                    GameNest is a platform designed for game developers to easily build, deploy, and showcase their
                    games. With seamless GitHub integration, automated builds, and real-time deployment logs, we empower
                    creators to focus on making great games while we handle the infrastructure.
                  </p>
                </div>
                <div className="w-full lg:justify-start justify-center items-center sm:gap-10 gap-5 inline-flex">
                  <div className="flex-col justify-start items-start inline-flex">
                    <h3 className="text-white text-4xl font-bold font-manrope leading-normal">5+</h3>
                    <h6 className="text-white/90 text-base font-normal leading-relaxed">Games Hosted</h6>
                  </div>
                  <div className="flex-col justify-start items-start inline-flex">
                    <h4 className="text-white text-4xl font-bold font-manrope leading-normal">10+</h4>
                    <h6 className="text-white/90 text-base font-normal leading-relaxed">Developers Onboarded</h6>
                  </div>
                  <div className="flex-col justify-start items-start inline-flex">
                    <h4 className="text-white text-4xl font-bold font-manrope leading-normal">1000+</h4>
                    <h6 className="text-white/90 text-base font-normal leading-relaxed">Builds Processed</h6>
                  </div>
                </div>
              </div>
              <Button title="Learn More" className="!bg-button-primary hover:bg-button-primary hover:brightness-110" />
            </div>
          </div>
        </div>
      </section>
      <section id="services" className="pb-32 pt-24">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="mb-14 text-center">
            <span className="py-1 px-4 bg-indigo-100 rounded-full text-xs font-medium text-indigo-600 text-center">
              Features
            </span>
            <h2 className="text-4xl text-center font-bold text-white py-5">Game Development, Simplified</h2>
            <p className="text-lg font-normal text-white/90 max-w-md md:max-w-2xl mx-auto">
              GameNest automates game deployment by integrating with GitHub, handling builds, and streaming real-time
              logs. Developers can focus on building games while we take care of the rest.
            </p>
          </div>
          <div className="flex justify-center items-center gap-x-5 gap-y-8 lg:gap-y-0 flex-wrap md:flex-wrap lg:flex-nowrap lg:flex-row lg:justify-between lg:gap-x-8">
            <div className="relative w-full text-center max-md:max-w-sm max-md:mx-auto group md:w-2/5 lg:w-1/4">
              <div className="bg-indigo-50 rounded-lg flex justify-center items-center mb-5 w-20 h-20 mx-auto cursor-pointer transition-all duration-500 group-hover:bg-indigo-600">
                <svg
                  className="stroke-indigo-600 transition-all duration-500 group-hover:stroke-white"
                  width="30"
                  height="30"
                  viewBox="0 0 30 30"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M10 27.5L15 25M15 25V21.25M15 25L20 27.5M8.75 14.375L12.5998 11.0064C13.1943 10.4862 14.1163 10.6411 14.5083 11.327L15.4917 13.048C15.8837 13.7339 16.8057 13.8888 17.4002 13.3686L21.25 10M2.5 2.5H27.5M26.25 2.5V13.25C26.25 17.0212 26.25 18.9069 25.0784 20.0784C23.9069 21.25 22.0212 21.25 18.25 21.25H11.75C7.97876 21.25 6.09315 21.25 4.92157 20.0784C3.75 18.9069 3.75 17.0212 3.75 13.25V2.5"
                    stroke=""
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  ></path>
                </svg>
              </div>
              <h4 className="text-lg font-medium text-white mb-3 capitalize">Seamless GitHub Integration</h4>
              <p className="text-sm font-normal text-white/90">
                Connect your repository and let GameNest handle builds and deployments automatically.
              </p>
            </div>

            <div className="relative w-full text-center max-md:max-w-sm max-md:mx-auto group md:w-2/5 lg:w-1/4">
              <div className="bg-pink-50 rounded-lg flex justify-center items-center mb-5 w-20 h-20 mx-auto cursor-pointer transition-all duration-500 group-hover:bg-pink-600">
                <svg
                  className="stroke-pink-600 transition-all duration-500 group-hover:stroke-white"
                  width="30"
                  height="30"
                  viewBox="0 0 30 30"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M2.5 7.5C2.5 4.73858 4.73858 2.5 7.5 2.5C10.2614 2.5 12.5 4.73858 12.5 7.5C12.5 10.2614 10.2614 12.5 7.5 12.5C4.73858 12.5 2.5 10.2614 2.5 7.5Z"
                    stroke=""
                    strokeWidth="2"
                  ></path>
                  <path
                    d="M2.5 22.5C2.5 20.143 2.5 18.9645 3.23223 18.2322C3.96447 17.5 5.14298 17.5 7.5 17.5C9.85702 17.5 11.0355 17.5 11.7678 18.2322C12.5 18.9645 12.5 20.143 12.5 22.5C12.5 24.857 12.5 26.0355 11.7678 26.7678C11.0355 27.5 9.85702 27.5 7.5 27.5C5.14298 27.5 3.96447 27.5 3.23223 26.7678C2.5 26.0355 2.5 24.857 2.5 22.5Z"
                    stroke=""
                    strokeWidth="2"
                  ></path>
                  <path
                    d="M17.5 7.5C17.5 5.14298 17.5 3.96447 18.2322 3.23223C18.9645 2.5 20.143 2.5 22.5 2.5C24.857 2.5 26.0355 2.5 26.7678 3.23223C27.5 3.96447 27.5 5.14298 27.5 7.5C27.5 9.85702 27.5 11.0355 26.7678 11.7678C26.0355 12.5 24.857 12.5 22.5 12.5C20.143 12.5 18.9645 12.5 18.2322 11.7678C17.5 11.0355 17.5 9.85702 17.5 7.5Z"
                    stroke=""
                    strokeWidth="2"
                  ></path>
                  <path
                    d="M17.5 22.5C17.5 19.7386 19.7386 17.5 22.5 17.5C25.2614 17.5 27.5 19.7386 27.5 22.5C27.5 25.2614 25.2614 27.5 22.5 27.5C19.7386 27.5 17.5 25.2614 17.5 22.5Z"
                    stroke=""
                    strokeWidth="2"
                  ></path>
                </svg>
              </div>
              <h4 className="text-lg font-medium text-white mb-3 capitalize">Automated Game Builds</h4>
              <p className="text-sm font-normal text-white/90">
                Every push to your repository triggers a build using Godot CLI, ensuring a smooth workflow.
              </p>
            </div>

            <div className="relative w-full text-center max-md:max-w-sm max-md:mx-auto group md:w-2/5 lg:w-1/4">
              <div className="bg-teal-50 rounded-lg flex justify-center items-center mb-5 w-20 h-20 mx-auto cursor-pointer transition-all duration-500 group-hover:bg-teal-600">
                <svg
                  className="stroke-teal-600 transition-all duration-500 group-hover:stroke-white"
                  width="30"
                  height="30"
                  viewBox="0 0 30 30"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M3.75 26.25H26.25M6.25 22.875C4.86929 22.875 3.75 21.8676 3.75 20.625V12.75C3.75 11.5074 4.86929 10.5 6.25 10.5C7.63071 10.5 8.75 11.5074 8.75 12.75V20.625C8.75 21.8676 7.63071 22.875 6.25 22.875ZM15 22.875C13.6193 22.875 12.5 21.8676 12.5 20.625V9.375C12.5 8.13236 13.6193 7.125 15 7.125C16.3807 7.125 17.5 8.13236 17.5 9.375V20.625C17.5 21.8676 16.3807 22.875 15 22.875ZM23.75 22.875C22.3693 22.875 21.25 21.8676 21.25 20.625V6C21.25 4.75736 22.3693 3.75 23.75 3.75C25.1307 3.75 26.25 4.75736 26.25 6V20.625C26.25 21.8676 25.1307 22.875 23.75 22.875Z"
                    stroke=""
                    strokeWidth="2"
                    strokeLinecap="round"
                  ></path>
                </svg>
              </div>
              <h4 className="text-lg font-medium text-white mb-3 capitalize">Real-Time Build Logs</h4>
              <p className="text-sm font-normal text-white/90">
                Get live feedback on build status and error messages with real-time streaming logs.
              </p>
            </div>

            <div className="relative w-full text-center max-md:max-w-sm max-md:mx-auto group md:w-2/5 lg:w-1/4">
              <div className="bg-orange-50 rounded-lg flex justify-center items-center mb-5 w-20 h-20 mx-auto cursor-pointer transition-all duration-500 group-hover:bg-orange-600">
                <svg
                  className="stroke-orange-600 transition-all duration-500 group-hover:stroke-white"
                  width="30"
                  height="30"
                  viewBox="0 0 30 30"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M15.4167 12.0833V21.25M5.41667 21.25V20.8333C5.41667 19.262 5.41667 18.4763 5.90482 17.9882C6.39298 17.5 7.17865 17.5 8.75 17.5H22.0833C23.6547 17.5 24.4404 17.5 24.9285 17.9882C25.4167 18.4763 25.4167 19.262 25.4167 20.8333V21.25M15.4167 9.16667C13.8453 9.16667 13.0596 9.16667 12.5715 8.67851C12.0833 8.19036 12.0833 7.40468 12.0833 5.83333C12.0833 4.26198 12.0833 3.47631 12.5715 2.98816C13.0596 2.5 13.8453 2.5 15.4167 2.5C16.988 2.5 17.7737 2.5 18.2618 2.98816C18.75 3.47631 18.75 4.26198 18.75 5.83333C18.75 7.40468 18.75 8.19036 18.2618 8.67851C17.7737 9.16667 16.988 9.16667 15.4167 9.16667ZM7.08333 25.8333C7.08333 26.7538 6.33714 27.5 5.41667 27.5C4.49619 27.5 3.75 26.7538 3.75 25.8333C3.75 24.9129 4.49619 24.1667 5.41667 24.1667C6.33714 24.1667 7.08333 24.9129 7.08333 25.8333ZM17.0833 25.8333C17.0833 26.7538 16.3371 27.5 15.4167 27.5C14.4962 27.5 13.75 26.7538 13.75 25.8333C13.75 24.9129 14.4962 24.1667 15.4167 24.1667C16.3371 24.1667 17.0833 24.9129 17.0833 25.8333ZM27.0833 25.8333C27.0833 26.7538 26.3371 27.5 25.4167 27.5C24.4962 27.5 23.75 26.7538 23.75 25.8333C23.75 24.9129 24.4962 24.1667 25.4167 24.1667C26.3371 24.1667 27.0833 24.9129 27.0833 25.8333Z"
                    stroke=""
                    strokeWidth="2"
                    strokeLinecap="round"
                  ></path>
                </svg>
              </div>
              <h4 className="text-lg font-medium text-white mb-3 capitalize">Secure Deployment</h4>
              <p className="text-sm font-normal text-white/90">
                We ensure secure management of GitHub tokens, builds, and deployments with best practices.
              </p>
            </div>
          </div>
        </div>
      </section>
      <section id="contact_us" className="py-24">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="md:flex gap-x-24 clear-left md:mb-16 mb-10">
            <div className=" md:mb-0 mb-4">
              <h2 className="text-white font-manrope text-4xl font-semibold leading-10 mb-5 md:text-left text-center">
                Get In Touch
              </h2>
              <p className="text-white/90 text-lg font-normal leading-7 mb-7 md:text-left text-center">
                Whether you have a concern or simply want to say hello, We are here to facilitate communication with
                you.
              </p>
              <div className="flex md:items-center md:justify-start justify-center">
                <Button title="Contact Us" className="rounded-3xl !px-4" />
              </div>
            </div>
            <div className="border-l-2 md:border-primary border-white px-10 py-6">
              <div className="mb-8">
                <h6 className="text-white/90 text-sm font-medium leading-5 pb-3 md:text-start text-center">
                  Email Address
                </h6>
                <h3 className="text-white text-xl font-semibold leading-8 md:text-start text-center">
                  GameNest@contact.com
                </h3>
              </div>
              <div>
                <h6 className="text-white/90 text-sm font-medium leading-5 pb-3 md:text-start text-center">
                  Phone Number
                </h6>
                <h3 className="text-white text-xl font-semibold leading-8 md:text-start text-center">470-601-1911</h3>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
