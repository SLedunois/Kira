import React, {useEffect, useRef} from 'react';

type IClickOutsideHandler = {
  onClickOutside: Function,
  children: any
  className?: string,
}

export const ClickOutsideHandler = (props: IClickOutsideHandler) => {
  const ref = useRef(null);

  useEffect(() => {
    function handleClickOutside(event: any) {
      if (ref.current && !ref.current.contains(event.target)) {
        props.onClickOutside();
      }
    }

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      // Unbind the event listener on clean up
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [ref])

  return (
    <div className={props.className} ref={ref}>
      {...props.children}
    </div>
  )
}
