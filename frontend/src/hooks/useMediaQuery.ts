import { useState, useEffect } from "react";

function useMediaQuery(query: string): boolean {
  const [matches, setMatches] = useState(false);

  useEffect(() => {
    const mediaQueryList = window.matchMedia(query);
    setMatches(mediaQueryList.matches);
    console.log(matches);
    
    const listener = (event: MediaQueryListEvent) => {
      console.log("matches=" + event.matches);
      
      setMatches(event.matches);
    };

    if (mediaQueryList.addEventListener) {
      mediaQueryList.addEventListener("change", listener);
    } else {
      mediaQueryList.addListener(listener); // Deprecated but needed for some older browsers
    }

    return () => {
      if (mediaQueryList.removeEventListener) {
        mediaQueryList.removeEventListener("change", listener);
      } else {
        mediaQueryList.removeListener(listener); // Deprecated but needed for some older browsers
      }
    };
  }, [query]);

  return matches;
}

export default useMediaQuery;
