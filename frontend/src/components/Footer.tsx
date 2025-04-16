import { Navbar } from "flowbite-react";
import { Link } from "react-router-dom";

type Props = {};

export default function Footer({ }: Props) {
  const scrollToAboutUs = () => {
    const joinUsElement = document.getElementById("about_us");
    if (joinUsElement) {
      joinUsElement.scrollIntoView({ behavior: "smooth" });
    }
  };
  const scrollToServices = () => {
    const joinUsElement = document.getElementById("services");
    if (joinUsElement) {
      joinUsElement.scrollIntoView({ behavior: "smooth" });
    }
  };

  return (
    <footer className="w-full bg-[#111]">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        {/* <!--Grid--> */}
        <div className="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-6 gap-3 gap-y-8 md:gap-8 py-10 max-w-sm mx-auto sm:max-w-3xl lg:max-w-full">
          <div className="col-span-full mb-10 lg:col-span-2 lg:mb-0">
            <Link to="/" className="flex items-center">
              <img src="/assets/GameNest_logo.png" className="mr-3 h-12" alt="GameNest Logo" />
              <span className="self-center whitespace-nowrap text-xl font-semibold text-white">GameNest</span>
            </Link>

            <p className="py-8 text-sm text-[#898989] lg:max-w-xs text-center lg:text-left">
              GameNest is your all-in-one platform for hosting, building, and deploying indie games. Join us in
              revolutionizing game development!
            </p>
          </div>
          {/* <!--End Col--> */}
          <div className="lg:mx-auto text-left">
            <h4 className="text-lg text-white font-medium mb-7">GameNest</h4>
            <ul className="text-sm transition-all duration-500">
              <li className="mb-6">
                <Link to="/" className="text-[#898989] hover:text-primary">
                  Home
                </Link>
              </li>
              <li className="mb-6">
                <p onClick={scrollToAboutUs} className="text-[#898989] cursor-pointer hover:text-primary">
                  About
                </p>
              </li>
              <li>
                <p onClick={scrollToServices} className="text-[#898989] hover:text-primary">
                  Features
                </p>
              </li>
            </ul>
          </div>
          {/* <!--End Col--> */}
          <div className="lg:mx-auto text-left">
            <h4 className="text-lg text-white font-medium mb-7">Developers</h4>
            <ul className="text-sm transition-all duration-500">
              <li className="mb-6">
                <Link to="/github-integration" className="text-[#898989] hover:text-primary">
                  GitHub Integration
                </Link>
              </li>
              <li className="mb-6">
                <Link to="/api-docs" className="text-[#898989] hover:text-primary">
                  API Documentation
                </Link>
              </li>
              <li>
                <Link to="/build-deploy" className="text-[#898989] hover:text-primary">
                  Build & Deploy
                </Link>
              </li>
            </ul>
          </div>
          {/* <!--End Col--> */}
          <div className="lg:mx-auto text-left">
            <h4 className="text-lg text-white font-medium mb-7">Support</h4>
            <ul className="text-sm transition-all duration-500">
              <li className="mb-6">
                <Link to="/support" className="text-[#898989] hover:text-primary">
                  Customer Support
                </Link>
              </li>
              <li className="mb-6">
                <Link to="/terms" className="text-[#898989] hover:text-primary">
                  Terms & Conditions
                </Link>
              </li>
              <li>
                <Link to="/privacy" className="text-[#898989] hover:text-primary">
                  Privacy Policy
                </Link>
              </li>
            </ul>
          </div>
          {/* <!--End Col--> */}
          <div className="lg:mx-auto text-left">
            <h4 className="text-lg text-white font-medium mb-7">Stay Updated</h4>
            <p className="text-sm text-white/80 leading-6 mb-7">Subscribe to get the latest updates from GameNest.</p>
            <a
              href="#"
              className="flex items-center justify-center gap-2 border border-[#4c9cdb] rounded-full py-3 px-6 w-fit lg:mx-0 text-sm text-primary font-semibold transition-all duration-500 hover:bg-indigo-50"
            >
              Subscribe
              <svg width="15" height="12" viewBox="0 0 15 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M1.25 6L13.25 6M9.5 10.5L13.4697 6.53033C13.7197 6.28033 13.8447 6.15533 13.8447 6C13.8447 5.84467 13.7197 5.71967 13.4697 5.46967L9.5 1.5"
                  stroke="#4c9cdb"
                  strokeWidth="1.8"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </a>
          </div>
        </div>
        {/* <!--Grid--> */}
        <div className="py-7 border-t border-gray-200">
          <div className="flex items-center justify-center flex-col lg:justify-between lg:flex-row">
            <span className="text-sm text-gray-500 ">
              © <Link to="/">GameNest</Link> 2025, All rights reserved.
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
}
