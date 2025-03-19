import React from "react";
import { Avatar, Dropdown, Navbar } from "flowbite-react";
import { Link } from "react-router-dom";

export default function NavBar() {
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
  const scrollToContact = () => {
    const joinUsElement = document.getElementById("contact_us");
    if (joinUsElement) {
      joinUsElement.scrollIntoView({ behavior: "smooth" });
    }
  };

  return (
    <Navbar className="shadow-xl fixed z-20 w-full bg-transparent backdrop-blur-sm h-[8vh] " fluid>
      <Navbar.Brand as={Link} to="/">
        <img src="/assets/GameNest_logo.png" className="mr-3 h-12" alt="Flowbite React Logo" />
        <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white text-white">
          GameNest
        </span>
      </Navbar.Brand>
      <div className="flex md:order-2">
        <Dropdown
          arrowIcon={false}
          inline
          label={<Avatar alt="User settings" img="/assets/default_user.png" rounded />}
        >
          <Dropdown.Header>
            <span className="block text-sm">Bonnie Green</span>
            <span className="block truncate text-sm font-medium">name@flowbite.com</span>
          </Dropdown.Header>
          <Dropdown.Item>Dashboard</Dropdown.Item>
          <Dropdown.Item>Settings</Dropdown.Item>
          <Dropdown.Item>Earnings</Dropdown.Item>
          <Dropdown.Divider />
          <Dropdown.Item>Sign out</Dropdown.Item>
        </Dropdown>
        <Navbar.Toggle />
      </div>
      <Navbar.Collapse className="mr-24">
        <Navbar.Link as={Link} to="/" className="cursor-pointer text-white hover:text-[#5696c3] font-medium">
          Home
        </Navbar.Link>
        <Navbar.Link onClick={scrollToAboutUs} className="cursor-pointer text-white hover:text-[#5696c3] font-medium">
          About
        </Navbar.Link>
        <Navbar.Link onClick={scrollToServices} className="cursor-pointer text-white hover:text-[#5696c3] font-medium">
          Services
        </Navbar.Link>
        <Navbar.Link onClick={scrollToContact} className="cursor-pointer text-white hover:text-[#5696c3] font-medium">
          Contact
        </Navbar.Link>
      </Navbar.Collapse>
    </Navbar>
  );
}
