import React, { Component } from "react"
import { Avatar, Dropdown, Navbar } from "flowbite-react"
import { Link } from "react-router-dom"

type Props = {}

type State = {}

export default class navbar extends Component<Props, State> {
  state = {}

  render() {
    return (
      <Navbar fluid rounded>
        <Navbar.Brand as={Link} to="/">
          <img
            src="/public/assets/GameNest_logo.png"
            className="mr-3 h-12"
            alt="Flowbite React Logo"
          />
          <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white">
            GameNest
          </span>
        </Navbar.Brand>
        <div className="flex md:order-2">
          <Dropdown
            arrowIcon={false}
            inline
            label={
              <Avatar
                alt="User settings"
                img="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
                rounded
              />
            }
          >
            <Dropdown.Header>
              <span className="block text-sm">Bonnie Green</span>
              <span className="block truncate text-sm font-medium">
                name@flowbite.com
              </span>
            </Dropdown.Header>
            <Dropdown.Item>Dashboard</Dropdown.Item>
            <Dropdown.Item>Settings</Dropdown.Item>
            <Dropdown.Item>Earnings</Dropdown.Item>
            <Dropdown.Divider />
            <Dropdown.Item>Sign out</Dropdown.Item>
          </Dropdown>
          <Navbar.Toggle />
        </div>
        <Navbar.Collapse>
          <Navbar.Link as={Link} to="/" active>
            Home
          </Navbar.Link>
          <Navbar.Link as={Link} to="/about">About</Navbar.Link>
          <Navbar.Link as={Link} to="#">Services</Navbar.Link>
          <Navbar.Link as={Link} to="#">Pricing</Navbar.Link>
          <Navbar.Link as={Link} to="#">Contact</Navbar.Link>
        </Navbar.Collapse>
      </Navbar>
    )
  }
}
