import React from "react"
import { Link, useLocation } from "react-router-dom"
import { FaHome } from "react-icons/fa"

const Breadcrumbs: React.FC = () => {
  const { pathname } = useLocation()

  const segments = pathname
    .split("/")
    .filter(segment => segment !== "" && !segment.match(/^[a-z]{2}$/))

  if (segments.length === 0) {
    return null
  }

  const breadcrumbs = segments.map((segment, index) => {
    const href = "/" + segments.slice(0, index + 1).join("/")
    const text = decodeURIComponent(segment.replace(/-/g, " "))
    const isLast = index === segments.length - 1
    return (
      <li key={index} className="inline-flex items-center">
        <Link to={href} className="hover:text-secondary-2">
          {text.charAt(0).toUpperCase() + text.slice(1)}
        </Link>
        {!isLast && (
          <span className="mx-4 h-auto text-gray-400 font-medium">/</span>
        )}
      </li>
    )
  })

  return (
    <div className="py-4 pt-6 pl-6 bg-gray-100">
      <ul className="flex items-center font-medium text-primary-2">
        <li className="inline-flex items-center">
          <Link to="/" className="hover:text-secondary-2">
            <FaHome className="h-6 w-6" />
          </Link>
          <span className="mx-4 h-auto text-gray-400 font-medium">/</span>
        </li>
        {breadcrumbs}
      </ul>
    </div>
  )
}

export default Breadcrumbs
