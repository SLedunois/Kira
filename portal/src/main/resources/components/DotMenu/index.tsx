import React, {useEffect, useLayoutEffect, useRef, useState} from 'react';
import {Dots} from '@ui/icons';

export const DotMenu = (props: any) => {
  const [display, show] = useState(false);
  const dropdownRef = useRef(null);

  useLayoutEffect(() => {
    if (dropdownRef.current) placeDropDown(dropdownRef);
  })

  useEffect(() => {
    function handleClickOutside(event: Event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        show(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      // Unbind the event listener on clean up
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [dropdownRef])

  function placeDropDown(elem: React.MutableRefObject<any>) {

    // Get element's bounding
    const bounding = elem.current.getBoundingClientRect();

    // Check if it's out of the viewport on each side
    const out = {
      bottom: false,
      right: false
    };
    out.bottom = bounding.bottom > (window.innerHeight || document.documentElement.clientHeight);
    out.right = bounding.right > (window.innerWidth || document.documentElement.clientWidth);

    if (out.bottom) dropdownRef.current.style.bottom = `${bounding.bottom - (window.innerHeight || document.documentElement.clientHeight)}px`
    if (out.right) dropdownRef.current.style.right = `${bounding.right - (window.innerWidth || document.documentElement.clientWidth)}px`
  }

  return (
    <div>
      <button className="block">
        <Dots className="h-7 w-7 fill-grey" onClick={() => show(!display)}/>
      </button>
      {
        display &&
        <div className="absolute bg-white border border-grey-25 mt-2 rounded-lg shadow-xl w-48" ref={dropdownRef}>
          <a
            className="block px-4 py-2 text-black bg-white hover:bg-primary-100 hover:text-white rounded-t-lg transition ease-in-out duration-300 cursor-pointer">
            Account settings
          </a>
          <a
            className="block px-4 py-2 text-black bg-white hover:bg-primary-100 hover:text-white transition ease-in-out duration-300 cursor-pointer">
            Support
          </a>
          <a
            className="block px-4 py-2 text-black bg-white hover:bg-primary-100 hover:text-white rounded-b-lg transition ease-in-out duration-300 cursor-pointer">
            Sign out
          </a>
        </div>
      }
    </div>
  );
}
