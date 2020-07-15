import React, {useEffect, useLayoutEffect, useRef, useState} from 'react';
import {Dots} from '@ui/icons';

type IDotMenuItem = {
  label: string
  onClick: any
}

export const DotMenu = (props: any) => {
  const [display, show] = useState(false);
  const [classes, setClasses] = useState([]);
  const dropdownRef = useRef(null);

  useLayoutEffect(() => {
    if (dropdownRef.current) placeDropDown(dropdownRef);
  })

  useEffect(() => {
    function handleClickOutside(event: any) {
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

    const newClasses = [];

    if (out.bottom) newClasses.push('bottom-0');
    if (out.right) newClasses.push('right-0');
    if (newClasses.length > 0) setClasses(newClasses);
  }

  return (
    <div className="relative">
      <button className="block">
        <Dots className="h-7 w-7 fill-grey" onClick={() => show(!display)}/>
      </button>
      {
        display &&
        <div
          className={`absolute bg-white border border-grey-25 mt-2 rounded-lg shadow-xl min-w-10 overflow-hidden ${classes.join(' ')}`}
          ref={dropdownRef}>
          {...props.children}
        </div>
      }
    </div>
  );
}

export const DotMenuItem = ({label, onClick}: IDotMenuItem) => (
  <a onClick={onClick}
     className="dot-menu-item block px-4 py-2 text-black bg-white hover:bg-primary-100 hover:text-white transition ease-in-out duration-300 cursor-pointer">
    {label}
  </a>
)
