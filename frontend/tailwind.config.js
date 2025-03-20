const flowbite = require("flowbite-react/tailwind");

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}", flowbite.content()],
  theme: {
    extend: {
      colors: {
        primary: "#4c9cdb",
        secondary: "#3b4856",
        "gray-primary": "#25282b",
        "gray-secondary": "#38343c",
        "button-primary": "#333639",
        "dashboard-primary": "#0a0b0b",
        "dashboard-secondary": "#0e0f0f",
        "border-primary": "#2c2d2c",
        white: "#fffefe",
      },
      borderColor: {
        primary: "#2c2d2c",
      },
      fontFamily: {
        sans: ["Poppins", "sans-serif"],
      },
    },
  },
  plugins: [flowbite.plugin()],
};
