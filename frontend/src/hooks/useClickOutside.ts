import React, { useEffect } from "react";

/**
 * Utility hook to detect clicks outside a specified element.
 * @template T - The type of the HTML element being referred to. Must extend HTMLElement.
 * @param {React.RefObject<T>} ref - The React ref object attached to the element to monitor.
 * @param {() => void} callback - The function to call when a click outside the element is detected.
 */
export const useClickOutside = <T extends HTMLElement>(ref: React.RefObject<T>, callback: () => void): void => {
  useEffect(() => {
    /**
     * Handles the click event on the document.
     * Checks if the clicked target is outside the referenced element.
     * @param {MouseEvent} event - The mouse event object.
     */
    const handleClickOutside = (event: MouseEvent) => {
      if (ref.current && !ref.current.contains(event.target as Node)) {
        callback();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [ref, callback]);
};
